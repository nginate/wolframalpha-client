package com.github.nginate.wolframalpha.model.selection;

import com.github.nginate.wolframalpha.full.FullResultsApi;
import com.github.nginate.wolframalpha.model.QueryResult;
import com.github.nginate.wolframalpha.model.ResultFormat;
import com.github.nginate.wolframalpha.model.params.GeoCoordinates;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.github.nginate.wolframalpha.model.selection.FullApiCaptorHelper.catchApiCallArguments;
import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class SelectorTest {
    private FullResultsApi client;
    private Selector selector;

    private QueryResult queryResult;

    private String input;
    private String token;

    @Before
    public void setUp() throws Exception {
        client = mock(FullResultsApi.class);
        when(client.withCustomSelection()).thenCallRealMethod();
        selector = client.withCustomSelection();

        input = random(String.class);
        token = random(String.class);
        queryResult = new QueryResult();

        when(client.getFullResults(eq(input), eq(token), any(), any(), any(), any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(queryResult);

    }

    @After
    public void tearDown() throws Exception {
        verify(client).withCustomSelection();
        verifyNoMoreInteractions(client);
    }

    @Test
    public void verifyResultFormats() throws Exception {
        ResultFormat resultFormat = random(ResultFormat.class);

        QueryResult results = selector.withResultFormat(resultFormat).getResults(input, token);
        assertThat(results).isNotNull().isEqualTo(queryResult);

        catchApiCallArguments(client)
                .captorHadCaught(FullApiCaptorHelper::getFormatsCaptor, resultFormat)
                .allCaptorsReceivedNulls();
    }

    @Test
    public void verifyAssumptions() throws Exception {
        String assumption = random(String.class);

        QueryResult results = selector.withAssumption(assumption).getResults(input, token);
        assertThat(results).isNotNull().isEqualTo(queryResult);

        catchApiCallArguments(client)
                .captorHadCaught(FullApiCaptorHelper::getAssumptionsCaptor, assumption)
                .allCaptorsReceivedNulls();
    }

    @Test
    public void verifyLocation() throws Exception {
        String location = random(String.class);

        QueryResult results = selector.asLocatedIn(location).getResults(input, token);
        assertThat(results).isNotNull().isEqualTo(queryResult);

        catchApiCallArguments(client)
                .captorHadCaught(FullApiCaptorHelper::getLocationCaptor, location)
                .allCaptorsReceivedNulls();
    }

    @Test
    public void verifyGeoCoordinates() throws Exception {
        double lat = random(long.class);
        double lon = random(long.class);

        QueryResult results = selector.asLocatedIn(lat, lon).getResults(input, token);
        assertThat(results).isNotNull().isEqualTo(queryResult);

        catchApiCallArguments(client)
                .captorHadCaught(FullApiCaptorHelper::getCoordinatesCaptor, new GeoCoordinates(lat, lon))
                .allCaptorsReceivedNulls();
    }

    @Test
    public void verifyLocationIP() throws Exception {
        String locationIP = random(String.class);

        QueryResult results = selector.asLocatedAt(locationIP).getResults(input, token);
        assertThat(results).isNotNull().isEqualTo(queryResult);

        catchApiCallArguments(client)
                .captorHadCaught(FullApiCaptorHelper::getIpCaptor, locationIP)
                .allCaptorsReceivedNulls();
    }

    @Test
    public void verifyPodStates() throws Exception {
        String state = random(String.class);

        QueryResult results = selector.withPodState(state).getResults(input, token);
        assertThat(results).isNotNull().isEqualTo(queryResult);

        catchApiCallArguments(client)
                .captorHadCaught(FullApiCaptorHelper::getPodStatesCaptor, state)
                .allCaptorsReceivedNulls();
    }

    @Test
    public void verifyAsync() throws Exception {
        Float timeout = random(Float.class);

        QueryResult results = selector.usingAsyncTimeout(timeout).getResults(input, token);
        assertThat(results).isNotNull().isEqualTo(queryResult);

        catchApiCallArguments(client)
                .captorHadCaught(FullApiCaptorHelper::getAsyncTimeoutCaptor, timeout)
                .allCaptorsReceivedNulls();
    }

    @Test
    public void verifyScanTimeout() throws Exception {
        Float timeout = random(Float.class);

        QueryResult results = selector.usingScanTimeout(timeout).getResults(input, token);
        assertThat(results).isNotNull().isEqualTo(queryResult);

        catchApiCallArguments(client)
                .captorHadCaught(FullApiCaptorHelper::getScanTimeoutCaptor, timeout)
                .allCaptorsReceivedNulls();
    }

    @Test
    public void verifyPodTimeout() throws Exception {
        Float timeout = random(Float.class);

        QueryResult results = selector.usingPodTimeout(timeout).getResults(input, token);
        assertThat(results).isNotNull().isEqualTo(queryResult);

        catchApiCallArguments(client)
                .captorHadCaught(FullApiCaptorHelper::getPodTimeoutCaptor, timeout)
                .allCaptorsReceivedNulls();
    }

    @Test
    public void verifyParseTimeout() throws Exception {
        Float timeout = random(Float.class);

        QueryResult results = selector.usingParseTimeout(timeout).getResults(input, token);
        assertThat(results).isNotNull().isEqualTo(queryResult);

        catchApiCallArguments(client)
                .captorHadCaught(FullApiCaptorHelper::getParseTimeoutCaptor, timeout)
                .allCaptorsReceivedNulls();
    }

    @Test
    public void verifyTotalTimeout() throws Exception {
        Float timeout = random(Float.class);

        QueryResult results = selector.usingTotalTimeout(timeout).getResults(input, token);
        assertThat(results).isNotNull().isEqualTo(queryResult);

        catchApiCallArguments(client)
                .captorHadCaught(FullApiCaptorHelper::getTotalTimeoutCaptor, timeout)
                .allCaptorsReceivedNulls();
    }

    @Test
    public void verifyIncludedPod() throws Exception {
        String pod = random(String.class);

        QueryResult results = selector.withPodId(pod).getResults(input, token);
        assertThat(results).isNotNull().isEqualTo(queryResult);

        catchApiCallArguments(client)
                .captorHadCaught(FullApiCaptorHelper::getIncludedPodsCaptor, pod)
                .allCaptorsReceivedNulls();
    }

    @Test
    public void verifyExcludedPod() throws Exception {
        String pod = random(String.class);

        QueryResult results = selector.withoutPodId(pod).getResults(input, token);
        assertThat(results).isNotNull().isEqualTo(queryResult);

        catchApiCallArguments(client)
                .captorHadCaught(FullApiCaptorHelper::getExcludedPodsCaptor, pod)
                .allCaptorsReceivedNulls();
    }

    @Test
    public void verifyPodTitle() throws Exception {
        String pod = random(String.class);

        QueryResult results = selector.withPodTitle(pod).getResults(input, token);
        assertThat(results).isNotNull().isEqualTo(queryResult);

        catchApiCallArguments(client)
                .captorHadCaught(FullApiCaptorHelper::getPodTitlesCaptor, pod)
                .allCaptorsReceivedNulls();
    }

    @Test
    public void verifyPodIndex() throws Exception {
        Integer pod = random(Integer.class);

        QueryResult results = selector.withPodIndex(pod).getResults(input, token);
        assertThat(results).isNotNull().isEqualTo(queryResult);

        catchApiCallArguments(client)
                .captorHadCaught(FullApiCaptorHelper::getIndexesCaptor, pod)
                .allCaptorsReceivedNulls();
    }

    @Test
    public void verifyPodScanner() throws Exception {
        String scanner = random(String.class);

        QueryResult results = selector.withPodsUsingScanner(scanner).getResults(input, token);
        assertThat(results).isNotNull().isEqualTo(queryResult);

        catchApiCallArguments(client)
                .captorHadCaught(FullApiCaptorHelper::getScannersCaptor, scanner)
                .allCaptorsReceivedNulls();
    }
}
