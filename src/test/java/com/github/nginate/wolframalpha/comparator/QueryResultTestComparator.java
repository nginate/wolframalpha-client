package com.github.nginate.wolframalpha.comparator;

import com.github.nginate.wolframalpha.model.Pod;
import com.github.nginate.wolframalpha.model.QueryResult;

import java.util.Comparator;
import java.util.List;

import static com.github.nginate.wolframalpha.comparator.TestComparators.*;

public class QueryResultTestComparator implements Comparator<QueryResult> {
    @Override
    public int compare(QueryResult o1, QueryResult o2) {
        return Comparator.comparing(QueryResult::getSuccess)
                .thenComparing(QueryResult::getError)
                .thenComparing(QueryResult::getNumpods)
                .thenComparing(QueryResult::getVersion)
                .thenComparing(QueryResult::getDatatypes, collectionComparator())
                .thenComparing(queryResult -> {
                    List<Pod> pods = queryResult.getPods();
                    pods.sort(Comparator.comparing(Pod::getPosition));
                    return pods;
                }, collectionComparator(podComparator()))
                .thenComparing(QueryResult::getAssumptions, assumptionsComparator())
                .compare(o1, o2);
    }
}
