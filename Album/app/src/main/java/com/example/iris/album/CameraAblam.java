package com.example.iris.album;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;

public class CameraAblam extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_ablam);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Bundle bundle = this.getIntent().getExtras();
        byte[] b = bundle.getByteArray("image");
        final Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);

        Button button1 = (Button) findViewById(R.id.button3);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent();
                intent.setClass(CameraAblam.this  , Album_main.class);

                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG,50,bs);
                bundle.putByteArray("image",bs.toByteArray());

                //將Bundle物件assign給intent
                intent.putExtras(bundle);

                //切換Activity
                startActivity(intent);
            }

        });

        Button button2 = (Button) findViewById(R.id.button4);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(CameraAblam.this  , Album_add.class);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG,50,bs);
                bundle.putByteArray("image",bs.toByteArray());

                //將Bundle物件assign給intent
                intent.putExtras(bundle);
                startActivity(intent);
            }

        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
