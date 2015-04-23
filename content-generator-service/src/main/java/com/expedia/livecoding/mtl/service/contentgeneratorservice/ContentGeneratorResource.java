package com.expedia.livecoding.mtl.service.contentgeneratorservice;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import net._01001111.text.LoremIpsum;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContentGeneratorResource {

    private final List<Hotel> hotels;

    public ContentGeneratorResource() {
        LoremIpsum jlorem = new LoremIpsum();

        hotels = IntStream.range(1, 5).mapToObj(i -> {
            Hotel hotel = new Hotel();
            hotel.setName(jlorem.randomWord());
            hotel.setAddress(jlorem.sentence());
            hotel.setDescription(jlorem.paragraphs(2));
            hotel.setCity("Montreal");
            hotel.setPostalCode("AAAAA");
            hotel.setState("Quebec");
            hotel.setCountry("Canada");
            return hotel;
        }).collect(Collectors.toList());

    }

    @HystrixCommand(groupKey = "content-generator-service")
    @RequestMapping(value = "/hotels",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Hotel>> getAll() {

        return ResponseEntity.ok(hotels);
    }

}
