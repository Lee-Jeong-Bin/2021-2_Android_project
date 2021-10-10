package com.androidproject.texipool;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Server {

    public FirebaseDatabase database;
    public DatabaseReference myRef;


    Server(FirebaseDatabase fd){        //서버연결

        this.database = fd;
        myRef = database.getReference();

    }

    void getUserinfo(String aid){   //유저 정보를 가져와준다. 입력값은 유저 키값이다.
        //완성후 리턴값 변경




    }

    void getGroup(String aid){      //그룹 정보를 가져와 준다. 입력값은 그룹 키값이다.
        //완성후 리턴값 변경





    }



}
