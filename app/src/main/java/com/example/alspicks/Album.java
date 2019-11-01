package com.example.alspicks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.Iterator;


public class Album implements Collection<Album>{
    //Firebase requires these to be public
    public String name;
    public String artist;
    public String year;
    public String style;


    public Album(){}

    public Album(String artist, String name, String year, String style){
        this.name = name;
        this.artist = artist;
        this.year = year;
        this.style = style;
    }

    String getName(){
        return name;
    }

    String getArtist(){
        return artist;
    }

    String getYear(){
        return year;
    }

    String getStyle(){
        return style;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(@Nullable Object o) {
        return false;
    }

    @NonNull
    @Override
    public Iterator<Album> iterator() {
        return null;
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] ts) {
        return null;
    }

    @Override
    public boolean add(Album album) {
        return false;
    }

    @Override
    public boolean remove(@Nullable Object o) {
        return false;
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        return false;
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends Album> collection) {
        return false;
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        return false;
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        return false;
    }

    @Override
    public void clear() {

    }
}
