package com.lifesync.jobtracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StatusUpdateRequest {

    @NotBlank(message = "Status update request")
    private String status;
}
