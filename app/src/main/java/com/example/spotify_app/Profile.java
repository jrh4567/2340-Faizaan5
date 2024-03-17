package com.example.spotify_app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Profile {
    private String country;
    private String displayName;
    private String email;
    private boolean explicitContentFilterEnabled;
    private boolean explicitContentFilterLocked;
    private String spotifyUrl;
    private int followersTotal;
    private String userId;
    private List<String> imagesUrls;
    private String product;
    private String type;
    private String uri;

    public Profile(String country, String displayName, String email, boolean explicitContentFilterEnabled, boolean explicitContentFilterLocked, String spotifyUrl, int followersTotal, String userId, List<String> imagesUrls, String product, String type, String uri) {
        this.country = country;
        this.displayName = displayName;
        this.email = email;
        this.explicitContentFilterEnabled = explicitContentFilterEnabled;
        this.explicitContentFilterLocked = explicitContentFilterLocked;
        this.spotifyUrl = spotifyUrl;
        this.followersTotal = followersTotal;
        this.userId = userId;
        this.imagesUrls = imagesUrls;
        this.product = product;
        this.type = type;
        this.uri = uri;
    }

    public String getCountry() {
        return country;
    }
    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isExplicitContentFilterEnabled() {
        return explicitContentFilterEnabled;
    }

    public boolean isExplicitContentFilterLocked() {
        return explicitContentFilterLocked;
    }

    public String getSpotifyUrl() {
        return spotifyUrl;
    }

    public int getFollowersTotal() {
        return followersTotal;
    }

    public String getUserId() {
        return userId;
    }

    public List<String> getImagesUrls() {
        return imagesUrls;
    }

    public String getProduct() {
        return product;
    }

    public String getType() {
        return type;
    }

    public String getUri() {
        return uri;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setExplicitContentFilterEnabled(boolean explicitContentFilterEnabled) {
        this.explicitContentFilterEnabled = explicitContentFilterEnabled;
    }

    public void setExplicitContentFilterLocked(boolean explicitContentFilterLocked) {
        this.explicitContentFilterLocked = explicitContentFilterLocked;
    }

    public void setSpotifyUrl(String spotifyUrl) {
        this.spotifyUrl = spotifyUrl;
    }

    public void setFollowersTotal(int followersTotal) {
        this.followersTotal = followersTotal;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setImagesUrls(List<String> imagesUrls) {
        this.imagesUrls = imagesUrls;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    // Static method to parse JSON object into Profile
    public static Profile fromJson(JSONObject jsonObject) throws JSONException {
        String country = jsonObject.optString("country");
        String displayName = jsonObject.optString("display_name");
        String email = jsonObject.optString("email");

        JSONObject explicitContent = jsonObject.optJSONObject("explicit_content");
        boolean filterEnabled = explicitContent != null && explicitContent.optBoolean("filter_enabled");
        boolean filterLocked = explicitContent != null && explicitContent.optBoolean("filter_locked");

        JSONObject externalUrls = jsonObject.optJSONObject("external_urls");
        String spotifyUrl = externalUrls != null ? externalUrls.optString("spotify") : "";

        JSONObject followers = jsonObject.optJSONObject("followers");
        int followersTotal = followers != null ? followers.optInt("total") : 0;

        String userId = jsonObject.optString("id");

        List<String> imagesUrls = new ArrayList<>();
        JSONArray images = jsonObject.optJSONArray("images");
        if (images != null) {
            for (int i = 0; i < images.length(); i++) {
                JSONObject imageObject = images.optJSONObject(i);
                if (imageObject != null) {
                    String imageUrl = imageObject.optString("url");
                    imagesUrls.add(imageUrl);
                }
            }
        }

        String product = jsonObject.optString("product");
        String type = jsonObject.optString("type");
        String uri = jsonObject.optString("uri");

        return new Profile(country, displayName, email, filterEnabled, filterLocked, spotifyUrl, followersTotal, userId, imagesUrls, product, type, uri);
    }

    @Override
    public String toString() {
        return "Profile \n" +
                "country='" + country + '\n' +
                ", displayName='" + displayName + '\n' +
                ", email='" + email + '\n' +
                ", explicitContentFilterEnabled=" + explicitContentFilterEnabled +
                ", explicitContentFilterLocked=" + explicitContentFilterLocked +
                ", spotifyUrl='" + spotifyUrl + '\n' +
                ", followersTotal=" + followersTotal +
                ", userId='" + userId + '\n' +
                ", imagesUrls=" + imagesUrls +
                ", product='" + product + '\n' +
                ", type='" + type + '\n' +
                ", uri='" + uri + '\n';
    }
}
