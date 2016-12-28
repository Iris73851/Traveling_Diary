package com.example.iris.album;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private GridView gridView;
    private ImageView imageView;
    private List<String> thumbs;  //存放縮圖的id
    private List<String> imagePaths;  //存放圖片的路徑
    private ImageAdapter imageAdapter;  //用來顯示縮圖

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gridView1);
        imageView = (ImageView) findViewById(R.id.imageView1);

        ContentResolver cr = getContentResolver();
        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};

        //查詢SD卡的圖片
        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null);

        thumbs = new ArrayList<String>();
        imagePaths = new ArrayList<String>();

        for (int i = 0; i < cursor.getCount(); i++) {

            cursor.moveToPosition(i);
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Images.Media._ID));// ID
            thumbs.add(id + "");

            String filepath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));//抓路徑

            imagePaths.add(filepath);
        }

        cursor.close();

        imageAdapter = new ImageAdapter(MainActivity.this, thumbs);
        gridView.setAdapter(imageAdapter);
        imageAdapter.notifyDataSetChanged();


        imageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                imageView.setVisibility(View.GONE);
                gridView.setVisibility(View.VISIBLE);
            }

        });
        imageView.setVisibility(View.GONE);

    }




    public void setImageView(int position) {
        Bitmap bm = BitmapFactory.decodeFile(imagePaths.get(position));
        imageView.setImageBitmap(bm);
        imageView.setVisibility(View.VISIBLE);
        gridView.setVisibility(View.GONE);
    }
}