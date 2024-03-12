package com.example.spotify_app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String CLIENT_ID = "629459785f9d4452a9f57b9d71adc085";
    public static final String REDIRECT_URI = "spotify-app://auth";

    public static final int AUTH_TOKEN_REQUEST_CODE = 0;
    public static final int AUTH_CODE_REQUEST_CODE = 1;

    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private String mAccessToken, mAccessCode;
    private Call mCall;

    private TextView tokenTextView, codeTextView, profileTextView, recTextView;
    private String topArtist;
    private ArrayList<String> topArtists = new ArrayList<>();
    private String timeRange = "short_term"; //change this variable for time range user story

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the views
        tokenTextView = (TextView) findViewById(R.id.token_text_view);
        codeTextView = (TextView) findViewById(R.id.code_text_view);
        profileTextView = (TextView) findViewById(R.id.response_text_view);
        recTextView = (TextView) findViewById(R.id.recommend_text_view);

        // Initialize the buttons
        Button tokenBtn = (Button) findViewById(R.id.token_btn);
        Button codeBtn = (Button) findViewById(R.id.code_btn);
        Button profileBtn = (Button) findViewById(R.id.profile_btn);
        Button settingBtn = (Button) findViewById(R.id.setting_btn);

        // Set the click listeners for the buttons

        tokenBtn.setOnClickListener((v) -> {
            getToken();
        });

        codeBtn.setOnClickListener((v) -> {
            getCode();
        });

        profileBtn.setOnClickListener((v) -> {
            onGetUserProfileClicked();
        });
        settingBtn.setOnClickListener((v) -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

    }

    /**
     * Get token from Spotify
     * This method will open the Spotify login activity and get the token
     * What is token?
     * https://developer.spotify.com/documentation/general/guides/authorization-guide/
     */
    public void getToken() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN);
        AuthorizationClient.openLoginActivity(MainActivity.this, AUTH_TOKEN_REQUEST_CODE, request);
    }

    /**
     * Get code from Spotify
     * This method will open the Spotify login activity and get the code
     * What is code?
     * https://developer.spotify.com/documentation/general/guides/authorization-guide/
     */
    public void getCode() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.CODE);
        AuthorizationClient.openLoginActivity(MainActivity.this, AUTH_CODE_REQUEST_CODE, request);
    }


    /**
     * When the app leaves this activity to momentarily get a token/code, this function
     * fetches the result of that external activity to get the response from Spotify
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);

        // Check which request code is present (if any)
        if (AUTH_TOKEN_REQUEST_CODE == requestCode) {
            mAccessToken = response.getAccessToken();
            setTextAsync(mAccessToken, tokenTextView);

        } else if (AUTH_CODE_REQUEST_CODE == requestCode) {
            mAccessCode = response.getCode();
            setTextAsync(mAccessCode, codeTextView);
        }
    }

    /**
     * Get user profile
     * This method will get the user profile using the token
     */
    public void onGetUserProfileClicked() {
        if (mAccessToken == null) {
            Toast.makeText(this, "You need to get an access token first!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a request to get the user profile
        final Request requestProfile = new Request.Builder()
                .url("https://api.spotify.com/v1/me")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();
        // request for top artists or tracks
        final Request requestArtists = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/artists?time_range=" + TimeRange.getTime()) //for {type}, replace with artists or tracks, for more options such as time range, go to https://developer.spotify.com/documentation/web-api/reference/get-users-top-artists-and-tracks
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        cancelCall();
        mCall = mOkHttpClient.newCall(requestProfile);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                Toast.makeText(MainActivity.this, "Failed to fetch data, watch Logcat for more details",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    // Extract JSON response for user profile
                    final JSONObject profileJsonObject = new JSONObject(response.body().string());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Update UI with user profile data
                            profileTextView.setText(profileJsonObject.toString());

                            // Make a second request to get the top artists
                            makeTopArtistsRequest(requestArtists);
                        }
                    });

                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse user profile data: " + e);
                    Toast.makeText(MainActivity.this, "Failed to parse user profile data, watch Logcat for more details",
                            Toast.LENGTH_SHORT).show();
                }
            }
            // Method to make a request to get top artists
            private void makeTopArtistsRequest(Request request) {
                mCall = mOkHttpClient.newCall(request);

                mCall.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("HTTP", "Failed to fetch top artists data: " + e);
                        Toast.makeText(MainActivity.this, "Failed to fetch top artists data, watch Logcat for more details",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            // Extract JSON response for top artists
                            final JSONObject topArtistsJsonObject = new JSONObject(response.body().string());

                            // Parse JSON for top artists
                            List<Artist> artists = parseArtists(topArtistsJsonObject, "items", false);

                            // Update UI with top artists data
                            StringBuilder builder = new StringBuilder("Top Artists:\n");
                            for (Artist artist : artists) {
                                builder.append(artist.toString()).append("\n");
                            }
                            final String topArtistsData = builder.toString();
                            final Request requestRecommended = new Request.Builder()
                                    .url("https://api.spotify.com/v1/artists/"+ topArtist + "/related-artists")
                                    .addHeader("Authorization", "Bearer " + mAccessToken)
                                    .build();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    profileTextView.append("\n\n" + topArtistsData);
                                    makeRecRequest(requestRecommended);
                                }
                            });


                        } catch (JSONException e) {
                            Log.d("JSON", "Failed to parse top artists data: " + e);
                            Toast.makeText(MainActivity.this, "Failed to parse top artists data, watch Logcat for more details",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            private void makeRecRequest(Request request) {
                mCall = mOkHttpClient.newCall(request);

                mCall.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("HTTP", "Failed to fetch recommended artists data: " + e);
                        Toast.makeText(MainActivity.this, "Failed to fetch recommended artists data, watch Logcat for more details",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            // Extract JSON response for top artists
                            final JSONObject topArtistsJsonObject = new JSONObject(response.body().string());

                            // Parse JSON for top artists
                            List<Artist> artists = parseArtists(topArtistsJsonObject, "artists", true);

                            // Update UI with top artists data
                            StringBuilder builder = new StringBuilder("Recommended Artists:\n");
                            for (Artist artist : artists) {
                                builder.append(artist.toString()).append("\n");
                            }
                            final String topArtistsData = builder.toString();

                            setTextAsync(topArtistsData, recTextView);


                        } catch (JSONException e) {
                            Log.d("JSON", "Failed to parse recommended artists data: " + e);
                            Toast.makeText(MainActivity.this, "Failed to parse recommended artists data, watch Logcat for more details",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            private List<Artist> parseArtists(JSONObject jsonObject, String val, Boolean limit) throws JSONException {
                if (!limit) {
                    topArtists = new ArrayList<>();
                }
                List<Artist> artists = new ArrayList<>();
                JSONArray items = jsonObject.getJSONArray(val);
                Integer cnt = 0;
                int rec = (int) (Math.random() * (items.length() - 1));

                for (int i = 0; i < items.length(); i++) {
                    if (limit && cnt == 10) {
                        break;
                    }
                    JSONObject item = items.getJSONObject(i);
                    String name = item.getString("name");
                    JSONArray genresArray = item.getJSONArray("genres");
                    List<String> genres = new ArrayList<>();
                    for (int j = 0; j < genresArray.length(); j++) {
                        genres.add(genresArray.getString(j));
                    }
                    int popularity = item.getInt("popularity");
                    String spotifyId = item.getString("id");
                    if (i == rec) {
                        topArtist = item.getString("id");
                    }
                    if (!limit) {
                        topArtists.add(spotifyId);
                    } else {
                        if (topArtists.contains(spotifyId)) {
                            continue;
                        }
                    }
                    JSONArray imagesArray = item.getJSONArray("images");
                    List<String> images = new ArrayList<>();
                    for (int j = 0; j < imagesArray.length(); j++) {
                        JSONObject imageObject = imagesArray.getJSONObject(j);
                        images.add(imageObject.getString("url"));
                    }

                    Artist artist = new Artist(name, genres, popularity, spotifyId, images);
                    artists.add(artist);
                    cnt++;
                }

                return artists;
            }

            class Artist {
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

                @Override
                public String toString() {
                    String g = "";
                    for (int i = 0; i < genres.size(); i++) {
                        if (i == genres.size() - 1) {
                            g += genres.get(i);
                        } else {
                            g += (genres.get(i) + ", ");
                        }
                    }

                    return "Name:" + name + "\n" + "Genres:" + g + images;
                }
            }

        });
    }

    /**
     * Creates a UI thread to update a TextView in the background
     * Reduces UI latency and makes the system perform more consistently
     *
     * @param text the text to set
     * @param textView TextView object to update
     */
    private void setTextAsync(final String text, TextView textView) {
        runOnUiThread(() -> textView.setText(text));
    }

    /**
     * Get authentication request
     *
     * @param type the type of the request
     * @return the authentication request
     */
    private AuthorizationRequest getAuthenticationRequest(AuthorizationResponse.Type type) {
        return new AuthorizationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
                .setShowDialog(false)
                .setScopes(new String[] { "user-read-email", "user-top-read" }) // <--- Change the scope of your requested token here
                .setCampaign("your-campaign-token")
                .build();
    }
    /**
     * Get authentication request for top items of a user
     *
     * @param type the type of the request
     * @return the authentication request
     */
    //private AuthorizationRequest getAuthenticationRequest(AuthorizationResponse.Type type) {
    //    return new AuthorizationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
    //            .setShowDialog(false)
    //            .setScopes(new String[] { "user-top-read" }) // <--- Scope for token to read top artists or songs
    //            .setCampaign("your-campaign-token")
    //            .build();
    //}

    /**
     * Gets the redirect Uri for Spotify
     *
     * @return redirect Uri object
     */
    private Uri getRedirectUri() {
        return Uri.parse(REDIRECT_URI);
    }

    private void cancelCall() {
        if (mCall != null) {
            mCall.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        cancelCall();
        super.onDestroy();
    }
}