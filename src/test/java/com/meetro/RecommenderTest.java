package com.meetro;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class RecommenderTest {

    // One straight line: A - B - C - D - E
    private final SubwayGraph graph = new SubwayGraph(List.of(
            new Line("Test", false, List.of("A", "B", "C", "D", "E"))));

    // A and E have no facility data
    private final List<Facility> facilities = List.of(
            new Facility("B", Map.of("cafe", 5, "movie", 0, "food", 5)),
            new Facility("C", Map.of("cafe", 0, "movie", 3, "food", 5)),
            new Facility("D", Map.of("cafe", 5, "movie", 3, "food", 5)));

    private final Recommender recommender = new Recommender(graph, facilities);

    private static List<String> stations(List<Recommendation> result) {
        return result.stream().map(Recommendation::station).toList();
    }

    @Test
    void removesStationsWithNothingToDoForThePurpose() {
        // B has movie = 0, so it must not be recommended for "movie"
        List<Recommendation> result = recommender.recommend(List.of("A", "E"), "movie", TravelMode.LONGEST);

        assertEquals(List.of("C", "D"), stations(result));
    }

    @Test
    void removesStationsWithoutFacilityData() {
        // A and E have no facility data; C has cafe = 0
        List<Recommendation> result = recommender.recommend(List.of("A", "E"), "cafe", TravelMode.LONGEST);

        assertEquals(List.of("B", "D"), stations(result));
    }

    @Test
    void changingThePurposeChangesTheBestStation() {
        String bestForCafe = recommender.recommend(List.of("A", "E"), "cafe", TravelMode.LONGEST).get(0).station();
        String bestForMovie = recommender.recommend(List.of("A", "E"), "movie", TravelMode.LONGEST).get(0).station();

        assertEquals("B", bestForCafe);
        assertEquals("C", bestForMovie);
    }

    @Test
    void longestAndTotalCanGiveDifferentResults() {
        // Two people start at A, one person starts at E
        // Longest trip: B = 3, C = 2, D = 3  -> C is best
        // Total trip:   B = 5, C = 6, D = 7  -> B is best
        List<String> starts = List.of("A", "A", "E");

        assertEquals("C", recommender.recommend(starts, "food", TravelMode.LONGEST).get(0).station());
        assertEquals("B", recommender.recommend(starts, "food", TravelMode.TOTAL).get(0).station());
    }

    @Test
    void reportsStopsForEachPersonInOrder() {
        Recommendation best = recommender.recommend(List.of("A", "E"), "movie", TravelMode.LONGEST).get(0);

        assertEquals("C", best.station());
        assertEquals(List.of(2, 2), best.stopsPerPerson());
    }

    @Test
    void calculatesTheScoreWithTheWeights() {
        // movie: C (longest 2, movie 3), D (longest 3, movie 3)
        // C: 100 * (0.7 * 1.0 + 0.3 * 3/5) = 88
        // D: 100 * (0.7 * 0.0 + 0.3 * 3/5) = 18
        List<Recommendation> result = recommender.recommend(List.of("A", "E"), "movie", TravelMode.LONGEST);

        assertEquals(88, result.get(0).score());
        assertEquals(18, result.get(1).score());
    }

    @Test
    void breaksTiesByStationName() {
        // cafe: B and D both have longest trip 3 and cafe 5, so they have the same score
        List<Recommendation> result = recommender.recommend(List.of("A", "E"), "cafe", TravelMode.LONGEST);

        assertEquals(result.get(0).score(), result.get(1).score());
        assertEquals(List.of("B", "D"), stations(result));
    }

    @Test
    void returnsAtMostThreeStations() {
        List<Facility> everyStation = List.of(
                new Facility("A", Map.of("food", 5)),
                new Facility("B", Map.of("food", 5)),
                new Facility("C", Map.of("food", 5)),
                new Facility("D", Map.of("food", 5)),
                new Facility("E", Map.of("food", 5)));
        Recommender r = new Recommender(graph, everyStation);

        assertEquals(3, r.recommend(List.of("A", "E"), "food", TravelMode.LONGEST).size());
    }

    @Test
    void returnsAnEmptyListWhenNoStationFits() {
        List<Recommendation> result = recommender.recommend(List.of("A", "E"), "karaoke", TravelMode.LONGEST);

        assertTrue(result.isEmpty());
    }

    @Test
    void rejectsAnUnknownPurpose() {
        assertThrows(IllegalArgumentException.class,
                () -> recommender.recommend(List.of("A"), "shopping", TravelMode.LONGEST));
    }
}