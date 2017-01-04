package com.example.iris.album;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng place = new LatLng(25.1505207, 121.7711787);
        //BitmapDescriptor icon1 = BitmapDescriptorFactory.fromResource(R.drawable.sample_0);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(place).zoom(5).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        LatLng sydney = new LatLng(25.1505207, 121.7711787);
        mMap.addMarker(new MarkerOptions().position(sydney).title("海洋大學").icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("sample_0",50,50))));
        mMap.addMarker(new MarkerOptions().position(new LatLng(25.1362293, 121.785718)).title("新豐街").icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("sample_1",50,50))));
        mMap.addMarker(new MarkerOptions().position(new LatLng(25.1110928, 121.8416183)).title("九分").icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("sample_2",50,50))));
        mMap.addMarker(new MarkerOptions().position(new LatLng(25.1441298, 121.8012073)).title("潮境").icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("sample_3",50,50))));
        mMap.addMarker(new MarkerOptions().position(new LatLng(25.1604454, 121.7617068)).title("和平島").icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("sample_4",50,50))));

        // 標記點擊事件
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // 如果是目前位置標記
                Bundle bundle = new Bundle();
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                Bitmap bm;
                switch(marker.getTitle()) {
                    case "海洋大學":
                        bm = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier("sample_0", "drawable", getPackageName()));
                        bm.compress(Bitmap.CompressFormat.JPEG,50,bs);
                        break;
                    case "新豐街":
                        bm = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier("sample_1", "drawable", getPackageName()));
                        bm.compress(Bitmap.CompressFormat.JPEG,50,bs);
                        break;
                    case "九分":
                        bm = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier("sample_2", "drawable", getPackageName()));
                        bm.compress(Bitmap.CompressFormat.JPEG,50,bs);
                        break;
                    case "潮境":
                        bm = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier("sample_3", "drawable", getPackageName()));
                        bm.compress(Bitmap.CompressFormat.JPEG,50,bs);
                        break;
                    case "和平島":
                        bm = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier("sample_4", "drawable", getPackageName()));
                        bm.compress(Bitmap.CompressFormat.JPEG,50,bs);
                        break;


                }

                bundle.putByteArray("image",bs.toByteArray());
                Intent intent = new Intent();
                intent.setClass(MapsActivity.this, PhotoShow.class);
                //將Bundle物件assign給intent
                intent.putExtras(bundle);

                //切換Activity
                startActivity(intent);
                return false;
            }
        });
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public Bitmap resizeMapIcons(String iconName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }
}
