package com.androidproject.texipool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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

    ImageView iv;

    private RecyclerView lv;
    public ChatRoomRecycleAdapter adapter;

    private RecyclerView lv2;
    public ChatRoomRecycleAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mykey = getIntent().getStringExtra("mykey");
        mynickname = getIntent().getStringExtra("mynickname");

        //파이어 베이스 데이터베이스 연동
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        lv = (RecyclerView)findViewById(R.id.listView1);
        lv2 = (RecyclerView) findViewById(R.id.listView2);
        iv = (ImageView) findViewById(R.id.userButton);

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

                    reserve = new ArrayList<Group>();
                    end = new ArrayList<>();

                    //제일 처음 유저 정보를 가져온다.
                    myinfo = snapshot.child("UserInfo").child(mykey).getValue(UserInfo.class);

                    if (myinfo.groups == null) {
                        myinfo.groups = new ArrayList<String>();
                    }
                    if (myinfo.end_groups == null) {          //종료 삑사리 안터지게 함.
                        myinfo.end_groups = new ArrayList<String>();
                    }

                    if(myinfo.img != null){       //기본 자기 프로필 사진이 있다면 보여준다.

                        Bitmap bmp;
                        byte[] bytes = UserInfo.binaryStringToByteArray(myinfo.img);
                        bmp = BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
                        iv.setImageBitmap(bmp);
                        iv.setClipToOutline(true);              //모양에 맞게 사진 자르기

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

                    init();
                    init2();




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

        TextView cb = (TextView) findViewById(R.id.create);
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

        TextView jb = (TextView) findViewById(R.id.join);
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

        iv.setOnClickListener(new View.OnClickListener() {            //버튼 눌리면 마이페이지로 이동
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



    private void init() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        lv.setLayoutManager(linearLayoutManager);
        lv.addItemDecoration(new DividerItemDecoration(this, 1));

        reserve.sort(new CompareGroup<Group>());
        ArrayList<String> groups_name = new ArrayList<String>();
        for(int i = 0; i < reserve.size(); i++){

            groups_name.add(reserve.get(i).aid);

        }

        adapter = new ChatRoomRecycleAdapter(1);
        adapter.setOnItemClickListener(new ChatRoomRecycleAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View v, int pos)
            {
                // 실행 내용
                System.out.println(groups_name.get(pos));

                //이제 여기다가 채팅방 이동을 넣으면 된다.
                Intent chatIntent = new Intent(MainActivity.this, Reservationgroup.class);
                chatIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                chatIntent.putExtra("mykey",mykey);                       //자신의 키를 넘긴다.
                chatIntent.putExtra("GroupKey",groups_name.get(pos));      //그룹의 키를 넘긴다.
                chatIntent.putExtra("mynickname", mynickname);              //자신의 닉네임을 넘긴다.
                chatIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                chatIntent.putExtra("end", Integer.toString(0));
                startActivity(chatIntent);

            }
        });

        for (int i = 0; i < reserve.size(); i++) {      //이거 돌려야 들어간다.

            adapter.addItem(reserve.get(i));          //이거 해줘야 순서대로 다 들어간다.

        }
        lv.setAdapter(adapter);


    }

    private void init2() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        lv2.setLayoutManager(linearLayoutManager);
        lv2.addItemDecoration(new DividerItemDecoration(this, 1));

        end.sort(new CompareGroup<Group>());
        ArrayList<String> groups_name2 = new ArrayList<String>();
        for(int i = 0; i < end.size(); i++){

            groups_name2.add(end.get(i).aid);

        }

        adapter2 = new ChatRoomRecycleAdapter(1);
        adapter2.setOnItemClickListener(new ChatRoomRecycleAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View v, int pos)
            {
                // 실행 내용
                System.out.println(groups_name2.get(pos));

                //이제 여기다가 채팅방 이동을 넣으면 된다.
                Intent chatIntent = new Intent(MainActivity.this, Reservationgroup.class);
                chatIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                chatIntent.putExtra("mykey",mykey);                       //자신의 키를 넘긴다.
                chatIntent.putExtra("GroupKey",groups_name2.get(pos));      //그룹의 키를 넘긴다.
                chatIntent.putExtra("mynickname", mynickname);              //자신의 닉네임을 넘긴다.
                chatIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                chatIntent.putExtra("end", Integer.toString(1));
                startActivity(chatIntent);

            }
        });

        for (int i = 0; i < end.size(); i++) {      //이거 돌려야 들어간다.

            adapter2.addItem(end.get(i));          //이거 해줘야 순서대로 다 들어간다.

        }
        lv2.setAdapter(adapter2);


    }









}