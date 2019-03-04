package com.kyle_jason.myapplication;

import android.content.DialogInterface;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashSet;

public class BasicSimon extends Simon implements View.OnClickListener {

    private View[] views;

    private ArrayList<Integer> sequence;
    private ImageView red;
    private ImageView blue;
    private ImageView green;
    private ImageView yellow;
    private int index;
    private boolean playersTurn;
    private SoundPool soundPool;
    private HashSet<Integer> soundsLoaded;
    private int redSound;
    private int blueSound;
    private int greenSound;
    private int yellowSound;
    private int gameOver;
    private int success;
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simon);

        sequence = new ArrayList<>();
        red = findViewById(R.id.redImageView);
        blue = findViewById(R.id.blueImageView);
        green = findViewById(R.id.greenImageView);
        yellow = findViewById(R.id.yellowImageView);
        index = 0;
        playersTurn = false;
        soundsLoaded = new HashSet<>();

        views = new View[]{red, blue, green, yellow};
        for (int i = 0; i < views.length; i++) {
            views[i].setOnClickListener(this);
        }

        disableBoard(views);
        addMove(sequence);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showSequence();
            }
        }, 1500);
    }

    @Override
    protected void onResume() {
        super.onResume();

        AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
        attrBuilder.setUsage(AudioAttributes.USAGE_GAME);

        SoundPool.Builder spBuilder = new SoundPool.Builder();
        spBuilder.setAudioAttributes(attrBuilder.build());
        spBuilder.setMaxStreams(2);
        soundPool = spBuilder.build();

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) {
                    soundsLoaded.add(sampleId);
                    Log.i("SOUND", "Sound loaded " + sampleId);
                } else {
                    Log.i("SOUND", "Error cannot load sound status = " + status);
                }
            }
        });

        redSound = soundPool.load(this, R.raw.red, 1);
        blueSound = soundPool.load(this, R.raw.blue, 1);
        greenSound = soundPool.load(this, R.raw.green, 1);
        yellowSound = soundPool.load(this, R.raw.yellow, 1);
        gameOver = soundPool.load(this, R.raw.gameoverer, 1);
        success = soundPool.load(this, R.raw.success, 1);
    }

    private void playSound(int soundId) {
        if (soundsLoaded.contains(soundId)) {
            soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }

    private void showSequence() {
        if (sequence.get(index) == 1) {
            showRed();
        } else if (sequence.get(index) == 2) {
            showBlue();
        } else if (sequence.get(index) == 3) {
            showGreen();
        } else if (sequence.get(index) == 4) {
            showYellow();
        }
        index++;
        if (index < sequence.size()) {
            (new Handler()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    showSequence();
                }
            }, 1000);
        } else {
            playersTurn = true;
            enableBoard(views);
            index = 0;
        }
    }

    private void showRed() {
        red.setImageResource(R.drawable.push_red);
        playSound(redSound);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                red.setImageResource(R.drawable.red_button);
            }
        }, 400);
    }

    private void showBlue() {
        blue.setImageResource(R.drawable.push_blue);
        playSound(blueSound);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                blue.setImageResource(R.drawable.blue_button);
            }
        }, 400);
    }

    private void showGreen() {
        green.setImageResource(R.drawable.push_green);
        playSound(greenSound);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                green.setImageResource(R.drawable.green_button);
            }
        }, 400);
    }

    private void showYellow() {
        yellow.setImageResource(R.drawable.push_yellow);
        playSound(yellowSound);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                yellow.setImageResource(R.drawable.yellow_button);
            }
        }, 400);
    }

    @Override
    public void onClick(View view) {
        if (playersTurn) {
            if (checkMatch(view, sequence, index)) {
                continueGame();
            } else {
                endGame();
            }
            getSound(view);
        }
    }

    private void endGame() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gameOverDialog();
            }
        }, 200);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playSound(gameOver);
            }
        }, 800);
        index = 0;
        playersTurn = false;
        disableBoard(views);
    }

    private void gameOverDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        BasicSimon.super.onBackPressed();
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("You're such a loser!");
        alertDialog.show();
    }

    private void continueGame() {
        index++;
        if (sequence.size() == index) {
            Log.i("MOVE", "you beat the round");
            (new Handler()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    playSound(success);
                }
            }, 500);
            index = 0;
            playersTurn = false;
            disableBoard(views);
            addMove(sequence);
            (new Handler()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    showSequence();
                }
            }, 2000);
        }
    }

    private void getSound(View view) {
        if (view.getId() == R.id.redImageView) {
            playSound(redSound);
        } else if (view.getId() == R.id.blueImageView) {
            playSound(blueSound);
        } else if (view.getId() == R.id.greenImageView) {
            playSound(greenSound);
        } else if (view.getId() == R.id.yellowImageView) {
            playSound(yellowSound);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;

            soundsLoaded.clear();
        }
        handler.removeCallbacksAndMessages(null);
    }
}
