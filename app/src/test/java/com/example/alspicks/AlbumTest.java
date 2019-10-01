package com.example.alspicks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlbumTest {

    @Test
    void getName() {
        Album actual = new Album("jimi", "electric ladyland", "1969", "psychedelic");
        String expected = "electric ladyland";
        assertEquals(actual.name, expected);
    }

    @Test
    void getArtist() {
        Album actual = new Album("jimi", "electric ladyland", "1969", "psychedelic");
        String expected = "jimi";
        assertEquals(actual.artist, expected);
    }

    @Test
    void getYear() {
        Album actual = new Album("jimi", "electric ladyland", "1969", "psychedelic");
        String expected = "1969";
        assertEquals(actual.year, expected);
    }

    @Test
    void getStyle() {
        Album actual = new Album("jimi", "electric ladyland", "1969", "psychedelic");
        String expected = "psychedelic";
        assertEquals(actual.style, expected);
    }
}