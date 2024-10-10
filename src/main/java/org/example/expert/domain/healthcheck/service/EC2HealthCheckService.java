package org.example.expert.domain.healthcheck.service;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusRequest;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusResult;
import com.amazonaws.services.ec2.model.InstanceStatus;
import org.example.expert.domain.common.exception.ServerException;
import org.example.expert.domain.healthcheck.dto.HealthCheckResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class EC2HealthCheckService {

    @Value("${ec2.instanceId}")
    private String instanceId; // 확인할 EC2 인스턴스 ID

    @Value("${cloud.aws.region.static}")
    private String region;

    public HealthCheckResponseDto healthCheck() {
        AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard()
                .withRegion(region)
                .build();

        return checkInstanceHealth(ec2, instanceId);
    }

    private HealthCheckResponseDto checkInstanceHealth(AmazonEC2 ec2, String instanceId) {
        DescribeInstanceStatusRequest request = new DescribeInstanceStatusRequest()
                .withInstanceIds(instanceId);
        DescribeInstanceStatusResult response = ec2.describeInstanceStatus(request);

        if (!response.getInstanceStatuses().isEmpty()) {
            InstanceStatus status = response.getInstanceStatuses().get(0);
            String instanceState = status.getInstanceState().getName();
            String systemStatus = status.getSystemStatus().getStatus();
            String instanceStatus = status.getInstanceStatus().getStatus();

            return new HealthCheckResponseDto(instanceId, instanceState, systemStatus, instanceStatus);
        } else {
            throw new ServerException("No status available for the instance.");
        }
    }
}