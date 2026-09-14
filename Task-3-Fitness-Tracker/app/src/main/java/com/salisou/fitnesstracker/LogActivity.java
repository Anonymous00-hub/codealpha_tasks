package com.salisou.fitnesstracker;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LogActivity extends Activity {

    private EditText etSteps;
    private EditText etCalories;
    private EditText etDuration;
    private Spinner spinnerWorkout;
    private TextView btnSaveActivity;
    private TextView btnCancel;

    private ActivityDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        etSteps = findViewById(R.id.etSteps);
        etCalories = findViewById(R.id.etCalories);
        etDuration = findViewById(R.id.etDuration);
        spinnerWorkout = findViewById(R.id.spinnerWorkout);
        btnSaveActivity = findViewById(R.id.btnSaveActivity);
        btnCancel = findViewById(R.id.btnCancel);

        databaseHelper = new ActivityDatabaseHelper(this);

        setupWorkoutSpinner();

        btnSaveActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveActivity();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupWorkoutSpinner() {

        String[] workouts = {
                "Walking",
                "Running",
                "Cycling",
                "Gym",
                "Home Workout",
                "Other"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                workouts
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerWorkout.setAdapter(adapter);
    }

    private void saveActivity() {

        String stepsText = etSteps.getText().toString().trim();
        String caloriesText = etCalories.getText().toString().trim();
        String durationText = etDuration.getText().toString().trim();

        if (stepsText.length() == 0) {
            etSteps.setError("Enter your steps");
            etSteps.requestFocus();
            return;
        }

        if (caloriesText.length() == 0) {
            etCalories.setError("Enter calories burned");
            etCalories.requestFocus();
            return;
        }

        if (durationText.length() == 0) {
            etDuration.setError("Enter workout duration");
            etDuration.requestFocus();
            return;
        }

        int steps;
        int calories;
        int duration;

        try {
            steps = Integer.parseInt(stepsText);
            calories = Integer.parseInt(caloriesText);
            duration = Integer.parseInt(durationText);
        } catch (NumberFormatException e) {
            Toast.makeText(
                    LogActivity.this,
                    "Please enter valid numbers",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (steps < 0 || calories < 0 || duration < 0) {
            Toast.makeText(
                    LogActivity.this,
                    "Values cannot be negative",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        String workout = spinnerWorkout.getSelectedItem().toString();

        String date = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()
        ).format(new Date());

        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(
                ActivityDatabaseHelper.COLUMN_STEPS,
                steps
        );

        values.put(
                ActivityDatabaseHelper.COLUMN_CALORIES,
                calories
        );

        values.put(
                ActivityDatabaseHelper.COLUMN_WORKOUT,
                workout
        );

        values.put(
                ActivityDatabaseHelper.COLUMN_DURATION,
                duration
        );

        values.put(
                ActivityDatabaseHelper.COLUMN_DATE,
                date
        );

        long result = database.insert(
                ActivityDatabaseHelper.TABLE_ACTIVITIES,
                null,
                values
        );

        database.close();

        if (result != -1) {

            Toast.makeText(
                    LogActivity.this,
                    "Activity saved successfully",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        } else {

            Toast.makeText(
                    LogActivity.this,
                    "Failed to save activity",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}