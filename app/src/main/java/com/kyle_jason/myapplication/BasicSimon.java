package com.kyle_jason.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    private boolean paused;
    private boolean lockPlayButton = true;
    private int highScore;

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
        lockPlayButton = true;
        highScore = getHighScore("basic");

        //set onclick listener for image views
        views = new View[]{red, blue, green, yellow};
        for (int i = 0; i < views.length; i++) {
            views[i].setOnClickListener(this);
        }

        disableBoard(views);

        //calls pressedAbout when pressed
        findViewById(R.id.imageButton_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pressedAbout(highScore,R.layout.basic_about_dialog);
            }
        });

        //play button
        findViewById(R.id.imageButton_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pressedPlay();
            }
        });
    }

    private void pressedPlay() {
        if(lockPlayButton) {
            disableBoard(views);
            addMove(sequence);

            //begin game
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showSequence();
                }
            }, 1500);

            lockPlayButton = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (paused) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showSequence();
                }
            }, 1000);
            paused = !paused;
        }

        //load all sounds
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

    private void showSequence() {
        //display sequence to player
        if (sequence.get(index) == 1) {
            showRed(red,redSound,handler,soundsLoaded,soundPool,R.drawable.push_red,R.drawable.red_button);
        } else if (sequence.get(index) == 2) {
            showBlue(blue,blueSound,handler,soundsLoaded,soundPool,R.drawable.push_blue,R.drawable.blue_button);
        } else if (sequence.get(index) == 3) {
            showGreen(green,greenSound,handler,soundsLoaded,soundPool,R.drawable.push_green,R.drawable.green_button);
        } else if (sequence.get(index) == 4) {
            showYellow(yellow,yellowSound,handler,soundsLoaded,soundPool,R.drawable.push_yellow,R.drawable.yellow_button);
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
        //ends game because player is a loser
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gameOverDialog();
            }
        }, 200);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playSound(gameOver,soundsLoaded,soundPool);
            }
        }, 800);
        index = 0;
        playersTurn = false;
        disableBoard(views);
    }

    private void gameOverDialog() {
        //displays game over dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Quit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        BasicSimon.super.onBackPressed();
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("Lose Again",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Intent intent = new Intent(getApplicationContext(), BasicSimon.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Game Over");
        alertDialog.setMessage("You're such a loser!");
        alertDialog.show();
    }

    private void continueGame() {
        //shows next step in sequence
        index++;
        if (sequence.size() == index) {
            Log.i("MOVE", "you beat the round");
            if (sequence.size() > highScore) {
                saveHighScores("basic", sequence.size());
            }
            (new Handler()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    playSound(success,soundsLoaded,soundPool);
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
        //associates correct sound with item clicked
        if (view.getId() == R.id.redImageView) {
            playSound(redSound,soundsLoaded,soundPool);
        } else if (view.getId() == R.id.blueImageView) {
            playSound(blueSound,soundsLoaded,soundPool);
        } else if (view.getId() == R.id.greenImageView) {
            playSound(greenSound,soundsLoaded,soundPool);
        } else if (view.getId() == R.id.yellowImageView) {
            playSound(yellowSound,soundsLoaded,soundPool);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        simonOnPause(soundsLoaded,soundPool,handler,paused);
    }

    @Override
    public void onBackPressed() {
        //displays quit dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("Quit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        BasicSimon.super.onBackPressed();
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Do you want to quit?");
        alertDialog.show();
    }
}
