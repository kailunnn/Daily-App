package com.example.finalproject_daily;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CalendarView calendarView;
    ImageButton imgbtn_Today, imgbtn_Diary1, imgbtn_Calendar1, imgbtn_Note1;
    Button btnGoDiary, btnGoNote;
    String strDate;

    SQLiteDatabase db;
    static final String DB_Name = "DailyDB";
    static final String TB_NameA = "diary";
    static final String selectdateDiary="dateDiary";
    static final String TB_NameB = "Note";
    static final String specificDate="dbDate";

    Cursor cursorDiary, cursorNote;
    String iconDiary, iconNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        //宣告
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        btnGoDiary = (Button) findViewById(R.id.btnGoDiary);
        btnGoNote = (Button) findViewById(R.id.btnGoNote);
        imgbtn_Today = (ImageButton) findViewById(R.id.imgbtn_Today);
        imgbtn_Calendar1 = (ImageButton) findViewById(R.id.imgbtn_Calendar1);

        imgbtn_Today.setOnClickListener(this);
        imgbtn_Calendar1.setOnClickListener(this);

        imgbtn_Diary1 = (ImageButton) findViewById(R.id.imgbtn_Diary1);
        imgbtn_Diary1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MainActivity3v0.class);
                startActivity(intent);
            }
        });

        imgbtn_Note1 = (ImageButton)findViewById(R.id.imgbtn_Note1);
        imgbtn_Note1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity4v0.class);
                startActivity(intent);
            }
        });


        db = openOrCreateDatabase(DB_Name, Context.MODE_PRIVATE, null);


        String TableA = "CREATE TABLE IF NOT EXISTS " + TB_NameA +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                selectdateDiary + " DATETIME NOT NULL," +
                "headerDiary VARCHAR(32)," +
                "contentDiary VARCHAR(64)," +
                "imageDiary BLOB)";

        db.execSQL(TableA);

        String TableB = "CREATE TABLE IF NOT EXISTS " + TB_NameB +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                specificDate + " DATETIME,"+
                "dbType Varchar(16)," +
                "dbTime Varchar(16)," +
                "formateTime NUMBER," +
                "dbTitle Varchar(16)," +
                "dbContent Varchar(64))";

        db.execSQL(TableB);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //日期資料
        iconDiary = "SELECT DISTINCT dateDiary FROM "+ TB_NameA + " ORDER BY " + selectdateDiary;
        cursorDiary=db.rawQuery(iconDiary,null);
        //日期資料
        iconNote = "SELECT DISTINCT dbDate FROM "+ TB_NameB + " ORDER BY " + specificDate;
        cursorNote=db.rawQuery(iconNote,null);

        if (cursorDiary.getCount()>0) {
            List<EventDay> eventsA = new ArrayList<>();
            cursorDiary.moveToFirst();// 移到第 1 筆資料
            do {
                Calendar calendarA = Calendar.getInstance();
                String dateInStringA = cursorDiary.getString(cursorDiary.getColumnIndex("dateDiary"));
                try {
                    Date dd = sdf.parse(dateInStringA);
                    calendarA.setTime(dd);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                eventsA.add(new EventDay(calendarA, R.drawable.diary));
            }while (cursorDiary.moveToNext()); //有下一筆就繼續迴圈
            calendarView.setEvents(eventsA);
        }

        if (cursorNote.getCount()>0) {
            List<EventDay> eventsB = new ArrayList<>();
            cursorNote.moveToFirst();// 移到第 1 筆資料
            do {
                Calendar calendarB = Calendar.getInstance();
                String dateInStringB = cursorNote.getString(cursorNote.getColumnIndex("dbDate"));
                try {
                    Date dd = sdf.parse(dateInStringB);
                    calendarB.setTime(dd);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                eventsB.add(new EventDay(calendarB, R.drawable.notes));
            }while (cursorNote.moveToNext()); //有下一筆就繼續迴圈
            calendarView.setEvents(eventsB);
        }

        if (cursorDiary.getCount()>0 && cursorNote.getCount()>0) {
            List<EventDay> events = new ArrayList<>();
            cursorDiary.moveToFirst();// 移到第 1 筆資料
            cursorNote.moveToFirst();// 移到第 1 筆資料
            do {
                    Calendar calendarA = Calendar.getInstance();
                    String dateInStringA = cursorDiary.getString(cursorDiary.getColumnIndex("dateDiary"));
                    Calendar calendarB = Calendar.getInstance();
                    String dateInStringB = cursorNote.getString(cursorNote.getColumnIndex("dbDate"));
                    Calendar calendarC = Calendar.getInstance();
                    try {
                        Date ddA = sdf.parse(dateInStringA);
                        calendarA.setTime(ddA);
                        Date ddB = sdf.parse(dateInStringB);
                        calendarB.setTime(ddB);
                        if( ddA.equals(ddB)){
                        calendarC.setTime(ddA);}
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    events.add(new EventDay(calendarA, R.drawable.diary));
                    events.add(new EventDay(calendarB, R.drawable.notes));
                    events.add(new EventDay(calendarC, R.drawable.memo));
            }while (cursorDiary.moveToNext() && cursorNote.moveToNext()); //有下一筆就繼續迴圈
            calendarView.setEvents(events);
        }


        /*
        //日期資料
        iconDiary = "SELECT DISTINCT dateDiary FROM "+ TB_NameA;
        cursorDiary=db.rawQuery(iconDiary,null);

        if (cursorDiary.getCount()>0) {
            List<EventDay> eventsA = new ArrayList<>();
            cursorDiary.moveToFirst();// 移到第 1 筆資料
            do {
                Calendar calendarA = Calendar.getInstance();
                String dateInStringA = cursorDiary.getString(cursorDiary.getColumnIndex("dateDiary"));
                try {
                    Date dd = sdf.parse(dateInStringA);
                    calendarA.setTime(dd);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                eventsA.add(new EventDay(calendarA, R.drawable.diary));
            }while (cursorDiary.moveToNext()); //有下一筆就繼續迴圈
            calendarView.setEvents(eventsA);
        }

        //日期資料
        iconNote = "SELECT DISTINCT dbDate FROM "+ TB_NameB;
        cursorNote=db.rawQuery(iconNote,null);

        // 若有資料
        if (cursorNote.getCount()>0) {
            List<EventDay> eventsB = new ArrayList<>();
            cursorNote.moveToFirst();// 移到第 1 筆資料
            do {
                Calendar calendarB = Calendar.getInstance();
                String dateInStringB = cursorNote.getString(cursorNote.getColumnIndex("dbDate"));
                try {
                    Date dd = sdf.parse(dateInStringB);
                    calendarB.setTime(dd);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                eventsB.add(new EventDay(calendarB, R.drawable.notes));
            }while (cursorNote.moveToNext()); //有下一筆就繼續迴圈
            calendarView.setEvents(eventsB);
        }
        */





        //點選日期跳出alert
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {

                Calendar selectedDay = eventDay.getCalendar();
                //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                strDate = sdf.format(selectedDay.getTime());

                btnGoDiary.setVisibility(View.VISIBLE);
                btnGoNote.setVisibility(View.VISIBLE);

                btnGoDiary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //傳日期
                        Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("date",strDate);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 245);

//                        Intent intent = new Intent(MainActivity.this,MainActivity3.class);
//                        startActivity(intent);
                    }
                });

                btnGoNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, MainActivity4.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("date",strDate);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 237);
                    }
                });

                /*
                String ct = eventDay.getCalendar().getTime().toString();
                test.setText(ct);

                Calendar calendar = eventDay.getCalendar();
                List<EventDay> events = new ArrayList<>();
                events.add(new EventDay(calendar, R.drawable.heart));
                calendarView.setEvents(events);

                 */

            }
        });

         /*
                calendarView.setOnDayClickListener(new OnDayClickListener() {
                    @Override
                    public void onDayClick(EventDay eventDay) {
                        Calendar clickedDayCalendar = eventDay.getCalendar();
                        for (Calendar calendar : calendarView.getSelectedDates()) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                            Toast.makeText(MainActivity.this, sdf.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                 */

    }


    @Override
    public void onClick(View v) {
        //跳回今天日期
        if(v == imgbtn_Today){
            btnGoDiary.setVisibility(View.GONE);
            btnGoNote.setVisibility(View.GONE);

            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat year = new SimpleDateFormat("yyyy");
            SimpleDateFormat month = new SimpleDateFormat("MM");
            SimpleDateFormat day = new SimpleDateFormat("dd");

            //calendar設為今日
            calendar.set(Integer.parseInt(year.format(date))
                    , Integer.parseInt(month.format(date)) - 1
                    , Integer.parseInt(day.format(date)));

            try {
                calendarView.setDate(calendar);
            } catch (OutOfDateRangeException e) {
                e.printStackTrace();
            }
        }else if(v == imgbtn_Calendar1){

            btnGoDiary.setVisibility(View.GONE);
            btnGoNote.setVisibility(View.GONE);
        }

    }

    /*
    //傳值
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 245) {
            String name = data.getStringExtra("name");
            Double weight = data.getDoubleExtra("weight", 50);
            textView.setText(name + weight);
        }else if(resultCode == 217) {


        } else {
            textView.setText(requestCode);
            finish();
        }
    }

     */

}