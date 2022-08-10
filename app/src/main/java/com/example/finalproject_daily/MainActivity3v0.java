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
import android.util.Base64;
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
import java.util.Date;

public class MainActivity3v0 extends AppCompatActivity implements View.OnClickListener, TextWatcher, DatePickerDialog.OnDateSetListener ,
                                                                AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    //資料庫
    static final String DB_Name = "DailyDB";
    static final String TB_Name = "diary";
    static final String selectdateDiary="dateDiary";
    static final String[] FROMDIARY = new String[] {"dateDiary", "headerDiary"};
    SQLiteDatabase dbDiary;


    Cursor curDiary;
    SimpleCursorAdapter adapterDiary;

    ListView lvDiaryDBall;
    String queryDiary;
    String img_string="";


    TextView tvDate1A;
    Button btnAddDiaryA;
    Calendar cAA;
    String dateA, dateSearchA;
    ImageButton imgbtn_Diary2A, imgbtn_Calendar2A, imgbtn_Note2A;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3v0);

        getSupportActionBar().hide();

        tvDate1A = (TextView)findViewById(R.id.tvDate1A);
        cAA = Calendar.getInstance();
        lvDiaryDBall = (ListView) findViewById(R.id.lvDiaryDBall);

        tvDate1A.setOnClickListener(this);
        tvDate1A.addTextChangedListener(this);
        lvDiaryDBall.setOnItemClickListener(this);
        lvDiaryDBall.setOnItemLongClickListener(this);

        imgbtn_Diary2A = (ImageButton) findViewById(R.id.imgbtn_Diary2A);
        imgbtn_Diary2A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        imgbtn_Note2A = (ImageButton) findViewById(R.id.imgbtn_Note2A);
        imgbtn_Note2A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity3v0.this,MainActivity4v0.class);
                startActivity(intent);
            }
        });

        //open or create database
        dbDiary = openOrCreateDatabase(DB_Name, Context.MODE_PRIVATE, null);

        String createTable = "CREATE TABLE IF NOT EXISTS " + TB_Name +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                selectdateDiary + " DATETIME NOT NULL," +
                "headerDiary VARCHAR(32)," +
                "contentDiary VARCHAR(64)," +
                "imageDiary BLOB)";

        dbDiary.execSQL(createTable); //執行資料表

        CursorReset();

        requeryDiary();



    }

    public void CursorReset(){
        dateSearchA = tvDate1A.getText().toString();
        String sqldateDiary = "'"+dateSearchA+"'";

        if(tvDate1A.getText().toString().length() > 0){
            queryDiary = "SELECT * FROM " + TB_Name +" WHERE "+ selectdateDiary + "=" + sqldateDiary ;
        }else {
            queryDiary = "SELECT * FROM " + TB_Name + " ORDER BY " + selectdateDiary + " DESC ";
        }
//        queryDiary = "SELECT * FROM " + TB_Name;
//        queryDiary = "SELECT * FROM " + TB_Name +" WHERE "+ selectdateDiary + "=" + sqldateDiary ;
        curDiary = dbDiary.rawQuery(queryDiary, null);

        // create Adapter object 將資料放入lv
        adapterDiary = new SimpleCursorAdapter(this, R.layout.layout_lvdiarydb, curDiary, FROMDIARY, new int[]{R.id.date, R.id.header}, 0);
        lvDiaryDBall.setAdapter(adapterDiary);

    }


    public void requeryDiary(){
        //改listview內容
        curDiary = dbDiary.rawQuery(queryDiary, null);
        adapterDiary.changeCursor(curDiary);
    }

    public void addDataDiary(String dateDiary, String headerDiary, String contentDiary, String img_string) {
        // insert
        ContentValues cv = new ContentValues(4);
        cv.put("dateDiary", dateDiary);
        cv.put("headerDiary", headerDiary);
        cv.put("contentDiary", contentDiary);
        if (img_string.toString() != "") {
            cv.put("imageDiary", img_string.toString());
        }
        dbDiary.insert(TB_Name, null, cv);
        requeryDiary();;
    }

    public void  delDataDiary() {
        dbDiary.delete(TB_Name, "_id=" + curDiary.getInt(0), null);
        requeryDiary();
    }

    public void UpdateDataDiary(String dateDiary, String headerDiary, String contentDiary, String img_string) {
        ContentValues cv = new ContentValues(4);
        cv.put("dateDiary", dateDiary);
        cv.put("headerDiary", headerDiary);
        cv.put("contentDiary", contentDiary);
        cv.put("imageDiary", img_string.toString());
        dbDiary.update(TB_Name, cv,"_id=" + curDiary.getInt(0), null);

        requeryDiary();
    }

    //回 首頁Main
    public void Diary2BackCalendar(View view){
        Intent intent = new Intent(MainActivity3v0.this,MainActivity.class);
        startActivity(intent);
    }

    //前往 新增日記Main
    public void AddDiaryA(View view){
        Date date = new Date();
        Calendar calendarNow = Calendar.getInstance();
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
//        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
//        SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
//        SimpleDateFormat sdfDay = new SimpleDateFormat("dd");

        dateA = sdfDate.format(calendarNow.getTime());
        int a = 248;
        Intent intent2 = new Intent(MainActivity3v0.this,MainActivity3_1.class);
        Bundle bundle2 = new Bundle();
        bundle2.putString("date2",dateA);
        bundle2.putInt("requestCodeA",a);
        intent2.putExtras(bundle2);
        startActivityForResult(intent2, a);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        SimpleDateFormat strdate = new SimpleDateFormat("yyyy-MM-dd");
        cAA.set(Calendar.YEAR,year);
        cAA.set(Calendar.MONTH,month);
        cAA.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String dates = strdate.format(cAA.getTime());
        tvDate1A.setText(dates);
    }


    @Override
    public void onClick(View view) {
        if (view == tvDate1A) {
            new DatePickerDialog(this, this,
                    cAA.get(Calendar.YEAR), cAA.get(Calendar.MONTH), cAA.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //將資料傳至"修改"頁面
        String date = curDiary.getString(curDiary.getColumnIndex("dateDiary"));
        String header = curDiary.getString(curDiary.getColumnIndex("headerDiary"));
        String content = curDiary.getString(curDiary.getColumnIndex("contentDiary"));
        byte[] imageDiary  = Base64.decode(curDiary.getString(curDiary.getColumnIndex("imageDiary")), Base64.DEFAULT);

        int c = 249;
        Intent intent = new Intent(MainActivity3v0.this, MainActivity3_2.class);
        Bundle bundle = new Bundle();
        bundle.putString("dateDiary", date);
        bundle.putString("headerDiary", header);
        bundle.putString("contentDiary", content);
        bundle.putByteArray("imageDiaryDiary", imageDiary);
        bundle.putInt("requestCodeC", c);
        intent.putExtras(bundle);
        startActivityForResult(intent, c);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 248) {
            //Bundle bundle = new Bundle();
            String dateDiary = data.getStringExtra("strdateDiary");
            String headerDiary = data.getStringExtra("strheaderDiary");
            String contentDiary = data.getStringExtra("strcontentDiary");
            byte[] bytesAddDiary = data.getByteArrayExtra("bytesAddDiary");
            String AddDiary = saveImage(bytesAddDiary);
            addDataDiary(dateDiary, headerDiary, contentDiary, AddDiary);
        }else if(resultCode == 249){
            String dateDiaryNew = data.getStringExtra("dateDiaryNew");
            String headerDiaryNew = data.getStringExtra("headerDiaryNew");
            String contentDiaryNew = data.getStringExtra("contentDiaryNew");
            byte[] imageDiaryNew = data.getByteArrayExtra("imageDiaryNew");
            String AddDiaryNew = saveImage(imageDiaryNew);
            UpdateDataDiary(dateDiaryNew, headerDiaryNew, contentDiaryNew, AddDiaryNew);
        }
    }

    private String saveImage(byte[] bytesDiary) {
        String base64 = Base64.encodeToString(bytesDiary, Base64.DEFAULT);
        return base64;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder bdr = new AlertDialog.Builder(MainActivity3v0.this);
        bdr.setMessage("確定要刪除嗎?");
        bdr.setTitle("刪除");
        bdr.setNegativeButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delDataDiary();
                requeryDiary();
            }
        });

        bdr.setPositiveButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity3v0.this, "取消刪除", Toast.LENGTH_SHORT).show();
            }
        });
        bdr.show();

        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        CursorReset();
    }

}