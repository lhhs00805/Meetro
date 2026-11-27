package com.meetro;

import java.util.Map;

/**
 * Facility scores of one station, read from a file in data/facilities/.
 *
 * @param station the station name (must match the name in the line files)
 * @param scores  purpose key (e.g. "cafe") -> score from 0 to 5
 */
public record Facility(String station, Map<String, Integer> scores) {

    /** Returns the score for a purpose, or 0 if the purpose is not in the data. */
    public int scoreFor(String purpose) {
        return scores.getOrDefault(purpose, 0);
    }
}