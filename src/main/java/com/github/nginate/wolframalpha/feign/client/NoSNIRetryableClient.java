package com.github.nginate.wolframalpha.feign.client;

import feign.Client;
import feign.Request;
import feign.Response;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLProtocolException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

import static feign.Util.*;
import static java.lang.String.format;

@Slf4j
public class NoSNIRetryableClient implements Client {
    public static final Client INSTANCE = new NoSNIRetryableClient();
    private static final HostnameVerifier NO_SNI_HOSTNAME_VERIFIER = new NoSNIHostnameVerifier();
    private static final String ACCEPT_HEADER = "Accept";

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        try {
            HttpURLConnection connection = convertAndSend(request, options, true);
            return convertResponse(connection).toBuilder().request(request).build();
        } catch (SSLProtocolException exception) {
            if (exception.getMessage().equals("handshake alert:  unrecognized_name")) {
                log.warn("Misconfigured wolfram server {}. Retrying without NSI", request.url());
                HttpURLConnection connection = convertAndSend(request, options, false);
                return convertResponse(connection).toBuilder().request(request).build();
            } else {
                throw exception;
            }
        }
    }

    HttpURLConnection convertAndSend(Request request, Request.Options options, boolean useSNI) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) new URL(request.url()).openConnection();
        if (connection instanceof HttpsURLConnection) {
            HttpsURLConnection sslCon = (HttpsURLConnection) connection;
            if (!useSNI) {
                sslCon.setHostnameVerifier(NO_SNI_HOSTNAME_VERIFIER);
            }
        }

        connection.setConnectTimeout(options.connectTimeoutMillis());
        connection.setReadTimeout(options.readTimeoutMillis());
        connection.setAllowUserInteraction(false);
        connection.setInstanceFollowRedirects(true);
        connection.setRequestMethod(request.method());

        Collection<String> encodingValues = request.headers().get(CONTENT_ENCODING);
        boolean gzipEncodedRequest = encodingValues != null && encodingValues.contains(ENCODING_GZIP);
        boolean deflateEncodedRequest = encodingValues != null && encodingValues.contains(ENCODING_DEFLATE);

        Integer contentLength = null;
        for (String field : request.headers().keySet()) {
            for (String value : request.headers().get(field)) {
                if (field.equals(CONTENT_LENGTH)) {
                    if (!gzipEncodedRequest && !deflateEncodedRequest) {
                        contentLength = Integer.valueOf(value);
                        connection.addRequestProperty(field, value);
                    }
                } else {
                    connection.addRequestProperty(field, value);
                }
            }
        }
        // Some servers choke on the default ACCEPT_HEADER string.
        if (!request.headers().containsKey(ACCEPT_HEADER)) {
            connection.addRequestProperty(ACCEPT_HEADER, "*/*");
        }

        if (request.body() != null) {
            if (contentLength != null) {
                connection.setFixedLengthStreamingMode(contentLength);
            } else {
                connection.setChunkedStreamingMode(8196);
            }
            connection.setDoOutput(true);
            try (OutputStream out = getOutputStream(connection, gzipEncodedRequest, deflateEncodedRequest)) {
                out.write(request.body());
            }
        }
        return connection;
    }

    @SneakyThrows
    private OutputStream getOutputStream(HttpURLConnection connection, boolean gzip, boolean deflate) {
        OutputStream out = connection.getOutputStream();
        if (gzip) {
            out = new GZIPOutputStream(out);
        } else if (deflate) {
            out = new DeflaterOutputStream(out);
        }
        return out;
    }

    Response convertResponse(HttpURLConnection connection) throws IOException {
        int status = connection.getResponseCode();
        String reason = connection.getResponseMessage();

        if (status < 0) {
            throw new IOException(format("Invalid status(%s) executing %s %s",
                    status, connection.getRequestMethod(), connection.getURL()));
        }

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> field : connection.getHeaderFields().entrySet()) {
            // response message
            if (field.getKey() != null) {
                headers.put(field.getKey(), field.getValue());
            }
        }

        Integer length = connection.getContentLength() == -1 ? null : connection.getContentLength();
        InputStream stream = status >= 400 ? connection.getErrorStream() : connection.getInputStream();
        return Response.builder()
                .status(status)
                .reason(reason)
                .headers(headers)
                .body(stream, length)
                .build();
    }
}