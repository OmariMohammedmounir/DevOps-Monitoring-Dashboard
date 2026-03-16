package com.example.devops.monitoring_dashboard.service;

import com.example.devops.monitoring_dashboard.model.ContainerInfo;
import com.example.devops.monitoring_dashboard.model.ContainerMetrics;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Statistics;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.okhttp.OkDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.springframework.stereotype.Service;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

@Service
public class DockerService {

    private final DockerClient dockerClient;

    public DockerService() {
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("tcp://localhost:2375")
                .build();

        DockerHttpClient httpClient = new OkDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .build();

        this.dockerClient = DockerClientBuilder.getInstance(config)
                .withDockerHttpClient(httpClient)
                .build();
    }

    // Basic container info
    public List<ContainerInfo> getContainers() {
        return dockerClient.listContainersCmd()
                .withShowAll(true)
                .exec()
                .stream()
                .map(c -> new ContainerInfo(
                        c.getId(),
                        c.getNames() != null && c.getNames().length > 0 ? c.getNames()[0] : "",
                        c.getImage(),
                        c.getStatus()
                ))
                .collect(Collectors.toList());
    }

    // Full container resource metrics
    public List<ContainerMetrics> getContainerMetrics() {
        return dockerClient.listContainersCmd()
                .withShowAll(true)
                .exec()
                .stream()
                .map(this::fetchMetrics)
                .collect(Collectors.toList());
    }

    private ContainerMetrics fetchMetrics(Container container) {
        ContainerMetrics metrics = new ContainerMetrics();
        metrics.setId(container.getId());
        metrics.setName(container.getNames() != null && container.getNames().length > 0 ? container.getNames()[0] : "");

        try {
            StatsCallback callback = new StatsCallback();
            dockerClient.statsCmd(container.getId()).exec(callback);
            Statistics stats = callback.getNextStats();

            metrics.setCpuPercent(calculateCpuPercent(stats));
            metrics.setMemoryUsage(stats.getMemoryStats().getUsage());
            metrics.setMemoryLimit(stats.getMemoryStats().getLimit());
            metrics.setNetworkRx(stats.getNetworks() != null ?
                    stats.getNetworks().values().stream().mapToLong(n -> n.getRxBytes()).sum() : 0L);
            metrics.setNetworkTx(stats.getNetworks() != null ?
                    stats.getNetworks().values().stream().mapToLong(n -> n.getTxBytes()).sum() : 0L);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return metrics;
    }

private double calculateCpuPercent(Statistics stats) {
    if (stats.getCpuStats() == null || stats.getPreCpuStats() == null) {
        return 0.0;
    }

    Long totalUsage = stats.getCpuStats().getCpuUsage() != null ?
            stats.getCpuStats().getCpuUsage().getTotalUsage() : null;
    Long preTotalUsage = stats.getPreCpuStats().getCpuUsage() != null ?
            stats.getPreCpuStats().getCpuUsage().getTotalUsage() : null;

    Long systemCpuUsage = stats.getCpuStats().getSystemCpuUsage();
    Long preSystemCpuUsage = stats.getPreCpuStats().getSystemCpuUsage();

    int onlineCpus = stats.getCpuStats().getOnlineCpus() != null
            ? stats.getCpuStats().getOnlineCpus().intValue()
            : 1;

    if (totalUsage == null || preTotalUsage == null || systemCpuUsage == null || preSystemCpuUsage == null) {
        return 0.0;
    }

    long cpuDelta = totalUsage - preTotalUsage;
    long systemDelta = systemCpuUsage - preSystemCpuUsage;

    if (systemDelta > 0 && cpuDelta > 0) {
        return ((double) cpuDelta / systemDelta) * onlineCpus * 100.0;
    }
    return 0.0;
}
    // Custom blocking callback to get first stats
    private static class StatsCallback implements ResultCallback<Statistics> {
        private final BlockingQueue<Statistics> queue = new ArrayBlockingQueue<>(1);

        @Override
        public void onStart(Closeable closeable) {}

        @Override
        public void onNext(Statistics object) {
            queue.offer(object);
            try {
                close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
        }

        @Override
        public void onComplete() {}

        @Override
        public void close() throws IOException {}

        public Statistics getNextStats() throws InterruptedException {
            return queue.take();
        }
    }
}