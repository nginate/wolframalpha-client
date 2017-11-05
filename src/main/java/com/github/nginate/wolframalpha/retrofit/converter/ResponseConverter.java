package com.github.nginate.wolframalpha.retrofit.converter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.github.nginate.wolframalpha.model.QueryResult;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import java.io.IOException;

@Slf4j
public class ResponseConverter implements Converter<ResponseBody, QueryResult> {
    private final ObjectMapper mapper = new XmlMapper();

    public ResponseConverter() {
        mapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector(TypeFactory.defaultInstance()));
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public QueryResult convert(ResponseBody value) throws IOException {
        String string = value.string();
        log.debug("Response body : {}", string);
        return mapper.readValue(string, QueryResult.class);
    }
}
