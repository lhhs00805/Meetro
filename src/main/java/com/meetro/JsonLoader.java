
package com.meetro;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

/**
 * Loads subway lines and facility scores from JSON files.
 */
public class JsonLoader {

    public static List<Line> loadLines(Path directory) throws IOException {
        List<Line> lines = new ArrayList<>();

        for (Path file : jsonFiles(directory)) {
            JsonObject json = readJson(file);

            String name = json.get("line").getAsString();
            boolean circular = json.get("circular").getAsBoolean();

            List<String> stations = new ArrayList<>();
            JsonArray stationArray = json.getAsJsonArray("stations");

            for (JsonElement element : stationArray) {
                JsonObject station = element.getAsJsonObject();
                stations.add(station.get("name").getAsString());
            }

            lines.add(new Line(name, circular, stations));
        }

        return lines;
    }

    public static List<Facility> loadFacilities(Path directory)
            throws IOException {
        List<Facility> facilities = new ArrayList<>();

        for (Path file : jsonFiles(directory)) {
            JsonObject json = readJson(file);

            String station = json.get("station").getAsString();
            JsonObject scoresJson = json.getAsJsonObject("facilities");

            Map<String, Integer> scores = new TreeMap<>();

            for (Map.Entry<String, JsonElement> entry
                    : scoresJson.entrySet()) {
                scores.put(entry.getKey(), entry.getValue().getAsInt());
            }

            facilities.add(new Facility(station, scores));
        }

        return facilities;
    }

    private static JsonObject readJson(Path file) throws IOException {
        try (Reader reader = Files.newBufferedReader(file)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }

    private static List<Path> jsonFiles(Path directory) throws IOException {
        try (Stream<Path> files = Files.list(directory)) {
            return files
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName()
                            .toString().endsWith(".json"))
                    .sorted()
                    .toList();
        }
    }
}
