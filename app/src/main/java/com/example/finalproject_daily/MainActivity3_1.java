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
import android.util.Base64;
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

public class MainActivity3_1 extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener{

    //宣告
    TextView tvDate2;
    Calendar ca;
    EditText edtHeader, edtContent;
    Button btnTakePhoto, btnPickPhoto, btnInsertDiary;
    ImageButton imgbtn_BackDiary1;
    int a;

    Uri imgUri;
    ImageView imv;
    Bitmap bmp;
    String img_string="";

    //傳資料
    String strdateDiary, strheaderDiary, strcontentDiary;
    byte[] bytes, bytesAddDiary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3_1);

        getSupportActionBar().hide();

        tvDate2 = (TextView)findViewById(R.id.tvDate2);
        ca = Calendar.getInstance();

        edtHeader = (EditText)findViewById(R.id.edtHeader2);
        edtContent = (EditText)findViewById(R.id.edtContent2);

        btnTakePhoto = (Button) findViewById(R.id.btnTakePhoto2);
        btnPickPhoto = (Button) findViewById(R.id.btnPickPhoto2);
        imv = (ImageView) findViewById(R.id.imageDiary2);

        btnInsertDiary = (Button) findViewById(R.id.btnInsertDiary);
        imgbtn_BackDiary1 = (ImageButton) findViewById(R.id.imgbtn_BackDiary1);

        tvDate2.setOnClickListener(this);
        btnInsertDiary.setOnClickListener(this);
        imgbtn_BackDiary1.setOnClickListener(this);



        //接受main3 日期值
        Bundle bundle = getIntent().getExtras();
        String date = bundle.getString("date2");
        a = bundle.getInt("requestCodeA");
        tvDate2.setText(date);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        SimpleDateFormat strdate = new SimpleDateFormat("yyyy-MM-dd");
        ca.set(Calendar.YEAR,year);
        ca.set(Calendar.MONTH,month);
        ca.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String dates = strdate.format(ca.getTime());
        tvDate2.setText(dates);
    }

    @Override
    public void onClick(View view) {
        if (view == tvDate2) {
            new DatePickerDialog(this, this,
                    ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH)).show();
        }else if(view == btnInsertDiary){
            //儲存
            if(a == 246){
                SendInsert1();
            }else {
                SendInsert2();
            }

        }else if(view == imgbtn_BackDiary1){
            // 新增頁面的返回
            AlertDialog.Builder bdr = new AlertDialog.Builder(MainActivity3_1.this);
            bdr.setMessage("確定要返回嗎?");
            bdr.setTitle("返回");
            bdr.setNegativeButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SendBack();
                }
            });

            bdr.setPositiveButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(MainActivity3_1.this, "繼續新增日記", Toast.LENGTH_SHORT).show();
                }
            });
            bdr.show();
        }
    }

    public void SendInsert1(){  //傳資料
        strdateDiary = tvDate2.getText().toString();
        strheaderDiary = edtHeader.getText().toString();
        strcontentDiary = edtContent.getText().toString();

        Intent intent = new Intent(this, MainActivity3.class);
        Bundle bundle = new Bundle();
        bundle.putString("strdateDiary", strdateDiary);
        bundle.putString("strheaderDiary", strheaderDiary);
        bundle.putString("strcontentDiary", strcontentDiary);
        bundle.putByteArray("bytesAddDiary", bytesAddDiary);
        intent.putExtras(bundle);
        setResult(246,intent);
        MainActivity3_1.this.finish(); //回傳後要結束,不然程式會出錯

    }

    public void SendInsert2(){  //傳資料
        strdateDiary = tvDate2.getText().toString();
        strheaderDiary = edtHeader.getText().toString();
        strcontentDiary = edtContent.getText().toString();

        Intent intent = new Intent(this, MainActivity3.class);
        Bundle bundle = new Bundle();
        bundle.putString("strdateDiary", strdateDiary);
        bundle.putString("strheaderDiary", strheaderDiary);
        bundle.putString("strcontentDiary", strcontentDiary);
        bundle.putByteArray("bytesAddDiary", bytesAddDiary);
        intent.putExtras(bundle);
        setResult(248,intent);
        MainActivity3_1.this.finish(); //回傳後要結束,不然程式會出錯

    }

    public void SendBack(){  //按下返回
        Intent intent = new Intent(MainActivity3_1.this,MainActivity3.class);
        startActivity(intent);
    }

    public void onGet(View v) throws IOException {
        //按下照相機後會放到imgUri路徑裡
        imgUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        //Intent it = new Intent("android.media.action.IMAGE_CAPTURE");
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        it.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
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
            BitmapFactory.decodeStream(getContentResolver().openInputStream(imgUri), null, option);
        } catch (IOException e) {
            Toast.makeText(this, "讀取照片資訊時發生錯誤", Toast.LENGTH_LONG).show();
            return;
        }

        iw = option.outWidth;
        ih = option.outHeight;
        vw = imv.getWidth();
        vh = imv.getHeight();

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


        bmp = null;
        try {
            //讀取調整size後的圖檔
            bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(imgUri), null, option);
        } catch (IOException e) {
            Toast.makeText(this, "無法取得照片", Toast.LENGTH_LONG).show();
        }

        if (needRotate) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bmp = Bitmap.createBitmap(bmp,
                    0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        }

        imv.setImageBitmap(bmp);

        bytesAddDiary = saveImg(bmp);
        //img_string = saveImgaaa(bmp);

    }

    private byte[] saveImg(Bitmap bmp) {
        //saveImg
        //將 bitmap 轉成 byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        bytes = stream.toByteArray();
        return bytes;
    }

    private String saveImgaaa(Bitmap bmp) {
        //saveImg
        //將 bitmap 轉成 byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        bytes = stream.toByteArray();

        //將 byte 變成 base64
        String base64 = Base64.encodeToString(bytes, Base64.DEFAULT);
        return base64;
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data){ //預設有 所以要override
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK){      //拍照成功
            if (requestCode == 100){
                Intent it = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imgUri); //放入共享檔內
                sendBroadcast(it);
            }else if (requestCode == 101){
                imgUri = data.getData();
            }
            showImg();//拍照成功才會顯示

        }else {
            Toast.makeText(this, requestCode==100? "沒有拍到照片" : "沒有選取相片", Toast.LENGTH_LONG).show();
        }

    }



}