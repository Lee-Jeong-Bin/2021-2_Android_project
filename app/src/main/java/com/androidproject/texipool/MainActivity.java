package com.androidproject.texipool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    //DB 관련
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    //나의 고유번호를 저장한다.
    private String mykey;
    private String mynickname;
    private User myinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mykey = getIntent().getStringExtra("mykey");
        mynickname = getIntent().getStringExtra("mynickname");

        //파이어 베이스 데이터베이스 연동
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        setting();
        create();
        join();
        mypage();
        texicall();

    }

    //리스트뷰 및 기본 세팅
    void setting(){         //여기다가 서버에서 가져온 정보로 리스트뷰 작성







    }

    //그룹 생성 버튼(끝)
    void create(){              //인탠트에서 1을 넘겨주어야 한다.

        Button cb = (Button)findViewById(R.id.create);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Startmap.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("mykey", mykey);              //자신의 고유번호를 넘겨준다.
                intent.putExtra("mynickname", mynickname);    //닉네임도 넘겨준다.
                String mycase = "1";
                intent.putExtra("case", mycase);              //1을 넘김
                startActivity(intent);

            }
        });
    }

    //그룹 참가 버튼(끝)
    void join(){                //인탠트에서 2를 넘겨주어야 한다.

        Button jb = (Button)findViewById(R.id.join);
        jb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Startmap.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("mykey", mykey);              //자신의 고유번호를 넘겨준다.
                intent.putExtra("mynickname", mynickname);    //닉네임도 넘겨준다.
                String mycase = "2";
                intent.putExtra("case", mycase);              //1을 넘김
                startActivity(intent);

            }
        });
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
                mypageIntent.putExtra("mykey", mykey);              //자신의 고유번호를 넘겨준다.
                mypageIntent.putExtra("mynickname", mynickname);    //닉네임도 넘겨준다.
                //시작
                startActivity(mypageIntent);


            }
        });
    }
    //택시 호출 버튼
    void texicall(){

    }

}