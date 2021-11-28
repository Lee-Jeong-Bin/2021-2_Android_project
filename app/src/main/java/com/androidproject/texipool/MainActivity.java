package com.androidproject.texipool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static MainActivity mainActivity;

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

    //리스트 뷰
    ListView reserve_listview;
    ListView end_listview;

    //행과 실제 칼럼 ArrayList
    ArrayList<ArrayList<String>> row = new ArrayList<ArrayList<String>>();
    ArrayList<String> dataArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mykey = getIntent().getStringExtra("mykey");
        mynickname = getIntent().getStringExtra("mynickname");

        //파이어 베이스 데이터베이스 연동
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        reserve_listview = (ListView)findViewById(R.id.listView1);
        end_listview = (ListView)findViewById(R.id.listView2);

        mainActivity = MainActivity.this;

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

                try {

                    //제일 처음 유저 정보를 가져온다.
                    myinfo = snapshot.child("UserInfo").child(mykey).getValue(UserInfo.class);

                    if (myinfo.groups == null) {
                        myinfo.groups = new ArrayList<String>();
                    }
                    if (myinfo.end_groups == null) {          //종료 삑사리 안터지게 함.
                        myinfo.end_groups = new ArrayList<String>();
                    }

                    //예약 그룹부터 가져온다.
                    for (int i = 0; i < myinfo.groups.size(); i++) {

                        Group gp = snapshot.child("Group").child(myinfo.groups.get(i)).getValue(Group.class);
                        gp.aid = myinfo.groups.get(i);
                        reserve.add(gp);

                    }


                    //종료 그룹부터 가져온다.
                    for (int j = 0; j < myinfo.end_groups.size(); j++) {

                        System.out.println("왜?");

                        //end 그룹을 하나 만들기
                        Group gp2 = snapshot.child("EndGroup").child(myinfo.end_groups.get(j)).getValue(Group.class);
                        gp2.aid = myinfo.end_groups.get(j);
                        end.add(gp2);

                    }

                    //정렬하기
                    reserve.sort(new CompareGroup<Group>());
                    end.sort(new CompareGroup<Group>());

                    //예약 어뎁터 작성하기

                    for (int i = 0; i < reserve.size(); i++) {            //예약 그룹 숫자만큼 반복


                        String msg = "*" + reserve.get(i).destination + "*   " + Integer.toString(reserve.get(i).users.size()) + "명   "
                                + Integer.toString(reserve.get(i).year) + "년" +
                                Integer.toString(reserve.get(i).month) + "월"
                                + Integer.toString(reserve.get(i).day) + "일 "
                                + Integer.toString(reserve.get(i).start_hours) + "시 " +
                                Integer.toString(reserve.get(i).start_minutes) + "분";


                        dataArray.add(msg);


                    }//for문 끝

                    ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, dataArray);

                    reserve_listview.setAdapter(arrayAdapter);
                    reserve_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Intent intent = new Intent(MainActivity.this, Reservationgroup.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("mykey", mykey);                //자신의 고유번호를 넘겨준다.
                            intent.putExtra("mynickname", mynickname);      //닉네임도 넘겨준다.
                            intent.putExtra("GroupKey", reserve.get(i).aid); //그룹 키값을 넘겨준다.
                            intent.putExtra("end", Integer.toString(0));
                            startActivity(intent);

                        }
                    });


                    //종료 어뎁터 작성하기

                }catch (Exception e){}
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