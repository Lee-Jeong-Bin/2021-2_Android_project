package com.androidproject.texipool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Rating extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    //기본으로 인탠트에서 가져오는 것
    private String mykey;
    private String mynickname;
    private String groupkey;

    //동승자
    ArrayList<String> pp = new ArrayList<String>();
    ArrayList<UserInfo> partners = new ArrayList<UserInfo>();
    ArrayList<User> uss = new ArrayList<User>();

    ImageView[] iv = new ImageView[3];
    TextView[] names = new TextView[3];
    RatingBar[] stars = new RatingBar[3];

    Alarm al;

    Button b1;
    Button b2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_page);

        mykey = getIntent().getStringExtra("mykey");
        mynickname = getIntent().getStringExtra("mynickname");
        groupkey = getIntent().getStringExtra("GroupKey");

        //파이어 베이스 데이터베이스 연동
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        iv[0] = (ImageView)findViewById(R.id.iv1);
        iv[1] = (ImageView)findViewById(R.id.iv2);
        iv[2] = (ImageView)findViewById(R.id.iv3);

        names[0] = (TextView)findViewById(R.id.nick1);
        names[1] = (TextView)findViewById(R.id.nick2);
        names[2] = (TextView)findViewById(R.id.nick3);

        stars[0] = (RatingBar)findViewById(R.id.star1);
        stars[1] = (RatingBar)findViewById(R.id.star2);
        stars[2] = (RatingBar)findViewById(R.id.star3);

        b1 = (Button)findViewById(R.id.five_button);
        b2 = (Button)findViewById(R.id.est_button);


        Setting();


    }

    private  void Setting(){

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Group gp = snapshot.child("EndGroup").child(groupkey).getValue(Group.class);        //클래스 가져옴
                al = snapshot.child("Alarm").child(mykey).getValue(Alarm.class);

                for(int i = 0; i < gp.users.size(); i++){       //키 저장

                    if(!mykey.equals(gp.users.get(i).toString())){

                        pp.add(gp.users.get(i));

                    }

                }

                //갖은 키로 이제 사람을 구한다.
                for(int i = 0; i < pp.size(); i++){

                    UserInfo userInfo = snapshot.child("UserInfo").child(pp.get(i)).getValue(UserInfo.class);   //저장됨
                    User ui = snapshot.child("User").child(pp.get(i)).getValue(User.class);
                    partners.add(userInfo);
                    uss.add(ui);

                }

                for(int i = 0; i < partners.size(); i++){

                    names[i].setText( uss.get(i).nickname + "님");

                    if(partners.get(i).img != null){       //기본 자기 프로필 사진이 있다면 보여준다.

                        Bitmap bmp;
                        byte[] bytes = UserInfo.binaryStringToByteArray(partners.get(i).img);
                        bmp = BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
                        iv[i].setImageBitmap(bmp);
                        iv[i].setClipToOutline(true);              //모양에 맞게 사진 자르기

                    }

                }

                //안보이게 하기
                for(int i = pp.size(); i < 3; i++){       //없는 것은 안보이게 하기

                    iv[i].setVisibility(View.INVISIBLE);
                    stars[i].setVisibility(View.INVISIBLE);
                    names[i].setVisibility(View.INVISIBLE);

                }

                //여기까지 성공





                five();
                check();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void five(){        //모두 5점

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < partners.size(); i++){

                    //System.out.println(stars[i].getRating()); 값 잘들어온다.

                    if(partners.get(i).stars == null){

                        partners.get(i).stars = new ArrayList<Double>();

                    }

                    partners.get(i).stars.add(5.0);
                    myRef.child("UserInfo").child(pp.get(i)).setValue(partners.get(i));     //저장

                }
                //키값을 빼준다.
                for(int i = 0; i < al.groupkeys.size(); i++){
                    if(groupkey.equals(al.groupkeys.get(i))){
                        al.groupkeys.remove(i);
                    }
                }
                myRef.child("Alarm").child(mykey).setValue(al);     //저장
                Intent escapeIntent = new Intent(Rating.this, MainActivity.class);
                escapeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                escapeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                escapeIntent.putExtra("mykey", mykey);
                escapeIntent.putExtra("mynickname", mynickname);
                startActivity(escapeIntent);







            }
        });






    }

    void check(){       //개개인

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < partners.size(); i++){

                    //System.out.println(stars[i].getRating()); 값 잘들어온다.

                    if(partners.get(i).stars == null){

                        partners.get(i).stars = new ArrayList<Double>();

                    }

                    partners.get(i).stars.add((double)stars[i].getRating());
                    System.out.println((double)stars[i].getRating());
                    myRef.child("UserInfo").child(pp.get(i)).setValue(partners.get(i));     //저장

                }
                //키값을 빼준다.

                for(int i = 0; i < al.groupkeys.size(); i++){
                    if(groupkey.equals(al.groupkeys.get(i))){
                        al.groupkeys.remove(i);
                    }
                }
                myRef.child("Alarm").child(mykey).setValue(al);     //저장
                Intent escapeIntent = new Intent(Rating.this, MainActivity.class);
                escapeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                escapeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                escapeIntent.putExtra("mykey", mykey);
                escapeIntent.putExtra("mynickname", mynickname);
                startActivity(escapeIntent);



            }
        });

    }





}