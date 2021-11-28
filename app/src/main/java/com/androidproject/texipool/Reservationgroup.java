package com.androidproject.texipool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Reservationgroup extends AppCompatActivity {

    //DB 관련
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    //나의 고유번호를 저장한다.
    private String mykey;
    private String nickname;
    private String mygroupkey;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservationgroup);

        mykey = getIntent().getStringExtra("mykey");
        nickname = getIntent().getStringExtra("mynickname");
        mygroupkey = getIntent().getStringExtra("GroupKey");

        //파이어 베이스 데이터베이스 연동
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();



        setting();
        cancle();

    }

    void setting(){






    }

    void cancle(){

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


}