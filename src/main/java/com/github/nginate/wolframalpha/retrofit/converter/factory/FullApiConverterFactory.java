package com.github.nginate.wolframalpha.retrofit.converter.factory;

import com.github.nginate.wolframalpha.model.params.GeoCoordinates;
import com.github.nginate.wolframalpha.retrofit.BooleanTimeout;
import com.github.nginate.wolframalpha.retrofit.converter.AsyncTimeoutConverter;
import com.github.nginate.wolframalpha.retrofit.converter.CommaJoiningConverter;
import com.github.nginate.wolframalpha.retrofit.converter.GeoCoordsConverter;
import com.github.nginate.wolframalpha.retrofit.converter.ResponseConverter;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;

import static java.util.Arrays.stream;

public class FullApiConverterFactory extends Converter.Factory {
    private final Converter.Factory paramsConverter = ScalarsConverterFactory.create();

    private final AsyncTimeoutConverter asyncTimeoutConverter = new AsyncTimeoutConverter();
    private final GeoCoordsConverter geoCoordsConverter = new GeoCoordsConverter();
    private final CommaJoiningConverter commaJoiningConverter = new CommaJoiningConverter();
    private final ResponseConverter responseConverter = new ResponseConverter();

    private FullApiConverterFactory() {
    }

    public static FullApiConverterFactory create() {
        return new FullApiConverterFactory();
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return responseConverter;
    }

    @Nullable
    @Override
    public Converter<?, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        Class<?> rawParameterType = getRawType(type);

        if (contains(annotations, BooleanTimeout.class)) {
            return asyncTimeoutConverter;
        }
        if (Collection.class.isAssignableFrom(rawParameterType)) {
            return commaJoiningConverter;
        }
        if (GeoCoordinates.class.isAssignableFrom(rawParameterType)) {
            return geoCoordsConverter;
        }
        return paramsConverter.stringConverter(type, annotations, retrofit);
    }

    private boolean contains(Annotation[] annotations, Class<?> annotationClass) {
        return stream(annotations).anyMatch(annotation -> annotation.annotationType().equals(annotationClass));
    }
}
