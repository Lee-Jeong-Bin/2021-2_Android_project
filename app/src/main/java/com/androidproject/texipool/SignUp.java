package com.androidproject.texipool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.InputMismatchException;

public class SignUp extends AppCompatActivity {

    private User user;
    //서버 관련
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    //버튼 관련
    private Button registerButton;
    private Button cancleButton;

    //입력칸
    EditText id;
    EditText passwd;
    EditText nickname;
    EditText phonenum;

    //입력정보
    String sid = null;
    String spasswd = null;
    String snickname = null;
    String sphonenum = null;
    long iphonenum = 0;

    //중복값 판별
    /*
           0 = 중복이 발생하지 않음
           1 = 아이디가 중복 발생
           2 = 닉네임이 중복 발생
           3 = 전화번호 중복 발생

     */
    int detect = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        //서버 연결
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        //텍스트 연결
        id = findViewById(R.id.sign_id);
        passwd = findViewById(R.id.sign_passwd);
        nickname = findViewById(R.id.sign_nickname);
        phonenum = findViewById(R.id.sign_phone);

        register();
        cancle();

    }

    void register(){    //회원가입 버튼

        registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{

                    //텍스트에 적힌 정보를 가져온다.
                    sid = id.getText().toString();
                    spasswd = passwd.getText().toString();
                    snickname = nickname.getText().toString();
                    sphonenum = phonenum.getText().toString();
                    if(sphonenum.length() != 11) {    //11자리가 안맞는 경우

                        //예외발생
                        System.out.println("11자리가 안 맞음");
                        PhoneException ep = new PhoneException("발생");
                        throw ep;

                    }
                    iphonenum = Long.parseLong(phonenum.getText().toString());
                    if(iphonenum == 0){     //숫자 외에 문자를 입력하였을 경우

                        //예외발생
                        System.out.println("숫자 외에 문자 입력");
                        PhoneException ep = new PhoneException("발생");
                        throw ep;

                    }

                    //아이디 비번 닉네임이 공백일 경우 발생
                    if(sid.equals("") || spasswd.equals("") || snickname.equals("")){

                        Exception e = new Exception();
                        throw e;

                    }

                    //여기까지 정보가 전부 저장됨
                    detect = 0;
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            //중복 데이터부터 검색
                            for(DataSnapshot ds1 : snapshot.getChildren()){
                                for(DataSnapshot ds2 : ds1.getChildren()){       //push로 만든 키값은  이렇게 2번 해줘야 한다.

                                    User checkUser = ds2.getValue(User.class);
                                    if(sid.equals(checkUser.userid)){
                                        id.setText("");
                                        id.setHint("이미 있는 아이디입니다.");
                                        detect = 1;
                                        break;
                                    }
                                    if(snickname.equals(checkUser.nickname)){
                                        System.out.println("안들어옴?");
                                        nickname.setText("");
                                        nickname.setHint("이미 있는 닉네임입니다.");
                                        detect = 2;
                                        break;
                                    }

                                    if(sphonenum.equals(checkUser.phone_num)){
                                        phonenum.setText("");
                                        phonenum.setHint("이미 가입한 전화번호입니다.");
                                        detect = 3;
                                        break;
                                    }




                                }
                                if(detect != 0)break;
                            }//검사 끝

                            if(detect == 0){//중복이 없음으로 서버에 저장

                                user = new User(sid, spasswd, snickname, sphonenum);        //유저 정보 인스턴스
                                myRef.child("User").push().setValue(user);                  //서버에 저장

                                Intent registIntent = new Intent(SignUp.this, Login.class);
                                //이거 아래 2개 해줘야 뒤로가기 버튼 눌러도 뒤로 안가진다.
                                registIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                registIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                //시작
                                startActivity(registIntent);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });




                }catch(PhoneException phoneevent) {        //전화번호가 11자리가 안 맞을 경우

                    phonenum.setText("");
                    phonenum.setHint(" - 없이 11자리로 다시 입력하여 주세요");

                }catch (Exception e){       //에러 발생 다시 입력

                    id.setText("");
                    passwd.setText("");
                    nickname.setText("");
                    phonenum.setText("");

                    id.setHint("아이디");
                    passwd.setHint("비밀번호");
                    nickname.setHint("닉네임");
                    phonenum.setHint("전화번호(숫자만 입력하시오)");

                }

            }
        });
    }

    void cancle(){  //취소 버튼

        cancleButton = findViewById(R.id.cancle_button);
        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //첫 번째 매개변수는 자신, 두 번째는 이동
                Intent cancleIntent = new Intent(SignUp.this, Login.class);
                //이거 아래 2개 해줘야 뒤로가기 버튼 눌러도 뒤로 안가진다.
                cancleIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                cancleIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //시작
                startActivity(cancleIntent);

            }
        });

    }



}