package com.example.devops.monitoring_dashboard.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SystemMetrics {

    private double cpuUsage;
    private long totalMemory;
    private long freeMemory;
    private long usedMemory;

}