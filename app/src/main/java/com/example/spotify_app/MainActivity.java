package com.example.spotify_app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
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
    public static final String REDIRECT_URI = "Spotify-App://auth";

    public static final int AUTH_TOKEN_REQUEST_CODE = 0;
    public static final int AUTH_CODE_REQUEST_CODE = 1;

    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private String mAccessToken, mAccessCode;
    private Call mCall;

    private TextView tokenTextView, codeTextView, profileTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the views
        tokenTextView = (TextView) findViewById(R.id.token_text_view);
        codeTextView = (TextView) findViewById(R.id.code_text_view);
        profileTextView = (TextView) findViewById(R.id.response_text_view);

        // Initialize the buttons
        Button tokenBtn = (Button) findViewById(R.id.token_btn);
        Button codeBtn = (Button) findViewById(R.id.code_btn);
        Button profileBtn = (Button) findViewById(R.id.profile_btn);

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
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();
        // request for top artists or tracks
        //final Request request = new Request.Builder()
        //        .url("https://api.spotify.com/v1/me/top/{type}") //for {type}, replace with artists or tracks, for more options such as time range, go to https://developer.spotify.com/documentation/web-api/reference/get-users-top-artists-and-tracks
        //        .addHeader("Authorization", "Bearer " + mAccessToken)
        //        .build();

        cancelCall();
        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                Toast.makeText(MainActivity.this, "Failed to fetch data, watch Logcat for more details",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            //public void onResponse(Call call, Response response) throws IOException {
            //    try {
            //        final JSONObject jsonObject = new JSONObject(response.body().string());
            //        setTextAsync(jsonObject.toString(3), profileTextView);
            //    } catch (JSONException e) {
            //        Log.d("JSON", "Failed to parse data: " + e);
            //        Toast.makeText(MainActivity.this, "Failed to parse data, watch Logcat for more details",
            //                Toast.LENGTH_SHORT).show();
            //    }
            //}
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    // Extract JSON response
                    final JSONObject jsonObject = new JSONObject(response.body().string());

                    // Parse JSON and store data
                    List<Artist> artists = parseArtists(jsonObject); //assuming api call is for top artists

                    // Update UI with parsed data
                    StringBuilder builder = new StringBuilder();
                    for (Artist artist : artists) {
                        builder.append(artist.toString()).append("\n");
                    }
                    final String artistsData = builder.toString();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            profileTextView.setText(artistsData);
                        }
                    });

                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    Toast.makeText(MainActivity.this, "Failed to parse data, watch Logcat for more details",
                            Toast.LENGTH_SHORT).show();
                }
            }

            // Function to parse artists from JSON response
            private List<Artist> parseArtists(JSONObject jsonObject) throws JSONException {
                List<Artist> artists = new ArrayList<>();

                // Extract relevant information from JSON response and create Artist objects
                JSONArray items = jsonObject.getJSONArray("items");
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    String name = item.getString("name");
                    JSONArray genresArray = item.getJSONArray("genres");
                    List<String> genres = new ArrayList<>();
                    for (int j = 0; j < genresArray.length(); j++) {
                        genres.add(genresArray.getString(j));
                    }
                    int popularity = item.getInt("popularity");

                    // Create Artist object and add it to the list
                    Artist artist = new Artist(name, genres, popularity);
                    artists.add(artist);
                }

                return artists;
            }

            // Define Artist class to store artist information, can extract if needed to another file for readability
            class Artist {
                private String name;
                private List<String> genres;
                private int popularity;

                public Artist(String name, List<String> genres, int popularity) {
                    this.name = name;
                    this.genres = genres;
                    this.popularity = popularity;
                }

                @Override
                public String toString() {
                    return "Name: " + name + ", Genres: " + genres + ", Popularity: " + popularity;
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
                .setScopes(new String[] { "user-read-email" }) // <--- Change the scope of your requested token here
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