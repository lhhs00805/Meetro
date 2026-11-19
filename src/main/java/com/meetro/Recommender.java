package com.meetro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Recommends meeting stations for a group, based on everyone's number of stops
 * and the facility score for the chosen purpose.
 */
public class Recommender {

    public static final Set<String> PURPOSES = Set.of("food", "cafe", "movie", "karaoke", "study", "walk");

    // Distance matters more than facilities, because fairness is the main goal of Meetro
    private static final double DISTANCE_WEIGHT = 0.7;
    private static final double FACILITY_WEIGHT = 0.3;
    private static final int MAX_FACILITY_SCORE = 5;
    private static final int TOP_N = 3;

    private final SubwayGraph graph;
    private final Map<String, Facility> facilities = new HashMap<>();

    public Recommender(SubwayGraph graph, List<Facility> facilityList) {
        this.graph = graph;
        for (Facility facility : facilityList) {
            facilities.put(facility.station(), facility);
        }
    }

    /**
     * Returns up to 3 recommended stations, best first.
     * Returns an empty list if no station fits the purpose.
     */
    public List<Recommendation> recommend(List<String> starts, String purpose, TravelMode mode) {
        if (starts.isEmpty()) {
            throw new IllegalArgumentException("At least one starting station is needed");
        }
        if (!PURPOSES.contains(purpose)) {
            throw new IllegalArgumentException("Unknown purpose: " + purpose);
        }

        // Step 1: run BFS once for each person
        List<Map<String, Integer>> stopsByPerson = new ArrayList<>();
        for (String start : starts) {
            stopsByPerson.add(graph.stopsFrom(start));
        }

        // Step 2: keep only stations that have something to do and that everyone can reach
        List<Candidate> candidates = new ArrayList<>();
        for (Facility facility : facilities.values()) {
            int facilityScore = facility.scoreFor(purpose);
            if (facilityScore == 0) {
                continue;   // nothing to do here for this purpose
            }
            List<Integer> stops = stopsForEveryone(stopsByPerson, facility.station());
            if (stops == null) {
                continue;   // someone cannot reach this station
            }
            candidates.add(new Candidate(facility.station(), facilityScore, stops, travel(stops, mode)));
        }
        if (candidates.isEmpty()) {
            return List.of();
        }

        // Step 3: sort by score
        int minTravel = candidates.stream().mapToInt(Candidate::travel).min().getAsInt();
        int maxTravel = candidates.stream().mapToInt(Candidate::travel).max().getAsInt();

        candidates.sort(Comparator
                .comparingInt((Candidate c) -> -score(c, minTravel, maxTravel))   // higher score first
                .thenComparingInt(Candidate::travel)                                // then less travel
                .thenComparing(Candidate::station));                                // then by name (A to Z)

        // Step 4: return the top 3
        List<Recommendation> result = new ArrayList<>();
        for (Candidate c : candidates.subList(0, Math.min(TOP_N, candidates.size()))) {
            result.add(new Recommendation(c.station(), score(c, minTravel, maxTravel), c.stops()));
        }
        return result;
    }

    /** Returns everyone's stops to the station, or null if someone cannot reach it. */
    private static List<Integer> stopsForEveryone(List<Map<String, Integer>> stopsByPerson, String station) {
        List<Integer> stops = new ArrayList<>();
        for (Map<String, Integer> personStops : stopsByPerson) {
            Integer s = personStops.get(station);
            if (s == null) {
                return null;
            }
            stops.add(s);
        }
        return stops;
    }

    private static int travel(List<Integer> stops, TravelMode mode) {
        if (mode == TravelMode.LONGEST) {
            return Collections.max(stops);
        }
        return stops.stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Score from 0 to 100.
     * Distance score: 1.0 for the candidate with the least travel, 0.0 for the most travel.
     * Facility score: facility score divided by 5.
     */
    private static int score(Candidate c, int minTravel, int maxTravel) {
        double distanceScore = (maxTravel == minTravel)
                ? 1.0
                : (double) (maxTravel - c.travel()) / (maxTravel - minTravel);
        double facilityScore = (double) c.facilityScore() / MAX_FACILITY_SCORE;
        return (int) Math.round(100 * (DISTANCE_WEIGHT * distanceScore + FACILITY_WEIGHT * facilityScore));
    }

    /** A station that passed the filters, with the values needed for scoring. */
    private record Candidate(String station, int facilityScore, List<Integer> stops, int travel) {
    }
}