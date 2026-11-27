package com.meetro;

import java.util.List;

/**
 * One recommended station.
 *
 * @param station        the station name
 * @param score          total score from 0 to 100
 * @param stopsPerPerson number of stops for each person, in the same order as the starting stations
 */
public record Recommendation(String station, int score, List<Integer> stopsPerPerson) {
}