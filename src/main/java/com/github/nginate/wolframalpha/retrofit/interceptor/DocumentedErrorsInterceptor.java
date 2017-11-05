package com.github.nginate.wolframalpha.retrofit.interceptor;

import com.github.nginate.wolframalpha.exceptions.InvalidAppIdException;
import com.github.nginate.wolframalpha.exceptions.MissingAppIdException;
import com.github.nginate.wolframalpha.exceptions.WolframClientException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static com.github.nginate.wolframalpha.util.RetrofitUtil.getRequestId;
import static com.github.nginate.wolframalpha.util.RetrofitUtil.responseBodyToString;

@Slf4j
public class DocumentedErrorsInterceptor implements Interceptor {
    private static final Map<String, Supplier<? extends WolframClientException>> exceptionBuilders;

    static {
        exceptionBuilders = new HashMap<>();
        exceptionBuilders.put("Error 1: Invalid appid", InvalidAppIdException::new);
        exceptionBuilders.put("Error 2: Appid missing", MissingAppIdException::new);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        int requestId = getRequestId(request);

        int code = response.code();
        if (code != 200) {
            String body = Optional.ofNullable(response.body()).map(this::readBody).orElse("");
            log.warn("{} <--- received error response : {}", requestId, body);
            throw exceptionBuilders.getOrDefault(body, () -> new WolframClientException(body)).get();
        }
        return response;
    }

    @SneakyThrows
    private String readBody(ResponseBody body) {
        return responseBodyToString(body);
    }
}
