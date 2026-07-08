package com.lifesync.jobtracker.service;

import com.lifesync.jobtracker.dto.*;
import com.lifesync.jobtracker.model.ApplicationStatus;
import com.lifesync.jobtracker.model.InterviewRound;
import com.lifesync.jobtracker.model.JobApplication;
import com.lifesync.jobtracker.model.RoundOutcome;
import com.lifesync.jobtracker.repository.InterviewRoundRepository;
import com.lifesync.jobtracker.repository.JobApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobTrackerService {

    private final JobApplicationRepository jobApplicationRepository;
    private final InterviewRoundRepository interviewRoundRepository;
    private final JobEventProducer eventProducer;

    public JobApplicationResponse createApplication(String userEmail, JobApplicationRequest request) {
        JobApplication application = JobApplication.builder()
                .userEmail(userEmail)
                .companyName(request.getCompanyName())
                .role(request.getRole())
                .appliedDate(request.getAppliedDate())
                .followUpDate(request.getFollowUpDate())
                .notes(request.getNotes())
                .jobUrl(request.getJobUrl())
                .build();

        return mapToResponse(jobApplicationRepository.save(application));
    }

    public List<JobApplicationResponse> getAllApplications(String userEmail) {
        return jobApplicationRepository
                .findByUserEmailOrderByCreatedAtDesc(userEmail)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public JobApplicationResponse getApplicationById(String userEmail, Long id) {
        JobApplication application = findApplication(userEmail, id);
        return mapToResponse(application);
    }

    public JobApplicationResponse updateApplication(String userEmail, Long id, JobApplicationRequest request) {
        JobApplication application = findApplication(userEmail, id);

        application.setCompanyName(request.getCompanyName());
        application.setRole(request.getRole());
        application.setFollowUpDate(request.getFollowUpDate());
        application.setNotes(request.getNotes());
        application.setJobUrl(request.getJobUrl());

        return mapToResponse(jobApplicationRepository.save(application));
    }

    public void deleteApplication(String userEmail, Long id) {
        JobApplication application = findApplication(userEmail, id);
        jobApplicationRepository.delete(application);
    }

    public JobApplicationResponse updateStatus(String userEmail, Long id, StatusUpdateRequest request) {
        JobApplication application = findApplication(userEmail, id);

        ApplicationStatus newStatus = ApplicationStatus.valueOf(request.getStatus().toUpperCase());

        application.setStatus(newStatus);

        JobStatusEvent event = JobStatusEvent.builder()
                .userEmail(userEmail)
                .companyName(application.getCompanyName())
                .role(application.getRole())
                .oldStatus(application.getStatus().name())
                .newStatus(newStatus.name())
                .eventType(newStatus == ApplicationStatus.OFFER ? "OFFER_RECEIVED" : "STATUS_CHANGED")
                .build();

        eventProducer.publishStatusEvent(event);

        return mapToResponse(jobApplicationRepository.save(application));
    }

    public InterviewRound addRound(String userEmail, Long applicationId, InterviewRoundRequest request) {
        JobApplication application = findApplication(userEmail, applicationId);

        RoundOutcome outcome = RoundOutcome.PENDING;

        if (request.getOutcome() != null) {
            outcome = RoundOutcome.valueOf(request.getOutcome().toUpperCase());
        }

        InterviewRound round = InterviewRound.builder()
                .roundName(request.getRoundName())
                .roundDate(request.getRoundDate())
                .notes(request.getNotes())
                .outcome(outcome)
                .application(application)
                .build();

        if (application.getStatus() == ApplicationStatus.SHORTLISTED ||
                application.getStatus() == ApplicationStatus.APPLIED) {
            application.setStatus(ApplicationStatus.INTERVIEW);
            jobApplicationRepository.save(application);
        }

        return interviewRoundRepository.save(round);

    }

    public InterviewRound updateRound(String userEmail, Long applicationId, Long roundId, InterviewRoundRequest request) {
        findApplication(userEmail, applicationId);

        InterviewRound round = interviewRoundRepository
                .findByIdAndApplicationUserEmail(roundId, userEmail)
                .orElseThrow(() -> new RuntimeException("Round not found"));

        round.setRoundName(request.getRoundName());
        round.setRoundDate(request.getRoundDate());
        round.setNotes(request.getNotes());

        if (request.getOutcome() != null) {
            round.setOutcome(
                    RoundOutcome.valueOf(request.getOutcome().toUpperCase()));
        }

        return interviewRoundRepository.save(round);

    }

    public List<InterviewRound> getRounds(String userEmail, Long applicationId) {
        findApplication(userEmail, applicationId);
        return interviewRoundRepository
                .findByApplicationIdOrderByRoundDateAsc(applicationId);
    }

    public JobStatsResponse getStats(String userEmail) {
        return JobStatsResponse.builder()
                .totalApplications(jobApplicationRepository.countByUserEmail(userEmail))
                .applied(jobApplicationRepository.countByUserEmailAndStatus(userEmail, ApplicationStatus.APPLIED))
                .shortlisted(jobApplicationRepository.countByUserEmailAndStatus(userEmail, ApplicationStatus.SHORTLISTED))
                .interviews(jobApplicationRepository.countByUserEmailAndStatus(userEmail, ApplicationStatus.INTERVIEW))
                .offers(jobApplicationRepository.countByUserEmailAndStatus(userEmail, ApplicationStatus.OFFER))
                .rejected(jobApplicationRepository.countByUserEmailAndStatus(userEmail, ApplicationStatus.REJECTED))
                .build();
    }

    private JobApplicationResponse mapToResponse(JobApplication app) {

        return JobApplicationResponse.builder()
                .id(app.getId())
                .companyName(app.getCompanyName())
                .role(app.getRole())
                .status(app.getStatus())
                .appliedDate(app.getAppliedDate())
                .followUpDate(app.getFollowUpDate())
                .notes(app.getNotes())
                .jobUrl(app.getJobUrl())
                .createdAt(app.getCreatedAt())
                .rounds(app.getRounds())
                .build();
    }

    private JobApplication findApplication(String userEmail, Long id) {
        return jobApplicationRepository.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new RuntimeException("Application not found"));
    }
}
