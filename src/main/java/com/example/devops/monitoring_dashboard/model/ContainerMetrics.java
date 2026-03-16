package com.example.devops.monitoring_dashboard.model;

public class ContainerMetrics {

    private String id;
    private String name;
    private double cpuPercent;
    private long memoryUsage;
    private long memoryLimit;
    private long networkRx;
    private long networkTx;

    // Constructors
    public ContainerMetrics() {
    }

    public ContainerMetrics(String id, String name, double cpuPercent, long memoryUsage,
                            long memoryLimit, long networkRx, long networkTx) {
        this.id = id;
        this.name = name;
        this.cpuPercent = cpuPercent;
        this.memoryUsage = memoryUsage;
        this.memoryLimit = memoryLimit;
        this.networkRx = networkRx;
        this.networkTx = networkTx;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getCpuPercent() {
        return cpuPercent;
    }
    public void setCpuPercent(double cpuPercent) {
        this.cpuPercent = cpuPercent;
    }
    public long getMemoryUsage() {
        return memoryUsage;
    }
    public void setMemoryUsage(long memoryUsage) {
        this.memoryUsage = memoryUsage;
    }
    public long getMemoryLimit() {
        return memoryLimit;
    }
    public void setMemoryLimit(long memoryLimit) {
        this.memoryLimit = memoryLimit;
    }
    public long getNetworkRx() {
        return networkRx;
    }
    public void setNetworkRx(long networkRx) {
        this.networkRx = networkRx;
    }
    public long getNetworkTx() {
        return networkTx;
    }
    public void setNetworkTx(long networkTx) {
        this.networkTx = networkTx;
    }
}