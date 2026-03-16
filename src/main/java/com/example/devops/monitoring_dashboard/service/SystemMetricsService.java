package com.example.devops.monitoring_dashboard.service;


import com.example.devops.monitoring_dashboard.model.SystemMetrics;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

@Service
public class SystemMetricsService {

 public SystemMetrics getSystemMetrics() {

    OperatingSystemMXBean osBean =
            (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    double cpuUsage = osBean.getCpuLoad() * 100;

    long totalMemory = osBean.getTotalMemorySize();
    long freeMemory = osBean.getFreeMemorySize();
    long usedMemory = totalMemory - freeMemory;

    return new SystemMetrics(cpuUsage, totalMemory, freeMemory, usedMemory);
}
}