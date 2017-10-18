package com.github.nginate.wolframalpha;

import com.github.nginate.wolframalpha.full.FullResultsApi;
import com.github.nginate.wolframalpha.model.Pod;
import com.github.nginate.wolframalpha.model.QueryResult;
import com.github.nginate.wolframalpha.model.Subpod;
import com.github.nginate.wolframalpha.util.parser.TimeParser;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class FullResultsLocationIT {
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
    public void testUTCTimeByIP() throws Exception {
        String saintHelenaIP = "197.155.128.0"; // UTC
        String newZealandIP = "27.252.0.0"; // UTC+12
        String request = "current time";

        QueryResult utcResult = fullResultsApi.getFullResultsForIP(request, token, saintHelenaIP);
        QueryResult utcOppositeResult = fullResultsApi.getFullResultsForIP(request, token, newZealandIP);

        verifyTimeShiftAccordingToLocation(utcResult, utcOppositeResult);
    }

    @Test
    public void testUTCTimeByIPWithSelector() throws Exception {
        String saintHelenaIP = "197.155.128.0"; // UTC
        String newZealandIP = "27.252.0.0"; // UTC+12
        String request = "current time";

        QueryResult utcResult = fullResultsApi.withCustomSelection()
                .asLocatedAt(saintHelenaIP)
                .getResults(request, token);
        QueryResult utcOppositeResult = fullResultsApi.withCustomSelection()
                .asLocatedAt(newZealandIP)
                .getResults(request, token);

        verifyTimeShiftAccordingToLocation(utcResult, utcOppositeResult);
    }

    @Test
    public void testUTCTimeByLocation() throws Exception {
        String request = "current time";

        QueryResult utcResult = fullResultsApi.getFullResults(request, token, "Saint Helena");
        QueryResult utcOppositeResult = fullResultsApi.getFullResults(request, token, "New Zealand");

        verifyTimeShiftAccordingToLocation(utcResult, utcOppositeResult);
    }

    @Test
    public void testUTCTimeByLocationWithSelector() throws Exception {
        String request = "current time";

        QueryResult utcResult = fullResultsApi.withCustomSelection()
                .asLocatedIn("Saint Helena")
                .getResults(request, token);
        QueryResult utcOppositeResult = fullResultsApi.withCustomSelection()
                .asLocatedIn("New Zealand")
                .getResults(request, token);

        verifyTimeShiftAccordingToLocation(utcResult, utcOppositeResult);
    }

    @Test
    public void testUTCTimeByCoordinates() throws Exception {
        String request = "current time";

        QueryResult utcResult = fullResultsApi.getFullResults(request, token, -15.945996, -5.687040);
        QueryResult utcOppositeResult = fullResultsApi.getFullResults(request, token, -41.292973, 174.764016);

        verifyTimeShiftAccordingToLocation(utcResult, utcOppositeResult);
    }

    @Test
    public void testUTCTimeByCoordinatesWithSelector() throws Exception {
        String request = "current time";

        QueryResult utcResult = fullResultsApi.withCustomSelection()
                .asLocatedIn(-15.945996, -5.687040)
                .getResults(request, token);
        QueryResult utcOppositeResult = fullResultsApi.withCustomSelection()
                .asLocatedIn(-41.292973, 174.764016)
                .getResults(request, token);

        verifyTimeShiftAccordingToLocation(utcResult, utcOppositeResult);
    }

    private void verifyTimeShiftAccordingToLocation(QueryResult utcResult, QueryResult utcOppositeResult) {
        assertThat(utcResult.getSuccess()).isTrue();

        String utcTime = extractPlainTextData(utcResult);
        Date utcDate = TimeParser.parseDateTime(utcTime);
        assertThat(utcDate).isNotNull();

        assertThat(utcOppositeResult.getSuccess()).isTrue();

        String utcOppositeTime = extractPlainTextData(utcOppositeResult);
        Date utcOppositeDate = TimeParser.parseDateTime(utcOppositeTime);
        assertThat(utcOppositeDate).isNotNull();

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(utcDate);
        int utcHour = calendar.get(Calendar.HOUR_OF_DAY);

        calendar.setTime(utcOppositeDate);
        int utcOppositeHour = calendar.get(Calendar.HOUR_OF_DAY);

        assertThat(utcOppositeHour).isEqualTo((utcHour + 12) % 24);
    }

    private String extractPlainTextData(QueryResult result) {
        return result.getPods().stream()
                .filter(Pod::getPrimary)
                .flatMap(pod -> pod.getSubpods().stream())
                .filter(Subpod::getPrimary)
                .map(Subpod::getPlaintext)
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
