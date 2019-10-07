package com.example.alspicks;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NewTunesTest {

    @Test
    void defaultSort() {
        ArrayList<Album> actualList = new ArrayList<>();
        actualList.add(new Album("jimi", "electric ladyland", "1969", "psychedelic"));
        actualList.add(new Album("miles", "get up with it", "1974", "jazz"));
        actualList.add(new Album("felt", "fake", "1972", "rock"));
        actualList.add(new Album("funkadelic", "free your mind", "1974", "funk"));

        ArrayList<Album> actualSorted = (ArrayList<Album>) actualList.stream().sorted(Comparator.comparing(Album::getArtist)).collect(Collectors.toList());

        assertEquals("felt", actualSorted.get(0).artist);
        assertEquals("funkadelic", actualSorted.get(1).artist);
        assertEquals("jimi", actualSorted.get(2).artist);
        assertEquals("miles", actualSorted.get(3).artist);

    }

    @Test
    void yearSort() {
        ArrayList<Album> actualList = new ArrayList<>();
        actualList.add(new Album("jimi", "electric ladyland", "1969", "psychedelic"));
        actualList.add(new Album("miles", "get up with it", "1974", "jazz"));
        actualList.add(new Album("felt", "fake", "1972", "rock"));
        actualList.add(new Album("funkadelic", "free your mind", "1974", "funk"));

        ArrayList<Album> actualSorted = (ArrayList<Album>) actualList.stream().sorted(Comparator.comparing(Album::getYear)).collect(Collectors.toList());

        assertEquals("1969", actualSorted.get(0).year);
        assertEquals("1972", actualSorted.get(1).year);
        assertEquals("1974", actualSorted.get(2).year);
        assertEquals("1974", actualSorted.get(3).year);

    }

    @Test
    void styleSort() {
        ArrayList<Album> actualList = new ArrayList<>();
        actualList.add(new Album("jimi", "electric ladyland", "1969", "psychedelic"));
        actualList.add(new Album("miles", "get up with it", "1974", "jazz"));
        actualList.add(new Album("felt", "fake", "1972", "rock"));
        actualList.add(new Album("funkadelic", "free your mind", "1974", "funk"));

        ArrayList<Album> actualSorted = (ArrayList<Album>) actualList.stream().sorted(Comparator.comparing(Album::getStyle)).collect(Collectors.toList());

        assertEquals("funk", actualSorted.get(0).style);
        assertEquals("jazz", actualSorted.get(1).style);
        assertEquals("psychedelic", actualSorted.get(2).style);
        assertEquals("rock", actualSorted.get(3).style);

    }
}