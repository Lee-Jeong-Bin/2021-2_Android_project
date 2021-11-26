package com.androidproject.texipool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    //DB 관련
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    //버튼 관련
    private Button loginButton;
    private Button signUpButton;
    //텍스트 관련
    EditText id;
    EditText passwd;

    //사용자 정보
    String sid;
    String spasswd;

    //아이디와 비밀번호가 일치할 경우 0, 아이디 실패 1, 비번 실패 2
    int trigger = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {    //main문 이라고 생각하면 편함
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //파이어 베이스 데이터베이스 연동
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        //텍스트 연결
        id = findViewById(R.id.sign_id);
        passwd = findViewById(R.id.sign_passwd);

        login();
        sign_up();

    }

    void login(){   //로그인 버튼 누르면 발생시

        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {     //데이터베이스 검색
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        //텍스트에 적힌 아이디 비번 저장
                        sid = id.getText().toString();
                        spasswd = passwd.getText().toString();

                        for(DataSnapshot ds1 : snapshot.getChildren()){
                            for(DataSnapshot ds2 : ds1.getChildren()) {       //push로 만든 키값은  이렇게 2번 해줘야 한다.

                                User user = ds2.getValue(User.class);       //검색
                                if(sid.equals(user.userid)){                //아이디가 일치할 경우
                                    if(spasswd.equals(user.userpasswd)){    //비밀번호가 일치할 경우

                                        //첫 번째 매개변수는 자신, 두 번째는 이동
                                        Intent loginIntent = new Intent(Login.this, MainActivity.class);
                                        //이거 아래 2개 해줘야 뒤로가기 버튼 눌러도 뒤로 안가진다.
                                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        loginIntent.putExtra("mykey", ds2.getKey());        //자신의 고유번호를 넘겨준다.
                                        loginIntent.putExtra("mynickname", user.nickname);  //자신의 닉네임을 넘긴다.
                                        //시작
                                        startActivity(loginIntent);


                                        trigger = 0;    //모두 일치

                                    }else{      //비밀번호가 일치하지 않을 경우

                                        passwd.setText("");
                                        passwd.setHint("비밀번호가 일치하지 않습니다.");

                                        trigger = 2;    //비번 실패

                                    }

                                }
                            }
                        }

                        if(trigger == 1){       //일치하는 아이디를 찾지 못함

                            id.setText("");
                            id.setHint("일치하는 아이디가 없습니다");
                            passwd.setText("");

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });

            }//클릭
        });


    }

    void sign_up(){     //회원가입 버튼 누르면 발생시

        signUpButton = findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //첫 번째 매개변수는 자신, 두 번째는 이동
                Intent loginIntent = new Intent(Login.this, SignUp.class);
                //이거 아래 2개 해줘야 뒤로가기 버튼 눌러도 뒤로 안가진다.
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //시작
                startActivity(loginIntent);

            }
        });

    }
}