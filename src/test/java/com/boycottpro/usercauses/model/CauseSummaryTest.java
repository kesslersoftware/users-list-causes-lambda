package com.boycottpro.usercauses.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CauseSummaryTest {

    @Test
    void getCause_id() {
        CauseSummary causeSummary = new CauseSummary("cause123", "Save the Earth");
        assertEquals("cause123", causeSummary.getCause_id());
        assertEquals("Save the Earth", causeSummary.getCause_desc());
    }

    @Test
    void setCause_id() {
        CauseSummary causeSummary = new CauseSummary();
        causeSummary.setCause_id("cause456");
        assertEquals("cause456", causeSummary.getCause_id());
    }

    @Test
    void getCause_desc() {
        CauseSummary causeSummary = new CauseSummary("cause123", "Save the Earth");
        assertEquals("cause123", causeSummary.getCause_id());
        assertEquals("Save the Earth", causeSummary.getCause_desc());
    }

    @Test
    void setCause_desc() {
        CauseSummary causeSummary = new CauseSummary();
        causeSummary.setCause_desc("Protect Wildlife");
        assertEquals("Protect Wildlife", causeSummary.getCause_desc());
    }

    @Test
    void testConstructorAndGetters() {
        CauseSummary causeSummary = new CauseSummary("cause123", "Save the Earth");
        assertEquals("cause123", causeSummary.getCause_id());
        assertEquals("Save the Earth", causeSummary.getCause_desc());
    }
}