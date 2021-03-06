package com.github.nginate.wolframalpha;

import com.github.nginate.wolframalpha.exceptions.InvalidAppIdException;
import com.github.nginate.wolframalpha.exceptions.MissingAppIdException;
import com.github.nginate.wolframalpha.simple.SimpleApi;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleApiIT {
    private SimpleApi simpleApi;
    private String token;

    @Before
    public void setUp() throws Exception {
        simpleApi = ClientFactory.simpleApiClient();
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/application.properties"));
        token = properties.getProperty("api.token");
    }

    @Test
    public void requestFullQuery() throws Exception {
        byte[] image = simpleApi.query("What day is it now", token, null, null, null, 12, 400, null, 30)
                .execute()
                .body();
        assertThat(image).isNotNull().isNotEmpty();
    }

    @Test
    public void requestSimpleQuery() throws Exception {
        byte[] image = simpleApi.query("What day is it now", token, 12, 400, 30)
                .execute()
                .body();
        assertThat(image).isNotNull().isNotEmpty();
    }

    @Test
    public void requestWidthChangesSize() throws Exception {
        int width = 400;
        byte[] image = simpleApi.query("What day is it now", token, 12, width, 30)
                .execute()
                .body();

        byte[] wideImage = simpleApi.query("What day is it now", token, 12, width * 2, 30)
                .execute()
                .body();
        assertThat(wideImage.length).isGreaterThan(image.length);
    }

    @Test
    public void requestFontChangesSize() throws Exception {
        int width = 400;
        int fontSize = 12;
        byte[] image = simpleApi.query("What day is it now", token, fontSize, width, 30)
                .execute()
                .body();

        byte[] wideImage = simpleApi.query("What day is it now", token, fontSize * 2, width, 30)
                .execute()
                .body();
        assertThat(wideImage.length).isGreaterThan(image.length);
    }

    @Test(expected = MissingAppIdException.class)
    public void requestWithoutToken() throws Exception {
        simpleApi.query("What day is it now", null, 12, 400, 1).execute();
    }

    @Test(expected = InvalidAppIdException.class)
    public void requestWithWrongToken() throws Exception {
        simpleApi.query("What day is it now", "null", 12, 400, 1).execute();
    }
}
