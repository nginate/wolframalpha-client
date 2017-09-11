package com.github.nginate.wolframalpha.feign.client;

import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

@Slf4j
public class NoSNIHostnameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String s, SSLSession sslSession) {
        log.warn("Ignoring host {} verification for SSL session {}", s, sslSession.getId());
        return true;
    }
}
