package com.androidproject.texipool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapInfo;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

public class Participategroup extends AppCompatActivity {

    //DB 관련
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    UserInfo myinfo;

    //나의 고유번호를 저장한다.
    private String mykey;
    private String nickname;
    private String mygroupkey;

    //지도 관련
    TMapView mapView;
    double x, y;
    private Context context=this;
    ArrayList<TMapPoint> point = new ArrayList<TMapPoint>();

    //상세화면 그룹 가져오기
    Group gp;
    ArrayList<UserInfo> userlist = new ArrayList<UserInfo>();

    //뷰 값들 저장
    ImageView[] img = new ImageView[4];      //이미지 관리
    TextView[] stars = new TextView[4];      //별점 관리
    TextView[] ccper = new TextView[4];      //취소율 관리
    TextView textView;

    int checkme = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participategroup);

        //파이어 베이스 데이터베이스 연동
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        mykey = getIntent().getStringExtra("mykey");
        nickname = getIntent().getStringExtra("mynickname");
        mygroupkey = getIntent().getStringExtra("mygroup");






        setting();
        cancle();
        joinGroup();

    }

    void setting(){

        //뷰 연결
        img[0] = (ImageView)findViewById(R.id.profile1);
        img[1] = (ImageView)findViewById(R.id.profile2);
        img[2] = (ImageView)findViewById(R.id.profile3);
        img[3] = (ImageView)findViewById(R.id.profile4);

        stars[0] = (TextView)findViewById(R.id.stars1);
        stars[1] = (TextView)findViewById(R.id.stars2);
        stars[2] = (TextView)findViewById(R.id.stars3);
        stars[3] = (TextView)findViewById(R.id.stars4);

        ccper[0] = (TextView)findViewById(R.id.cancle_percentage1);
        ccper[1] = (TextView)findViewById(R.id.cancle_percentage2);
        ccper[2] = (TextView)findViewById(R.id.cancle_percentage3);
        ccper[3] = (TextView)findViewById(R.id.cancle_percentage4);

        textView = (TextView)findViewById(R.id.introduction);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                myinfo = snapshot.child("UserInfo").child(mykey).getValue(UserInfo.class);
                gp = snapshot.child("Group").child(mygroupkey).getValue(Group.class);           //gp에 저장

                //소개말
                textView.setText(gp.text);

                for(int i = 0; i < gp.users.size(); i++){           //키 값 가지고 있으니 클래스로 가져온다.

                    UserInfo uf = snapshot.child("UserInfo").child(gp.users.get(i)).getValue(UserInfo.class);
                    userlist.add(uf);

                }

                for(int i = 0; i < userlist.size() ; i++){      //있는 만큼 세팅하여 준다.

                       //이미지
                       if(userlist.get(i).img != null){        //이미지 파일이 있는 경우만

                           Bitmap bmp;
                           byte[] bytes = UserInfo.binaryStringToByteArray(userlist.get(i).img);
                           bmp = BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
                           img[i].setImageBitmap(bmp);
                           img[i].setClipToOutline(true);              //모양에 맞게 사진 자르기

                       }

                       //평점
                       if(userlist.get(i).stars == null || userlist.get(i).stars.size() < 10){

                           stars[i].setText("평가 10회 미만");

                       }else{

                           double estimate;
                           double sum = 0;

                           for(int j = 0; j < userlist.get(i).stars.size(); j++){

                               sum = sum + userlist.get(i).stars.get(j);

                           }
                           estimate = sum / (double)userlist.get(i).stars.size();
                           stars[i].setText(Float.toString((float)(Math.round(estimate*100)/100.0)) + "점");

                       }
                       //취소율
                       if(userlist.get(i).end_groups == null || userlist.get(i).end_groups.size() + userlist.get(i).fail_count < 10){

                           ccper[i].setText("탑승 10회 미만");

                       }else{

                           ccper[i].setText(String.format("%.2lf", (double)userlist.get(i).fail_count / ((double)userlist.get(i).end_groups.size()
                                   + (double)userlist.get(i).fail_count)) + "%" );
                       }

                   }

                   for(int i = userlist.size(); i < 4; i++){       //없는 것은 안보이게 하기

                       img[i].setVisibility(View.INVISIBLE);
                       stars[i].setVisibility(View.INVISIBLE);
                       ccper[i].setVisibility(View.INVISIBLE);

                   }

                   //지도 세팅
                mapView = (TMapView) findViewById(R.id.map_par);
                   mapView.setSKTMapApiKey("l7xx303267b599d441eb85003eeddd7b4d4c");
                   mapView.setLanguage(TMapView.LANGUAGE_KOREAN);
                   final TMapPoint[] starttMapPoint = {new TMapPoint(y, x)};
                   final TMapPoint[] finishtMapPoint = {new TMapPoint(y, x)};

                   String start = gp.start_address;
                   double startX = gp.start_x;
                   double startY = gp.start_y;

                   String finish = gp.destination;
                   double finishX = gp.finish_x;
                   double finishY = gp.finish_y;

                   mapView.setCenterPoint((startY+finishY)/2, (startX+finishX)/2);
                   mapView.setMapType(TMapView.MAPTYPE_STANDARD);
                   starttMapPoint[0] = new TMapPoint(startX, startY);
                   finishtMapPoint[0] = new TMapPoint(finishX, finishY);
                   searchRoute(starttMapPoint[0], finishtMapPoint[0]);

                   point.add(starttMapPoint[0]);
                   point.add(finishtMapPoint[0]);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    void cancle(){      //완성

        ImageButton cancleButton = (ImageButton) findViewById(R.id.imageView4);
        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Participategroup.this, MainActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                it.putExtra("mykey", mykey);                //자신의 고유번호를 넘겨준다.
                it.putExtra("mynickname", nickname);        //자신의 닉네임을 넘긴다.
                startActivity(it);

            }
        });
    }

    private void searchRoute(TMapPoint starttMapPoint, TMapPoint finishtMapPoint){
        TMapData data = new TMapData();

        data.findPathData(starttMapPoint, finishtMapPoint, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(final TMapPolyLine path) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TMapInfo info = mapView.getDisplayTMapInfo(point);
                        mapView.setCenterPoint(info.getTMapPoint().getLongitude(),info.getTMapPoint().getLatitude());
                        int zoom = info.getTMapZoomLevel();
                        mapView.setZoomLevel(zoom - 1);
                        path.setLineWidth(10);
                        path.setLineColor(Color.GREEN);
                        mapView.addTMapPath(path);
                        Bitmap start = BitmapFactory.decodeResource(context.getResources(),R.drawable.blue);
                        Bitmap end = BitmapFactory.decodeResource(context.getResources(),R.drawable.red);
                        mapView.setTMapPathIcon(start, end);

                    }
                });
            }
        });
    }

    void joinGroup(){

        //testgroup 키 값을 사용하여 테스트한다.
        /*
                    순서
                    1.먼저 자신이 들어가려는 키값의 그룹이 자신이 이미 속해 있는지 확인한다.
                    2.속하지 않았다면 그룹에 리스트에 자신의 목록을 추가한다.
                    3.이제 사용자 그룹 리스트에 자신을 추가한다.

         */

        TextView b2 = (TextView)findViewById(R.id.join_button);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //한 번만 조회함으로 이것이 필요.
                myRef.child("Group").child(mygroupkey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Group a = snapshot.getValue(Group.class);

                        for(int i = 0; i < a.users.size(); i++){

                            System.out.println( i+ "번 " + a.users.get(i).toString());

                            if(mykey.equals(a.users.get(i).toString())){      //참여자 키에서 자신이 이미 있다면

                                System.out.println("들어옴 여기");
                                checkme = 1;                //자신을 발견한다.

                            }
                        }

                        if(checkme == 0){       //자신을 발견하지 못한 경우 추가한다.

                            a.users.add(mykey);         //자신의 키를 그룹에 추가
                            myRef.child("Group").child(mygroupkey).setValue(a);      //서버에 그룹 저장

                            if(myinfo.groups == null){  //그룹이 없어서 비어있다면

                                myinfo.groups = new ArrayList<String>();
                                myinfo.groups.add(mygroupkey);


                            }else{

                                myinfo.groups.add(mygroupkey);       //자신의 정보에 그룹 키 저장

                            }
                            myRef.child("UserInfo").child(mykey).setValue(myinfo);  //서버에 사용자 정보 저장
                            Intent it = new Intent(Participategroup.this, MainActivity.class);
                            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            it.putExtra("mykey", mykey);                //자신의 고유번호를 넘겨준다.
                            it.putExtra("mynickname", nickname);        //자신의 닉네임을 넘긴다.
                            startActivity(it);

                        }
                        checkme = 0;
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });


    }


}