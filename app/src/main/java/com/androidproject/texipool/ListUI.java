package com.androidproject.texipool;

import androidx.appcompat.app.AppCompatActivity;

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
    private Button lookupButton;

    //아이디와 비밀번호가 일치할 경우 0, 아이디 실패 1, 비번 실패 2
    int trigger = 1;

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

        lookupButton = findViewById(R.id.look); //조회 버튼
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

                                    //청소하기
                                    row.clear();
                                    dataArray.clear();

                                    for(int i = 0; i < searchlist.size(); i++){
                                        MainData md2 = new MainData(searchlist.get(i).destination,Integer.toString(searchlist.get(i).users.size()),
                                                Integer.toString(searchlist.get(i).year) + "-" +
                                                        Integer.toString(searchlist.get(i).month)+ "-"
                                                        + Integer.toString(searchlist.get(i).day),
                                                Integer.toString(searchlist.get(i).start_hours) + "시 " +
                                                        Integer.toString(searchlist.get(i).start_minutes) + "분");
                                        row.add(md2.returnME());
                                        dataArray.clear();
                                    }
                                    ArrayAdapter arrayAdapter = new ArrayAdapter(ListUI.this,android.R.layout.simple_list_item_1, row);
                                    xListView = (ListView)findViewById(R.id.glist);
                                    xListView.setAdapter(arrayAdapter);



                                    break;


                                case "500":

                                case "700":

                                case "1000":

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
}
