package com.airtribe.meditrack.interfaces;

/**
 * Interface for entities that support search functionality.
 * Demonstrates interface with default methods (Java 8+).
 */
public interface Searchable {

    /**
     * Check if this entity matches the given keyword.
     *
     * @param keyword the search keyword
     * @return true if the entity matches
     */
    boolean matchesSearchCriteria(String keyword);

    /**
     * Default method — returns a search-friendly summary string.
     */
    default String getSearchableSummary() {
        return toString();
    }

    /**
     * Default method — case-insensitive contains check.
     */
    default boolean containsIgnoreCase(String source, String keyword) {
        if (source == null || keyword == null) return false;
        return source.toLowerCase().contains(keyword.toLowerCase());
    }
}
