package com.velosight.dto;

import jakarta.validation.constraints.NotBlank;

public class AlertStatusUpdateRequest {
    @NotBlank
    private String status;

    public AlertStatusUpdateRequest() {}

    public AlertStatusUpdateRequest(String status) {
        this.status = status;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
