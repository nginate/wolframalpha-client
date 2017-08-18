package com.github.nginate.wolframalpha;

import com.github.nginate.wolframalpha.full.FullResultsApi;
import com.github.nginate.wolframalpha.model.Pod;
import com.github.nginate.wolframalpha.model.QueryResult;
import com.github.nginate.wolframalpha.model.Subpod;
import feign.Feign;
import feign.Logger;
import feign.jaxb.JAXBContextFactory;
import feign.jaxb.JAXBDecoder;
import feign.slf4j.Slf4jLogger;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;

public class FullResultsApiIT {
    private FullResultsApi fullResultsApi;
    private String token;

    @Before
    public void setUp() throws Exception {
        JAXBContextFactory jaxbFactory = new JAXBContextFactory.Builder()
                .withMarshallerJAXBEncoding("UTF-8")
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
}
