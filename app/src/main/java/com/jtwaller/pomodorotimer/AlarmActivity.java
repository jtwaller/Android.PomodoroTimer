package com.jtwaller.pomodorotimer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Button b = (Button) findViewById(R.id.alarmOffButton);
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AlarmService.class);
                stopService(intent);

                Intent goToBreak = new Intent(getApplicationContext(), BreakActivity.class);
                startActivity(goToBreak);
            }

        });

    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();

        stopService(new Intent(getApplicationContext(), AlarmService.class));
        startActivity(new Intent(getApplicationContext(), BreakActivity.class));
    }

}
