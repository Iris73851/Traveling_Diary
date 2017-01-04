package com.example.iris.album;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ImageAdapter extends BaseAdapter {

    private ViewGroup layout;
    private Context context;
    private List coll;
    private List nameList;
    private int flag;

    public ImageAdapter(Context context, List coll,List nameList,int flag) {

        super();
        this.context = context;
        this.coll = coll;
        this.nameList = nameList;
        this.flag = flag;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowview = inflater.inflate(R.layout.content_item, parent, false);
        layout = (ViewGroup) rowview.findViewById(R.id.r_content_item);
        ImageView imageView = (ImageView) rowview.findViewById(R.id.imageView1);
        TextView text = (TextView) rowview.findViewById(R.id.text1);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float dd = dm.density;
        float px = 25 * dd;
        float screenWidth = dm.widthPixels;
        float screeHeight = dm.heightPixels;
        int newHeight = (int) (screeHeight - px) / 4;
        int newWidth = (int) (screenWidth - px); //一行顯示一個縮圖

        if(flag == 2){
            newHeight = newWidth = (int) (screenWidth - px) / 4; //一行顯示四個縮圖;
        }else{
            String textString = nameList.get(position).toString();
            text.setText(textString);
        }


        layout.setLayoutParams(new GridView.LayoutParams(newWidth, newHeight));
        imageView.setId(position);

        BitmapFactory.Options options =new BitmapFactory.Options();
        options.inJustDecodeBounds =true;
        // 獲取這個圖片的寬和高
        Bitmap bitmap =BitmapFactory.decodeFile((String)coll.get(position), options); //此時返回bm為空
        options.inJustDecodeBounds =false;
        //計算縮放比
        int be = (int)(options.outHeight/ (float)200);
        if (be <= 0)
            be = 1;
        options.inSampleSize = be;
        //重新讀入圖片，注意這次要把options.inJustDecodeBounds 設為 false哦
        bitmap=BitmapFactory.decodeFile((String)coll.get(position),options);

        imageView.setImageBitmap(bitmap);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        //點擊照片
        imageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "index:" + position, Toast.LENGTH_SHORT)
                        .show();
                if(flag == 2){
                    ((Album_choosen)context).setImageView(position);
                }else if(flag == 1){
                    ((Album_main)context).setImageView(position);
                }

            }

        });

        imageView.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v)
                    {

                        return true;
                    }
                });

        return rowview;
    }

    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 刪除完裡面所有內容
            String filePath = folderPath;
            filePath = filePath.toString();
            File f = new File(filePath);
            f.delete(); // 刪除空文件夾
        } catch (NullPointerException e) {
        } catch (Exception e) {
        }
    }

    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        File[] tempList = file.listFiles();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i].toString());
            } else {
                temp = new File(path + File.separator + tempList[i].toString());
            }
            if (temp.isFile() && !temp.toString().contains("appconfig")) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先刪除文件夾裡面的文件
                delFolder(path + "/" + tempList[i]);// 再刪除空文件夾
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return coll.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return coll.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

}
