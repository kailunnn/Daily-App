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

public class MainActivity4v1 extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, View.OnClickListener,
                                                            DatePickerDialog.OnDateSetListener{

    Button btnEdit;
    EditText edtTitle, edtContent;
    Spinner spType;
    TextView tvTime, tvShowDate2;
    Calendar c;
    ImageButton imgBtnBack;

    //由lv傳過來的字串
    String rcDate, rcType, rcTime, rcTitle, rcContent;
    int rcordertime, formatetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4v1);

        getSupportActionBar().hide();

        c = Calendar.getInstance();

        btnEdit = (Button)findViewById(R.id.btnUpdateNote_all);
        btnEdit.setOnClickListener(this);

        imgBtnBack = (ImageButton)findViewById(R.id.imgbtn_BackNote3);
        imgBtnBack.setOnClickListener(this);

        edtTitle = (EditText)findViewById(R.id.edt_edit_title_all);
        edtContent = (EditText)findViewById(R.id.edt_edit_content_all);
        spType = (Spinner)findViewById(R.id.sp_edit_type_all);

        tvTime = (TextView)findViewById(R.id.tv_edit_time_all);
        tvTime.setOnClickListener(this);

        tvShowDate2 = (TextView) findViewById(R.id.tv_show_date_2);
        tvShowDate2.setOnClickListener(this);

        //取得從lv傳過來的值
        Bundle bundle = getIntent().getExtras();
        rcDate = bundle.getString("date");
        rcType = bundle.getString("type");
        rcTime = bundle.getString("time");
        rcTitle = bundle.getString("title");
        rcContent = bundle.getString("content");
        rcordertime = bundle.getInt("ordertime",0);

        //再將自料放進對應的元件裡
        //spinner類別
        if (rcType.equals("工作")) {
            spType.setSelection(0);
        } else if (rcType.equals("學校")) {
            spType.setSelection(1);
        }else if (rcType.equals("生活")) {
            spType.setSelection(2);
        }else if (rcType.equals("重要")) {
            spType.setSelection(3);
        }else if (rcType.equals("社團")) {
            spType.setSelection(4);
        }

        tvShowDate2.setText(rcDate);
        //時間
        tvTime.setText(rcTime);
        //標題
        edtTitle.setText(rcTitle);
        //內容
        edtContent.setText(rcContent);

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
        formatetime = Integer.parseInt(hour+min);
    }

    @Override
    public void onClick(View v) {
        if (v == tvTime) {
            new TimePickerDialog(this, this,
                    c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
        }else if(v==btnEdit){
            sendMessage();
        }else if(v==imgBtnBack){
            sendMessage2();
        }else if (v == tvShowDate2) {
            new DatePickerDialog(this, this,
                    c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

    private void sendMessage() {
        //將修改後的資料傳回main2主畫面之lv
        Intent intent = getIntent();
        Bundle bundle2 = new Bundle();
        bundle2.putString("newDate",tvShowDate2.getText().toString());
        bundle2.putString("newType",spType.getSelectedItem().toString());
        bundle2.putString("newTime",tvTime.getText().toString());
        bundle2.putString("newTitle",edtTitle.getText().toString());
        bundle2.putString("newContent",edtContent.getText().toString());
        bundle2.putInt("newordertime",formatetime);
        intent.putExtras(bundle2);
        setResult(555,intent);
        MainActivity4v1.this.finish();
    }

    private void sendMessage2() {
        //將修改後的資料傳回main2主畫面之lv
        Intent intent = getIntent();
        Bundle bundle2 = new Bundle();
        bundle2.putString("newDate", rcDate);
        bundle2.putString("newType", rcType);
        bundle2.putString("newTime", rcTime);
        bundle2.putString("newTitle", rcTitle);
        bundle2.putString("newContent", rcContent);
        bundle2.putInt("newordertime", rcordertime);
        intent.putExtras(bundle2);
        setResult(555, intent);
        MainActivity4v1.this.finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        SimpleDateFormat strdate = new SimpleDateFormat("yyyy-MM-dd");
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String dates = strdate.format(c.getTime());
        tvShowDate2.setText(dates);
    }
}