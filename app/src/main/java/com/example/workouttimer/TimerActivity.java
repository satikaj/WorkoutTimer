package com.example.workouttimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.View;

import com.example.workouttimer.databinding.ActivityTimerBinding;

import java.util.Set;

public class TimerActivity extends AppCompatActivity {

    ActivityTimerBinding binding;
    int workoutDuration, restDuration, totalSets, currentSet, currentDuration;
    boolean isWorkout;
    CountDownTimer currentTimer;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Get workout info from user input
        Intent workoutInfo = getIntent();
        workoutDuration = Integer.parseInt(workoutInfo.getStringExtra("workoutDuration"));
        restDuration = Integer.parseInt(workoutInfo.getStringExtra("restDuration"));
        totalSets = Integer.parseInt(workoutInfo.getStringExtra("totalSets"));

        // Start with the workout phase of set 1
        currentSet = 1;
        isWorkout = true;
        currentDuration = workoutDuration;
        updatePhase();

        mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI);

        binding.startTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });

        binding.stopTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
            }
        });
    }

    private void updatePhase() {
        // Display updated workout phase info
        binding.timeLeft.setText(String.valueOf(currentDuration));
        binding.phase.setText(isWorkout ? "Workout" : "Rest");
        binding.setNum.setText("Set " + currentSet + " of " + totalSets);

        binding.progressBar.setMax(currentDuration);
        binding.progressBar.setProgress(0);
    }

    private void updateTime(long millisUntilFinished) {
        // Display updated time
        int secsUntilFinished = (int) millisUntilFinished / 1000;
        binding.timeLeft.setText(String.valueOf(secsUntilFinished));
        binding.progressBar.setProgress(currentDuration - secsUntilFinished);
    }

    private void startTimer() {
        // Create timer
        currentTimer = new CountDownTimer(currentDuration * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTime(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                // Play a sound when the timer has finished
                mediaPlayer.start();

                // If the last set is done, display done
                if (currentSet == totalSets && isWorkout) {
                    binding.timeLeft.setText("Done");
                    mediaPlayer.release();
                }
                else {
                    // If rest is done, start next set
                    if (!isWorkout) currentSet++;

                    // Switch from workout to rest, and vice versa
                    isWorkout = !isWorkout;
                    currentDuration = isWorkout ? workoutDuration : restDuration;
                    updatePhase();
                }
            }
        };

        // Start timer
        currentTimer.start();
    }

    private void stopTimer() {
        // If a time exists, stop timer
        if (currentTimer != null) currentTimer.cancel();
    }
}