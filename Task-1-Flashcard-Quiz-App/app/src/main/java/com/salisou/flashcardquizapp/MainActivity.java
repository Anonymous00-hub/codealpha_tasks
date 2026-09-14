package com.salisou.flashcardquizapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView tvCardCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCardCount = findViewById(R.id.tvCardCount);

        View btnAddCard = findViewById(R.id.btnAddCard);
        View btnStudy = findViewById(R.id.btnStudy);
        View btnManageFlashcards = findViewById(R.id.btnManageFlashcards);

        btnAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        MainActivity.this,
                        AddCardActivity.class
                );

                startActivity(intent);
            }
        });

        btnStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        MainActivity.this,
                        StudyActivity.class
                );

                startActivity(intent);
            }
        });

        btnManageFlashcards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        MainActivity.this,
                        ManageFlashcardsActivity.class
                );

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCardCount();
    }

    private void updateCardCount() {

        SharedPreferences preferences = getSharedPreferences(
                "FlashcardQuizApp",
                MODE_PRIVATE
        );

        int cardCount = preferences.getInt("card_count", 0);

        tvCardCount.setText(String.valueOf(cardCount));
    }
}