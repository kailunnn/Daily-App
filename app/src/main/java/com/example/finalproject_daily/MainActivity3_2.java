package com.example.finalproject_daily;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity3_2 extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener{

    //宣告
    TextView tvDateNew;
    Calendar ca2;
    EditText edtHeaderNew, edtContentNew;
    Button btnTakePhotoNew, btnPickPhotoNew, btnUpdateDiary;
    ImageButton imgbtn_BackDiary2;

    Uri imgUriNew;
    ImageView imvNew;
    Bitmap bmpNew;
    byte[] bytesNew;
    String Xdate, Xheader, Xcontent;
    int c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3_2);

        getSupportActionBar().hide();

        tvDateNew = (TextView) findViewById(R.id.tvDateNew);
        ca2 = Calendar.getInstance();

        edtHeaderNew = (EditText)findViewById(R.id.edtHeaderNew);
        edtContentNew = (EditText)findViewById(R.id.edtContentNew);

        btnTakePhotoNew = (Button) findViewById(R.id.btnTakePhotoNew);
        btnPickPhotoNew = (Button) findViewById(R.id.btnPickPhotoNew);
        imvNew = (ImageView) findViewById(R.id.imageDiaryNew);

        btnUpdateDiary = (Button) findViewById(R.id.btnUpdateDiary);
        imgbtn_BackDiary2 = (ImageButton) findViewById(R.id.imgbtn_BackDiary2);

        tvDateNew.setOnClickListener(this);
        btnUpdateDiary.setOnClickListener(this);
        imgbtn_BackDiary2.setOnClickListener(this);

        //接受main3 所有值
        Bundle bundle = getIntent().getExtras();
        Xdate = bundle.getString("dateDiary");
        Xheader = bundle.getString("headerDiary");
        Xcontent = bundle.getString("contentDiary");
        bytesNew =bundle.getByteArray("imageDiaryDiary");
        c = bundle.getInt("requestCodeC");

        tvDateNew.setText(Xdate);
        edtHeaderNew.setText(Xheader);
        edtContentNew.setText(Xcontent);

        bmpNew = BitmapFactory.decodeByteArray(bytesNew, 0, bytesNew.length);
        imvNew.setImageBitmap(bmpNew);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        SimpleDateFormat strdate = new SimpleDateFormat("yyyy-MM-dd");
        ca2.set(Calendar.YEAR,year);
        ca2.set(Calendar.MONTH,month);
        ca2.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String dates = strdate.format(ca2.getTime());
        tvDateNew.setText(dates);
    }

    @Override
    public void onClick(View view) {
        if (view == tvDateNew) {
            new DatePickerDialog(this, this,
                    ca2.get(Calendar.YEAR), ca2.get(Calendar.MONTH), ca2.get(Calendar.DAY_OF_MONTH)).show();
        }else if (view == btnUpdateDiary) {
            if(c == 247) {
                SendtUpdate1();
            }else {
                SendtUpdate2();
            }
        }else if(view == imgbtn_BackDiary2){
            // 修改頁面的返回
            AlertDialog.Builder bdr = new AlertDialog.Builder(MainActivity3_2.this);
            bdr.setMessage("確定要返回嗎?");
            bdr.setTitle("返回");
            bdr.setNegativeButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SendtSame();
                }
            });

            bdr.setPositiveButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(MainActivity3_2.this, "繼續新增日記", Toast.LENGTH_SHORT).show();
                }
            });
            bdr.show();

        }
    }

    public void SendtUpdate1(){  //傳資料
        Intent intent = new Intent(this, MainActivity3.class);
        Bundle bundle2 = new Bundle();
        bundle2.putString("dateDiaryNew", tvDateNew.getText().toString());
        bundle2.putString("headerDiaryNew", edtHeaderNew.getText().toString());
        bundle2.putString("contentDiaryNew", edtContentNew.getText().toString());
        bundle2.putByteArray("imageDiaryNew", bytesNew);
        intent.putExtras(bundle2);
        setResult(247,intent);
        MainActivity3_2.this.finish(); //回傳後要結束,不然程式會出錯
    }

    public void SendtUpdate2(){  //傳資料
        Intent intent = new Intent(this, MainActivity3.class);
        Bundle bundle2 = new Bundle();
        bundle2.putString("dateDiaryNew", tvDateNew.getText().toString());
        bundle2.putString("headerDiaryNew", edtHeaderNew.getText().toString());
        bundle2.putString("contentDiaryNew", edtContentNew.getText().toString());
        bundle2.putByteArray("imageDiaryNew", bytesNew);
        intent.putExtras(bundle2);
        setResult(249,intent);
        MainActivity3_2.this.finish(); //回傳後要結束,不然程式會出錯
    }

    public void SendtSame(){  //按返回健
        Intent intent = new Intent(MainActivity3_2.this, MainActivity3.class);
        Bundle bundle2 = new Bundle();
        bundle2.putString("dateDiaryNew", Xdate);
        bundle2.putString("headerDiaryNew", Xheader);
        bundle2.putString("contentDiaryNew", Xcontent);
        bundle2.putByteArray("imageDiaryNew", bytesNew);
        intent.putExtras(bundle2);
        setResult(247,intent);
        setResult(249,intent);
        MainActivity3_2.this.finish(); //回傳後要結束,不然程式會出錯
    }

    public void onGet(View v) throws IOException {
        //按下照相機後會放到imgUri路徑裡
        imgUriNew = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        //Intent it = new Intent("android.media.action.IMAGE_CAPTURE");
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        it.putExtra(MediaStore.EXTRA_OUTPUT, imgUriNew);
        startActivityForResult(it, 100);

    }

    public void onPick(View v){
        Intent it = new Intent(Intent.ACTION_GET_CONTENT);
        it.setType("image/*");
        startActivityForResult(it, 101);
    }

    void showImg() {
        int iw, ih, vw, vh; //vw,vh-imgview比例(手機大小) iw,ih-原圖大小
        boolean needRotate;//是否要旋轉

        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;//讀圖檔而已


        try {//讀取圖檔資訊存到option裡面
            BitmapFactory.decodeStream(getContentResolver().openInputStream(imgUriNew), null, option);
        } catch (IOException e) {
            Toast.makeText(this, "讀取照片資訊時發生錯誤", Toast.LENGTH_LONG).show();
            return;
        }

        iw = option.outWidth;
        ih = option.outHeight;
        vw = imvNew.getWidth();
        vh = imvNew.getHeight();

        int scaleFactor;
        if (iw < ih) {
            needRotate = false;
            scaleFactor = Math.min(iw / vw, ih / vh);//按照比率去做調整
        } else {
            needRotate = true;//太寬的話可以旋轉
            scaleFactor = Math.min(iw / vh, ih / vw);//按照比率去做調整
        }

        option.inJustDecodeBounds = false;
        option.inSampleSize = scaleFactor;//縮小比率 假如原本是2就會縮小成原本ㄉ1/2


        bmpNew = null;
        try {
            //讀取調整size後的圖檔
            bmpNew = BitmapFactory.decodeStream(getContentResolver().openInputStream(imgUriNew), null, option);
        } catch (IOException e) {
            Toast.makeText(this, "無法取得照片", Toast.LENGTH_LONG).show();
        }

        if (needRotate) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bmpNew = Bitmap.createBitmap(bmpNew,
                    0, 0, bmpNew.getWidth(), bmpNew.getHeight(), matrix, true);
        }

        imvNew.setImageBitmap(bmpNew);

        bytesNew = saveByteImg(bmpNew);

    }

    private byte[] saveByteImg(Bitmap bmpNew) {
        //saveByteImg
        //將 bitmap 轉成 byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmpNew.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        bytesNew = stream.toByteArray();
        return bytesNew;
    }


    protected void onActivityResult (int requestCode, int resultCode, Intent data){ //預設有 所以要override
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK){      //拍照成功
            if (requestCode == 100){
                Intent it = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imgUriNew); //放入共享檔內
                sendBroadcast(it);
            }else if (requestCode == 101){
                imgUriNew = data.getData();
            }
            showImg();//拍照成功才會顯示

        }else {
            Toast.makeText(this, requestCode==100? "沒有拍到照片" : "沒有選取相片", Toast.LENGTH_LONG).show();
        }

    }




}