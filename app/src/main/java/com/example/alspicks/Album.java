package com.example.alspicks;

public class Album {
    //Firebase requires these to be public
    public String albumName;
    public String albumArtist;
    public String albumYear;
    public String albumStyle;

    public Album(){}

    public Album(String name, String artist, String year, String style){
        this.albumName = name;
        this.albumArtist = artist;
        this.albumYear = year;
        this.albumStyle = style;
    }

    String getAlbumName(){
        return albumName;
    }

    String getAlbumArtist(){
        return albumArtist;
    }

    String getAlbumYear(){
        return albumYear;
    }

    String getAlbumStyle(){
        return albumStyle;
    }

}
