package com.example.devops.monitoring_dashboard.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.model.Statistics;

import java.io.InputStream;

public class StatisticsParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Statistics parse(InputStream stream) {
        try {
            return objectMapper.readValue(stream, Statistics.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Docker stats", e);
        }
    }
}