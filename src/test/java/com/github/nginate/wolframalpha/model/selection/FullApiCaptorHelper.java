package com.github.nginate.wolframalpha.model.selection;

import com.github.nginate.wolframalpha.full.FullResultsApi;
import com.github.nginate.wolframalpha.model.ResultFormat;
import com.github.nginate.wolframalpha.model.params.GeoCoordinates;
import lombok.Getter;
import org.mockito.ArgumentCaptor;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@Getter
public class FullApiCaptorHelper {
    private final Set<ArgumentCaptor<?>> captors = new HashSet<>();

    private ArgumentCaptor<List<ResultFormat>> formatsCaptor;
    private ArgumentCaptor<List<String>> assumptionsCaptor;
    private ArgumentCaptor<String> locationCaptor;
    private ArgumentCaptor<GeoCoordinates> coordinatesCaptor;
    private ArgumentCaptor<String> ipCaptor;
    private ArgumentCaptor<List<String>> podStatesCaptor;
    private ArgumentCaptor<Float> asyncTimeoutCaptor;
    private ArgumentCaptor<Float> scanTimeoutCaptor;
    private ArgumentCaptor<Float> podTimeoutCaptor;
    private ArgumentCaptor<Float> formatTimeoutCaptor;
    private ArgumentCaptor<Float> parseTimeoutCaptor;
    private ArgumentCaptor<Float> totalTimeoutCaptor;
    private ArgumentCaptor<List<String>> includedPodsCaptor;
    private ArgumentCaptor<List<String>> excludedPodsCaptor;
    private ArgumentCaptor<List<String>> podTitlesCaptor;
    private ArgumentCaptor<List<Integer>> indexesCaptor;
    private ArgumentCaptor<List<String>> scannersCaptor;

    private FullApiCaptorHelper() {
        formatsCaptor = ArgumentCaptor.forClass(List.class);
        assumptionsCaptor = ArgumentCaptor.forClass(List.class);
        locationCaptor = ArgumentCaptor.forClass(String.class);
        coordinatesCaptor = ArgumentCaptor.forClass(GeoCoordinates.class);
        ipCaptor = ArgumentCaptor.forClass(String.class);
        podStatesCaptor = ArgumentCaptor.forClass(List.class);
        asyncTimeoutCaptor = ArgumentCaptor.forClass(Float.class);
        scanTimeoutCaptor = ArgumentCaptor.forClass(Float.class);
        podTimeoutCaptor = ArgumentCaptor.forClass(Float.class);
        formatTimeoutCaptor = ArgumentCaptor.forClass(Float.class);
        parseTimeoutCaptor = ArgumentCaptor.forClass(Float.class);
        totalTimeoutCaptor = ArgumentCaptor.forClass(Float.class);
        includedPodsCaptor = ArgumentCaptor.forClass(List.class);
        excludedPodsCaptor = ArgumentCaptor.forClass(List.class);
        podTitlesCaptor = ArgumentCaptor.forClass(List.class);
        indexesCaptor = ArgumentCaptor.forClass(List.class);
        scannersCaptor = ArgumentCaptor.forClass(List.class);

        captors.add(formatsCaptor);
        captors.add(assumptionsCaptor);
        captors.add(locationCaptor);
        captors.add(coordinatesCaptor);
        captors.add(ipCaptor);
        captors.add(podStatesCaptor);
        captors.add(asyncTimeoutCaptor);
        captors.add(scanTimeoutCaptor);
        captors.add(podTimeoutCaptor);
        captors.add(formatTimeoutCaptor);
        captors.add(parseTimeoutCaptor);
        captors.add(totalTimeoutCaptor);
        captors.add(includedPodsCaptor);
        captors.add(excludedPodsCaptor);
        captors.add(podTitlesCaptor);
        captors.add(indexesCaptor);
        captors.add(scannersCaptor);
    }

    public static FullApiCaptorHelper catchApiCallArguments(FullResultsApi fullResultsApi) {
        return new FullApiCaptorHelper().catchArguments(fullResultsApi);
    }

    private FullApiCaptorHelper catchArguments(FullResultsApi fullResultsApi) {
        verify(fullResultsApi).getFullResults(anyString(), anyString(), formatsCaptor.capture(),
                assumptionsCaptor.capture(), locationCaptor.capture(), coordinatesCaptor.capture(), ipCaptor.capture(),
                podStatesCaptor.capture(), asyncTimeoutCaptor.capture(), scanTimeoutCaptor.capture(),
                podTimeoutCaptor.capture(), formatTimeoutCaptor.capture(), parseTimeoutCaptor.capture(),
                totalTimeoutCaptor.capture(), includedPodsCaptor.capture(), excludedPodsCaptor.capture(),
                podTitlesCaptor.capture(), indexesCaptor.capture(), scannersCaptor.capture());
        return this;
    }

    public FullApiCaptorHelper captorHadCaught(Function<FullApiCaptorHelper, ArgumentCaptor> extractor,
                                               Object... expected) {
        ArgumentCaptor argumentCaptor = extractor.apply(this);
        Object caughtValue = argumentCaptor.getValue();
        if (caughtValue instanceof Collection) {
            assertThat(((Collection) caughtValue)).containsExactly(expected);
        } else {
            assertThat(expected).contains(caughtValue).hasSize(1);
        }
        captors.remove(argumentCaptor);
        return this;
    }

    public void allCaptorsReceivedNulls() {
        captors.forEach(argumentCaptor -> {
            Object value = argumentCaptor.getValue();
            assertThat(value).isNull();
        });
    }
}
