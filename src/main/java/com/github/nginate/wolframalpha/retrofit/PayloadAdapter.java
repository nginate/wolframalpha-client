package com.github.nginate.wolframalpha.retrofit;

import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class PayloadAdapter extends CallAdapter.Factory {
    @Nullable
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return new CallAdapter<Object, Object>() {
            @Override
            public Type responseType() {
                return returnType;
            }

            @Override
            @SneakyThrows
            public Object adapt(Call<Object> call) {
                return call.execute().body();
            }
        };
    }
}
