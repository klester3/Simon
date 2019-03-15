package com.kyle_jason.myapplication;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.imageButton_basic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BasicSimon.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.imageButton_extreme).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ExtremeSimon.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.imageButton_cat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CatSimon.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.imageButton_credit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pressedCredit();
            }
        });
    }

    private void pressedCredit() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.credit_dialog, null);
        AlertDialog.Builder quitAlert = new AlertDialog.Builder(this);
        quitAlert.setView(alertLayout);
        quitAlert.setCancelable(true);
        final AlertDialog quitDialog = quitAlert.create();
        quitDialog.show();
    }
}