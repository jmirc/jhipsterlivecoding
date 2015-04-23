package com.expedia.livecoding.mtl.service.contentgeneratorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@SpringCloudApplication
@EnableHystrix
public class ContentGeneratorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentGeneratorServiceApplication.class, args);
    }
}


