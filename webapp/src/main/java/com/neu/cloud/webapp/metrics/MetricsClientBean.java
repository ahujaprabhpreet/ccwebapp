package com.neu.cloud.webapp.metrics;

import com.timgroup.statsd.NoOpStatsDClient;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsClientBean {

    @Value("${print.metrics}")
    private Boolean printMetrics;

    @Value("${metrics.server.hostname}")
    private String metricServerHost;

    @Value("${metrics.server.port}")
    private int metricServerPort;

    @Bean
    public StatsDClient statsDClient() {

        if (printMetrics){
            return new NonBlockingStatsDClient("csye6225", metricServerHost, metricServerPort);
        }

        return new NoOpStatsDClient();
    }
}