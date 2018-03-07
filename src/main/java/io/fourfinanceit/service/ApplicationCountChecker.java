package io.fourfinanceit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class ApplicationCountChecker {

    private final long applicationLimitPerIp;
    private final Clock clock;

    private final ConcurrentMap<String, Queue<Long>> applicationsByIp = new ConcurrentHashMap<>();

    public ApplicationCountChecker(@Value("${application.limit.per.ip}") long applicationLimitPerIp,
                                   Clock clock) {
        this.applicationLimitPerIp = applicationLimitPerIp;
        this.clock = clock;
    }

    public boolean validate(String ip) {
        applicationsByIp.putIfAbsent(ip, new LinkedList<>());
        Queue<Long> applicationTimes = applicationsByIp.get(ip);
        synchronized (applicationTimes) {
            removeExpired(applicationTimes);
            applicationTimes.add(clock.millis());
            return applicationTimes.size() <= applicationLimitPerIp;
        }
    }

    private void removeExpired(Queue<Long> applicationTimes) {
        while (applicationTimes.peek() != null && isExpired(applicationTimes.peek())) {
            applicationTimes.poll();
        }
    }

    private boolean isExpired(long time) {
        return time < clock.millis() - 24 * 60 * 60 * 1000;
    }
}
