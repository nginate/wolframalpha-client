package com.github.nginate.wolframalpha;

import com.github.nginate.wolframalpha.exceptions.InvalidAppIdException;
import com.github.nginate.wolframalpha.exceptions.MissingAppIdException;
import com.github.nginate.wolframalpha.model.Units;
import com.github.nginate.wolframalpha.shortanswer.ShortAnswersApi;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class ShortAnswersApiIT {
    private static final String EXAMPLE_INPUT = "What are the colors of the Belgian flag?";
    private static final String EXAMPLE_INPUT_RESPONSE = "three equal vertical bands of black hoist side, yellow, and" +
            " red; the design was based on the flag of France";

    private ShortAnswersApi shortAnswersApi;
    private String token;

    @Before
    public void setUp() throws Exception {
        shortAnswersApi = ClientFactory.shortAnswersApi();
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/application.properties"));
        token = properties.getProperty("api.token");
    }

    @Test
    public void nonEmptyResponse() throws Exception {
        String shortAnswer = shortAnswersApi.getShortAnswer(EXAMPLE_INPUT, token, Units.METRIC, 30)
                .execute()
                .body();

        assertThat(shortAnswer).isNotBlank().isEqualTo(EXAMPLE_INPUT_RESPONSE);
    }

    @Test
    public void nonEmptyResponseWithDefaultTimeout() throws Exception {
        String shortAnswer = shortAnswersApi.getShortAnswer(EXAMPLE_INPUT, token, Units.METRIC).execute().body();

        assertThat(shortAnswer).isNotBlank().isEqualTo(EXAMPLE_INPUT_RESPONSE);
    }

    @Test(expected = MissingAppIdException.class)
    public void emptyToken() throws Exception {
        shortAnswersApi.getShortAnswer(EXAMPLE_INPUT, null, Units.METRIC, 30).execute();
    }

    @Test(expected = InvalidAppIdException.class)
    public void wrongToken() throws Exception {
        shortAnswersApi.getShortAnswer(EXAMPLE_INPUT, "null", Units.METRIC, 30).execute();
    }
}
