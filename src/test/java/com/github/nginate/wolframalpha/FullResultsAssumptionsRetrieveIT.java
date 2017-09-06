package com.github.nginate.wolframalpha;

import com.github.nginate.wolframalpha.full.FullResultsApi;
import com.github.nginate.wolframalpha.model.Assumptions;
import com.github.nginate.wolframalpha.model.Assumptions.Assumption.AssumptionType;
import com.github.nginate.wolframalpha.model.QueryResult;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Properties;

import static com.github.nginate.wolframalpha.model.Assumptions.Assumption.AssumptionType.*;
import static com.github.nginate.wolframalpha.util.AssumptionUtil.findAssumptionByType;
import static org.assertj.core.api.Assertions.assertThat;

public class FullResultsAssumptionsRetrieveIT {
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
    public void checkClashRetrieval() throws Exception {
        testAssumptionForQueryAndType("pi", token, CLASH);
    }

    @Test
    public void checkUnitRetrieval() throws Exception {
        testAssumptionForQueryAndType("1m", token, UNIT);
    }

    @Test
    public void checkAngleUnitRetrieval() throws Exception {
        testAssumptionForQueryAndType("sin(30)", token, ANGLE_UNIT);
    }

    @Test
    public void checkFunctionRetrieval() throws Exception {
        testAssumptionForQueryAndType("log 20", token, FUNCTION);
    }

    @Test
    public void checkMultiClashRetrieval() throws Exception {
        QueryResult result = fullResultsApi.getFullResults("delta sigma", token);

        Assumptions assumptions = result.getAssumptions();
        checkAssumptionCount(assumptions);

        List<Assumptions.Assumption> assumptionList = findAssumptionByType(assumptions, MULTI_CLASH);

        assertThat(assumptionList).hasSize(1);
        checkMultiClashAssumptionValues(assumptionList);
    }

    @Test
    public void checkSubCategoryRetrieval() throws Exception {
        testAssumptionForQueryAndType("hamburger", token, SUB_CATEGORY);
    }

    @Test
    public void checkAttributeRetrieval() throws Exception {
        testAssumptionForQueryAndType("hamburger", token, ATTRIBUTE);
    }

    @Test
    public void checkTimeAmOrPmRetrieval() throws Exception {
        testAssumptionForQueryAndType("3:00", token, TIME_AM_OR_PM);
    }

    @Test
    public void checkDateOrderRetrieval() throws Exception {
        testAssumptionForQueryAndType("12/11/1996", token, DATE_ORDER);
    }

    @Test
    public void checkListOrTimesRetrieval() throws Exception {
        testAssumptionForQueryAndType("3 x", token, LIST_OR_TIMES);
    }

    @Test
    public void checkListOrNumberRetrieval() throws Exception {
        testAssumptionForQueryAndType("1,234.5", token, LIST_OR_NUMBER);
    }

    @Test
    public void checkCoordinateSystemRetrieval() throws Exception {
        testAssumptionForQueryAndType("div(x rho,y z,z x)", token, COORDINATE_SYSTEM);
    }

    @Test
    public void checkIRetrieval() throws Exception {
        testAssumptionForQueryAndType("2i", token, I);
    }

    @Test
    public void checkNumberBaseRetrieval() throws Exception {
        testAssumptionForQueryAndType("1011001110", token, NUMBER_BASE);
    }

    @Test
    public void checkMixedFractionRetrieval() throws Exception {
        testAssumptionForQueryAndType("3 1/2", token, MIXED_FRACTION);
    }

    @Test
    public void checkMortalityYearDOBRetrieval() throws Exception {
        testAssumptionForQueryAndType("life expectancy France 1910", token, MORTALITY_YEAR_DOB);
    }

    @Test
    public void checkTideStationRetrieval() throws Exception {
        testAssumptionForQueryAndType("tides Seattle", token, TIDE_STATION);
    }

    @Test
    public void checkFormulaSelectRetrieval() throws Exception {
        testAssumptionForQueryAndType("Doppler shift", token, FORMULA_SELECT);
    }

