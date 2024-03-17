package com.example.spotify_app;

import java.util.List;

public class Data {
    // handle data
    private static String time = "medium_term";
    private static String displayData = ""; //data display in the wrapped activity
    private static Profile userProfile; //user profile
    private static List<Artist> topArtists; // top artists
    private static List<Artist> recArtists; // recommended artists
    private static List<Track> tracks; // top tracks
    public static void setTime(String t) {
        time = t;
    }
    public static String getTime() {
        return time;
    }
    public static void setData(String t) {
        displayData = t;
    }
    public static void appendData(String t) {
        displayData += t;
    }
    public static String getData() {
        return displayData;
    }
    public static void setProfile(Profile p) { userProfile = p; }
    public static Profile getProfile() { return userProfile; }
    public static void setTopArtists(List<Artist> a) {topArtists = a; }
    public static List<Artist> getTopArtists() { return topArtists; }
    public static void setRecArtists(List<Artist> a) {recArtists = a; }
    public static List<Artist> getRecArtists() { return recArtists; }
    public static void setTracks(List<Track> a) {tracks = a; }
    public static List<Track> getTracks() { return tracks; }





}
