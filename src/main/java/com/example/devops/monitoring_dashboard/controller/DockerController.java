package com.example.devops.monitoring_dashboard.controller;

import com.example.devops.monitoring_dashboard.model.ContainerMetrics;
import com.example.devops.monitoring_dashboard.model.ContainerInfo;
import com.example.devops.monitoring_dashboard.service.DockerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/containers")
public class DockerController {

    private final DockerService dockerService;

    public DockerController(DockerService dockerService) {
        this.dockerService = dockerService;
    }

    /**
     * Get basic container info (ID, name, image, status)
     */
    @GetMapping("/info")
    public List<ContainerInfo> getContainersInfo() {
        return dockerService.getContainers();
    }

    /**
     * Get resource metrics for all containers (CPU %, memory usage, network)
     */
    @GetMapping("/metrics")
    public List<ContainerMetrics> getContainersMetrics() {
        // ✅ Call the correct method from DockerService
        return dockerService.getContainerMetrics();
    }
}