package com.github.nginate.wolframalpha;

import com.github.nginate.wolframalpha.comparator.QueryResultTestComparator;
import com.github.nginate.wolframalpha.full.FullResultsApi;
import com.github.nginate.wolframalpha.model.Assumptions.Assumption;
import com.github.nginate.wolframalpha.model.QueryResult;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.nginate.wolframalpha.model.Assumptions.Assumption.AssumptionType.CLASH;
import static com.github.nginate.wolframalpha.util.AssumptionUtil.findAssumptionByType;
import static org.assertj.core.api.Assertions.assertThat;

public class FullResultsAssumptionsApplyIT {
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
    public void applyClashAssumptions() throws Exception {
        String query = "pi";

        QueryResult result = fullResultsApi.getFullResults(query, token);
        Assumption assumption = findAssumptionByType(result.getAssumptions(), CLASH).get(0);

        Set<QueryResult> results = assumption.getValues().stream()
                .map(value -> fullResultsApi.getFullResultsForAssumptions(query, token, value.getInput()))
                .collect(Collectors.toSet());
        assertThat(results).hasSameSizeAs(assumption.getValues());
        assertThat(results).usingElementComparator(new QueryResultTestComparator()).contains(result);
    }
}