    @Test
    public void checkFormulaSolveRetrieval() throws Exception {
        testAssumptionForQueryAndType("Doppler shift", token, FORMULA_SOLVE);
    }

    @Test
    public void checkFormulaVariableOptionRetrieval() throws Exception {
        testAssumptionForQueryAndType("Doppler shift", token, FORMULA_VARIABLE_OPTION);
    }

    @Test
    public void checkFormulaVariableIncludeRetrieval() throws Exception {
        testAssumptionForQueryAndType("Doppler shift", token, FORMULA_VARIABLE_INCLUDE);
    }

    @Test
    public void checkFormulaVariableRetrieval() throws Exception {
        QueryResult result = fullResultsApi.getFullResults("Doppler shift", token);

        Assumptions assumptions = result.getAssumptions();
        checkAssumptionCount(assumptions);

        List<Assumptions.Assumption> assumptionList = findAssumptionByType(assumptions, FORMULA_VARIABLE);

        assertThat(assumptionList).hasSize(2);
        checkAssumptionFormulaValues(assumptionList);
    }

    private void testAssumptionForQueryAndType(String query, String token, AssumptionType type) {
        QueryResult result = fullResultsApi.getFullResults(query, token);

        Assumptions assumptions = result.getAssumptions();
        checkAssumptionCount(assumptions);

        List<Assumptions.Assumption> assumptionList = findAssumptionByType(assumptions, type);

        assertThat(assumptionList).hasSize(1);
        checkAssumptionValues(assumptionList);
    }

    private void checkAssumptionCount(Assumptions assumptions) {
        assertThat(assumptions.getCount()).isGreaterThan(0);
        assertThat(assumptions.getAssumptions()).isNotEmpty().hasSize(assumptions.getCount());
    }

    private void checkAssumptionValues(List<Assumptions.Assumption> assumptions) {
        Assumptions.Assumption assumption = assumptions.get(0);

        assertThat(assumption.getType()).isNotNull();
        assertThat(assumption.getCount()).isNotNull();
        assertThat(assumption.getTemplate()).isNotNull();
        assertThat(assumption.getValues()).isNotNull();
        if (assumption.getWord() != null) {
            assertThat(assumption.getWord()).isNotEmpty();
        }
        assertThat(assumption.getCount()).isGreaterThan(0);
        assertThat(assumption.getValues()).isNotEmpty().hasSize(assumption.getCount());

        for (Assumptions.Assumption.AssumptionValue value : assumption.getValues()) {
            assertThat(value).hasNoNullFieldsOrProperties();
        }
    }

    private void checkMultiClashAssumptionValues(List<Assumptions.Assumption> assumptions) {
        Assumptions.Assumption assumption = assumptions.get(0);

        assertThat(assumption.getType()).isNotNull();
        assertThat(assumption.getCount()).isNotNull();
        assertThat(assumption.getTemplate()).isNotNull();
        assertThat(assumption.getValues()).isNotNull();
        if (assumption.getWord() != null) {
            assertThat(assumption.getWord()).isEmpty();
        }
        assertThat(assumption.getCount()).isGreaterThan(0);
        assertThat(assumption.getValues()).isNotEmpty().hasSize(assumption.getCount());

        for (Assumptions.Assumption.AssumptionValue value : assumption.getValues()) {
            assertThat(value).hasNoNullFieldsOrProperties();
        }
    }

    private void checkAssumptionFormulaValues(List<Assumptions.Assumption> assumptions) {
        Assumptions.Assumption assumption = assumptions.get(0);

        assertThat(assumption.getType()).isNotNull();
        assertThat(assumption.getCount()).isNotNull();
        assertThat(assumption.getTemplate()).isNull();
        assertThat(assumption.getValues()).isNotNull();
        if (assumption.getWord() != null) {
            assertThat(assumption.getWord()).isNotEmpty();
        }
        assertThat(assumption.getCount()).isGreaterThan(0);
        assertThat(assumption.getValues()).isNotEmpty().hasSize(assumption.getCount());

        for (Assumptions.Assumption.AssumptionValue value : assumption.getValues()) {
            assertThat(value).hasNoNullFieldsOrProperties();
        }
    }
}
