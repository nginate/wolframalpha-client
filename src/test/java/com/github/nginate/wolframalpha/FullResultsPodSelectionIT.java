package com.github.nginate.wolframalpha;

import com.github.nginate.wolframalpha.full.FullResultsApi;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

public class FullResultsPodSelectionIT {
    private FullResultsApi fullResultsApi;
    private String token;

    @Before
    public void setUp() throws Exception {
        fullResultsApi = ClientFactory.fullResultsApi();
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/application.properties"));
        token = properties.getProperty("api.token");
    }

    @Test
    public void shouldReturnOnlyIncludedPods() throws Exception {

    }
}
