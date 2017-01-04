package com.example.iris.album;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private final static int CAMERA = 66;
    private LocationManager mLocationManager;               //宣告定位管理控制

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //取得定位權限
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);	//使用GPS定位座標
                Bundle bundle = new Bundle();
                bundle.putDouble("longitude",location.getLongitude()); //經度
                bundle.putDouble("latitude",location.getLatitude());   //緯度*/
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Album_main.class);
                startActivity(intent);
            }

        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //開啟相機功能，並將拍照後的圖片存入SD卡相片集內，須由startActivityForResult且帶入requestCode進行呼叫，原因為拍照完畢後返回程式後則呼叫onActivityResult
                ContentValues value = new ContentValues();
                value.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                Uri uri= getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        value);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri.getPath());
                startActivityForResult(intent, CAMERA);
            }

        });

        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }

        });
    }

    //拍照完畢或選取圖片後呼叫此函式
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data)
    {
        //藉由requestCode判斷是否為開啟相機或開啟相簿而呼叫的，且data不為null
        if ((requestCode == CAMERA  ) && data != null)
        {
            //取得照片路徑uri
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();

            //讀取照片，型態為Bitmap
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            //imageView.setImageBitmap(bm);

            Intent intent = new Intent();
            intent.setClass(MainActivity.this, CameraAblam.class);

            //new一個Bundle物件，並將要傳遞的資料傳入
            Bundle bundle = new Bundle();
            //將ImageView轉Bitmap
            //SetIcon.buildDrawingCache();
            //Bitmap bitmap = yourImageView.getDrawingCache() ;
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 50, bs);
            bundle.putByteArray("image", bs.toByteArray());

            //將Bundle物件assign給intent
            intent.putExtras(bundle);

            //切換Activity
            startActivity(intent);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}
