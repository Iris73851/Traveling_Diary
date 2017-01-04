package com.example.iris.album;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;


public class Album_main extends AppCompatActivity {

    private GridView gridView;
    private List<String> thumbs;  //存放縮圖的id
    private List<String> imagePaths;  //存放圖片的路徑
    private ImageAdapter imageAdapter;  //用來顯示縮圖
    private int count=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Album");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Album_main.this  , Album_add.class);
                //切換Activity
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gridView = (GridView) findViewById(R.id.gridView1);

        //查詢SD卡的圖片
        thumbs = new ArrayList<String>();
        imagePaths = new ArrayList<String>();

        String filePath = Environment.getExternalStorageDirectory().toString()+"/Travel";//抓路徑
        // 得到该路径文件夹下所有的文件
        File fileAll = new File(filePath);
        File[] files = fileAll.listFiles();
        count = files.length;
        // 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            thumbs.add(file.getName());
            imagePaths.add(file.getPath()+"/Image0.jpg");

        }

        //int flag = 1;
        imageAdapter = new ImageAdapter(Album_main.this, imagePaths, thumbs, 1);
        gridView.setAdapter(imageAdapter);
        imageAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu, menu);
        return true;
    }

    public void setImageView(int position){
        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null){
            //放入相片
            try {
                String path = Environment.getExternalStorageDirectory().toString () +"/Travel/"+thumbs.get(position);
                byte[] b = bundle.getByteArray("image");
                Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
                // 開啟檔案
                File file = new File( path, "Image"+count+".jpg");
                // 開啟檔案串流
                FileOutputStream out = new FileOutputStream(file );
                // 將 Bitmap壓縮成指定格式的圖片並寫入檔案串流
                bmp.compress ( Bitmap. CompressFormat.PNG , 90 , out);
                // 刷新並關閉檔案串流
                out.flush ();
                out.close ();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace ();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace ();
            }
        }

        Bundle bun = new Bundle();
        Bitmap bm = BitmapFactory.decodeFile(imagePaths.get(position));
        //imageView.setImageBitmap(bm);

        Intent intent = new Intent();
        intent.setClass(Album_main.this  , Album_choosen.class);

        bun.putString("AlbumName", thumbs.get(position).toString());

        //將Bundle物件assign給intent
        intent.putExtras(bun);

        //切換Activity
        startActivity(intent);
    }
}
