package com.jtwaller.pomodorotimer;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

public class dbTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_test);

        SQLiteHelper mDbHelper = new SQLiteHelper(this);
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Button b = (Button) findViewById(R.id.dbadd);
        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(SQLContract.PomoTable.COLUMN_NAME_DATE_CREATED, System.currentTimeMillis());
                values.put(SQLContract.PomoTable.COLUMN_NAME_COMPLETED, true);

                db.insert(SQLContract.PomoTable.TABLE_NAME, null, values);
            }
        });

        Button b2 = (Button) findViewById(R.id.dbdrop);
        b2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                db.delete(SQLContract.PomoTable.TABLE_NAME, null, null);
            }
        });

        Button b3 = (Button) findViewById(R.id.dbview);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tv = (TextView) findViewById(R.id.dataView);
                Cursor c = db.rawQuery("SELECT * FROM " + SQLContract.PomoTable.TABLE_NAME, null);
                c.moveToFirst();

                StringBuilder sb = new StringBuilder();

                while (!c.isAfterLast()) {
                    for(int i = 0; i < c.getColumnCount(); i++) {
                        sb.append(c.getString(i)).append(" ");
                        if(i==1) {
                            DateFormat df = DateFormat.getDateTimeInstance();
                            Date date = new Date(c.getLong(i));
                            Log.d("dbTest", df.format(date));
                        }
                    }
                    sb.append(System.lineSeparator());
                    c.moveToNext();
                }
                c.close();
                tv.setText(sb);
            }
        });
    }
}
