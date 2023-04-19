package com.epam.restaurant.bean.criteria;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The {@code Criteria} class is designed to provide search by criteria
 */
public class Criteria {
    private Map<String, Object> criteria = new HashMap<>();

    public Criteria() {
    }

    public void add(String searchCriteria, Object value) {
        criteria.put(searchCriteria, value);
    }

    public void remove(String searchCriteria) {
        criteria.remove(searchCriteria);
    }

    public Map<String, Object> getCriteria() {
        return criteria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Criteria criteria1 = (Criteria) o;
        return Objects.equals(criteria, criteria1.criteria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(criteria);
    }

    @Override
    public String toString() {
        return "Criteria{" +
                "criteria=" + criteria +
                '}';
    }
}