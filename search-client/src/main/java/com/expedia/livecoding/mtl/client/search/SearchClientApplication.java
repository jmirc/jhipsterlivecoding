package com.expedia.livecoding.mtl.client.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringCloudApplication
@EnableZuulProxy
@EnableHystrixDashboard
@EnableTurbine
public class SearchClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchClientApplication.class, args);
    }
}

@RestController
class MessageController {

    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/msg")
    public String getMessage() {
        return messageService.getMessage();
    }
}

@RefreshScope
@Service
class MessageService {

    @Value("${message:no}")
    private String message;

    public String getMessage() {
        return message;
    }
}
