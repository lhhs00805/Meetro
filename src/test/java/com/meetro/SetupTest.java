package com.meetro;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Checks that JUnit and Maven are set up correctly.
 * This is a temporary test until real feature tests are added. 
 */
class SetupTest {

    @Test
    void junitRunsCorrectly() {
        assertEquals(2, 1 + 1);
    }
}