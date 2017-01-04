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

public class Item extends AppCompatActivity {

    private ImageView imageView;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        bundle = this.getIntent().getExtras();

        setSupportActionBar(toolbar);

        imageView = (ImageView) findViewById(R.id.imageView1);

        byte[] b = bundle.getByteArray("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        imageView.setImageBitmap(bmp);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(Item.this, Album_choosen.class);
                String Aname = bundle.getString("AlbumName");
                bundle.putString("AlbumName", Aname);
                homeIntent.putExtras(bundle);
                startActivity(homeIntent);
                return true;
        }
        return false;
    }
}