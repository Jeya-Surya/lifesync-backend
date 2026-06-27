package com.lifesync.jobtracker.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobStatsResponse {
    private long totalApplications;
    private long applied;
    private long shortlisted;
    private long interviews;
    private long offers;
    private long rejected;
}