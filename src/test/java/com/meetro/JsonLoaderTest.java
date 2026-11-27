
package com.meetro;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonLoaderTest {

    @TempDir
    Path tempDir;

    @Test
    void loadLinesWithoutElevator() throws IOException {
        Path directory = Files.createDirectory(tempDir.resolve("lines"));

        Files.writeString(directory.resolve("line7.json"), """
                {
                  "line": "Line 7",
                  "circular": false,
                  "stations": [
                    {"name": "Nowon"},
                    {"name": "Junggye"},
                    {"name": "Hagye"}
                  ]
                }
                """);

        List<Line> lines = JsonLoader.loadLines(directory);

        assertEquals(1, lines.size());
        assertEquals("Line 7", lines.get(0).name());
        assertFalse(lines.get(0).circular());
        assertEquals(List.of("Nowon", "Junggye", "Hagye"),
                lines.get(0).stations());
    }

    @Test
    void loadFacilitiesWithEmptyPlacesAndSource() throws IOException {
        Path directory = Files.createDirectory(tempDir.resolve("facilities"));

        Files.writeString(directory.resolve("gongneung.json"), """
                {
                  "station": "Gongneung",
                  "updated": "2026-11",
                  "source": "Naver Map",
                  "facilities": {
                    "food": 5,
                    "cafe": 5,
                    "movie": 0,
                    "karaoke": 5,
                    "study": 5,
                    "walk": 3
                  },
                  "places": []
                }
                """);

        List<Facility> facilities = JsonLoader.loadFacilities(directory);

        assertEquals(1, facilities.size());
        assertEquals("Gongneung", facilities.get(0).station());
        assertEquals(5, facilities.get(0).scoreFor("food"));
        assertEquals(3, facilities.get(0).scoreFor("walk"));
        assertEquals(0, facilities.get(0).scoreFor("movie"));
        assertEquals(0, facilities.get(0).scoreFor("unknown"));
    }
}
