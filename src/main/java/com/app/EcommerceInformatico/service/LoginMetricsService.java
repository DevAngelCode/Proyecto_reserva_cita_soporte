package com.app.EcommerceInformatico.service;

import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Service
public class LoginMetricsService {

    private final Counter successCounter;
    private final Counter failedCounter;

    public LoginMetricsService(MeterRegistry meterRegistry) {
        this.successCounter = meterRegistry.counter("login_attempts_success");
        this.failedCounter = meterRegistry.counter("login_attempts_failed");
    }

    public void incrementSuccess() {
        successCounter.increment();
    }

    public void incrementFailure() {
        failedCounter.increment();
    }
}