package com.expedia.livecoding.mtl.service.aggregator;

import java.util.List;

import feign.RequestLine;

public interface AdminServiceClient {

    @RequestLine("GET /hotels")
    List<Hotel> getHotels();
}
