package com.androidproject.texipool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //DB 관련
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    //나의 고유번호를 저장한다.
    private String mykey;
    private String mynickname;
    private UserInfo myinfo;

    //진짜 그룹 저장
    List<Group> reserve = new ArrayList<Group>();       //예약 그룹들
    List<Group> end = new ArrayList<Group>();           //종료 그룹들

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

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //제일 처음 유저 정보를 가져온다.
                myinfo = snapshot.child("UserInfo").child(mykey).getValue(UserInfo.class);
                if(myinfo.groups == null){
                    myinfo.groups = new ArrayList<String>();
                }
                if(myinfo.end_groups == null){          //종료 삑사리 안터지게 함.
                    myinfo.end_groups = new ArrayList<String>();
                }

                //예약 그룹부터 가져온다.
                for(int i = 0; i < myinfo.groups.size(); i++){

                    Group gp = snapshot.child("Group").child(myinfo.groups.get(i)).getValue(Group.class);
                    reserve.add(gp);

                }
                //종료 그룹부터 가져온다.
                for(int j = 0; j < myinfo.end_groups.size(); j++){

                    //end 그룹을 하나 만들기
                    Group gp = snapshot.child("EndGroup").child(myinfo.end_groups.get(j)).getValue(Group.class);
                    end.add(gp);

                }
                //정렬하기
                reserve.sort(new CompareGroup<Group>());
                end.sort(new CompareGroup<Group>());

                //예약 어뎁터 작성하기





                //종료 어뎁터 작성하기





            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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