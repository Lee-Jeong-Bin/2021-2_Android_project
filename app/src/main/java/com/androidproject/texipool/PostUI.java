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
import android.widget.ImageButton;
import android.widget.Spinner;

public class PostUI extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_u_i);

        ImageButton b = (ImageButton)findViewById(R.id.xbtn);
        Button b1 = (Button)findViewById(R.id.btn);

        b.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostUI.this, Startmap.class);
                startActivity(intent);
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostUI.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        //모집인원 콤보박스
        Spinner memberSpinner = (Spinner)findViewById(R.id.memnum);
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
        Spinner yearSpinner = (Spinner)findViewById(R.id.year);
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
        Spinner monthSpinner = (Spinner)findViewById(R.id.month);
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
        Spinner daySpinner = (Spinner)findViewById(R.id.day);
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
        Spinner clockSpinner = (Spinner)findViewById(R.id.clock);
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
        Spinner minSpinner = (Spinner)findViewById(R.id.minute);
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

        createGroup();
        cancle();

    }

    void createGroup(){                 //서버에 그룹을 생성한다.

        Button b1 = (Button)findViewById(R.id.btn);










    }

    void cancle(){                      //생성 취소 버튼

        ImageButton bc = (ImageButton)findViewById(R.id.xbtn);







    }

}