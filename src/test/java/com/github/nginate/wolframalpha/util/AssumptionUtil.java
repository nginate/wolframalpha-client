package com.github.nginate.wolframalpha.util;

import com.github.nginate.wolframalpha.model.Assumptions;
import com.github.nginate.wolframalpha.model.Assumptions.Assumption.AssumptionType;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class AssumptionUtil {
    public static List<Assumptions.Assumption> findAssumptionByType(Assumptions assumptions, AssumptionType type) {
        return assumptions.getAssumptions().stream()
                .filter(assumption -> assumption.getType() == type)
                .collect(Collectors.toList());
    }
}
