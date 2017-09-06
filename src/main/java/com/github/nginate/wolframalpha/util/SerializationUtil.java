package com.github.nginate.wolframalpha.util;

import feign.jaxb.JAXBContextFactory;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.w3c.dom.Node;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@UtilityClass
public class SerializationUtil {
    private static final JAXBContextFactory jaxbContextFactory = buildJAXBFactory();

    @SneakyThrows
    public static <T> T fromXML(Node xml, Type type) {
        //noinspection unchecked
        Class<T> tClass = (Class<T>) type;
        return tClass.cast(jaxbContextFactory.createUnmarshaller(tClass).unmarshal(xml));
    }

    public static JAXBContextFactory buildJAXBFactory() {
        return new JAXBContextFactory.Builder()
                .withMarshallerJAXBEncoding(StandardCharsets.UTF_8.name())
                .build();
    }
}
