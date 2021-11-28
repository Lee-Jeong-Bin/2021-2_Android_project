package com.androidproject.texipool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

public class Finishmap extends AppCompatActivity {

    private Context context=this;

    private String mykey;
    private String nickname;
    private String nextNum;             //여기에 들어오는 숫자로 1이면 도착지, 2면
    private String start;
    private String startx;              //위도
    private String starty;              //경도
    int nextcase = 0;                   //Integer.parseInt()로 String을 int로 만들기 가능함.

    TMapView mapView;

    LocationManager mLM;
    String mProvider = LocationManager.NETWORK_PROVIDER;
    EditText keywordView;
    ListView listView;
    ArrayAdapter<POI> mAdapter;

    final String[] finish = new String[1];
    final double[] finishX = new double[1];
    final double[] finishY = new double[1];

    final TMapPoint[] tMapPoint = {new TMapPoint(finishY[0], finishX[0])};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_map);

        setting();
        next();
        back();

    }


    void setting(){

        //먼저 키 값들을 가져온다.
        mykey = getIntent().getStringExtra("mykey");
        nickname = getIntent().getStringExtra("mynickname");
        nextNum = getIntent().getStringExtra("case");
        start = getIntent().getStringExtra("start");
        startx = getIntent().getStringExtra("startX");
        starty = getIntent().getStringExtra("startY");


        //여기다가 tmap 관련하여서 코딩
        InputMethodManager manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);  // 키패드 컨트롤

        keywordView = (EditText) findViewById(R.id.finish_edit_keyword);
        listView = (ListView) findViewById(R.id.finish_listView);
        listView.setVisibility(listView.INVISIBLE);
        mAdapter = new ArrayAdapter<POI>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(mAdapter);
        ImageButton b = (ImageButton)findViewById(R.id.finish_xbtn); //도착지 지도화면에서 뒤로가기
        ImageButton b2 = (ImageButton)findViewById(R.id.finish_nextbtn); //도착지 지도화면에서 다음 버튼 (게시글 작성화면)

        mLM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mapView = (TMapView) findViewById(R.id.finish_map_view);
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


        Button btn = (Button) findViewById(R.id.finish_btn_search);
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
                finish[0] = poi.item.getPOIName();
                finishX[0] = poi.item.getPOIPoint().getLatitude();
                finishY[0] = poi.item.getPOIPoint().getLongitude();
                System.out.println("도착 지역 : " + poi.item.getPOIName());
                System.out.println("도착 위도 : " + poi.item.getPOIPoint().getLatitude());
                System.out.println("도착 경도 : " + poi.item.getPOIPoint().getLongitude());


                TMapMarkerItem item = new TMapMarkerItem();
                tMapPoint[0] = new TMapPoint(poi.item.getPOIPoint().getLatitude(), poi.item.getPOIPoint().getLongitude());
                item.setTMapPoint(tMapPoint[0]);
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.red);
                item.setIcon(bitmap);
                item.setPosition(0.5f, 1);
                mapView.addMarkerItem("item", item);
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); // 키패드 내리기

            }
        });



    }

    void next(){

        ImageButton ib = (ImageButton)findViewById(R.id.finish_nextbtn);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Finishmap.this, PostUI.class);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                it.putExtra("mykey", mykey);                //자신의 고유번호를 넘겨준다.
                it.putExtra("mynickname", nickname);        //자신의 닉네임을 넘긴다.
                it.putExtra("start", start);
                it.putExtra("startX", startx);
                it.putExtra("startY", starty);
                it.putExtra("finish", finish[0]);
                it.putExtra("finishX", Double.toString(finishX[0]));
                it.putExtra("finishY", Double.toString(finishY[0]));
                //시작
                startActivity(it);
            }
        });

    }

    void back(){

        ImageButton ib = (ImageButton)findViewById(R.id.finish_xbtn);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Finishmap.this, Startmap.class);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                it.putExtra("mykey", mykey);                //자신의 고유번호를 넘겨준다.
                it.putExtra("mynickname", nickname);        //자신의 닉네임을 넘긴다.
                it.putExtra("case",nextNum);               //뒤돌아 올 때를 대비하여 넣어줌
                it.putExtra("start", start);
                it.putExtra("startX", startx);
                it.putExtra("startY", starty);
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

    boolean isInitialized = false;

    private void setupMap() {
        isInitialized = true;
        mapView.setMapType(TMapView.MAPTYPE_STANDARD);
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