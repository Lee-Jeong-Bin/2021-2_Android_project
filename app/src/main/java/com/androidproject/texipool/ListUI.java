package com.androidproject.texipool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class ListUI extends AppCompatActivity {

    //나의 고유번호를 저장한다.
    private String mykey;
    private String nickname;

    //DB
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private ArrayList<Group> searchlist = new ArrayList<Group>();
    private TextView lookupButton;

    //리스트
    private RecyclerView lv;
    public ChatRoomRecycleAdapter adapter;

    //스피너 관련
    Spinner distance;
    String dis;

    ArrayList<Group> fulllist;

    //행과 실제 칼럼 ArrayList
    ArrayList<ArrayList<String>> row = new ArrayList<ArrayList<String>>();
    ArrayList<String> dataArray = new ArrayList<String>();

    //리스트뷰
    ListView xListView;

    private double startx;              //위도
    private double starty;              //경도



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_u_i);

        //파이어 베이스 데이터베이스 연동
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        distance = findViewById(R.id.distance);

        //인탠트로 받아온다.
        mykey = getIntent().getStringExtra("mykey");
        nickname = getIntent().getStringExtra("mynickname");
        startx = Double.parseDouble(getIntent().getStringExtra("startX"));
        starty = Double.parseDouble(getIntent().getStringExtra("startY"));

        lv = (RecyclerView)findViewById(R.id.glist);


        movemain();
        combobox();
        lookup();





    }
    void movemain(){  // 뒤로가기 클릭시 메인으로 이동
        ImageButton b = (ImageButton)findViewById(R.id.xbtn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ListUI.this, MainActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                it.putExtra("mykey", mykey);                //자신의 고유번호를 넘겨준다.
                it.putExtra("mynickname", nickname);        //자신의 닉네임을 넘긴다.
                startActivity(it);
            }
        });
    }


    void combobox(){
        //거리 콤보박스
        Spinner disSpinner = (Spinner)findViewById(R.id.distance);
        ArrayAdapter disAdapter = ArrayAdapter.createFromResource(this,R.array.distance, android.R.layout.simple_spinner_dropdown_item);
        disAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        disSpinner.setAdapter(disAdapter);

        disSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dis = disSpinner.getSelectedItem().toString(); // 스피너 선택값 가져오기
                Log.d("거리",dis); //확인
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    void lookup(){   //조회 버튼 누르면 발생시
        dis = distance.getSelectedItem().toString();

        lookupButton = (TextView)findViewById(R.id.look); //조회 버튼
        lookupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    myRef.child("Group").addValueEventListener(new ValueEventListener() {     //데이터베이스 검색 (이 줄때문에 조회버튼 누르면 앱이 종료됨.)
                        @Override
                        public void onDataChange(DataSnapshot snapshot) { //데이터베이스 상황 저장
                            fulllist = new ArrayList<Group>(); // Group에 대한 fulllist 배열 생성
                            searchlist = new ArrayList<Group>();
                            for(DataSnapshot ds1 : snapshot.getChildren()){

                                Group gp = ds1.getValue(Group.class); // 그룹 클래스의 정보들을 객체에 저장
                                gp.aid = ds1.getKey();
                                fulllist.add(gp); // 정보들을 fulllist 배열에 저장
                            }

                            //조회를 바탕으로 출력한다.
                            StringTokenizer st = new StringTokenizer(dis,"M");
                            dis = st.nextToken();

                            switch (dis){           //완성

                                case "300": for(int i = 0; i < fulllist.size(); i++){

                                    if (fulllist.get(i).start_x < startx + 0.003 && fulllist.get(i).start_x > startx - 0.003) {  // 출발지 위도가 출발지 위도+-300M 이고

                                        System.out.println("들어옴?");

                                        if (fulllist.get(i).start_y < starty + 0.003 && fulllist.get(i).start_y > starty - 0.003) { //출발지 경도가 출발지 경도+-300M 이면
                                            //잘 들어옴
                                            searchlist.add(fulllist.get(i));     //저장 완료
                                        }
                                    }
                                }
                                init();
                                break;


                                case "500": for(int i = 0; i < fulllist.size(); i++){

                                    if (fulllist.get(i).start_x < startx + 0.005 && fulllist.get(i).start_x > startx - 0.005) {  // 출발지 위도가 출발지 위도+-300M 이고

                                        System.out.println("들어옴?");

                                        if (fulllist.get(i).start_y < starty + 0.005 && fulllist.get(i).start_y > starty - 0.005) { //출발지 경도가 출발지 경도+-300M 이면
                                            //잘 들어옴
                                            searchlist.add(fulllist.get(i));     //저장 완료
                                        }
                                    }
                                }
                                    init();
                                    break;

                                case "700": for(int i = 0; i < fulllist.size(); i++){

                                    if (fulllist.get(i).start_x < startx + 0.007 && fulllist.get(i).start_x > startx - 0.007) {  // 출발지 위도가 출발지 위도+-300M 이고

                                        System.out.println("들어옴?");

                                        if (fulllist.get(i).start_y < starty + 0.007 && fulllist.get(i).start_y > starty - 0.007) { //출발지 경도가 출발지 경도+-300M 이면
                                            //잘 들어옴
                                            searchlist.add(fulllist.get(i));     //저장 완료
                                        }
                                    }
                                }
                                    init();
                                    break;

                                case "1000": for(int i = 0; i < fulllist.size(); i++){

                                    if (fulllist.get(i).start_x < startx + 0.01 && fulllist.get(i).start_x > startx - 0.01) {  // 출발지 위도가 출발지 위도+-300M 이고

                                        System.out.println("들어옴?");

                                        if (fulllist.get(i).start_y < starty + 0.01 && fulllist.get(i).start_y > starty - 0.01) { //출발지 경도가 출발지 경도+-300M 이면
                                            //잘 들어옴
                                            searchlist.add(fulllist.get(i));     //저장 완료
                                        }
                                    }
                                }
                                    init();
                                    break;

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });
                } catch(Exception e){}
            }
        });
    }

    private void init() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        lv.setLayoutManager(linearLayoutManager);
        lv.addItemDecoration(new DividerItemDecoration(this, 1));

        for(int i = 0; i < searchlist.size(); i++){

            if(searchlist.get(i).limit_person + 1 == searchlist.get(i).users.size()){       //인원이 다 찬 경우

                searchlist.remove(i);       //목록에서 삭제

            }
        }

        searchlist.sort(new CompareGroup<Group>());
        ArrayList<String> groups_name = new ArrayList<String>();
        for(int i = 0; i < searchlist.size(); i++){

            groups_name.add(searchlist.get(i).aid);

        }

        adapter = new ChatRoomRecycleAdapter();
        adapter.setOnItemClickListener(new ChatRoomRecycleAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View v, int pos)
            {
                // 실행 내용
                System.out.println(groups_name.get(pos));

                //이제 여기다가 채팅방 이동을 넣으면 된다.
                Intent chatIntent = new Intent(ListUI.this, Participategroup.class);
                chatIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                chatIntent.putExtra("mykey",mykey);                       //자신의 키를 넘긴다.
                chatIntent.putExtra("mygroup",groups_name.get(pos));      //그룹의 키를 넘긴다.
                chatIntent.putExtra("mynickname", nickname);              //자신의 닉네임을 넘긴다.
                chatIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(chatIntent);

            }
        });
        for (int i = 0; i < searchlist.size(); i++) {      //이거 돌려야 들어간다.

            adapter.addItem(searchlist.get(i));          //이거 해줘야 순서대로 다 들어간다.

        }
        lv.setAdapter(adapter);


    }
}
