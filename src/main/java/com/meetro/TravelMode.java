package com.meetro;

/** How Meetro compares travel for a group. */
public enum TravelMode {
    /** Make the person who travels the longest arrive as soon as possible (default). */
    LONGEST,
    /** Make the total travel of everyone as small as possible. */
    TOTAL
}