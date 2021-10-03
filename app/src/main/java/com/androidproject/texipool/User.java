package com.androidproject.texipool;

import java.util.List;

public class User {     //회원정보

    public String aid = null;              //고유번호(이건 서버에서 자동적으로 정해준다.)
    public String userid = null;           //아이디
    public String userpasswd = null;       //비번
    public String nickname = null;         //별명
    public String phone_num = null;             //전화번호
    public int fail = 0;                   //나중에 취소율 구하기 -> ()/(end_groups의 인덱스 + 1)
    public List stars = null;              //별점 구하기 -> 나중에 평점 구할 때 (배열 모든 값 더한 수)/(인덱스+1)
    public List groups = null;             //참가한 그룹 리스트
    public List end_groups = null;         //종료된 그룹 리스트

    public User(){

    }

    public User(String id, String passwd, String nickname, String phone_num){     //회원가입에서 사용됨

        userid = id;
        userpasswd = passwd;
        this.nickname = nickname;
        this.phone_num = phone_num;

    }


}