package com.expedia.livecoding.mtl.service.aggregator;

import com.google.common.cache.Cache;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AggregatorHotelService {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private Cache<String, Hotel> hotelCache;

    @Value("${eureka.instance.hostname}")
    private String hostname;

    @Scheduled(fixedDelay = 10000)
    public void aggregate() {
        discoveryClient.getInstances("admin-service").stream()
                .forEach(s -> {
//                            System.out.println("*******************");
//                            System.out.println(s.getHost());
//                            System.out.println(s.getPort());
//                            System.out.println(s.getUri());
//                            System.out.println(s.getServiceId());
//                            System.out.println("*******************");

                            AdminServiceClient adminServiceClient = retrieveAdminServiceClient(s);
                            adminServiceClient.getHotels()
                                    .forEach(hotel -> {
                                        hotel.setSource(hostname);
                                        hotelCache.put(hotel.getName(), hotel);
                                    });
                        }
                );
    }

    private AdminServiceClient retrieveAdminServiceClient(ServiceInstance serviceInstance) {
        return Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .requestInterceptor(new XAuthTokenRequestInterceptor(serviceInstance))
                .target(AdminServiceClient.class, serviceInstance.getUri().toString());
    }
}
