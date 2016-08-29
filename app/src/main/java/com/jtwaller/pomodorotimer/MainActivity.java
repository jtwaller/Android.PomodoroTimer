package com.jtwaller.pomodorotimer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void gotoCountdown(View view) {
        Intent intent = new Intent(this, CountdownActivity.class);
        startActivity(intent);
    }

    public void gotoUsage(View view) {
        Intent intent = new Intent(this, UsageActivity.class);
        startActivity(intent);
    }

    public void dbTest(View view) {
        Intent intent = new Intent(this, dbTest.class);
        startActivity(intent);
    }

    // TODO: Set onBack button to go to homepage when it makes sense.

}