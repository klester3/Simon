package com.kyle_jason.myapplication;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Integer> sequence;
    private ImageView red;
    private ImageView blue;
    private ImageView green;
    private ImageView yellow;
    private int index;

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

        addMove();
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                showSequence();
            }
        }, 1500);

        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMove();
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showSequence();
                    }
                }, 1000);
            }
        });
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
            enableBoard();
        }
    }

    private void showRed() {
        red.setImageResource(R.drawable.push_red);
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                red.setImageResource(R.drawable.red_button);
            }
        }, 400);
    }

    private void showBlue() {
        blue.setImageResource(R.drawable.push_blue);
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                blue.setImageResource(R.drawable.blue_button);
            }
        }, 400);
    }

    private void showGreen() {
        green.setImageResource(R.drawable.push_green);
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                green.setImageResource(R.drawable.green_button);
            }
        }, 400);
    }

    private void showYellow() {
        yellow.setImageResource(R.drawable.push_yellow);
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
}
