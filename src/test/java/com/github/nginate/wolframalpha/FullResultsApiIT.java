package com.github.nginate.wolframalpha;

import com.github.nginate.wolframalpha.full.FullResultsApi;
import com.github.nginate.wolframalpha.model.*;
import feign.Feign;
import feign.Logger;
import feign.jaxb.JAXBContextFactory;
import feign.jaxb.JAXBDecoder;
import feign.slf4j.Slf4jLogger;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import static com.github.nginate.wolframalpha.model.ResultFormat.*;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;

public class FullResultsApiIT {
    private FullResultsApi fullResultsApi;
    private String token;

    @Before
    public void setUp() throws Exception {
        JAXBContextFactory jaxbFactory = new JAXBContextFactory.Builder()
                .withMarshallerJAXBEncoding(StandardCharsets.UTF_8.name())
                .build();

        fullResultsApi = Feign.builder()
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.FULL)
                .decoder(new JAXBDecoder(jaxbFactory))
                .target(FullResultsApi.class, "https://api.wolframalpha.com");
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/application.properties"));
        token = properties.getProperty("api.token");
    }

    @Test
    public void nonEmptyResponse() throws Exception {
        QueryResult result = fullResultsApi.getFullResults("How many minutes are there in an hour", token);

        assertThat(result).isNotNull();
        assertThat(result).hasNoNullFieldsOrProperties();
    }

    @Test
    public void atLeastOnePrimaryPod() throws Exception {
        QueryResult result = fullResultsApi.getFullResults("How many minutes are there in an hour", token);

        Optional<Pod> pod = result.getPods().stream().filter(p -> TRUE.equals(p.getPrimary())).findAny();
        assertThat(pod).isPresent();
    }

    @Test
    public void primarySubpodInPrimaryPod() throws Exception {
        QueryResult result = fullResultsApi.getFullResults("How many minutes are there in an hour", token);

        Optional<Pod> pod = result.getPods().stream().filter(p -> TRUE.equals(p.getPrimary())).findAny();
        assertThat(pod).isPresent();

        Optional<Subpod> subpod = pod.get().getSubpods().stream().filter(s -> TRUE.equals(s.getPrimary())).findAny();
        assertThat(subpod).isPresent();
    }

    @Test
    public void podNumberEqualsListSize() throws Exception {
        QueryResult result = fullResultsApi.getFullResults("How many minutes are there in an hour", token);

        List<Pod> pods = result.getPods();
        assertThat(pods).hasSize(result.getNumpods());
    }

    @Test
    public void subpodNumberEqualsListSize() throws Exception {
        QueryResult result = fullResultsApi.getFullResults("How many minutes are there in an hour", token);

        for (Pod pod : result.getPods()) {
            assertThat(pod.getSubpods()).hasSize(pod.getNumsubpods());
        }
    }

    @Test
    public void rectanglesEmptyForNonImagemapFormat() throws Exception {
        QueryResult result = fullResultsApi.getFullResults("france", token);
        List<ImageRectangle> rectangles = result.getPods().stream()
                .flatMap(pod -> pod.getSubpods().stream())
                .map(Subpod::getImageMap)
                .filter(Objects::nonNull)
                .flatMap(imageMap -> imageMap.getRectangles().stream())
                .collect(Collectors.toList());

        assertThat(rectangles).isEmpty();
    }

    @Test
    public void rectanglesNonEmptyForImagemapFormat() throws Exception {
        QueryResult result = fullResultsApi.getFullResults("france", token, IMAGEMAP, IMAGE);
        List<ImageRectangle> rectangles = result.getPods().stream()
                .flatMap(pod -> pod.getSubpods().stream())
                .map(Subpod::getImageMap)
                .filter(Objects::nonNull)
                .flatMap(imageMap -> imageMap.getRectangles().stream())
                .collect(Collectors.toList());

        assertThat(rectangles).isNotEmpty();
    }

    @Test
    public void mathMlNonEmpty() throws Exception {
        QueryResult result = fullResultsApi.getFullResults("france", token, MATHML);
        List<Object> mathMls = result.getPods().stream()
                .flatMap(pod -> pod.getSubpods().stream())
                .map(Subpod::getMathMl)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        assertThat(mathMls).isNotEmpty();
    }
}
