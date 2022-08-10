package com.example.finalproject_daily;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity4v0 extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,
                                                            View.OnClickListener, TextWatcher, DatePickerDialog.OnDateSetListener {

    SQLiteDatabase db;
    static final String DB_Name = "DailyDB";
    static final String TB_Name = "Note";
    static final String specificDate="dbDate";
    String query;
    Cursor cursor;

    ListView lvAll;
    ImageButton imageButton, imgbtn_Diary3A, imgbtn_Calendar3A, imgbtn_Note3A, imgbtn_addA;
    TextView tv_show_dateA ;
    Calendar c;

    SimpleCursorAdapter adapterAll;
    static final String[] FROMNOTE2 = new String[]{"dbDate", "dbType", "dbTime", "dbTitle"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4v0);

        getSupportActionBar().hide();

        c = Calendar.getInstance();

        tv_show_dateA = (TextView) findViewById(R.id.tv_show_dateA);
        tv_show_dateA.setOnClickListener(this);
        tv_show_dateA.addTextChangedListener(this);

        imgbtn_addA = (ImageButton)findViewById(R.id.imgbtn_addA);
        imgbtn_addA.setOnClickListener(this);


        imageButton = (ImageButton)findViewById(R.id.imageButton2);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity4v0.this,MainActivity.class);
                startActivity(intent);
            }
        });

        imgbtn_Diary3A = (ImageButton)findViewById(R.id.imgbtn_Diary3A);
        imgbtn_Diary3A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity4v0.this,MainActivity3v0.class);
                startActivity(intent);
            }
        });

        imgbtn_Calendar3A = (ImageButton)findViewById(R.id.imgbtn_Calendar3A);
        imgbtn_Calendar3A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity4v0.this,MainActivity.class);
                startActivity(intent);
            }
        });

        imgbtn_Note3A = (ImageButton) findViewById(R.id.imgbtn_Note3A);
        imgbtn_Note3A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        lvAll = (ListView)findViewById(R.id.lv_all_note);
        lvAll.setOnItemClickListener(this);
        lvAll.setOnItemLongClickListener(this);

        db = openOrCreateDatabase(DB_Name, Context.MODE_PRIVATE, null);

        String createTable = "CREATE TABLE IF NOT EXISTS " + TB_Name +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                specificDate + " DATETIME,"+
                "dbType Varchar(16)," +
                "dbTime Varchar(16)," +
                "formateTime NUMBER," +
                "dbTitle Varchar(16)," +
                "dbContent Varchar(64))";

        db.execSQL(createTable);

        selectDate();

        requery();

    }

    public void selectDate(){
        String tem = tv_show_dateA.getText().toString();
        String sqldate = "'"+tem+"'";

        if(tv_show_dateA.getText().toString().length() > 0){
            query = "SELECT * FROM "+ TB_Name +" WHERE "+ specificDate + "=" + sqldate + " ORDER BY " + "dbTime";
        }else {
            query = "SELECT * FROM " + TB_Name  + " ORDER BY " + specificDate + ", dbTime";
        }
        //資料庫所有的資料

        cursor = db.rawQuery(query, null); //查值

        adapterAll = new SimpleCursorAdapter(this, R.layout.layout_lvnotedb_all, cursor, FROMNOTE2, new int[]{R.id.sh_date_all, R.id.sh_type_all, R.id.sh_time_all, R.id.sh_title_all}, 0);
        lvAll.setAdapter(adapterAll);


//        cursorshow = "SELECT * FROM "+ TB_Name +" WHERE "+ specificDate + "=" + sqldate + " ORDER BY " + "formateTime";
//        cursorspecific=db.rawQuery(cursorshow,null);
//
//        // create Adapter object 將資料放入lv
//        adapter = new SimpleCursorAdapter(this, R.layout.layout_lvnotedb, cursorspecific, FROMNOTE, new int[]{R.id.sh_date_all, R.id.sh_time_all, R.id.sh_title_all}, 0);
//        listView.setAdapter(adapter);
//        //reequery();
    }

    public void requery() {
        cursor = db.rawQuery(query, null);
        adapterAll.changeCursor(cursor);
    }

    public void addData(String date, String type, String time, String title, String content, int ordertTime) {
        ContentValues cv = new ContentValues(6);
        cv.put("dbDate", date);
        cv.put("dbType", type);
        cv.put("dbTime", time);
        cv.put("dbTitle", title);
        cv.put("dbContent", content);
        cv.put("formateTime", ordertTime);
        db.insert(TB_Name, null, cv);
        requery();
    }

    public void delData() {
        db.delete(TB_Name, "_id=" + cursor.getInt(0), null);
        requery();
    }

    public void updateData(String newDate, String newType, String newTime, String newTitle, String newContent, int newOrderTime) {
        ContentValues cv = new ContentValues(6);
        cv.put("dbDate", newDate);
        cv.put("dbType", newType);
        cv.put("dbTime", newTime);
        cv.put("dbTitle", newTitle);
        cv.put("dbContent", newContent);
        cv.put("formateTime", newOrderTime);
        //db.update(TB_Name, cv, "_id=" + cursor.getInt(0), null);
        db.update(TB_Name, cv, "_id=" + cursor.getInt(0), null);
        requery();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String date = cursor.getString(cursor.getColumnIndex("dbDate"));
        String type = cursor.getString(cursor.getColumnIndex("dbType"));
        String time = cursor.getString(cursor.getColumnIndex("dbTime"));
        String title = cursor.getString(cursor.getColumnIndex("dbTitle"));
        String content = cursor.getString(cursor.getColumnIndex("dbContent"));
        int ordertime = cursor.getInt(cursor.getColumnIndex("formateTime"));

        Intent intent = new Intent(MainActivity4v0.this, MainActivity4v1.class);
        Bundle bundle = new Bundle();
        bundle.putString("date", date);
        bundle.putString("type", type);
        bundle.putString("time", time);
        bundle.putString("title", title);
        bundle.putString("content", content);
        bundle.putInt("ordertime", ordertime);
        intent.putExtras(bundle);
        startActivityForResult(intent, 555);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder bdr = new AlertDialog.Builder(MainActivity4v0.this);
        bdr.setMessage("確定要刪除嗎?");
        bdr.setTitle("刪除");
        bdr.setNegativeButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delData();
                requery();
            }
        });

        bdr.setPositiveButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity4v0.this, "取消刪除", Toast.LENGTH_SHORT).show();
            }
        });
        bdr.show();

        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 232) {
            //取得從新增頁面傳過來的資料
            String dateA = data.getStringExtra("date");
            String type = data.getStringExtra("type");
            String time = data.getStringExtra("time");
            String title = data.getStringExtra("title");
            String content = data.getStringExtra("content");
            int ordertime = data.getIntExtra("ordertime",0);
            addData(dateA, type, time, title, content, ordertime);
        } else if (resultCode == 555) {
            //取得從修改頁面傳過來的資料
            String newDate = data.getStringExtra("newDate");
            String newType = data.getStringExtra("newType");
            String newTime = data.getStringExtra("newTime");
            String newTitle = data.getStringExtra("newTitle");
            String newContent = data.getStringExtra("newContent");
            int newordertime = data.getIntExtra("newordertime", 0);
            updateData(newDate, newType, newTime, newTitle, newContent, newordertime);
            requery();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        SimpleDateFormat strdate = new SimpleDateFormat("yyyy-MM-dd");
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String dates = strdate.format(c.getTime());
        tv_show_dateA.setText(dates);
    }

    @Override
    public void onClick(View v) {
        if (v == tv_show_dateA) {
            new DatePickerDialog(this, this,
                    c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        }else if (v == imgbtn_addA) {
            Calendar calendarNow = Calendar.getInstance();
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            String dateA = sdfDate.format(calendarNow.getTime());

            int b = 232;
            //跳至新增頁面
            Intent intent = new Intent(MainActivity4v0.this, MainActivity4_1.class);
            Bundle bundle2 = new Bundle();
            bundle2.putString("dateA",dateA); //復原
            bundle2.putInt("requestCodeB",b);
            intent.putExtras(bundle2);
            startActivityForResult(intent, b);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        selectDate();
    }

}