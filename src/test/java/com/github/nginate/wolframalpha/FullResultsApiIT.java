package com.github.nginate.wolframalpha;

import com.github.nginate.wolframalpha.comparator.QueryResultTestComparator;
import com.github.nginate.wolframalpha.full.FullResultsApi;
import com.github.nginate.wolframalpha.model.*;
import com.github.nginate.wolframalpha.model.States.State;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

import static com.github.nginate.wolframalpha.model.ResultFormat.*;
import static java.lang.Boolean.TRUE;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class FullResultsApiIT {
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
    public void nonEmptyResponse() throws Exception {
        QueryResult result = fullResultsApi.getFullResults("How many minutes are there in an hour", token);

        assertThat(result).isNotNull();
        assertThat(result).hasNoNullFieldsOrPropertiesExcept("assumptions");
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
                .collect(toList());

        assertThat(rectangles).isEmpty();
    }

    @Test
    public void rectanglesNonEmptyForImagemapFormat() throws Exception {
        QueryResult result = fullResultsApi.getFullResults("France", token, IMAGEMAP, IMAGE);
        List<ImageRectangle> rectangles = result.getPods().stream()
                .flatMap(pod -> pod.getSubpods().stream())
                .map(Subpod::getImageMap)
                .filter(Objects::nonNull)
                .flatMap(imageMap -> imageMap.getRectangles().stream())
                .collect(toList());

        assertThat(rectangles).isNotEmpty();
        rectangles.forEach(imageRectangle -> assertThat(imageRectangle).hasNoNullFieldsOrProperties());
    }

    @Test
    public void mathMlNonEmpty() throws Exception {
        QueryResult result = fullResultsApi.getFullResults("france", token, MATHML);
        List<Object> mathMls = result.getPods().stream()
                .flatMap(pod -> pod.getSubpods().stream())
                .map(Subpod::getMathMl)
                .filter(Objects::nonNull)
                .collect(toList());
        assertThat(mathMls).isNotEmpty();
    }

    @Test
    public void audioNonEmpty() throws Exception {
        QueryResult result = fullResultsApi.getFullResults("C major", token, SOUND);
        List<Sounds> sounds = result.getPods().stream()
                .map(Pod::getSounds)
                .collect(toList());

        assertThat(sounds).isNotEmpty();

        for (Sounds sound : sounds) {
            assertThat(sound.getCount()).isGreaterThan(0);
            assertThat(sound.getSounds()).hasSize(sound.getCount());

            for (Sounds.Sound soundEntry : sound.getSounds()) {
                assertThat(soundEntry).hasNoNullFieldsOrProperties();
            }
        }
    }

    @Test
    public void minputResults() throws Exception {
        QueryResult result = fullResultsApi.getFullResults("Continued fraction", token, PLAINTEXT, MINPUT);

        List<String> minputs = result.getPods().stream()
                .flatMap(pod -> pod.getSubpods().stream())
                .map(Subpod::getMinput)
                .filter(Objects::nonNull)
                .collect(toList());
        assertThat(minputs).isNotEmpty();
    }

    @Test
    public void moutputResults() throws Exception {
        QueryResult result = fullResultsApi.getFullResults("Continued fraction", token, PLAINTEXT, MOUTPUT);

        List<String> moutputs = result.getPods().stream()
                .flatMap(pod -> pod.getSubpods().stream())
                .map(Subpod::getMoutput)
                .filter(Objects::nonNull)
                .collect(toList());
        assertThat(moutputs).isNotEmpty();
    }

    @Test
    public void cellResults() throws Exception {
        QueryResult result = fullResultsApi.getFullResults("Continued fraction", token, PLAINTEXT, CELL);

        List<Cell> cells = result.getPods().stream()
                .flatMap(pod -> pod.getSubpods().stream())
                .map(Subpod::getCell)
                .filter(Objects::nonNull)
                .collect(toList());
        assertThat(cells).isNotEmpty();

        cells.forEach(cell -> assertThat(cell).hasNoNullFieldsOrProperties());
    }

    @Test
    public void podStatesNotEmpty() throws Exception {
        QueryResult result = fullResultsApi.getFullResults("weather", token);
        List<States> statesList = result.getPods().stream()
                .map(Pod::getStates)
                .filter(Objects::nonNull)
                .collect(toList());

        verifyStates(statesList);
    }

    @Test
    public void applyRootState() throws Exception {
        String query = "weather";

        QueryResult result = fullResultsApi.getFullResults(query, token);
        Optional<String> input = result.getPods().stream()
                .map(Pod::getStates)
                .filter(Objects::nonNull)
                .flatMap(states -> states.getStates().stream())
                .filter(Objects::nonNull)
                .map(State::getInput)
                .findAny();

        assertThat(input).isPresent();
        QueryResult podStateResult = fullResultsApi.getFullResultsForPodStates(query, token, input.get());
        assertThat(podStateResult).usingComparator(new QueryResultTestComparator()).isNotEqualTo(result);
    }

    @Test
    public void applyInnerListState() throws Exception {
        String query = "weather";

        QueryResult result = fullResultsApi.getFullResults(query, token);
        Optional<String> input = result.getPods().stream()
                .map(Pod::getStates)
                .filter(Objects::nonNull)
                .map(States::getStateList)
                .filter(Objects::nonNull)
                .flatMap(list -> list.getStates().stream())
                .filter(Objects::nonNull)
                .map(State::getInput)
                .findAny();

        assertThat(input).isPresent();
        QueryResult podStateResult = fullResultsApi.getFullResultsForPodStates(query, token, input.get());
        assertThat(podStateResult).usingComparator(new QueryResultTestComparator()).isNotEqualTo(result);
    }

    @Test
    public void subpodStatesNotEmpty() throws Exception {
        QueryResult result = fullResultsApi.getFullResults("inductance of a circular coil", token);
        List<States> statesList = result.getPods().stream()
                .flatMap(pod -> pod.getSubpods().stream())
                .map(Subpod::getStates)
                .filter(Objects::nonNull)
                .collect(toList());
        verifyStates(statesList);
    }

    @Test
    public void applyRootSubpodState() throws Exception {
        String query = "inductance of a circular coil";

        QueryResult result = fullResultsApi.getFullResults(query, token);
        Optional<String> input = result.getPods().stream()
                .flatMap(pod -> pod.getSubpods().stream())
                .map(Subpod::getStates)
                .filter(Objects::nonNull)
                .flatMap(states -> states.getStates().stream())
                .filter(Objects::nonNull)
                .map(State::getInput)
                .findAny();

        assertThat(input).isPresent();
        QueryResult podStateResult = fullResultsApi.getFullResultsForPodStates(query, token, input.get());
        assertThat(podStateResult).usingComparator(new QueryResultTestComparator()).isNotEqualTo(result);
    }

    @Test
    public void emptyInnerListSubpodState() throws Exception {
        String query = "inductance of a circular coil";

        QueryResult result = fullResultsApi.getFullResults(query, token);
        Optional<String> input = result.getPods().stream()
                .flatMap(pod -> pod.getSubpods().stream())
                .map(Subpod::getStates)
                .filter(Objects::nonNull)
                .map(States::getStateList)
                .filter(Objects::nonNull)
                .flatMap(list -> list.getStates().stream())
                .filter(Objects::nonNull)
                .map(State::getInput)
                .findAny();

        assertThat(input).isEmpty();
    }

    @Test
    public void nonEmptyRecalculateResponse() throws Exception {
        QueryResult result = fullResultsApi.getFullResults("How many minutes are there in an hour", token,
                PLAINTEXT);

        assertThat(result).isNotNull();
        assertThat(result).hasNoNullFieldsOrPropertiesExcept("assumptions");
    }

    private void verifyStates(List<States> statesList) {
        assertThat(statesList).isNotEmpty();

        for (States states : statesList) {
            assertThat(states.getStates()).isNotNull()
                    .hasSize(nonNull(states.getStateList()) ? states.getCount() - 1 : states.getCount());
            List<States.StateList> innerLists = statesList.stream()
                    .map(States::getStateList)
                    .filter(Objects::nonNull)
                    .collect(toList());
            for (States.StateList innerList : innerLists) {
                assertThat(innerList).hasNoNullFieldsOrProperties();
                assertThat(innerList.getStates()).hasSize(innerList.getCount());
                innerList.getStates().forEach(state -> assertThat(state).hasNoNullFieldsOrProperties());
            }
        }
    }
}
