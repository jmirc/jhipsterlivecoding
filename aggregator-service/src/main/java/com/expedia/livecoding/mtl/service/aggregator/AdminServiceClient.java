package com.expedia.livecoding.mtl.service.aggregator;

import java.util.List;

import feign.Body;
import feign.Param;
import feign.RequestLine;

public interface AdminServiceClient {

    @RequestLine("GET /api/hotels")
    List<Hotel> getHotels();

    @Body("username={username}&password={password}")
    @RequestLine("POST /api/authenticate")
    Token authorize(@Param("username") String username, @Param("password") String password);
}
