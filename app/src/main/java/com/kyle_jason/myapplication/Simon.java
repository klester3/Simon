package com.kyle_jason.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class Simon extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    int catHighScore;
    int basicHighScore;
    int extremeHighScore;

    public static void disableBoard(View[] views) {
        //disable entire game board
        for (int i = 0; i < views.length; i++) {
            views[i].setEnabled(false);
        }
        Log.i("MOVE", "disabled");
    }

    public static void enableBoard(View[] views) {
        //enable entire game board
        for (int i = 0; i < views.length; i++) {
            views[i].setEnabled(true);
        }
        Log.i("MOVE", "enabled");
    }

    public static void addMove(ArrayList<Integer> sequence) {
        //increase sequence
        sequence.add((int) (Math.random() * 4) + 1);
        Log.i("MOVE", sequence.toString());
    }

    public static boolean checkMatch(View view, ArrayList<Integer> sequence, int index) {
        //checks if player made correct move
        if (sequence.get(index) == Integer.valueOf(view.getTag().toString())) {
            Log.i("MOVE", "correct");
            return true;
        } else {
            Log.i("MOVE", "wrong");
            Log.i("MOVE", "you lose");
            return false;
        }
    }

    public int getHighScore(String key) {
        sharedPreferences = getSharedPreferences("simonHighScores", Context.MODE_PRIVATE);
        catHighScore = sharedPreferences.getInt("catHighScore", 0);
        basicHighScore = sharedPreferences.getInt("basicHighScore", 0);
        extremeHighScore = sharedPreferences.getInt("extremeHighScore", 0);
        if (key.equals("cat")) {
            return catHighScore;
        } else if (key.equals("basic")) {
            return basicHighScore;
        } else if (key.equals("extreme")) {
            return extremeHighScore;
        } else {
            return 0;
        }
    }

    public void saveHighScores(String key, int score) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (key.equals("cat")) {
            editor.putInt("catHighScore", score);
            editor.putInt("basicHighScore", basicHighScore);
            editor.putInt("extremeHighScore", extremeHighScore);
        } else if (key.equals("basic")) {
            editor.putInt("catHighScore", catHighScore);
            editor.putInt("basicHighScore", score);
            editor.putInt("extremeHighScore", extremeHighScore);
        } else if (key.equals("basic")) {
            editor.putInt("catHighScore", catHighScore);
            editor.putInt("basicHighScore", basicHighScore);
            editor.putInt("extremeHighScore", score);
        }
        editor.apply();
    }

}
