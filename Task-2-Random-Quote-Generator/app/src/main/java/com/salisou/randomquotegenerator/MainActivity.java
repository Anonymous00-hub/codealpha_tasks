package com.salisou.randomquotegenerator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends Activity {

    private TextView tvQuote;
    private TextView tvAuthor;

    private final String[][] quotes = {
            {
                    "Believe you can and you are halfway there.",
                    "Theodore Roosevelt"
            },
            {
                    "The future depends on what you do today.",
                    "Mahatma Gandhi"
            },
            {
                    "It always seems impossible until it is done.",
                    "Nelson Mandela"
            },
            {
                    "The only way to do great work is to love what you do.",
                    "Steve Jobs"
            },
            {
                    "Success is not final; failure is not fatal.",
                    "Winston Churchill"
            },
            {
                    "Do something today that your future self will thank you for.",
                    "Sean Patrick Flanery"
            },
            {
                    "Dream big and dare to fail.",
                    "Norman Vaughan"
            },
            {
                    "Great things are done by a series of small things brought together.",
                    "Vincent van Gogh"
            },
            {
                    "The secret of getting ahead is getting started.",
                    "Mark Twain"
            },
            {
                    "Your limitation, it is only your imagination.",
                    "Unknown"
            }
    };

    private final Random random = new Random();

    private int lastQuoteIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvQuote = findViewById(R.id.tvQuote);
        tvAuthor = findViewById(R.id.tvAuthor);

        View btnNewQuote = findViewById(R.id.btnNewQuote);

        showRandomQuote();

        btnNewQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRandomQuote();
            }
        });
    }

    private void showRandomQuote() {

        int index;

        do {
            index = random.nextInt(quotes.length);
        } while (quotes.length > 1 && index == lastQuoteIndex);

        lastQuoteIndex = index;

        String quote = quotes[index][0];
        String author = quotes[index][1];

        tvQuote.setText(quote);
        tvAuthor.setText("— " + author);
    }
}