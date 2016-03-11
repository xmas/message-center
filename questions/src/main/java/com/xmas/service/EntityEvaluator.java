package com.xmas.service;

/**
 * Represent entity evaluator.
 * Class that is used for getting data, evaluating script
 * and collecting result of evaluating
 * @param <T> Class of entity that can be evaluates by this evaluator
 */
public interface EntityEvaluator<T> {

    /**
     * Evaluates entity
     * i.e. get data if needed, run script, collect data created by script
     * @param entity entity that should be evaluated
     */
    void evaluate(T entity);
}
