package com.xmas.util.filter;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EntityFilter<T> implements Specification<T> {

    public static final String LOWER_LIMIT_PARAM_NAME = "lower";
    public static final String UPPER_LIMIT_PARAM_NAME = "upper";

    private List<Condition> conditions = new ArrayList<>();
    private List<JoinMapCondition> joinMapConditions = new ArrayList<>();
    private Root<T> root;
    private CriteriaBuilder criteriaBuilder;

    private EntityFilter(List<Condition> conditions, List<JoinMapCondition> joinMapConditions) {
        this.conditions = conditions;
        this.joinMapConditions = joinMapConditions;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        initFilter(root, criteriaBuilder);
        List<Predicate> predicates = buildPredicates();

        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }

    private void initFilter(Root<T> root, CriteriaBuilder criteriaBuilder) {
        this.root = root;
        this.criteriaBuilder = criteriaBuilder;
    }

    private List<Predicate> buildPredicates() {
        List<Predicate> predicates = conditions.stream()
                .map(this::buildPredicate)
                .collect(Collectors.toList());

        List<Predicate> jmPredicates = (List<Predicate>) joinMapConditions.stream()
                .map(this::buildMapJoinPredicate)
                .collect(Collectors.toList());

        predicates.addAll(jmPredicates);

        return predicates;
    }

    private <K, V> Predicate buildMapJoinPredicate(JoinMapCondition<K, V> joinMapCondition) {
        MapJoin<T, K, V> mapJoin = root.joinMap(joinMapCondition.getField());

        Predicate keyPredicate = criteriaBuilder.equal(mapJoin.key(), joinMapCondition.getKey());
        Predicate valPredicate = criteriaBuilder.equal(mapJoin.value(), joinMapCondition.getValue());

        return criteriaBuilder.and(keyPredicate, valPredicate);
    }

    private Predicate buildPredicate(Condition condition) {
        switch (condition.getComparison()) {
            case EQUAL:
                return buildEqualPredicate(condition);
            case LIKE:
                return buildLikePredicate(condition);
            case BETWEEN:
                return buildBetweenTimePredicate(condition);
            case IN:
                return buildInPredicate(condition);
            case GT:
                return buildGreaterThanPredicate(condition);
            case LT:
                return buildLesThanPredicate(condition);
            default:
                throw new IllegalArgumentException("Unsupported filter type");
        }
    }

    private Predicate buildGreaterThanPredicate(Condition condition) {
        return criteriaBuilder.greaterThanOrEqualTo(root.<Integer>get(condition.getField()), new Integer(condition.getValue().toString()));
    }

    private Predicate buildLesThanPredicate(Condition condition) {
        return criteriaBuilder.lessThanOrEqualTo(root.<Integer>get(condition.getField()), new Integer(condition.getValue().toString()));
    }

    @SuppressWarnings("unchecked")
    private <P extends Comparable<P>> Predicate buildBetweenTimePredicate(Condition condition) {
        if (checkBetweenCondition(condition)) {

            P lowerLimit = ((Map<String, P>) condition.getValue()).get(LOWER_LIMIT_PARAM_NAME);
            P upperLimit = ((Map<String, P>) condition.getValue()).get(UPPER_LIMIT_PARAM_NAME);

            return criteriaBuilder.between(root.<P>get(condition.getField()), lowerLimit, upperLimit);
        } else {
            throw new IllegalArgumentException("Method buildInPredicate can be applied only for collections");
        }
    }

    private Predicate buildInPredicate(Condition condition) {
        if (condition.getValue() instanceof Collection) {
            if (((Collection) condition.getValue()).isEmpty())
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            return root.get(condition.getField()).in(condition.getValue());
        } else throw new IllegalArgumentException("Method buildInPredicate can be applied only for collections");
    }

    private Predicate buildEqualPredicate(Condition condition) {
        return criteriaBuilder.equal(root.<String>get(condition.getField()), condition.getValue());
    }

    private Predicate buildLikePredicate(Condition condition) {
        return criteriaBuilder.like(root.<String>get(condition.getField()), makeLikePattern(condition.getValue().toString()));
    }

    private String makeLikePattern(String rawString) {
        return "%" + rawString + "%";
    }

    @SuppressWarnings("unchecked")
    private <P> boolean checkBetweenCondition(Condition condition) {

        if (!(condition.getValue() instanceof Map)) return false;

        Map<String, P> values = (Map<String, P>) condition.getValue();

        return values.containsKey(LOWER_LIMIT_PARAM_NAME) && values.containsKey(UPPER_LIMIT_PARAM_NAME);
    }

    public static class EntityFilterBuilder<T> {

        private List<Condition> conditions = new ArrayList<>();
        private List<JoinMapCondition> joinMapConditions = new ArrayList<>();

        public EntityFilterBuilder<T> withCondition(Condition condition) {
            if(checkCondition(condition)) conditions.add(condition);
            return this;
        }

        public EntityFilterBuilder<T> withJoinMapCondition(JoinMapCondition condition) {
            if(checkCondition(condition)) joinMapConditions.add(condition);
            return this;
        }

        public EntityFilter<T> build() {
            return new EntityFilter<>(conditions, joinMapConditions);
        }
    }

    public static <T> EntityFilterBuilder<T> builder() {
        return new EntityFilterBuilder<>();
    }

    private static boolean checkCondition(Condition condition) {
        return condition.getValue() != null && condition.getComparison() != null && condition.getField() != null;
    }

    private static boolean checkCondition(JoinMapCondition condition) {
        return condition.getValue() != null
                && condition.getComparison() != null
                && condition.getField() != null
                && condition.getKey() != null;
    }
}
