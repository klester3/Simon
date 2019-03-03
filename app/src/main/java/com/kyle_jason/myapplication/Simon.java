package com.kyle_jason.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class Simon extends AppCompatActivity implements View.OnClickListener {

    public static void disableBoard(ImageView[] imageViews) {
        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i].setEnabled(false);
            Log.i("MOVE", "disabled" + imageViews[i].toString());
        }
    }

    public static void enableBoard(ImageView[] imageViews) {
        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i].setEnabled(true);
            Log.i("MOVE", "enabled" + imageViews[i].toString());
        }
    }

    @Override
    public void onClick(View view) {

    }
}
