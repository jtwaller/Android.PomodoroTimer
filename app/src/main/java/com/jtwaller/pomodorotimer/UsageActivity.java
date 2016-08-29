package com.jtwaller.pomodorotimer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

public class UsageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage);

        SQLiteHelper mDbHelper = new SQLiteHelper(this);
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        TextView dbUsage = (TextView) findViewById(R.id.dataView);
        Cursor c = db.rawQuery("SELECT * FROM " + SQLContract.PomoTable.TABLE_NAME, null);
        StringBuilder sb = new StringBuilder();

        c.moveToFirst();
        while(!c.isAfterLast()) {
            for(int i = 0; i < c.getColumnCount(); i++) {
                if(i == 1) { // date column  TODO: refactor this nonsense
                    DateFormat df = DateFormat.getDateTimeInstance();
                    Date date = new Date(c.getLong(i));
                    sb.append(df.format(date)).append(" ");
                } else {
                    sb.append(c.getString(i)).append(" ");
                }
            }
            c.moveToNext();
            sb.append(System.lineSeparator());
        }
        c.close();
        dbUsage.setText(sb);

        Button clearButton = (Button) findViewById(R.id.dbDrop);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.delete(SQLContract.PomoTable.TABLE_NAME, null, null);
                finish();
                startActivity(getIntent());
            }
        });
    }
}
