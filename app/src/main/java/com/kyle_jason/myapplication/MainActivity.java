package com.kyle_jason.myapplication;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sequence = new ArrayList<>();
        red = findViewById(R.id.redImageView);
        blue = findViewById(R.id.blueImageView);
        green = findViewById(R.id.greenImageView);
        yellow = findViewById(R.id.yellowImageView);
        index = 0;
        playersTurn = false;
        soundsLoaded = new HashSet<Integer>();

        addMove();
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                showSequence();
            }
        }, 1500);

        findViewById(R.id.redImageView).setOnClickListener(this);
        findViewById(R.id.blueImageView).setOnClickListener(this);
        findViewById(R.id.greenImageView).setOnClickListener(this);
        findViewById(R.id.yellowImageView).setOnClickListener(this);

        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show game information
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
        attrBuilder.setUsage(AudioAttributes.USAGE_GAME);

        SoundPool.Builder spBuilder = new SoundPool.Builder();
        spBuilder.setAudioAttributes(attrBuilder.build());
        spBuilder.setMaxStreams(1);
        soundPool = spBuilder.build();

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) { // success
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

    private void disableBoard() {
        red.setEnabled(false);
        blue.setEnabled(false);
        green.setEnabled(false);
        yellow.setEnabled(false);
    }

    private void enableBoard() {
        red.setEnabled(true);
        blue.setEnabled(true);
        green.setEnabled(true);
        yellow.setEnabled(true);
        index = 0;
    }

    private void showSequence() {
        disableBoard();
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
            enableBoard();
        }
    }

    private void showRed() {
        red.setImageResource(R.drawable.push_red);
        playSound(redSound);
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                red.setImageResource(R.drawable.red_button);
            }
        }, 400);
    }

    private void showBlue() {
        blue.setImageResource(R.drawable.push_blue);
        playSound(blueSound);
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                blue.setImageResource(R.drawable.blue_button);
            }
        }, 400);
    }

    private void showGreen() {
        green.setImageResource(R.drawable.push_green);
        playSound(greenSound);
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                green.setImageResource(R.drawable.green_button);
            }
        }, 400);
    }

    private void showYellow() {
        yellow.setImageResource(R.drawable.push_yellow);
        playSound(yellowSound);
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                yellow.setImageResource(R.drawable.yellow_button);
            }
        }, 400);
    }

    private void addMove() {
        sequence.add((int) (Math.random() * 4) + 1);
        Log.i("MOVE", sequence.toString());
    }

    @Override
    public void onClick(View view) {
        if (playersTurn) {
            checkMatch(view);
            getSound(view);
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

    private void checkMatch(View view) {
        if (sequence.get(index) == Integer.valueOf(view.getTag().toString())) {
            Log.i("MOVE", "correct");
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
                addMove();
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showSequence();
                    }
                }, 2000);
            }
        } else {
            Log.i("MOVE", "wrong");
            Log.i("MOVE", "you lose");
            (new Handler()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    playSound(gameOver);
                }
            }, 800);
            index = 0;
            playersTurn = false;
            disableBoard();
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
    }
}
