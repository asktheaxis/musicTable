package com.example.alspicks;

public class Album {
    //Firebase requires these to be public
    public String name;
    public String artist;
    public String year;
    public String style;

    public Album(){}

    public Album(String name, String artist, String year, String style){
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

}
