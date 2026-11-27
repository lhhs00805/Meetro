package com.meetro;

import java.util.List;

/**
 * One subway line, read from a file in data/lines/.
 *
 * @param name     the line name, e.g. "Line 2"
 * @param circular true if the last station is connected to the first one
 * @param stations station names in order; stations next to each other are connected
 */
public record Line(String name, boolean circular, List<String> stations) {
}