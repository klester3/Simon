package com.kyle_jason.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class Simon extends AppCompatActivity {

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

}
