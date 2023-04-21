package com.example.workouttimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.workouttimer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass workout info to TimerActivity and start it
                Intent startIntent = new Intent(MainActivity.this, TimerActivity.class);
                startIntent.putExtra("workoutDuration", binding.workoutDuration.getText().toString().replaceAll("[^0-9]", ""));
                startIntent.putExtra("restDuration", binding.restDuration.getText().toString().replaceAll("[^0-9]", ""));
                startIntent.putExtra("totalSets", binding.totalSets.getText().toString().replaceAll("[^0-9]", ""));
                startActivity(startIntent);
            }
        });
    }
}