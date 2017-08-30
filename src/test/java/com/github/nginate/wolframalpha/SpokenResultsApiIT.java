package com.github.nginate.wolframalpha;

import com.github.nginate.wolframalpha.model.Units;
import com.github.nginate.wolframalpha.spoken.SpokenResultsApi;
import feign.FeignException;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class SpokenResultsApiIT {
    private SpokenResultsApi spokenResultsApi;
    private String token;

    @Before
    public void setUp() throws Exception {
        spokenResultsApi = ClientFactory.spokenResultsApi();
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/application.properties"));
        token = properties.getProperty("api.token");
    }

    @Test
    public void nonEmptyResponse() throws Exception {
        String shortAnswer = spokenResultsApi.getSpokenResults("How many minutes are there in an hour",
                token, Units.METRIC, 30);

        assertThat(shortAnswer).isNotBlank().isEqualTo("The answer is 60 minutes");
    }

    @Test
    public void nonEmptyResponseWithDefaultTimeout() throws Exception {
        String shortAnswer = spokenResultsApi.getSpokenResults("How many minutes are there in an hour",
                token, Units.METRIC);

        assertThat(shortAnswer).isNotBlank().isEqualTo("The answer is 60 minutes");
    }

    @Test(expected = FeignException.class)
    public void emptyToken() throws Exception {
        spokenResultsApi.getSpokenResults("How many minutes are there in an hour",
                null, Units.METRIC, 30);
    }

    @Test(expected = FeignException.class)
    public void wrongToken() throws Exception {
        spokenResultsApi.getSpokenResults("How many minutes are there in an hour",
                "null", Units.METRIC, 30);
    }
}
