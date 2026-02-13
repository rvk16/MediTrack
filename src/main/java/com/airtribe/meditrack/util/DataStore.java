package com.airtribe.meditrack.util;

import com.airtribe.meditrack.entity.MedicalEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Generic in-memory data store for MediTrack entities.
 * Demonstrates: generics (bounded type parameter), collections (HashMap, ArrayList),
 * iterators, Java 8 streams & lambdas, Comparator usage.
 *
 * @param <T> the type of entity stored, must extend MedicalEntity
 */
public class DataStore<T extends MedicalEntity> {

    private final Map<String, T> store;

    public DataStore() {
        this.store = new LinkedHashMap<>();
    }

    // --- CRUD operations ---

    public void add(T entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Entity and its ID must not be null");
        }
        store.put(entity.getId(), entity);
    }

    public Optional<T> getById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<T> getAll() {
        return new ArrayList<>(store.values());
    }

    public T update(T entity) {
        if (entity == null || entity.getId() == null || !store.containsKey(entity.getId())) {
            throw new NoSuchElementException("Entity not found for update: "
                    + (entity != null ? entity.getId() : "null"));
        }
        store.put(entity.getId(), entity);
        return entity;
    }

    public boolean remove(String id) {
        return store.remove(id) != null;
    }

    public boolean exists(String id) {
        return store.containsKey(id);
    }

    public int size() {
        return store.size();
    }

    // --- Search & filter using streams and lambdas ---

    public List<T> filter(Predicate<T> predicate) {
        return store.values().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public Optional<T> findFirst(Predicate<T> predicate) {
        return store.values().stream()
                .filter(predicate)
                .findFirst();
    }

    // --- Sorting with Comparator ---

    public List<T> getAllSorted(Comparator<T> comparator) {
        return store.values().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    // --- Iterator access ---

    public Iterator<T> iterator() {
        return store.values().iterator();
    }

    // --- Bulk operations ---

    public void addAll(Collection<T> entities) {
        for (T entity : entities) {
            add(entity);
        }
    }

    public void clear() {
        store.clear();
    }
}
