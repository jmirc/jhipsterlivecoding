package com.expedia.livecoding.mtl.service.aggregator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.cache.Cache;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AggregatorResource {

    @Autowired
    private Cache<String, Hotel> hotelCache;

    @HystrixCommand(groupKey = "aggregator-service", fallbackMethod = "defaultHotel")
    @RequestMapping(value = "/searchByName",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Hotel>> searchByName(@RequestParam("query") String name) {
        final List<Hotel> hotels = hotelCache.asMap().values().stream()
                .filter(hotel -> hotel.getName().contains(name))
                .collect(Collectors.toList());

        if (hotels.size() == 0) {
            throw new IllegalArgumentException();
        }

        return ResponseEntity.ok(hotels);
    }

    public ResponseEntity<List<Hotel>> defaultHotel(String name) {
        List<Hotel> hotels = new ArrayList<>();
        Hotel hotel = new Hotel();
        hotel.setName("Default hotel's name");
        hotels.add(hotel);
        return ResponseEntity.ok(hotels);
    }
}
