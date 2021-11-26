package com.androidproject.texipool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PostUI extends AppCompatActivity {

    //DB 관련
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    EditText et;
    UserInfo myinfo;

    //인탠트로 받는 값
    private String mykey;
    private String nickname;
    private double start_x;         //출발 경도
    private double start_y;         //출발 위도
    private double finish_x;        //도착 경도
    private double finish_y;        //도착 위도
    private String start_name = "";      //출발지 이름
    private String finish_name = "";     //도착지 이름
    private int cash = 0;                //요금

    //스피너만 따로
    Spinner memberSpinner;          //모집인원
    Spinner yearSpinner;            //년도
    Spinner monthSpinner;           //월
    Spinner daySpinner;             //일
    Spinner clockSpinner;           //시
    Spinner minSpinner;             //분




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_u_i);

        //파이어 베이스 데이터베이스 연동
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();


        //인탠트 정보값 받기
        mykey = getIntent().getStringExtra("mykey");
        nickname = getIntent().getStringExtra("mynickname");


        /*    인탠트로 아직 출발 도착 위도 경도 총 4개,  출 목 주소, 요금을 못 받음      */


        //서버
        myRef.child("UserInfo").child(mykey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                myinfo = snapshot.getValue(UserInfo.class);
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });     //기본으로 나의 정보를 가져온다.


        //버튼코딩
        ImageButton b = (ImageButton)findViewById(R.id.xbtn);
        Button b1 = (Button)findViewById(R.id.btn);
        et = (EditText)findViewById(R.id.introduce_text);

        b.setOnClickListener(new View.OnClickListener(){                //메인 화면으로 나가기(끝)

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostUI.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("mykey", mykey);                //자신의 고유번호를 넘겨준다.
                intent.putExtra("mynickname", nickname);        //자신의 닉네임을 넘긴다.
                startActivity(intent);
            }
        });



        b1.setOnClickListener(new View.OnClickListener() {              //서버에 저장으로 나가기
            @Override
            public void onClick(View v) {

                //서버에 그룹을 저장한다.
                String now = Time.nowTime();    //성공

                Time gpTime = new Time(Integer.parseInt(yearSpinner.getSelectedItem().toString()),
                        Integer.parseInt(monthSpinner.getSelectedItem().toString()),
                        Integer.parseInt(daySpinner.getSelectedItem().toString()),
                        Integer.parseInt(clockSpinner.getSelectedItem().toString()),
                        Integer.parseInt(minSpinner.getSelectedItem().toString()));

                System.out.println(Long.parseLong(now));
                System.out.println(Long.parseLong(gpTime.translateTime()));

                if(Long.parseLong(now) > Long.parseLong(gpTime.translateTime())){       //과거를 예약할 순 없다.

                    System.out.println("과거예약");

                }else {

                    //스피너에서 시간을 저장
                    Group gp = new Group(Integer.parseInt(memberSpinner.getSelectedItem().toString()),
                            gpTime.year, gpTime.month, gpTime.day, gpTime.hour, gpTime.minute,
                            start_name, finish_name, start_x, start_y, finish_x, finish_y,
                            0, et.getText().toString());

                    gp.users = new ArrayList<String>();
                    gp.users.add(mykey);

                    //그룹을 이제 서버에 저장
                    String mx = myRef.child("Group").push().getKey();               //랜덤 키 생성 후 저장
                    myRef.child("Group").child(mx).setValue(gp);                     //키 값에 데이터 입력 후 서버 저장
                    if (myinfo.groups == null) {                                            //생성자에 그룹에 넣어준다.

                        myinfo.groups = new ArrayList<String>();
                        myinfo.groups.add(mx);

                    } else {
                        myinfo.groups.add(mx);
                    }
                    myRef.child("UserInfo").child(mykey).setValue(myinfo);           //서버에 저장

                    //성공하였다면 메인 화면으로 나간다.
                    Intent intent = new Intent(PostUI.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("mykey", mykey);                //자신의 고유번호를 넘겨준다.
                    intent.putExtra("mynickname", nickname);        //자신의 닉네임을 넘긴다.
                    startActivity(intent);
                }
            }
        });




        //여기가 정보 가져오는 부분
        //모집인원 콤보박스
        memberSpinner = (Spinner)findViewById(R.id.memnum);
        ArrayAdapter memAdapter = ArrayAdapter.createFromResource(this,R.array.member_number, android.R.layout.simple_spinner_dropdown_item);
        //기본 스피너 레이아웃을 사용해서 어레이어댑더 만듦, R.array.member_number는  arrays.xml에 있는 array name
        memAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //선택목록이 나타날 때 사용할 레이아웃 지정
        memberSpinner.setAdapter(memAdapter);  //스피너에 어댑터 적용

        memberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String member = memberSpinner.getSelectedItem().toString(); // 스피너 선택값 가져오기
                Log.d("모집인원",member); //확인
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //년 콤보박스
        yearSpinner = (Spinner)findViewById(R.id.year);
        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(this,R.array.year, android.R.layout.simple_spinner_dropdown_item);
        //기본 스피너 레이아웃을 사용해서 어레이어댑더 만듦, R.array.year는  arrays.xml에 있는 array name
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //선택목록이 나타날 때 사용할 레이아웃 지정
        yearSpinner.setAdapter(yearAdapter); //스피너에 어댑터 적용
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String year = yearSpinner.getSelectedItem().toString();
                Log.d("탑승년도",year);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //월 콤보박스
        monthSpinner = (Spinner)findViewById(R.id.month);
        ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(this,R.array.date_month, android.R.layout.simple_spinner_dropdown_item);
        //기본 스피너 레이아웃을 사용해서 어레이어댑더 만듦, R.array.date_month는  arrays.xml에 있는 array name
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //선택목록이 나타날 때 사용할 레이아웃 지정
        monthSpinner.setAdapter(monthAdapter); //스피너에 어댑터 적용
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String month = monthSpinner.getSelectedItem().toString();
                Log.d("탑승 월",month);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        //일 콤보박스
        daySpinner = (Spinner)findViewById(R.id.day);
        ArrayAdapter dayAdapter = ArrayAdapter.createFromResource(this,R.array.date_day, android.R.layout.simple_spinner_dropdown_item);
        //기본 스피너 레이아웃을 사용해서 어레이어댑더 만듦, R.array.date_day는  arrays.xml에 있는 array name
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //선택목록이 나타날 때 사용할 레이아웃 지정
        daySpinner.setAdapter(dayAdapter); //스피너에 어댑터 적용

        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String day = daySpinner.getSelectedItem().toString();
                Log.d("탑승 일",day);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //시 콤보박스
        clockSpinner = (Spinner)findViewById(R.id.clock);
        ArrayAdapter clockAdapter = ArrayAdapter.createFromResource(this,R.array.time_clock, android.R.layout.simple_spinner_dropdown_item);
        //기본 스피너 레이아웃을 사용해서 어레이어댑더 만듦, R.array.time_clock는  arrays.xml에 있는 array name
        clockAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //선택목록이 나타날 때 사용할 레이아웃 지정
        clockSpinner.setAdapter(clockAdapter); //스피너에 어댑터 적용
        clockSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String clock = clockSpinner.getSelectedItem().toString();
                Log.d("탑승 시",clock);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //분 콤보박스
        minSpinner = (Spinner)findViewById(R.id.minute);
        ArrayAdapter minAdapter = ArrayAdapter.createFromResource(this,R.array.time_minute, android.R.layout.simple_spinner_dropdown_item);
        //기본 스피너 레이아웃을 사용해서 어레이어댑더 만듦, R.array.time_minute는  arrays.xml에 있는 array name
        minAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //선택목록이 나타날 때 사용할 레이아웃 지정
        minSpinner.setAdapter(minAdapter); //스피너에 어댑터 적용
        minSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String min = minSpinner.getSelectedItem().toString();
                Log.d("탑승 분",min);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



    }

}