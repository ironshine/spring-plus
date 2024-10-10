package org.example.expert.domain.healthcheck.dto;


import lombok.Getter;

@Getter
public class HealthCheckResponseDto {
    private final String instanceId;
    private final String instanceState;
    private final String systemStatus;
    private final String instanceStatus;

    public HealthCheckResponseDto(String instanceId, String instanceState, String systemStatus, String instanceStatus) {
        this.instanceId = instanceId;
        this.instanceState = instanceState;
        this.systemStatus = systemStatus;
        this.instanceStatus = instanceStatus;
    }
}
