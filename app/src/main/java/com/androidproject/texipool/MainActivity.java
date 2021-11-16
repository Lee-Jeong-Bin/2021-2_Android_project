package com.androidproject.texipool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

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

        ImageButton mypageButton = (ImageButton) findViewById(R.id.userButton);
        mypageButton.setOnClickListener(new View.OnClickListener() {            //버튼 눌리면 마이페이지로 이동
            @Override
            public void onClick(View v) {

                //첫 번째 매개변수는 자신, 두 번째는 이동
                Intent mypageIntent = new Intent(MainActivity.this, Mypage.class);
                //이거 아래 2개 해줘야 뒤로가기 버튼 눌러도 뒤로 안가진다.
                mypageIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mypageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mypageIntent.putExtra("mykey", mykey);      //자신의 고유번호를 넘겨준다.
                //시작
                startActivity(mypageIntent);


            }
        });
    }

    //택시 호출 버튼
    void texicall(){

    }

}