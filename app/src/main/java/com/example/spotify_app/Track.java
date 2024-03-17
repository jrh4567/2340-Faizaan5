package com.example.spotify_app;

import java.util.List;

public class Track {
    private String id;
    private String name;
    private String albumName;
    private List<String> artistNames;
    private int trackNumber;
    private int popularity;
    private boolean explicit;
    private String previewUrl;
    private List<String> availableMarkets;

    public Track(String id, String name, String albumName, List<String> artistNames, int trackNumber, int popularity, boolean explicit, String previewUrl, List<String> availableMarkets) {
        this.id = id;
        this.name = name;
        this.albumName = albumName;
        this.artistNames = artistNames;
        this.trackNumber = trackNumber;
        this.popularity = popularity;
        this.explicit = explicit;
        this.previewUrl = previewUrl;
        this.availableMarkets = availableMarkets;
    }
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAlbumName() {
        return albumName;
    }

    public List<String> getArtistNames() {
        return artistNames;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public int getPopularity() {
        return popularity;
    }

    public boolean isExplicit() {
        return explicit;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public List<String> getAvailableMarkets() {
        return availableMarkets;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setArtistNames(List<String> artistNames) {
        this.artistNames = artistNames;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public void setExplicit(boolean explicit) {
        this.explicit = explicit;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public void setAvailableMarkets(List<String> availableMarkets) {
        this.availableMarkets = availableMarkets;
    }


    @Override
    public String toString() {
        StringBuilder artistBuilder = new StringBuilder();
        for (int i = 0; i < artistNames.size(); i++) {
            artistBuilder.append(artistNames.get(i));
            if (i < artistNames.size() - 1) {
                artistBuilder.append(", ");
            }
        }

        return "ID: " + id + "\n" +
                "Name: " + name + "\n" +
                "Artists: " + artistBuilder.toString() + "\n" +
                "Album Name: " + albumName + "\n" +
                "Explicit: " + explicit + "\n" +
                "Preview URL: " + previewUrl + "\n";
    }
}
