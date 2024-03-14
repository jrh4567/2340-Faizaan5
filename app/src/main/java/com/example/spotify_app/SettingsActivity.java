package com.example.spotify_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Button backBtn = (Button) findViewById(R.id.back_btn);
        Button logoutBtn = (Button) findViewById(R.id.logout_btn);
        Button deleteBtn = (Button) findViewById(R.id.delete_btn);

        Spinner timeRangeSpinner = findViewById(R.id.time_range_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time_ranges, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeRangeSpinner.setAdapter(adapter);

        timeRangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                String selectedTimeRange = parent.getItemAtPosition(position).toString();
                Data.setTime(selectedTimeRange);
                // Handle the selected time range
                Toast.makeText(SettingsActivity.this, "Selected Time Range: " + selectedTimeRange, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        backBtn.setOnClickListener((v) -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
        });
        logoutBtn.setOnClickListener((v) -> {
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        deleteBtn.setOnClickListener((v) -> {
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            Toast.makeText(SettingsActivity.this, "deleted account", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });
    }

}
