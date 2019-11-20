package com.example.alspicks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


public class Album implements Collection<Album>{
    //Firebase requires these to be public
    public String name;
    public String artist;
    public String year;
    public String coverImage;
    public ArrayList<String> style;
    public ArrayList<String> genre;


    public Album(){}

    public Album(String artist, String name, String year, ArrayList<String> style, ArrayList<String> genre, String coverImage){
        this.name = name;
        this.artist = artist;
        this.year = year;
        this.style = style;
        this.genre = genre;
        this.coverImage = coverImage;
    }

    public Album(String artist, String name, String year, String coverImage) {
        this.name = name;
        this.artist = artist;
        this.year = year;
        this.coverImage = coverImage;
    }

    public String getName(){
        return name;
    }

    public String getArtist(){
        return artist;
    }

    public String getYear(){
        return year;
    }

    ArrayList<String> getStyle(){
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

    /*
    our sort methods for later use?
    public static ArrayList<Album> defaultSort(ArrayList<Album> albumArrayList){
        return (ArrayList<Album>) albumArrayList.stream().sorted(Comparator.comparing(Album::getArtist)).collect(Collectors.toList());
    }

    public static ArrayList<Album> yearSort(ArrayList<Album> albumArrayList) {
        return (ArrayList<Album>) albumArrayList.stream().sorted(Comparator.comparing(Album::getYear)).collect(Collectors.toList());
    }

    public static ArrayList<Album> styleSort(ArrayList<Album> albumArrayList) {
        return (ArrayList<Album>) albumArrayList.stream().sorted(Comparator.comparing(Album::getStyle)).collect(Collectors.toList());
    }

    public static ArrayList<Album> albumSort(ArrayList<Album> albumArrayList) {
        return (ArrayList<Album>) albumArrayList.stream().sorted(Comparator.comparing(Album::getName)).collect(Collectors.toList());
    }
     */
}
