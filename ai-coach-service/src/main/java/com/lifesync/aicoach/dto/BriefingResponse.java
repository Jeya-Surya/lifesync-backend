package com.lifesync.aicoach.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class BriefingResponse {

    private LocalDate date;
    private String greeting;
    private String overallSummary;
    private List<String> insights;
    private String focusForToday;
}
