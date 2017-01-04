package com.example.iris.album;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Album_choosen extends AppCompatActivity {

    private GridView gridView;
    private List<String> thumbs;  //存放縮圖的id
    private List<String> imagePaths;  //存放圖片的路徑
    private ImageAdapter imageAdapter;  //用來顯示縮圖
    private TextView introduction;
    private ImageView mImg;
    private DisplayMetrics mPhone;
    private String name;
    private int count = 0;
    private final static int PHOTO = 99 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_choosen);

        Bundle bundle =this.getIntent().getExtras();
        name = bundle.getString("AlbumName");

        //讀取手機解析度
        mPhone = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mPhone);

        //mImg = (ImageView) findViewById(R.id.img);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);

        gridView = (GridView) findViewById(R.id.gridView1);
        introduction = (TextView) findViewById(R.id.textView5);

        ContentResolver cr = getContentResolver();
        String[] projection = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };

        //查詢SD卡的圖片
        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null);



        thumbs = new ArrayList<String>();
        imagePaths = new ArrayList<String>();

        String filePath = Environment.getExternalStorageDirectory().toString () +"/Travel/"+name;//抓路徑
        // 得到该路径文件夹下所有的文件
        File fileAll = new File(filePath);
        File[] files = fileAll.listFiles();
        count = files.length;

        // 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (checkIsImageFile(file.getPath())) {
                thumbs.add(file.getName());
                imagePaths.add(file.getPath());
            }else{

                try{
                    //取得SD卡儲存路徑
                    File mSDFile = Environment.getExternalStorageDirectory();
                    //讀取文件檔路徑
                    FileReader mFileReader = new FileReader(file.getPath());

                    BufferedReader mBufferedReader = new BufferedReader(mFileReader);
                    String mReadText = "";
                    String mTextLine = mBufferedReader.readLine();

                    //一行一行取出文字字串裝入String裡，直到沒有下一行文字停止跳出
                    while (mTextLine!=null)
                    {
                        mReadText += mTextLine+"\n";
                        mTextLine = mBufferedReader.readLine();
                    }
                    //文字放入mText裡，並清空mEdit
                    introduction.setText(mReadText);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }


        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PHOTO);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cursor.close();
        int flag = 2;
        imageAdapter = new ImageAdapter(Album_choosen.this, imagePaths, thumbs, 2);
        gridView.setAdapter(imageAdapter);
        imageAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private boolean checkIsImageFile(String fName) {
        boolean isImageFile = false;
        // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        if (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("gif")
                || FileEnd.equals("jpeg")|| FileEnd.equals("bmp") ) {
            isImageFile = true;
        } else {
            isImageFile = false;
        }
        return isImageFile;
    }

    public void setImageView(int position){
        Bitmap bm = BitmapFactory.decodeFile(imagePaths.get(position));
        //imageView.setImageBitmap(bm);

        Intent intent = new Intent();
        intent.setClass(Album_choosen.this  , PhotoShow.class);

        //new一個Bundle物件，並將要傳遞的資料傳入
        Bundle bundle = new Bundle();
        //將ImageView轉Bitmap
        //SetIcon.buildDrawingCache();
        //Bitmap bitmap = yourImageView.getDrawingCache() ;
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,50,bs);
        bundle.putByteArray("image",bs.toByteArray());
        bundle.putString("AlbumName", name);

        //將Bundle物件assign給intent
        intent.putExtras(bundle);

        //切換Activity
        startActivity(intent);
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
                Bitmap bm = BitmapFactory.decodeStream(cr.openInputStream(uri));

                //放入相片
                try {
                    // 取得外部儲存裝置路徑
                    String photoPath = Environment.getExternalStorageDirectory().toString () +"/Travel/"+ name;

                    // 開啟檔案
                    File file = new File( photoPath, "Image"+count+".jpg");
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

                Intent intent = new Intent(Album_choosen.this, Album_choosen.class);
                //new一個Bundle物件，並將要傳遞的資料傳入
                Bundle bundle = new Bundle();
                bundle.putString("AlbumName", name);

                //將Bundle物件assign給intent
                intent.putExtras(bundle);
                startActivity(intent);
                //close this activity
                finish();
            }
            catch (FileNotFoundException e)
            {
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



}

