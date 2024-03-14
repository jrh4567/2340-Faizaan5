package com.example.spotify_app;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WrappedActivity extends AppCompatActivity {
    // class to handle wrapped activity, where the spotify wrapped is displayed, not yet connected
    private TextView displayTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrapped);

        displayTextView = (TextView) findViewById(R.id.display_text);
        setTextAsync(Data.getData(), displayTextView);
    }
    private void setTextAsync(final String text, TextView textView) {
        runOnUiThread(() -> textView.setText(text));
    }
}
