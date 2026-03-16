package com.example.devops.monitoring_dashboard.controller;


import com.example.devops.monitoring_dashboard.model.SystemMetrics;
import com.example.devops.monitoring_dashboard.service.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/metrics")
public class MetricsController {

    private final SystemMetricsService metricsService;

    public MetricsController(SystemMetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @GetMapping("/system")
    public SystemMetrics getSystemMetrics() {
        return metricsService.getSystemMetrics();
    }
}