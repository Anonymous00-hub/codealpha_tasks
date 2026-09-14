package com.salisou.fitnesstracker;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class HistoryActivity extends Activity {

    private LinearLayout historyContainer;
    private TextView tvNoHistory;

    private ActivityDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyContainer = findViewById(R.id.historyContainer);
        tvNoHistory = findViewById(R.id.tvNoHistory);

        databaseHelper = new ActivityDatabaseHelper(this);

        loadHistory();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadHistory();
    }

    private void loadHistory() {

        historyContainer.removeAllViews();

        SQLiteDatabase database =
                databaseHelper.getReadableDatabase();

        Cursor cursor = database.query(
                ActivityDatabaseHelper.TABLE_ACTIVITIES,
                null,
                null,
                null,
                null,
                null,
                ActivityDatabaseHelper.COLUMN_ID + " DESC"
        );

        if (cursor.getCount() == 0) {

            tvNoHistory.setVisibility(View.VISIBLE);

            cursor.close();
            return;
        }

        tvNoHistory.setVisibility(View.GONE);

        int idIndex = cursor.getColumnIndexOrThrow(
                ActivityDatabaseHelper.COLUMN_ID
        );

        int stepsIndex = cursor.getColumnIndexOrThrow(
                ActivityDatabaseHelper.COLUMN_STEPS
        );

        int caloriesIndex = cursor.getColumnIndexOrThrow(
                ActivityDatabaseHelper.COLUMN_CALORIES
        );

        int workoutIndex = cursor.getColumnIndexOrThrow(
                ActivityDatabaseHelper.COLUMN_WORKOUT
        );

        int durationIndex = cursor.getColumnIndexOrThrow(
                ActivityDatabaseHelper.COLUMN_DURATION
        );

        int dateIndex = cursor.getColumnIndexOrThrow(
                ActivityDatabaseHelper.COLUMN_DATE
        );

        while (cursor.moveToNext()) {

            int id = cursor.getInt(idIndex);

            int steps = cursor.getInt(stepsIndex);

            int calories = cursor.getInt(caloriesIndex);

            String workout = cursor.getString(workoutIndex);

            int duration = cursor.getInt(durationIndex);

            String date = cursor.getString(dateIndex);

            addHistoryCard(
                    id,
                    workout,
                    steps,
                    calories,
                    duration,
                    date
            );
        }

        cursor.close();
    }

    private void addHistoryCard(
            final int id,
            String workout,
            int steps,
            int calories,
            int duration,
            String date
    ) {

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                20,
                18,
                20,
                18
        );

        card.setBackgroundResource(
                R.drawable.bg_card
        );

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        cardParams.setMargins(
                0,
                0,
                0,
                16
        );

        card.setLayoutParams(cardParams);

        TextView tvWorkout = new TextView(this);

        tvWorkout.setText(workout);

        tvWorkout.setTextColor(
                getResources().getColor(
                        R.color.text_primary,
                        getTheme()
                )
        );

        tvWorkout.setTextSize(18);

        tvWorkout.setTypeface(
                android.graphics.Typeface.DEFAULT,
                android.graphics.Typeface.BOLD
        );

        card.addView(tvWorkout);

        TextView tvDate = new TextView(this);

        tvDate.setText(date);

        tvDate.setTextColor(
                getResources().getColor(
                        R.color.text_secondary,
                        getTheme()
                )
        );

        tvDate.setTextSize(13);

        LinearLayout.LayoutParams dateParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        dateParams.setMargins(
                0,
                5,
                0,
                14
        );

        tvDate.setLayoutParams(dateParams);

        card.addView(tvDate);

        TextView tvDetails = new TextView(this);

        String details =
                steps + " steps    •    "
                        + calories + " kcal    •    "
                        + duration + " min";

        tvDetails.setText(details);

        tvDetails.setTextColor(
                getResources().getColor(
                        R.color.text_primary,
                        getTheme()
                )
        );

        tvDetails.setTextSize(14);

        card.addView(tvDetails);

        card.setOnLongClickListener(
                new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {

                        showActivityOptions(id);

                        return true;
                    }
                }
        );

        historyContainer.addView(card);
    }

    private void showActivityOptions(final int id) {

        final String[] options = {
                "Edit Activity",
                "Delete Activity"
        };

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle("Activity Options");

        builder.setItems(
                options,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(
                            DialogInterface dialog,
                            int which
                    ) {

                        if (which == 0) {

                            editActivity(id);

                        } else if (which == 1) {

                            confirmDelete(id);
                        }
                    }
                }
        );

        builder.setNegativeButton(
                "Cancel",
                null
        );

        builder.show();
    }

    private void confirmDelete(final int id) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle("Delete Activity");

        builder.setMessage(
                "Are you sure you want to delete this activity?"
        );

        builder.setPositiveButton(
                "Delete",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(
                            DialogInterface dialog,
                            int which
                    ) {

                        deleteActivity(id);
                    }
                }
        );

        builder.setNegativeButton(
                "Cancel",
                null
        );

        builder.show();
    }

    private void deleteActivity(int id) {

        SQLiteDatabase database =
                databaseHelper.getWritableDatabase();

        int result = database.delete(
                ActivityDatabaseHelper.TABLE_ACTIVITIES,
                ActivityDatabaseHelper.COLUMN_ID + " = ?",
                new String[]{
                        String.valueOf(id)
                }
        );

        if (result > 0) {

            Toast.makeText(
                    HistoryActivity.this,
                    "Activity deleted",
                    Toast.LENGTH_SHORT
            ).show();

            loadHistory();

        } else {

            Toast.makeText(
                    HistoryActivity.this,
                    "Unable to delete activity",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void editActivity(final int id) {

        SQLiteDatabase database =
                databaseHelper.getReadableDatabase();

        Cursor cursor = database.query(
                ActivityDatabaseHelper.TABLE_ACTIVITIES,
                null,
                ActivityDatabaseHelper.COLUMN_ID + " = ?",
                new String[]{
                        String.valueOf(id)
                },
                null,
                null,
                null
        );

        if (!cursor.moveToFirst()) {

            cursor.close();

            Toast.makeText(
                    HistoryActivity.this,
                    "Activity not found",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        int steps = cursor.getInt(
                cursor.getColumnIndexOrThrow(
                        ActivityDatabaseHelper.COLUMN_STEPS
                )
        );

        int calories = cursor.getInt(
                cursor.getColumnIndexOrThrow(
                        ActivityDatabaseHelper.COLUMN_CALORIES
                )
        );

        int duration = cursor.getInt(
                cursor.getColumnIndexOrThrow(
                        ActivityDatabaseHelper.COLUMN_DURATION
                )
        );

        String workout = cursor.getString(
                cursor.getColumnIndexOrThrow(
                        ActivityDatabaseHelper.COLUMN_WORKOUT
                )
        );

        cursor.close();

        showEditDialog(
                id,
                steps,
                calories,
                duration,
                workout
        );
    }

    private void showEditDialog(
            final int id,
            int steps,
            int calories,
            int duration,
            String currentWorkout
    ) {

        LinearLayout layout =
                new LinearLayout(this);

        layout.setOrientation(
                LinearLayout.VERTICAL
        );

        layout.setPadding(
                40,
                10,
                40,
                0
        );

        TextView workoutLabel =
                new TextView(this);

        workoutLabel.setText(
                "Exercise Type"
        );

        workoutLabel.setTextSize(14);

        workoutLabel.setTextColor(
                getResources().getColor(
                        R.color.text_secondary,
                        getTheme()
                )
        );

        layout.addView(workoutLabel);

        final Spinner spinnerWorkout =
                new Spinner(this);

        String[] workouts = {
                "Walking",
                "Running",
                "Cycling",
                "Gym",
                "Home Workout",
                "Other"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_spinner_item,
                        workouts
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerWorkout.setAdapter(adapter);

        int selectedPosition = 0;

        for (int i = 0; i < workouts.length; i++) {

            if (workouts[i].equals(currentWorkout)) {

                selectedPosition = i;
                break;
            }
        }

        spinnerWorkout.setSelection(
                selectedPosition
        );

        layout.addView(spinnerWorkout);

        final EditText etSteps =
                new EditText(this);

        etSteps.setHint("Steps");

        etSteps.setInputType(
                InputType.TYPE_CLASS_NUMBER
        );

        etSteps.setText(
                String.valueOf(steps)
        );

        layout.addView(etSteps);

        final EditText etCalories =
                new EditText(this);

        etCalories.setHint("Calories");

        etCalories.setInputType(
                InputType.TYPE_CLASS_NUMBER
        );

        etCalories.setText(
                String.valueOf(calories)
        );

        layout.addView(etCalories);

        final EditText etDuration =
                new EditText(this);

        etDuration.setHint(
                "Workout duration (minutes)"
        );

        etDuration.setInputType(
                InputType.TYPE_CLASS_NUMBER
        );

        etDuration.setText(
                String.valueOf(duration)
        );

        layout.addView(etDuration);

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle(
                "Edit Activity"
        );

        builder.setView(layout);

        builder.setPositiveButton(
                "Save",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(
                            DialogInterface dialog,
                            int which
                    ) {

                        String workout =
                                spinnerWorkout
                                        .getSelectedItem()
                                        .toString();

                        updateActivity(
                                id,
                                workout,
                                etSteps,
                                etCalories,
                                etDuration
                        );
                    }
                }
        );

        builder.setNegativeButton(
                "Cancel",
                null
        );

        builder.show();
    }

    private void updateActivity(
            int id,
            String workout,
            EditText etSteps,
            EditText etCalories,
            EditText etDuration
    ) {

        String stepsText =
                etSteps.getText().toString().trim();

        String caloriesText =
                etCalories.getText().toString().trim();

        String durationText =
                etDuration.getText().toString().trim();

        if (stepsText.length() == 0 ||
                caloriesText.length() == 0 ||
                durationText.length() == 0) {

            Toast.makeText(
                    HistoryActivity.this,
                    "Please fill in all fields",
                    Toast.LENGTH_SHORT
            ).show();

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
                    HistoryActivity.this,
                    "Please enter valid numbers",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (steps < 0 ||
                calories < 0 ||
                duration < 0) {

            Toast.makeText(
                    HistoryActivity.this,
                    "Values cannot be negative",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        SQLiteDatabase database =
                databaseHelper.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                ActivityDatabaseHelper.COLUMN_WORKOUT,
                workout
        );

        values.put(
                ActivityDatabaseHelper.COLUMN_STEPS,
                steps
        );

        values.put(
                ActivityDatabaseHelper.COLUMN_CALORIES,
                calories
        );

        values.put(
                ActivityDatabaseHelper.COLUMN_DURATION,
                duration
        );

        int result = database.update(
                ActivityDatabaseHelper.TABLE_ACTIVITIES,
                values,
                ActivityDatabaseHelper.COLUMN_ID + " = ?",
                new String[]{
                        String.valueOf(id)
                }
        );

        if (result > 0) {

            Toast.makeText(
                    HistoryActivity.this,
                    "Activity updated successfully",
                    Toast.LENGTH_SHORT
            ).show();

            loadHistory();

        } else {

            Toast.makeText(
                    HistoryActivity.this,
                    "Unable to update activity",
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

