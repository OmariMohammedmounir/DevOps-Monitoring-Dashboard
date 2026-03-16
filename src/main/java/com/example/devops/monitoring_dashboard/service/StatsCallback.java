package com.example.devops.monitoring_dashboard.service;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Statistics;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class StatsCallback implements ResultCallback<Statistics> {

    private final BlockingQueue<Statistics> queue = new ArrayBlockingQueue<>(1);

    @Override
    public void onStart(Closeable closeable) {}

    @Override
    public void onNext(Statistics object) {
        queue.offer(object); // put first stat
        try {
            close(); // stop the stream after first stat
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
        // Wait until the first stat is received
        return queue.take();
    }
}