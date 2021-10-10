package com.androidproject.texipool;

import java.util.List;

public class User {     //회원정보

    public String userid = null;           //아이디
    public String userpasswd = null;       //비번
    public String nickname = null;         //별명
    public String phone_num = null;        //전화번호

    public User(){

    }

    public User(String id, String passwd, String nickname, String phone_num){     //회원가입에서 사용됨

        userid = id;
        userpasswd = passwd;
        this.nickname = nickname;
        this.phone_num = phone_num;

    }


}