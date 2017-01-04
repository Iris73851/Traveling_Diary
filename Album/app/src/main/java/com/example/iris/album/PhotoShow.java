package com.example.iris.album;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoShow extends AppCompatActivity {

    private ImageView imageView;
    private TextView address;
    private Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_show);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        bundle = this.getIntent().getExtras();

        setSupportActionBar(toolbar);

        imageView = (ImageView) findViewById(R.id.imageView);

        byte[] b = bundle.getByteArray("image");
        //Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        BitmapFactory.Options options =new BitmapFactory.Options();
        options.inJustDecodeBounds =true;
        // 獲取這個圖片的寬和高
        Bitmap bitmap =BitmapFactory.decodeByteArray(b, 0, b.length, options); //此時返回bm為空
        options.inJustDecodeBounds =false;
        //計算縮放比
        int be = (int)(options.outHeight/ (float)200);
        if (be <= 0)
            be = 1;
        options.inSampleSize = be;
        //重新讀入圖片，注意這次要把options.inJustDecodeBounds 設為 false哦
        bitmap=BitmapFactory.decodeByteArray(b, 0, b.length,options);
        imageView.setImageBitmap(bitmap);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(PhotoShow.this, Album_choosen.class);
                String Aname = bundle.getString("AlbumName");
                bundle.putString("AlbumName", Aname);
                homeIntent.putExtras(bundle);
                startActivity(homeIntent);
                return true;
        }
        return false;
    }

}
