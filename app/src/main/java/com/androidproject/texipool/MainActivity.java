package com.androidproject.texipool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    //나의 고유번호를 저장한다.
    private String mykey;
    private User myinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mykey = getIntent().getStringExtra("mykey");
        System.out.println(mykey);

        setting();
        create();
        join();
        mypage();
        texicall();

    }

    //리스트뷰 및 기본 세팅
    void setting(){

    }

    //그룹 생성 버튼
    void create(){

    }

    //그룹 참가 버튼
    void join(){

    }

    //마이 페이지 버튼
    void mypage(){

    }

    //택시 호출 버튼
    void texicall(){

    }

}