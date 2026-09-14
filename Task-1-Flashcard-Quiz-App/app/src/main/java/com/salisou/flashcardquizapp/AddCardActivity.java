package com.salisou.flashcardquizapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddCardActivity extends Activity {

    private EditText etQuestion;
    private EditText etAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        etQuestion = findViewById(R.id.etQuestion);
        etAnswer = findViewById(R.id.etAnswer);

        View btnSaveCard = findViewById(R.id.btnSaveCard);
        View btnCancel = findViewById(R.id.btnCancel);

        btnSaveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String question = etQuestion.getText().toString().trim();
                String answer = etAnswer.getText().toString().trim();

                if (question.isEmpty()) {
                    etQuestion.setError("Please enter a question");
                    etQuestion.requestFocus();
                    return;
                }

                if (answer.isEmpty()) {
                    etAnswer.setError("Please enter an answer");
                    etAnswer.requestFocus();
                    return;
                }

                saveFlashcard(question, answer);

                Toast.makeText(
                        AddCardActivity.this,
                        "Flashcard saved successfully!",
                        Toast.LENGTH_SHORT
                ).show();

                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveFlashcard(String question, String answer) {

        SharedPreferences preferences = getSharedPreferences(
                "FlashcardQuizApp",
                MODE_PRIVATE
        );

        String savedCards = preferences.getString("flashcards", "[]");

        try {
            JSONArray cards = new JSONArray(savedCards);

            JSONObject newCard = new JSONObject();
            newCard.put("question", question);
            newCard.put("answer", answer);

            cards.put(newCard);

            preferences.edit()
                    .putString("flashcards", cards.toString())
                    .putInt("card_count", cards.length())
                    .apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}