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


    }

    void  getMyinfo(){  //나의 회원정보를 가져옴.



    }

}