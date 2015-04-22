package com.expedia.livecoding.mtl.service.aggregator;

import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;

public class XAuthTokenRequestInterceptor implements RequestInterceptor {

    private Logger logger = LoggerFactory.getLogger(XAuthTokenRequestInterceptor.class);

    private ServiceInstance serviceInstance;

    public XAuthTokenRequestInterceptor(ServiceInstance serviceInstance) {
        this.serviceInstance = serviceInstance;
    }

    @Override
    public void apply(RequestTemplate template) {
        try {
            String username = "admin";
            String password = "admin";

            final AdminServiceClient adminServiceClient = Feign.builder()
                    .encoder(new GsonEncoder())
                    .decoder(new GsonDecoder())
                    .target(AdminServiceClient.class, serviceInstance.getUri().toString());

            final Token token = adminServiceClient.authorize(username, password);
            if (token != null) {
                template.header("x-auth-token", token.getToken());
            }

        } catch (Exception e) {
            logger.error("Failed to set the token", e);
        }
    }
}
