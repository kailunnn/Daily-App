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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity4 extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TextWatcher,
                                                        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    TextView tvShowDate;
    String date;
    ImageButton imgBtnBack, imgBtnAdd, imgBtnALL, imgbtn_Diary3, imgbtn_Calendar3;
    ListView listView;
    Calendar c;

    SQLiteDatabase db;
    static final String DB_Name = "DailyDB";
    static final String TB_Name = "Note";
    static final String specificDate="dbDate";

    Cursor cursorspecific;
    String cursorshow;

    SimpleCursorAdapter adapter;
    static final String[] FROMNOTE = new String[]{"dbType", "dbTime", "dbTitle"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        getSupportActionBar().hide();

        c = Calendar.getInstance();

        listView = (ListView) findViewById(R.id.lvNoteDB);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        imgBtnAdd = (ImageButton)findViewById(R.id.imgbtn_add);
        imgBtnAdd.setOnClickListener(this);

        tvShowDate = (TextView) findViewById(R.id.tv_show_date);
        tvShowDate.setOnClickListener(this);
        tvShowDate.addTextChangedListener(this);

        imgBtnALL = (ImageButton)findViewById(R.id.imgbtn_Note3);
        imgBtnALL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity4.this,MainActivity4v0.class);
                startActivity(intent);
            }
        });

        imgBtnBack = (ImageButton)findViewById(R.id.imgbtn_back);
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity4.this, MainActivity.class);
                startActivity(intent);
            }
        });

        imgbtn_Diary3 = (ImageButton)findViewById(R.id.imgbtn_Diary3);
        imgbtn_Diary3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity4.this, MainActivity3v0.class);
                startActivity(intent);
            }
        });

        imgbtn_Calendar3 = (ImageButton)findViewById(R.id.imgbtn_Calendar3);
        imgbtn_Calendar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity4.this, MainActivity.class);
                startActivity(intent);
            }
        });

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

        //取得從"行事曆"傳過來的日期
        Bundle bundle = getIntent().getExtras();
        date = bundle.getString("date");
        date = date.replace("/","-");
        tvShowDate.setText(date);

        reequery();
    }

    public void selectDate(){
        String tem = tvShowDate.getText().toString();
        String sqldate = "'"+tem+"'";
        cursorshow = "SELECT * FROM "+ TB_Name +" WHERE "+ specificDate + "=" + sqldate + " ORDER BY " + "dbTime";
        cursorspecific=db.rawQuery(cursorshow,null);

        // create Adapter object 將資料放入lv
        adapter = new SimpleCursorAdapter(this, R.layout.layout_lvnotedb, cursorspecific, FROMNOTE, new int[]{R.id.sh_date_all, R.id.sh_time_all, R.id.sh_title_all}, 0);
        listView.setAdapter(adapter);
        //reequery();
    }

    public void reequery() {
        cursorspecific=db.rawQuery(cursorshow,null);
        adapter.changeCursor(cursorspecific);
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
        reequery();
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
        db.update(TB_Name, cv, "_id=" + cursorspecific.getInt(0), null);
        reequery();
    }

    public void delData() {
        db.delete(TB_Name, "_id=" + cursorspecific.getInt(0), null);
        reequery();
    }

    @Override
    public void onClick(View v) {
        if (v == imgBtnAdd) {
            int b = 111;
            //跳至新增頁面
            Intent intent = new Intent(MainActivity4.this, MainActivity4_1.class);
            Bundle bundle2 = new Bundle();
            bundle2.putString("dateA",tvShowDate.getText().toString()); //復原
            bundle2.putInt("requestCodeB",b);
            intent.putExtras(bundle2);
            startActivityForResult(intent, b);
        }else if (v == tvShowDate) {
            new DatePickerDialog(this, this,
                    c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 111) {
            //取得從新增頁面傳過來的資料
            String dateA = data.getStringExtra("date");
            String type = data.getStringExtra("type");
            String time = data.getStringExtra("time");
            String title = data.getStringExtra("title");
            String content = data.getStringExtra("content");
            int ordertime = data.getIntExtra("ordertime",0);
            addData(dateA, type, time, title, content, ordertime);
        } else if (resultCode == 333) {
            //取得從修改頁面傳過來的資料
            String newDate = data.getStringExtra("newDate");
            String newType = data.getStringExtra("newType");
            String newTime = data.getStringExtra("newTime");
            String newTitle = data.getStringExtra("newTitle");
            String newContent = data.getStringExtra("newContent");
            int newordertime = data.getIntExtra("newordertime", 0);
            updateData(newDate, newType, newTime, newTitle, newContent, newordertime);
            reequery();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //將資料傳至"修改"頁面
        String date = cursorspecific.getString(cursorspecific.getColumnIndex("dbDate"));
        String type = cursorspecific.getString(cursorspecific.getColumnIndex("dbType"));
        String time = cursorspecific.getString(cursorspecific.getColumnIndex("dbTime"));
        String title = cursorspecific.getString(cursorspecific.getColumnIndex("dbTitle"));
        String content = cursorspecific.getString(cursorspecific.getColumnIndex("dbContent"));
        int ordertime = cursorspecific.getInt(cursorspecific.getColumnIndex("formateTime"));

        Intent intent = new Intent(MainActivity4.this, MainActivity4_2.class);
        Bundle bundle = new Bundle();
        bundle.putString("date", date);
        bundle.putString("type", type);
        bundle.putString("time", time);
        bundle.putString("title", title);
        bundle.putString("content", content);
        bundle.putInt("ordertime", ordertime);
        intent.putExtras(bundle);
        startActivityForResult(intent, 333);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder bdr = new AlertDialog.Builder(MainActivity4.this);
        bdr.setMessage("確定要刪除嗎?");
        bdr.setTitle("刪除");
        bdr.setNegativeButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delData();
                reequery();
            }
        });

        bdr.setPositiveButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity4.this, "取消刪除", Toast.LENGTH_SHORT).show();
            }
        });
        bdr.show();

        return true;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        SimpleDateFormat strdate = new SimpleDateFormat("yyyy-MM-dd");
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String dates = strdate.format(c.getTime());
        tvShowDate.setText(dates);
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