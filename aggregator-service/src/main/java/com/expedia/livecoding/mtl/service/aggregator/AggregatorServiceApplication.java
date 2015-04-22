package com.expedia.livecoding.mtl.service.aggregator;

import java.net.UnknownHostException;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringCloudApplication
@EnableScheduling
@EnableHystrix
public class AggregatorServiceApplication {

    public static void main(String[] args) throws UnknownHostException {
        SpringApplication.run(AggregatorServiceApplication.class, args);
    }

    @Bean
    public Cache<String, Hotel> cache() {
        return CacheBuilder.newBuilder().build();
    }

    @Bean
    CommandLineRunner displayServices(DiscoveryClient discoveryClient) {
        return args -> discoveryClient.getServices().forEach(System.out::println);
    }
}
