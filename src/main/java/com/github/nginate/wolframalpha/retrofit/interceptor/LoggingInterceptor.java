package com.github.nginate.wolframalpha.retrofit.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.internal.http.HttpHeaders;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.github.nginate.wolframalpha.util.RetrofitUtil.*;

@Slf4j
@RequiredArgsConstructor
public class LoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(@Nonnull Chain chain) throws IOException {
        Request request = chain.request();
        if (!log.isInfoEnabled()) {
            return chain.proceed(request);
        }

        int requestId = getRequestId(request);

        log.info("{} ---> {} {}", requestId, request.method(), request.url());
        RequestBody requestBody = request.body();
        if (requestBody != null) {
            log.info("{} ---> {} bytes of {}", requestId, requestBody.contentLength(), requestBody.contentType());

            Headers headers = request.headers();
            log.debug("{} ---> headers : {}", requestId, formatHeadersForLog(headers));
            if (!bodyEncoded(headers)) {
                log.info("{} ---> {}", requestId, requestBodyToString(requestBody));
            }
        }

        long startMs = System.currentTimeMillis();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            log.error("{} ---> failed : {}", requestId, e.getMessage(), e);
            throw e;
        }
        long tookMs = TimeUnit.MILLISECONDS.toMillis(System.currentTimeMillis() - startMs);

        ResponseBody responseBody = response.body();
        long size = Optional.ofNullable(responseBody).map(ResponseBody::contentLength).orElse(0L);
        log.info("{} <--- {} {} [{} bytes] : took {} ms", requestId, response.code(), response.message(), size, tookMs);

        Headers headers = response.headers();
        log.debug("{} <--- headers : {}", requestId, formatHeadersForLog(headers));
        if (HttpHeaders.hasBody(response)) {
            if (bodyEncoded(headers)) {
                log.info("{} <--- body encoded", requestId);
            } else if (size == 0) {
                log.info("{} <--- body is empty", requestId);
            } else if (responseBody != null) {
                log.info("{} <--- response body", requestId, responseBodyToString(responseBody));
            }
        }
        return response;
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    private String formatHeadersForLog(Headers headers) {
        return headers.names()
                .stream()
                .map(name -> {
                    String headerValues = headers.values(name).stream().reduce((s, s2) -> s + ", " + s2).orElse("");
                    return String.format("%s=[%s]", name, headerValues);
                })
                .reduce((s, s2) -> s + "; " + s2)
                .orElse("");
    }
}
