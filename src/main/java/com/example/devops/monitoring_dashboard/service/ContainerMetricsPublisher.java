package com.example.devops.monitoring_dashboard.service;

import com.example.devops.monitoring_dashboard.model.ContainerMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContainerMetricsPublisher {

    private final MeterRegistry registry;
    private final DockerService dockerService;

    public ContainerMetricsPublisher(MeterRegistry registry, DockerService dockerService) {
        this.registry = registry;
        this.dockerService = dockerService;
    }

    @Scheduled(fixedRate = 5000) // every 5 seconds
    public void publishMetrics() {
        List<ContainerMetrics> metricsList = dockerService.getContainerMetrics();
        metricsList.forEach(m -> {
            registry.gauge("container_cpu_percent", Tags.of("name", m.getName()), m.getCpuPercent());
            registry.gauge("container_memory_usage_bytes", Tags.of("name", m.getName()), m.getMemoryUsage());
            registry.gauge("container_memory_limit_bytes", Tags.of("name", m.getName()), m.getMemoryLimit());
            registry.gauge("container_network_rx_bytes", Tags.of("name", m.getName()), m.getNetworkRx());
            registry.gauge("container_network_tx_bytes", Tags.of("name", m.getName()), m.getNetworkTx());
        });
    }
}