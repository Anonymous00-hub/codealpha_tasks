package com.salisou.flashcardquizapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ManageFlashcardsActivity extends Activity {

    private LinearLayout cardListContainer;
    private LinearLayout emptyState;
    private TextView tvManageCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_flashcards);

        cardListContainer = findViewById(R.id.cardListContainer);
        emptyState = findViewById(R.id.emptyState);
        tvManageCount = findViewById(R.id.tvManageCount);

        View btnAddCard = findViewById(R.id.btnManageAddCard);

        btnAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(
                        ManageFlashcardsActivity.this,
                        AddCardActivity.class
                );

                startActivity(intent);
            }
        });

        loadFlashcards();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFlashcards();
    }

    private void loadFlashcards() {

        cardListContainer.removeAllViews();

        SharedPreferences preferences = getSharedPreferences(
                "FlashcardQuizApp",
                MODE_PRIVATE
        );

        String savedCards = preferences.getString(
                "flashcards",
                "[]"
        );

        try {

            JSONArray cards = new JSONArray(savedCards);

            int count = cards.length();

            if (count == 1) {
                tvManageCount.setText("1 flashcard");
            } else {
                tvManageCount.setText(count + " flashcards");
            }

            if (count == 0) {

                emptyState.setVisibility(View.VISIBLE);
                cardListContainer.setVisibility(View.GONE);

                return;
            }

            emptyState.setVisibility(View.GONE);
            cardListContainer.setVisibility(View.VISIBLE);

            for (int i = 0; i < count; i++) {

                JSONObject card = cards.getJSONObject(i);

                String question = card.getString("question");
                String answer = card.getString("answer");

                addCardView(
                        i,
                        question,
                        answer
                );
            }

        } catch (JSONException e) {

            e.printStackTrace();

            emptyState.setVisibility(View.VISIBLE);
            cardListContainer.setVisibility(View.GONE);
        }
    }

    private void addCardView(
            final int position,
            String question,
            String answer
    ) {

        LinearLayout cardLayout = new LinearLayout(this);

        cardLayout.setOrientation(
                LinearLayout.VERTICAL
        );

        cardLayout.setPadding(
                20,
                20,
                20,
                20
        );

        cardLayout.setBackgroundResource(
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

        cardLayout.setLayoutParams(cardParams);

        // Card number
        TextView numberView = new TextView(this);

        numberView.setText(
                "Card " + (position + 1)
        );

        numberView.setTextColor(
                getResources().getColor(R.color.primary)
        );

        numberView.setTextSize(12);

        numberView.setTypeface(
                null,
                Typeface.BOLD
        );

        // Question
        TextView questionView = new TextView(this);

        questionView.setText(question);

        questionView.setTextColor(
                getResources().getColor(R.color.text_primary)
        );

        questionView.setTextSize(17);

        questionView.setTypeface(
                null,
                Typeface.BOLD
        );

        LinearLayout.LayoutParams questionParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        questionParams.setMargins(
                0,
                8,
                0,
                0
        );

        questionView.setLayoutParams(
                questionParams
        );

        // Answer
        TextView answerView = new TextView(this);

        answerView.setText(answer);

        answerView.setTextColor(
                getResources().getColor(R.color.text_secondary)
        );

        answerView.setTextSize(14);

        LinearLayout.LayoutParams answerParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        answerParams.setMargins(
                0,
                8,
                0,
                16
        );

        answerView.setLayoutParams(
                answerParams
        );

        // Action row
        LinearLayout actionRow = new LinearLayout(this);

        actionRow.setOrientation(
                LinearLayout.HORIZONTAL
        );

        actionRow.setGravity(
                Gravity.CENTER_VERTICAL
        );

        // EDIT BUTTON
        TextView editButton = new TextView(this);

        editButton.setText("Edit");

        editButton.setTextColor(
                getResources().getColor(R.color.text_primary)
        );

        editButton.setTextSize(15);

        editButton.setTypeface(
                null,
                Typeface.BOLD
        );

        editButton.setGravity(
                Gravity.CENTER
        );

        editButton.setBackgroundResource(
                R.drawable.bg_secondary_button
        );

        editButton.setClickable(true);
        editButton.setFocusable(true);

        editButton.setPadding(
                0,
                0,
                0,
                0
        );

        LinearLayout.LayoutParams editParams =
                new LinearLayout.LayoutParams(
                        0,
                        54,
                        1
                );

        editParams.setMargins(
                0,
                0,
                6,
                0
        );

        editButton.setLayoutParams(
                editParams
        );

        // DELETE BUTTON
        TextView deleteButton = new TextView(this);

        deleteButton.setText("Delete");

        deleteButton.setTextColor(
                getResources().getColor(R.color.text_primary)
        );

        deleteButton.setTextSize(15);

        deleteButton.setTypeface(
                null,
                Typeface.BOLD
        );

        deleteButton.setGravity(
                Gravity.CENTER
        );

        deleteButton.setBackgroundResource(
                R.drawable.bg_secondary_button
        );

        deleteButton.setClickable(true);
        deleteButton.setFocusable(true);

        deleteButton.setPadding(
                0,
                0,
                0,
                0
        );

        LinearLayout.LayoutParams deleteParams =
                new LinearLayout.LayoutParams(
                        0,
                        54,
                        1
                );

        deleteParams.setMargins(
                6,
                0,
                0,
                0
        );

        deleteButton.setLayoutParams(
                deleteParams
        );

        // Edit action
        editButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editFlashcard(position);
                    }
                }
        );

        // Delete action
        deleteButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmDelete(position);
                    }
                }
        );

        actionRow.addView(editButton);
        actionRow.addView(deleteButton);

        cardLayout.addView(numberView);
        cardLayout.addView(questionView);
        cardLayout.addView(answerView);
        cardLayout.addView(actionRow);

        cardListContainer.addView(
                cardLayout
        );
    }

    private void editFlashcard(
            final int position
    ) {

        SharedPreferences preferences =
                getSharedPreferences(
                        "FlashcardQuizApp",
                        MODE_PRIVATE
                );

        String savedCards =
                preferences.getString(
                        "flashcards",
                        "[]"
                );

        try {

            JSONArray cards =
                    new JSONArray(savedCards);

            JSONObject card =
                    cards.getJSONObject(position);

            final EditText questionInput =
                    new EditText(this);

            questionInput.setText(
                    card.getString("question")
            );

            questionInput.setHint("Question");

            questionInput.setSingleLine(false);

            final EditText answerInput =
                    new EditText(this);

            answerInput.setText(
                    card.getString("answer")
            );

            answerInput.setHint("Answer");

            answerInput.setSingleLine(false);

            LinearLayout editLayout =
                    new LinearLayout(this);

            editLayout.setOrientation(
                    LinearLayout.VERTICAL
            );

            editLayout.setPadding(
                    40,
                    10,
                    40,
                    10
            );

            editLayout.addView(
                    questionInput
            );

            editLayout.addView(
                    answerInput
            );

            AlertDialog dialog =
                    new AlertDialog.Builder(this)
                            .setTitle("Edit Flashcard")
                            .setView(editLayout)
                            .setNegativeButton(
                                    "Cancel",
                                    null
                            )
                            .setPositiveButton(
                                    "Save",
                                    null
                            )
                            .create();

            dialog.setOnShowListener(
                    new android.content.DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(
                                android.content.DialogInterface dialogInterface
                        ) {

                            dialog.getButton(
                                    AlertDialog.BUTTON_POSITIVE
                            ).setOnClickListener(
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(
                                                View v
                                        ) {

                                            String question =
                                                    questionInput
                                                            .getText()
                                                            .toString()
                                                            .trim();

                                            String answer =
                                                    answerInput
                                                            .getText()
                                                            .toString()
                                                            .trim();

                                            if (question.isEmpty()) {

                                                questionInput.setError(
                                                        "Please enter a question"
                                                );

                                                questionInput.requestFocus();

                                                return;
                                            }

                                            if (answer.isEmpty()) {

                                                answerInput.setError(
                                                        "Please enter an answer"
                                                );

                                                answerInput.requestFocus();

                                                return;
                                            }

                                            updateFlashcard(
                                                    position,
                                                    question,
                                                    answer
                                            );

                                            dialog.dismiss();
                                        }
                                    }
                            );
                        }
                    }
            );

            dialog.show();

        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    private void updateFlashcard(
            int position,
            String question,
            String answer
    ) {

        SharedPreferences preferences =
                getSharedPreferences(
                        "FlashcardQuizApp",
                        MODE_PRIVATE
                );

        String savedCards =
                preferences.getString(
                        "flashcards",
                        "[]"
                );

        try {

            JSONArray cards =
                    new JSONArray(savedCards);

            JSONObject card =
                    cards.getJSONObject(position);

            card.put(
                    "question",
                    question
            );

            card.put(
                    "answer",
                    answer
            );

            preferences.edit()
                    .putString(
                            "flashcards",
                            cards.toString()
                    )
                    .putInt(
                            "card_count",
                            cards.length()
                    )
                    .apply();

            Toast.makeText(
                    this,
                    "Flashcard updated successfully!",
                    Toast.LENGTH_SHORT
            ).show();

            loadFlashcards();

        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    private void confirmDelete(
            final int position
    ) {

        new AlertDialog.Builder(this)
                .setTitle("Delete Flashcard")
                .setMessage(
                        "Are you sure you want to delete this flashcard?"
                )
                .setNegativeButton(
                        "Cancel",
                        null
                )
                .setPositiveButton(
                        "Delete",
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    android.content.DialogInterface dialog,
                                    int which
                            ) {

                                deleteFlashcard(
                                        position
                                );
                            }
                        }
                )
                .show();
    }

    private void deleteFlashcard(
            int position
    ) {

        SharedPreferences preferences =
                getSharedPreferences(
                        "FlashcardQuizApp",
                        MODE_PRIVATE
                );

        String savedCards =
                preferences.getString(
                        "flashcards",
                        "[]"
                );

        try {

            JSONArray cards =
                    new JSONArray(savedCards);

            cards.remove(position);

            preferences.edit()
                    .putString(
                            "flashcards",
                            cards.toString()
                    )
                    .putInt(
                            "card_count",
                            cards.length()
                    )
                    .apply();

            Toast.makeText(
                    this,
                    "Flashcard deleted",
                    Toast.LENGTH_SHORT
            ).show();

            loadFlashcards();

        } catch (JSONException e) {

            e.printStackTrace();
        }
    }
}