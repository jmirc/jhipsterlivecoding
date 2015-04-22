package com.expedia.livecoding.mtl.service.aggregator;

/**
 * The security token.
 */
public class Token {

    String token;
    long expires;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }
}
