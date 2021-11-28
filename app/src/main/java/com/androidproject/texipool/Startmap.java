package com.androidproject.texipool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import com.skt.Tmap.poi_item.TMapPOIItem;

import java.util.ArrayList;

public class Startmap extends AppCompatActivity {

    private String mykey;
    private String nickname;
    private String nextNum;             //여기에 들어오는 숫자로 1이면 도착지, 2면
    int nextcase = 0;                   //Integer.parseInt()로 String을 int로 만들기 가능함.

    TMapView mapView;

    LocationManager mLM;
    String mProvider = LocationManager.NETWORK_PROVIDER;

    EditText keywordView;
    ListView listView;
    ArrayAdapter<POI> mAdapter;

    final String[] start = new String[1];
    final double[] startX = new double[1];
    final double[] startY = new double[1];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_map);

        setting();
        next();
        back();
    }

    void setting(){

        //먼저 키 값들을 가져온다.
        mykey = getIntent().getStringExtra("mykey");
        nickname = getIntent().getStringExtra("mynickname");
        nextNum = getIntent().getStringExtra("case");
        nextcase = Integer.parseInt(nextNum);

        //여기다가 tmap 관련하여서 코딩
        keywordView = (EditText) findViewById(R.id.start_edit_keyword);
        listView = (ListView)findViewById(R.id.start_listView);
        listView.setVisibility(listView.INVISIBLE);
        mAdapter = new ArrayAdapter<POI>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(mAdapter);

        ImageButton b2 = (ImageButton)findViewById(R.id.start_nextbtn); //도착지 지도화면에서 다음 버튼 (게시글 작성화면)

        mLM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mapView = (TMapView) findViewById(R.id.start_map_view);
        mapView.setOnApiKeyListener(new TMapView.OnApiKeyListenerCallback() {
            @Override
            public void SKTMapApikeySucceed() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setupMap();
                    }
                });
            }

            @Override
            public void SKTMapApikeyFailed(String s) {

            }

        });
        mapView.setSKTMapApiKey("l7xx303267b599d441eb85003eeddd7b4d4c");
        mapView.setLanguage(TMapView.LANGUAGE_KOREAN);



        Button btn = (Button) findViewById(R.id.start_btn_search);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setVisibility(listView.VISIBLE);
                searchPOI();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                POI poi = (POI) listView.getItemAtPosition(position);
                moveMap(poi.item.getPOIPoint().getLatitude(), poi.item.getPOIPoint().getLongitude());
                listView.setVisibility(listView.INVISIBLE);
                keywordView.setText(poi.item.getPOIName());
                start[0] = poi.item.getPOIName();
                startX[0] = poi.item.getPOIPoint().getLatitude();
                startY[0] = poi.item.getPOIPoint().getLongitude();
                System.out.println("출발 지역 : " + poi.item.getPOIName());
                System.out.println("출발 위도 : " + poi.item.getPOIPoint().getLatitude());
                System.out.println("출발 경도 : " + poi.item.getPOIPoint().getLongitude());
            }
        });
    }

    void next(){        //다음으로

        ImageButton move = (ImageButton) findViewById(R.id.start_nextbtn);
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println(nextNum + "번 케이스 작동");
                if(nextcase == 1){              //그룹 생성

                    Intent it = new Intent(Startmap.this, Finishmap.class);
                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    it.putExtra("mykey", mykey);                //자신의 고유번호를 넘겨준다.
                    it.putExtra("mynickname", nickname);        //자신의 닉네임을 넘긴다.
                    it.putExtra("case",nextNum);               //뒤돌아 올 때를 대비하여 넣어줌
                    it.putExtra("start", start[0]);
                    it.putExtra("startX", Double.toString(startX[0]));
                    it.putExtra("startY", Double.toString(startY[0]));
                    //시작
                    startActivity(it);

                }else {                         //그룹 참여

                    //토요일에 합치기
                    Intent it = new Intent(Startmap.this, ListUI.class);
                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    it.putExtra("mykey", mykey);                //자신의 고유번호를 넘겨준다.
                    it.putExtra("mynickname", nickname);        //자신의 닉네임을 넘긴다.
                    //시작
                    startActivity(it);

                }
            }
        });
    }

    void back() {        //메인 화면으로

        ImageButton move = (ImageButton) findViewById(R.id.start_xbtn);
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Startmap.this, MainActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                it.putExtra("mykey", mykey);        //자신의 고유번호를 넘겨준다.
                it.putExtra("mynickname", nickname);  //자신의 닉네임을 넘긴다.
                //시작
                startActivity(it);

            }
        });
    }

    private void searchPOI() {
        TMapData data = new TMapData();
        String keyword = keywordView.getText().toString();
        if (!TextUtils.isEmpty(keyword)) {
            data.findAllPOI(keyword, new TMapData.FindAllPOIListenerCallback() {
                @Override
                public void onFindAllPOI(final ArrayList<TMapPOIItem> arrayList) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mapView.removeAllMarkerItem();
                            mAdapter.clear();

                            for (TMapPOIItem poi : arrayList) {
                                addMarker(poi);
                                mAdapter.add(new POI(poi));
                            }

                            if (arrayList.size() > 0) {
                                TMapPOIItem poi = arrayList.get(0);
                                moveMap(poi.getPOIPoint().getLatitude(), poi.getPOIPoint().getLongitude());
                            }
                        }
                    });
                }
            });
        }
    }

    public void addMarker(TMapPOIItem poi) {
        TMapMarkerItem item = new TMapMarkerItem();
        item.setTMapPoint(poi.getPOIPoint());
        Bitmap icon = ((BitmapDrawable) ContextCompat.getDrawable(this, android.R.drawable.ic_input_add)).getBitmap();
        item.setIcon(icon);
        item.setPosition(0.5f, 1);
        item.setCalloutTitle(poi.getPOIName());
        item.setCalloutSubTitle(poi.getPOIContent());
        Bitmap left = ((BitmapDrawable) ContextCompat.getDrawable(this, android.R.drawable.ic_dialog_alert)).getBitmap();
        item.setCalloutLeftImage(left);
        Bitmap right = ((BitmapDrawable) ContextCompat.getDrawable(this, android.R.drawable.ic_input_get)).getBitmap();
        item.setCalloutRightButtonImage(right);
        item.setCanShowCallout(true);
        mapView.addMarkerItem(poi.getPOIID(), item);
    }
    int id = 0;

    boolean isInitialized = false;

    private void setupMap() {
        isInitialized = true;
        mapView.setMapType(TMapView.MAPTYPE_STANDARD);
        //        mapView.setSightVisible(true);
        //        mapView.setCompassMode(true);
        //        mapView.setTrafficInfo(true);
        //        mapView.setTrackingMode(true);
        if (cacheLocation != null) {
            moveMap(cacheLocation.getLatitude(), cacheLocation.getLongitude());
            setMyLocation(cacheLocation.getLatitude(), cacheLocation.getLongitude());
        }
        mapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            @Override
            public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                listView.setVisibility(listView.INVISIBLE);
                return false;
            }

            @Override
            public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                return false;
            }
        });
        mapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = mLM.getLastKnownLocation(mProvider);
        if (location != null) {
            mListener.onLocationChanged(location);
        }
        mLM.requestSingleUpdate(mProvider, mListener, null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLM.removeUpdates(mListener);
    }

    Location cacheLocation = null;

    private void moveMap(double lat, double lng) {
        mapView.setCenterPoint(lng, lat);
    }

    private void setMyLocation(double lat, double lng) {
        Bitmap icon = ((BitmapDrawable) ContextCompat.getDrawable(this, android.R.drawable.ic_dialog_map)).getBitmap();
        mapView.setIcon(icon);
        mapView.setLocationPoint(lng, lat);
        mapView.setIconVisibility(true);
    }

    LocationListener mListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (isInitialized) {
                moveMap(location.getLatitude(), location.getLongitude());
                setMyLocation(location.getLatitude(), location.getLongitude());
            } else {
                cacheLocation = location;
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

}