package com.salisou.fitnesstracker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

    private TextView btnLogActivity;
    private TextView btnViewHistory;

    private TextView tvStepPercentage;
    private TextView tvStepsProgress;
    private TextView tvCalories;
    private TextView tvWorkoutTime;
    private TextView tvSteps;
    private TextView tvCaloriesCard;

    private TextView tvWeeklyActivities;
    private TextView tvWeeklySteps;
    private TextView tvWeeklyCalories;
    private TextView tvWeeklyWorkout;

    private ProgressBar progressSteps;

    private LinearLayout weeklyChartContainer;

    private ActivityDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogActivity = findViewById(R.id.btnLogActivity);
        btnViewHistory = findViewById(R.id.btnViewHistory);

        tvStepPercentage = findViewById(R.id.tvStepPercentage);
        tvStepsProgress = findViewById(R.id.tvStepsProgress);
        tvCalories = findViewById(R.id.tvCalories);
        tvWorkoutTime = findViewById(R.id.tvWorkoutTime);
        tvSteps = findViewById(R.id.tvSteps);
        tvCaloriesCard = findViewById(R.id.tvCaloriesCard);

        tvWeeklyActivities = findViewById(R.id.tvWeeklyActivities);
        tvWeeklySteps = findViewById(R.id.tvWeeklySteps);
        tvWeeklyCalories = findViewById(R.id.tvWeeklyCalories);
        tvWeeklyWorkout = findViewById(R.id.tvWeeklyWorkout);

        progressSteps = findViewById(R.id.progressSteps);

        weeklyChartContainer =
                findViewById(R.id.weeklyChartContainer);

        databaseHelper = new ActivityDatabaseHelper(this);

        btnLogActivity.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(
                                MainActivity.this,
                                LogActivity.class
                        );

                        startActivity(intent);
                    }
                }
        );

        btnViewHistory.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(
                                MainActivity.this,
                                HistoryActivity.class
                        );

                        startActivity(intent);
                    }
                }
        );

        loadFitnessData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadFitnessData();
    }

    private void loadFitnessData() {

        SQLiteDatabase database =
                databaseHelper.getReadableDatabase();

        String today = new SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
        ).format(new Date());

        int steps = 0;
        int calories = 0;
        int duration = 0;

        Cursor todayCursor = database.rawQuery(
                "SELECT SUM(steps), SUM(calories), SUM(duration) " +
                        "FROM activities " +
                        "WHERE date LIKE ?",
                new String[]{today + "%"}
        );

        if (todayCursor.moveToFirst()) {

            if (!todayCursor.isNull(0)) {
                steps = todayCursor.getInt(0);
            }

            if (!todayCursor.isNull(1)) {
                calories = todayCursor.getInt(1);
            }

            if (!todayCursor.isNull(2)) {
                duration = todayCursor.getInt(2);
            }
        }

        todayCursor.close();

        tvStepsProgress.setText(
                steps + " / 10000 steps"
        );

        tvCalories.setText(
                calories + " kcal"
        );

        tvWorkoutTime.setText(
                duration + " min"
        );

        tvSteps.setText(
                String.valueOf(steps)
        );

        tvCaloriesCard.setText(
                String.valueOf(calories)
        );

        int progress = Math.min(
                steps,
                10000
        );

        progressSteps.setProgress(progress);

        int percentage =
                (steps * 100) / 10000;

        if (percentage > 100) {
            percentage = 100;
        }

        tvStepPercentage.setText(
                percentage + "%"
        );

        loadWeeklySummary(database);
        loadWeeklyChart(database);
    }

    private void loadWeeklySummary(
            SQLiteDatabase database
    ) {

        int activities = 0;
        int steps = 0;
        int calories = 0;
        int workout = 0;

        Cursor cursor = database.rawQuery(
                "SELECT COUNT(*), SUM(steps), " +
                        "SUM(calories), SUM(duration) " +
                        "FROM activities " +
                        "WHERE date >= datetime('now', '-7 days')",
                null
        );

        if (cursor.moveToFirst()) {

            if (!cursor.isNull(0)) {
                activities = cursor.getInt(0);
            }

            if (!cursor.isNull(1)) {
                steps = cursor.getInt(1);
            }

            if (!cursor.isNull(2)) {
                calories = cursor.getInt(2);
            }

            if (!cursor.isNull(3)) {
                workout = cursor.getInt(3);
            }
        }

        cursor.close();

        tvWeeklyActivities.setText(
                String.valueOf(activities)
        );

        tvWeeklySteps.setText(
                String.valueOf(steps)
        );

        tvWeeklyCalories.setText(
                calories + " kcal"
        );

        tvWeeklyWorkout.setText(
                workout + " min"
        );
    }

    private void loadWeeklyChart(
            SQLiteDatabase database
    ) {

        weeklyChartContainer.removeAllViews();

        Calendar calendar =
                Calendar.getInstance();

        calendar.add(
                Calendar.DAY_OF_YEAR,
                -6
        );

        int[] dailySteps = new int[7];
        String[] dayNames = new String[7];

        int maximumSteps = 0;

        for (int i = 0; i < 7; i++) {

            String date =
                    new SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                    ).format(calendar.getTime());

            dayNames[i] =
                    new SimpleDateFormat(
                            "EEE",
                            Locale.getDefault()
                    ).format(calendar.getTime());

            Cursor cursor = database.rawQuery(
                    "SELECT SUM(steps) " +
                            "FROM activities " +
                            "WHERE date LIKE ?",
                    new String[]{date + "%"}
            );

            int steps = 0;

            if (cursor.moveToFirst()) {

                if (!cursor.isNull(0)) {
                    steps = cursor.getInt(0);
                }
            }

            cursor.close();

            dailySteps[i] = steps;

            if (steps > maximumSteps) {
                maximumSteps = steps;
            }

            calendar.add(
                    Calendar.DAY_OF_YEAR,
                    1
            );
        }

        for (int i = 0; i < 7; i++) {

            LinearLayout dayColumn =
                    new LinearLayout(this);

            dayColumn.setOrientation(
                    LinearLayout.VERTICAL
            );

            dayColumn.setGravity(
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL
            );

            LinearLayout.LayoutParams columnParams =
                    new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    );

            columnParams.weight = 1;

            dayColumn.setLayoutParams(
                    columnParams
            );

            TextView stepsLabel =
                    new TextView(this);

            stepsLabel.setText(
                    String.valueOf(dailySteps[i])
            );

            stepsLabel.setTextSize(10);

            stepsLabel.setTextColor(
                    getResources().getColor(
                            R.color.text_secondary,
                            getTheme()
                    )
            );

            stepsLabel.setGravity(
                    Gravity.CENTER
            );

            dayColumn.addView(
                    stepsLabel
            );

            int barHeight;

            if (maximumSteps == 0) {

                barHeight = 8;

            } else {

                barHeight =
                        (dailySteps[i] * 100)
                                / maximumSteps;

                if (barHeight < 8) {
                    barHeight = 8;
                }
            }

            View bar = new View(this);

            bar.setBackgroundColor(
                    getResources().getColor(
                            R.color.primary,
                            getTheme()
                    )
            );

            LinearLayout.LayoutParams barParams =
                    new LinearLayout.LayoutParams(
                            22,
                            barHeight
                    );

            barParams.gravity =
                    Gravity.CENTER_HORIZONTAL;

            bar.setLayoutParams(
                    barParams
            );

            dayColumn.addView(
                    bar
            );

            TextView dayLabel =
                    new TextView(this);

            dayLabel.setText(
                    dayNames[i]
            );

            dayLabel.setTextSize(11);

            dayLabel.setTextColor(
                    getResources().getColor(
                            R.color.text_secondary,
                            getTheme()
                    )
            );

            dayLabel.setTypeface(
                    Typeface.DEFAULT,
                    Typeface.BOLD
            );

            dayLabel.setGravity(
                    Gravity.CENTER
            );

            LinearLayout.LayoutParams dayParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

            dayParams.topMargin = 6;

            dayLabel.setLayoutParams(
                    dayParams
            );

            dayColumn.addView(
                    dayLabel
            );

            weeklyChartContainer.addView(
                    dayColumn
            );
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