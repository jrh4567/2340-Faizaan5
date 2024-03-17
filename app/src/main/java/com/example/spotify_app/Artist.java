package com.example.spotify_app;

import java.util.List;

public class Artist {
    private String name;
    private List<String> genres;
    private int popularity;
    private String spotifyId;
    private List<String> images;

    public Artist(String name, List<String> genres, int popularity, String spotifyId, List<String> images) {
        this.name = name;
        this.genres = genres;
        this.popularity = popularity;
        this.spotifyId = spotifyId;
        this.images = images;
    }

    // Getters
    public String getName() {
        return name;
    }

    public List<String> getGenres() {
        return genres;
    }

    public int getPopularity() {
        return popularity;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public List<String> getImages() {
        return images;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        StringBuilder genresString = new StringBuilder();
        for (int i = 0; i < genres.size(); i++) {
            genresString.append(genres.get(i));
            if (i < genres.size() - 1) {
                genresString.append(", ");
            }
        }

        StringBuilder imagesString = new StringBuilder();
        for (int i = 0; i < images.size(); i++) {
            imagesString.append(images.get(i));
            if (i < images.size() - 1) {
                imagesString.append(", ");
            }
        }

        return "Name: " + name + "\n" +
                "Genres: " + genresString.toString() + "\n" +
                "Popularity: " + popularity + "\n" +
                "Spotify ID: " + spotifyId + "\n" +
                "Images: " + imagesString.toString();
    }
}
