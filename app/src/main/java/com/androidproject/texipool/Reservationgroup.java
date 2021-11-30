package com.androidproject.texipool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
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

public class Reservationgroup extends AppCompatActivity {

    //DB 관련
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    //나의 고유번호를 저장한다.
    private String mykey;
    private String nickname;
    private String mygroupkey;
    private int end;                    //0이면 예약 중인 그룹이고 1이면 종료된 그룹이다.

    //상세화면 그룹 가져오기
    Group gp;
    UserInfo userInfo;
    ArrayList<UserInfo> userlist = new ArrayList<UserInfo>();

    //뷰 값들 저장
    ImageView[] img = new ImageView[4];      //이미지 관리
    TextView[] stars = new TextView[4];      //별점 관리
    TextView[] ccper = new TextView[4];      //취소율 관리
    TextView date;
    TextView time;



    //지도 관련
    TMapView mapView;
    double x, y;
    private Context context=this;
    ArrayList<TMapPoint> point = new ArrayList<TMapPoint>();






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservationgroup);

        mykey = getIntent().getStringExtra("mykey");
        nickname = getIntent().getStringExtra("mynickname");
        mygroupkey = getIntent().getStringExtra("GroupKey");
        System.out.println("이방 번호" + mygroupkey);
        end = Integer.parseInt(getIntent().getStringExtra("end"));

        //파이어 베이스 데이터베이스 연동
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        MainActivity mainActivity = (MainActivity)MainActivity.mainActivity;
        mainActivity.finish();

        setting();
        cancle();
        chat();
        groupout();
        arrival();

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

        date = (TextView)findViewById(R.id.rdate);
        time = (TextView)findViewById(R.id.rtime);


       myRef.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {

               userInfo = snapshot.child("UserInfo").child(mykey).getValue(UserInfo.class);    //자기 자신 저장
               gp = snapshot.child("Group").child(mygroupkey).getValue(Group.class);           //gp에 저장


               //날짜와 시간저장
               Time mytime = new Time(gp.year, gp.month, gp.day , gp.start_hours, gp.start_minutes);
               date.setText(mytime.date());
               time.setText(mytime.time());


               if (gp.users == null){

                   gp.users = new ArrayList<String>();

               }

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
                   img[i].setOnClickListener(new View.OnClickListener() {       //사진이 눌렸을 경우
                       @Override
                       public void onClick(View v) {
                           //상세 사진 화면 보기로 넘어간다.





                       }
                   });

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
               mapView = (TMapView) findViewById(R.id.map_reserve);
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

                Intent it = new Intent(Reservationgroup.this, MainActivity.class);
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

    void chat(){        //완성

        TextView chatroom = (TextView)findViewById(R.id.chat);
        chatroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent chatIntent = new Intent(Reservationgroup.this,Chat.class);
                chatIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                chatIntent.putExtra("mykey",mykey);                           //자신의 키를 넘긴다.
                chatIntent.putExtra("mygroup",mygroupkey);        //그룹의 키를 넘긴다.
                chatIntent.putExtra("mynickname", nickname);              //자신의 닉네임을 넘긴다.
                chatIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(chatIntent);

            }
        });
    }

    void groupout(){
        if(end == 0){

            TextView groupot = (TextView)findViewById(R.id.groupout);
            groupot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(gp.users.size() >= 2){

                        //그룹에서 자기 키 삭제
                        for(int i = 0; i < gp.users.size(); i++){

                            if(gp.users.get(i) == mykey){       //키가 같은 경우

                                gp.users.remove(i);     //삭제

                            }

                        }
                        //자신의 정보에서 그룹 키 삭제
                        for(int i = 0; i < userInfo.groups.size(); i++){

                            if(userInfo.groups.get(i).equals(mygroupkey)){       //키가 같은 경우

                                userInfo.groups.remove(i);     //삭제
                            }
                        }
                        //벌점 넣기
                        userInfo.fail_count++;
                        myRef.child("UserInfo").child(mykey).setValue(userInfo);

                        //서버에 저장
                        myRef.child("Group").child(mygroupkey).setValue(gp);

                        Intent it = new Intent(Reservationgroup.this, MainActivity.class);
                        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        it.putExtra("mykey", mykey);                //자신의 고유번호를 넘겨준다.
                        it.putExtra("mynickname", nickname);        //자신의 닉네임을 넘긴다.
                        startActivity(it);



                    }else{              //혼자일 경우

                        for(int i = 0; i < userInfo.groups.size(); i++){

                            System.out.println("비교" + userInfo.groups.get(i));
                            System.out.println(mygroupkey);

                            if(userInfo.groups.get(i).equals(mygroupkey)){       //키가 같은 경우

                                userInfo.groups.remove(i);     //삭제
                                System.out.println(userInfo.groups.size());

                            }
                        }

                        myRef.child("UserInfo").child(mykey).setValue(userInfo);    //서버에 저장
                        myRef.child("Group").child(mygroupkey).setValue(null);

                        Intent it = new Intent(Reservationgroup.this, MainActivity.class);
                        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        it.putExtra("mykey", mykey);                //자신의 고유번호를 넘겨준다.
                        it.putExtra("mynickname", nickname);        //자신의 닉네임을 넘긴다.
                        startActivity(it);

                    }

                }
            });
        }
    }

    //혼자일 경우는 평가 그게 안 뜸

    void arrival(){
        if(end == 0){

            TextView arrive = (TextView)findViewById(R.id.arrival);
            arrive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(gp.users.size() >= 2){       //userlist 사용

                        //여기가 평가 발생
                        myRef.child("EndGroup").child(mygroupkey).setValue(gp);     //종료 그룹에 저장
                        //평가
                        Alarm alarm = new Alarm();
                        alarm.groupkeys = new ArrayList<String>();
                        alarm.groupkeys.add(mygroupkey);

                        for(int i = 0; i < gp.users.size(); i++){

                            myRef.child("Alarm").child(gp.users.get(i)).setValue(alarm);

                        }

                        //유저 정보들 삭제
                        for(int i = 0; i < userlist.size(); i++){

                            if(userlist.get(i).end_groups == null){        //비어이 있는 경우 만들어줌
                                userlist.get(i).end_groups = new ArrayList<String>();
                            }

                            userlist.get(i).end_groups.add(mygroupkey);        //그룹 키를 넣는다.

                            //원래 가지고 있던 그룹 키는 삭제
                            for(int j = 0; j < userlist.get(i).groups.size(); j++){         //이게 왜 1번 돌지?

                                if(userlist.get(i).groups.get(j).equals(mygroupkey)){       //키가 같은 경우

                                    userlist.get(i).groups.remove(j);     //삭제

                                }
                            }

                            myRef.child("UserInfo").child(gp.users.get(i)).setValue(userlist.get(i));    //그룹 키 변동

                        }

                        //그룹에서 삭제
                        myRef.child("Group").child(mygroupkey).setValue(null);      //그룹에서 삭제
                        //성공
                        Intent it = new Intent(Reservationgroup.this, MainActivity.class);
                        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        it.putExtra("mykey", mykey);                //자신의 고유번호를 넘겨준다.
                        it.putExtra("mynickname", nickname);        //자신의 닉네임을 넘긴다.
                        startActivity(it);

                    }else{      //혼자 도착한 경우

                        if(userInfo.end_groups == null){        //비어이 있는 경우 만들어줌

                            userInfo.end_groups = new ArrayList<String>();

                        }

                        userInfo.end_groups.add(mygroupkey);        //그룹 키를 넣는다.
                        //원래 가지고 있던 그룹 키는 삭제
                        for(int i = 0; i < userInfo.groups.size(); i++){

                            if(userInfo.groups.get(i).equals(mygroupkey)){       //키가 같은 경우

                                userInfo.groups.remove(i);     //삭제

                            }
                        }

                        //이거 순서 바뀌면 버그 터진다.(유저 정보보다 endgroup부터 해야한다.)
                        myRef.child("EndGroup").child(mygroupkey).setValue(gp);     //종료 그룹에 저장
                        myRef.child("UserInfo").child(mykey).setValue(userInfo);    //그룹 키 변동
                        myRef.child("Group").child(mygroupkey).setValue(null);      //그룹에서 삭제

                        Intent it = new Intent(Reservationgroup.this, MainActivity.class);
                        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        it.putExtra("mykey", mykey);                //자신의 고유번호를 넘겨준다.
                        it.putExtra("mynickname", nickname);        //자신의 닉네임을 넘긴다.
                        startActivity(it);

                    }

                }
            });
        }
    }
}