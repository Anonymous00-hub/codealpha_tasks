package com.salisou.flashcardquizapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StudyActivity extends Activity {

    private TextView tvCardNumber;
    private TextView tvQuestion;
    private TextView tvAnswer;
    private TextView btnShowAnswer;
    private TextView btnPrevious;
    private TextView btnNext;

    private JSONArray cards;
    private int currentCard = 0;
    private boolean answerVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        tvCardNumber = findViewById(R.id.tvCardNumber);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvAnswer = findViewById(R.id.tvAnswer);

        btnShowAnswer = findViewById(R.id.btnShowAnswer);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);

        loadCards();

        btnShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnswer();
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreviousCard();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextCard();
            }
        });
    }

    private void loadCards() {

        SharedPreferences preferences = getSharedPreferences(
                "FlashcardQuizApp",
                MODE_PRIVATE
        );

        String savedCards = preferences.getString("flashcards", "[]");

        try {
            cards = new JSONArray(savedCards);

            if (cards.length() > 0) {
                currentCard = 0;
                displayCard();
            } else {
                showNoCards();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            showNoCards();
        }
    }

    private void displayCard() {

        try {
            JSONObject card = cards.getJSONObject(currentCard);

            String question = card.getString("question");
            String answer = card.getString("answer");

            tvQuestion.setText(question);
            tvAnswer.setText(answer);

            tvCardNumber.setText(
                    "Card " + (currentCard + 1) + " of " + cards.length()
            );

            answerVisible = false;

            tvAnswer.setVisibility(View.GONE);
            btnShowAnswer.setText("Show Answer");

            updateNavigationButtons();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showAnswer() {

        if (answerVisible) {
            tvAnswer.setVisibility(View.GONE);
            btnShowAnswer.setText("Show Answer");
            answerVisible = false;
        } else {
            tvAnswer.setVisibility(View.VISIBLE);
            btnShowAnswer.setText("Hide Answer");
            answerVisible = true;
        }
    }

    private void showPreviousCard() {

        if (currentCard > 0) {
            currentCard--;
            displayCard();
        }
    }

    private void showNextCard() {

        if (currentCard < cards.length() - 1) {
            currentCard++;
            displayCard();
        }
    }

    private void updateNavigationButtons() {

        if (currentCard == 0) {
            btnPrevious.setAlpha(0.5f);
        } else {
            btnPrevious.setAlpha(1.0f);
        }

        if (currentCard == cards.length() - 1) {
            btnNext.setAlpha(0.5f);
        } else {
            btnNext.setAlpha(1.0f);
        }
    }

    private void showNoCards() {

        tvCardNumber.setText("No flashcards yet");
        tvQuestion.setText("Add a flashcard to start studying.");
        tvAnswer.setVisibility(View.GONE);

        btnShowAnswer.setEnabled(false);
        btnPrevious.setEnabled(false);
        btnNext.setEnabled(false);

        btnShowAnswer.setAlpha(0.5f);
        btnPrevious.setAlpha(0.5f);
        btnNext.setAlpha(0.5f);
    }
}