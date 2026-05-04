package com.studyplanner.service;

import java.util.ArrayList;
import java.util.List;

public class PlannerRepository<T> {
    private final List<T> items = new ArrayList<T>();

    public void add(T item) {
        items.add(item);
    }

    public List<T> getAll() {
        return new ArrayList<T>(items);
    }

    public int size() {
        return items.size();
    }
}
