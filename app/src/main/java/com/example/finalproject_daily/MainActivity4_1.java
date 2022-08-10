package com.example.finalproject_daily;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity4_1 extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener,
                                                            DatePickerDialog.OnDateSetListener{

    Spinner spType;
    EditText edtTitle, edtContent;
    Button btnSave;
    TextView tvTime, tvShowDate4;
    Calendar c;
    ImageButton imageButton;
    //輸入的資料存成字串的方式
    String strDate, strTime, strType, strTitle, strContent, strResult;
    int formateTime, b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4_1);

        getSupportActionBar().hide();

        c = Calendar.getInstance();
        spType = (Spinner)findViewById(R.id.sp_edit_type);
        edtTitle = (EditText)findViewById(R.id.edt_edit_title);
        edtContent = (EditText)findViewById(R.id.edt_edit_content);

        btnSave = (Button)findViewById(R.id.btnInsertNote);
        btnSave.setOnClickListener(this);

        tvTime = (TextView)findViewById(R.id.tv_edit_time);
        tvTime.setOnClickListener(this);

        tvShowDate4 = (TextView) findViewById(R.id.tv_show_date_4);
        tvShowDate4.setOnClickListener(this);

        imageButton = (ImageButton)findViewById(R.id.imgbtn_BackNote1);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity4_1.this,MainActivity4.class);
                startActivity(intent);
            }
        });

        Bundle bundle = getIntent().getExtras();
        strDate = bundle.getString("dateA");
        b = bundle.getInt("requestCodeB");
        tvShowDate4.setText(strDate);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        SimpleDateFormat strdate = new SimpleDateFormat("yyyy-MM-dd");
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String dates = strdate.format(c.getTime());
        tvShowDate4.setText(dates);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        SimpleDateFormat strtime = new SimpleDateFormat("HH:mm");
        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
        c.set(Calendar.MINUTE,minute);
        String time = strtime.format(c.getTime());
        tvTime.setText(time);
        String hour = Integer.toString(hourOfDay);
        String min = Integer.toString(minute);
        formateTime = Integer.parseInt(hour+min);

//        tvTime.setText(hourOfDay+":"+minute);
//        String hour = Integer.toString(hourOfDay);
//        String min = Integer.toString(minute);
//        formateTime = Integer.parseInt(hour+min);
    }

    @Override
    public void onClick(View v) {
        if (v == tvTime) {
            new TimePickerDialog(this, this,
                    c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
        }else if(v==btnSave){
            if(b == 111) {
                showText1();
            }else {
                showText2();
            }
        }else if (v == tvShowDate4) {
            new DatePickerDialog(this, this,
                    c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

    public void showText1(){  //傳資料
        strType = spType.getSelectedItem().toString();
        strTime = tvTime.getText().toString();
        strTitle = edtTitle.getText().toString();
        strContent = edtContent.getText().toString();
        strResult = strType + "," + strTime + "," + strTitle + "," + strContent ;

        Intent intent = new Intent(this,MainActivity2.class);
        Bundle bundle = new Bundle();
        bundle.putString("date",tvShowDate4.getText().toString());
        bundle.putString("type",strType);
        bundle.putString("time",strTime);
        bundle.putString("title",strTitle);
        bundle.putString("content",strContent);
        bundle.putInt("ordertime",formateTime);
        intent.putExtras(bundle);
        setResult(111,intent);
        MainActivity4_1.this.finish(); //回傳後要結束,不然程式會出錯
    }

    public void showText2(){  //傳資料
        strType = spType.getSelectedItem().toString();
        strTime = tvTime.getText().toString();
        strTitle = edtTitle.getText().toString();
        strContent = edtContent.getText().toString();
        strResult = strType + "," + strTime + "," + strTitle + "," + strContent ;

        Intent intent = new Intent(this,MainActivity2.class);
        Bundle bundle = new Bundle();
        bundle.putString("date",tvShowDate4.getText().toString());
        bundle.putString("type",strType);
        bundle.putString("time",strTime);
        bundle.putString("title",strTitle);
        bundle.putString("content",strContent);
        bundle.putInt("ordertime",formateTime);
        intent.putExtras(bundle);
        setResult(232,intent);
        MainActivity4_1.this.finish(); //回傳後要結束,不然程式會出錯
    }

}