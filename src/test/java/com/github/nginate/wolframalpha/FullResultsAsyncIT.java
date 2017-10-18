package com.github.nginate.wolframalpha;

import com.github.nginate.wolframalpha.full.FullResultsApi;
import com.github.nginate.wolframalpha.model.Pod;
import com.github.nginate.wolframalpha.model.QueryResult;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Properties;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class FullResultsAsyncIT {
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
    public void asyncPodsNotEmpty() throws Exception {
        QueryResult result = fullResultsApi.withCustomSelection()
                .usingAsyncTimeout(.1f)
                .getResults("france", token);
        assertThat(getAsyncPods(result)).isNotEmpty();
    }

    @Test
    public void asyncPodsEmpty() throws Exception {
        QueryResult result = fullResultsApi.withCustomSelection()
                .usingAsyncTimeout(.0f)
                .getResults("france", token);
        assertThat(getAsyncPods(result)).isEmpty();

        QueryResult result2 = fullResultsApi.getFullResults("france", token);
        assertThat(getAsyncPods(result2)).isEmpty();
    }

    /**
     * Disabling till the API will be fixed on server side
     */
    @Ignore
    @Test
    public void retrieveAsyncPod() throws Exception {
        QueryResult result = fullResultsApi.withCustomSelection()
                .usingAsyncTimeout(.1f)
                 .getResults("birds", token);
        for (Pod pod : getAsyncPods(result)) {
            String async = pod.getAsync();
            assertThat(async).isNotEmpty();

            Pod retrieved = fullResultsApi.loadPodAsync(async);
            assertThat(retrieved.getTitle()).isNotEmpty();
            assertThat(retrieved.getError()).isFalse();
            assertThat(retrieved.getPosition()).isGreaterThan(0);
            assertThat(retrieved.getScanner()).isNotEmpty();
            assertThat(retrieved.getId()).isNotEmpty();
            assertThat(retrieved.getNumsubpods()).isGreaterThan(0);
            assertThat(retrieved.getPrimary()).isNotNull();
            assertThat(retrieved.getSubpods()).isNotEmpty().hasSize(retrieved.getNumsubpods());
        }
    }

    private List<Pod> getAsyncPods(QueryResult result) {
        return result.getPods().stream().filter(pod -> pod.getAsync() != null).collect(toList());
    }
}
