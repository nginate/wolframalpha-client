package com.github.nginate.wolframalpha.util;

import com.github.nginate.wolframalpha.retrofit.CDATAAdapter;
import com.github.nginate.wolframalpha.model.Cell;
import com.github.nginate.wolframalpha.model.QueryResult;
import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
import feign.jaxb.JAXBContextFactory;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Test;

import java.io.StringWriter;
import java.util.function.Supplier;

import static io.github.benas.randombeans.EnhancedRandomBuilder.aNewEnhancedRandomBuilder;
import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.assertj.core.api.Assertions.assertThat;

public class SerializationUtilTest {
    @Test
    public void testPojoDeserialization() throws Exception {
        EnhancedRandom enhancedRandom = aNewEnhancedRandomBuilder()
                .exclude(ElementNSImpl.class)
                .collectionSizeRange(1, 5)
                .randomize(Cell.class, cellSupplier())
                .build();
        QueryResult queryResult = enhancedRandom.nextObject(QueryResult.class);
        JAXBContextFactory contextFactory = SerializationUtil.buildJAXBFactory();

        StringWriter stringWriter = new StringWriter();
        contextFactory.createMarshaller(QueryResult.class).marshal(queryResult, stringWriter);
        String xml = stringWriter.toString();

        QueryResult deserialized = SerializationUtil.fromXML(xml, QueryResult.class);
        assertThat(deserialized).isEqualTo(queryResult);
    }

    private Supplier<Cell> cellSupplier() {
        return () -> {
            try {
                Cell cell = new Cell();
                cell.setCompressed(random(boolean.class));
                cell.setData(new CDATAAdapter().marshal(random(String.class)));
                return cell;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
