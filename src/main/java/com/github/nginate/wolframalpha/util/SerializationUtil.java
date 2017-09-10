package com.github.nginate.wolframalpha.util;

import feign.jaxb.JAXBContextFactory;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@UtilityClass
public class SerializationUtil {
    private static final JAXBContextFactory jaxbContextFactory = buildJAXBFactory();

    @SneakyThrows
    public static <T> T fromXML(String xml, Type type) {
        InputSource inputSource = new InputSource(new StringReader(xml));
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(false);
        DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(inputSource);

        return fromXML(document.getDocumentElement(), type);
    }

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
