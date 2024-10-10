package org.example.expert.domain.healthcheck.controller;


import lombok.RequiredArgsConstructor;
import org.example.expert.domain.healthcheck.dto.HealthCheckResponseDto;
import org.example.expert.domain.healthcheck.service.EC2HealthCheckService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class EC2HealthCheckController {

    private final EC2HealthCheckService ec2HealthCheckService;

    @GetMapping
    public ResponseEntity<HealthCheckResponseDto> healthCheck() {
        return ResponseEntity.ok(ec2HealthCheckService.healthCheck());
    }
}
