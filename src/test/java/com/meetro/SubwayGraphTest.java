package com.meetro;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class SubwayGraphTest {

    @Test
    void countsStopsOnAStraightLine() {
        // A - B - C - D - E
        SubwayGraph graph = new SubwayGraph(List.of(
                new Line("Test", false, List.of("A", "B", "C", "D", "E"))));

        Map<String, Integer> stops = graph.stopsFrom("A");

        assertEquals(0, stops.get("A"));
        assertEquals(2, stops.get("C"));
        assertEquals(4, stops.get("E"));
    }

    @Test
    void usesTheShorterDirectionOnACircularLine() {
        // A - B - C - D - E - (back to A)
        SubwayGraph graph = new SubwayGraph(List.of(
                new Line("Loop", true, List.of("A", "B", "C", "D", "E"))));

        Map<String, Integer> stops = graph.stopsFrom("A");

        assertEquals(1, stops.get("E"));   // not 4: going backwards is shorter
        assertEquals(2, stops.get("D"));   // not 3
    }

    @Test
    void connectsTwoLinesAtATransferStation() {
        // Line 1: A - B - C
        // Line 2: X - B - Y   (B is the transfer station)
        SubwayGraph graph = new SubwayGraph(List.of(
                new Line("Line 1", false, List.of("A", "B", "C")),
                new Line("Line 2", false, List.of("X", "B", "Y"))));

        Map<String, Integer> stops = graph.stopsFrom("A");

        assertEquals(2, stops.get("Y"));   // A -> B (transfer) -> Y
        assertEquals(5, graph.stations().size());   // B is counted only once
    }

    @Test
    void knowsWhichStationsExist() {
        SubwayGraph graph = new SubwayGraph(List.of(
                new Line("Test", false, List.of("A", "B"))));

        assertTrue(graph.hasStation("A"));
        assertEquals(false, graph.hasStation("Z"));
    }

    @Test
    void rejectsAnUnknownStartStation() {
        SubwayGraph graph = new SubwayGraph(List.of(
                new Line("Test", false, List.of("A", "B"))));

        assertThrows(IllegalArgumentException.class, () -> graph.stopsFrom("Z"));
    }
}