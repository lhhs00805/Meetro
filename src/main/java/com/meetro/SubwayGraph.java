package com.meetro;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The subway map as a graph: each station knows which stations are next to it.
 * A station that appears in two lines (e.g. Konkuk Univ.) becomes one station,
 * so the two lines are connected there.
 */
public class SubwayGraph {

    // "Address book": station name -> names of the stations next to it
    private final Map<String, Set<String>> neighbors = new HashMap<>();

    public SubwayGraph(List<Line> lines) {
        for (Line line : lines) {
            addLine(line);
        }
    }

    private void addLine(Line line) {
        List<String> stations = line.stations();

        // Register every station (a transfer station is registered only once)
        for (String station : stations) {
            neighbors.computeIfAbsent(station, name -> new HashSet<>());
        }

        // Connect stations that are next to each other in the list
        for (int i = 0; i + 1 < stations.size(); i++) {
            connect(stations.get(i), stations.get(i + 1));
        }

        // A circular line (e.g. Line 2) also connects the last and the first station
        if (line.circular() && stations.size() > 2) {
            connect(stations.get(stations.size() - 1), stations.get(0));
        }
    }

    private void connect(String a, String b) {
        neighbors.get(a).add(b);
        neighbors.get(b).add(a);
    }

    public boolean hasStation(String name) {
        return neighbors.containsKey(name);
    }

    public Set<String> stations() {
        return Collections.unmodifiableSet(neighbors.keySet());
    }

    /**
     * Counts the smallest number of stops from the start station to every station
     * it can reach, using BFS (breadth-first search).
     */
    public Map<String, Integer> stopsFrom(String start) {
        if (!hasStation(start)) {
            throw new IllegalArgumentException("Unknown station: " + start);
        }

        Map<String, Integer> stops = new HashMap<>();
        Deque<String> queue = new ArrayDeque<>();   // the "waiting line"

        stops.put(start, 0);
        queue.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();          // take the station at the front of the line
            for (String next : neighbors.get(current)) {
                if (!stops.containsKey(next)) {     // only stations we have not visited yet
                    stops.put(next, stops.get(current) + 1);
                    queue.add(next);                // put it at the back of the line
                }
            }
        }
        return stops;
    }
}