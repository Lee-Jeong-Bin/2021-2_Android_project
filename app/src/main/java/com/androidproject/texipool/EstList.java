package com.androidproject.texipool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EstList extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    //기본으로 인탠트에서 가져오는 것
    private String mykey;
    private String mynickname;
    private ArrayList<Group> end = new ArrayList<Group>();            //이걸로 넘겨준다.

    private RecyclerView lv2;
    public ChatRoomRecycleAdapter adapter2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evaluation);

        mykey = getIntent().getStringExtra("mykey");
        mynickname = getIntent().getStringExtra("mynickname");

        lv2 = (RecyclerView)findViewById(R.id.lv22);

        //파이어 베이스 데이터베이스 연동
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();


        setting();
        cancle();

    }


    private void setting(){

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try{

                    Alarm al = snapshot.child("Alarm").child(mykey).getValue(Alarm.class);
                    System.out.println( al.groupkeys.size() + "알람 들어옴");

                    if(al.groupkeys != null){       //1개라도 있을 경우
                        for(int i = 0; i < al.groupkeys.size() ; i++){

                            Group gp = snapshot.child("EndGroup").child(al.groupkeys.get(i)).getValue(Group.class);
                            gp.aid = snapshot.child("EndGroup").child(al.groupkeys.get(i)).getKey();
                            end.add(gp);


                        }

                        init2();

                    }

                }catch (Exception e){}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void cancle(){

        ImageButton estimate_button = (ImageButton)findViewById(R.id.move_main);
        estimate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent escapeIntent = new Intent(EstList.this, MainActivity.class);
                escapeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                escapeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                escapeIntent.putExtra("mykey", mykey);
                escapeIntent.putExtra("mynickname", mynickname);
                startActivity(escapeIntent);

            }
        });


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

                //이제 여기다가 채팅방 이동을 넣으면 된다.
                Intent chatIntent = new Intent(EstList.this, Rating.class);
                chatIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                chatIntent.putExtra("mykey",mykey);                       //자신의 키를 넘긴다.
                chatIntent.putExtra("GroupKey",groups_name2.get(pos));      //그룹의 키를 넘긴다.
                chatIntent.putExtra("mynickname", mynickname);              //자신의 닉네임을 넘긴다.
                chatIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(chatIntent);

            }
        });

        for (int i = 0; i < end.size(); i++) {      //이거 돌려야 들어간다.

            adapter2.addItem(end.get(i));          //이거 해줘야 순서대로 다 들어간다.

        }
        lv2.setAdapter(adapter2);


    }

}