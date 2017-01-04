package com.example.iris.album;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Album_add extends AppCompatActivity {

    private EditText AlbumName = null;
    private EditText AlbumInform = null;
    private Bitmap bm;
    //private String AlbumPath = "";
    //private ImageView mImg;
    private DisplayMetrics mPhone;
    private final static int PHOTO = 99 ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Bundle bundle = this.getIntent().getExtras();
        //讀取手機解析度
        mPhone = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mPhone);

        //mImg = (ImageView) findViewById(R.id.img);
        AlbumName = (EditText) findViewById(R.id.editText);
        AlbumInform = (EditText)findViewById(R.id.editText2);


        //新增按鈕
        Button add = (Button) findViewById(R.id.button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //新增資料夾
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))//確定SD可以讀
                {

                    String name = AlbumName.getText().toString();
                    String inform = AlbumInform.getText().toString();

                    File sdFile = android.os.Environment.getExternalStorageDirectory();
                    String path = sdFile.getPath() + File.separator +"Travel"+ File.separator + name;
                    File dirFile = new File(path);

                    if(!dirFile.exists()){//如果資料夾不存在
                        dirFile.mkdir();//建立資料夾
                    }

                    //放入相片
                    try {
                        // 取得外部儲存裝置路徑
                        String photoPath = Environment.getExternalStorageDirectory().toString () +"/Travel/"+ name;
                        if(bundle != null){
                            byte[] b = bundle.getByteArray("image");
                            bm = BitmapFactory.decodeByteArray(b, 0, b.length);
                        }
                        // 開啟檔案
                        File file = new File( photoPath, "Image0.jpg");
                        // 開啟檔案串流
                        FileOutputStream out = new FileOutputStream(file );
                        // 將 Bitmap壓縮成指定格式的圖片並寫入檔案串流
                        bm.compress ( Bitmap. CompressFormat.PNG , 90 , out);
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

                    //location
                    /*
                    File file = new File(photoPath, "location.txt");
                    String location1 = "25.1345287";
                    String location2 = "121.763035";
                    String location3 = "image0";
                    try {
                        FileOutputStream outputStream = new FileOutputStream(file, true);
                        //outputStream = openFileOutput(name, Context.MODE_PRIVATE);
                        outputStream.write(location1.getBytes());
                        outputStream.write(location2.getBytes());
                        outputStream.write(location3.getBytes());
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/

                    //儲存簡介
                    String photoPath = Environment.getExternalStorageDirectory().toString () +"/Travel/"+name;
                    File file1 = new File(photoPath, name+".txt");

                    try {
                        FileOutputStream outputStream = new FileOutputStream(file1, false);
                        //outputStream = openFileOutput(name, Context.MODE_PRIVATE);
                        outputStream.write(inform.getBytes());
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }



                Intent intent = new Intent();
                intent.setClass(Album_add.this  , Album_main.class);
                //切換Activity
                startActivity(intent);
            }
        });


        //瀏覽相片
        Button getPic = (Button) findViewById(R.id.button1);
        if(bundle != null) {
            getPic.setVisibility(View.INVISIBLE);
            TextView path = (TextView) findViewById(R.id.textView4);
            path.setText("已選取");
        }else {
            getPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //開啟相簿相片集，須由startActivityForResult且帶入requestCode進行呼叫，原因為點選相片後返回程式呼叫onActivityResult
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, PHOTO);
                }
            });
        }
    }

    //拍照完畢或選取圖片後呼叫此函式
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data)
    {
        //藉由requestCode判斷是否為開啟相機或開啟相簿而呼叫的，且data不為null
        if ((requestCode == PHOTO ) && data != null)
        {
            //取得照片路徑uri
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();

            try
            {
                //讀取照片，型態為Bitmap
               bm = BitmapFactory.decodeStream(cr.openInputStream(uri));
                TextView path = (TextView) findViewById(R.id.textView4);
                path.setText("已選取");

                /*
                //判斷照片為橫向或者為直向，並進入ScalePic判斷圖片是否要進行縮放
                if(bitmap.getWidth()>bitmap.getHeight())ScalePic(bitmap, mPhone.heightPixels);
                else ScalePic(bitmap,mPhone.widthPixels);*/
            }
            catch (FileNotFoundException e)
            {

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



}
