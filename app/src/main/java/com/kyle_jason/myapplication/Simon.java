package com.kyle_jason.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.SoundPool;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;

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
        } else if (key.equals("extreme")) {
            editor.putInt("catHighScore", catHighScore);
            editor.putInt("basicHighScore", basicHighScore);
            editor.putInt("extremeHighScore", score);
        }
        editor.apply();
    }

    //displays dialog box that informs about the game
    public void pressedAbout(int highScore, int id) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(id, null);
        AlertDialog.Builder quitAlert = new AlertDialog.Builder(this);
        quitAlert.setView(alertLayout);
        quitAlert.setCancelable(true);
        final AlertDialog quitDialog = quitAlert.create();
        quitDialog.show();
        TextView scoreTextView = quitDialog.findViewById(R.id.scoreTextView);
        scoreTextView.setText(Html.fromHtml("<font color='#000'><b>High Score: </b>"
                + Integer.valueOf(highScore) + "</font>"));
    }

    public void showRed(final ImageView red, int redSound, Handler handler, HashSet soundsLoaded, SoundPool soundPool, int idPush, final int idButton) {
        //animates red during showsequence
        red.setImageResource(idPush);
        playSound(redSound, soundsLoaded,soundPool);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                red.setImageResource(idButton);
            }
        }, 400);
    }

    public void showBlue(final ImageView blue, int blueSound, Handler handler, HashSet soundsLoaded, SoundPool soundPool, int idPush, final int idButton) {
        //animates blue during showsequence
        blue.setImageResource(idPush);
        playSound(blueSound,soundsLoaded,soundPool);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                blue.setImageResource(idButton);
            }
        }, 400);
    }

    public void showGreen(final ImageView green, int greenSound, Handler handler, HashSet soundsLoaded, SoundPool soundPool, int idPush, final int idButton) {
        //animates green during showsequence
        green.setImageResource(idPush);
        playSound(greenSound,soundsLoaded,soundPool);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                green.setImageResource(idButton);
            }
        }, 400);
    }

    public void showYellow(final ImageView yellow, int yellowSound, Handler handler, HashSet soundsLoaded, SoundPool soundPool, int idPush, final int idButton) {
        //animates yellow during showsequence
        yellow.setImageResource(idPush);
        playSound(yellowSound,soundsLoaded,soundPool);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                yellow.setImageResource(idButton);
            }
        }, 400);
    }

    public void playSound(int soundId, HashSet soundsLoaded,SoundPool soundPool) {
        //play required sound
        if (soundsLoaded.contains(soundId)) {
            soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }

    public void simonOnPause(HashSet soundsLoaded,SoundPool soundPool, Handler handler, boolean paused){
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;

            soundsLoaded.clear();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        paused = true;
    }

}
