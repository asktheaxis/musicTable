package com.example.alspicks;

public class Album {
    private String albumName;
    private String albumArtist;
    private String albumYear;
    private String albumStyle;

    public Album(String name, String artist, String year, String style){
        this.albumName = name;
        this.albumArtist = artist;
        this.albumYear = year;
        this.albumStyle = style;
    }

    public String getName(){
        return albumName;
    }

    public String getArtist(){
        return albumArtist;
    }

    public String getAlbumYear(){
        return albumYear;
    }

    public String getAlbumStyle(){
        return albumStyle;
    }

}
