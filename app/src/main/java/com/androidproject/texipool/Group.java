package com.androidproject.texipool;

import java.util.List;

public class Group {

    String aid = "";        //고유번호
    String title = "";      //제목
    List users = null;      //참여자 리스트
    int start_hours = 0;    //출발 시
    int start_minutes = 0;  //출발 분
    //출발 위치
    float start_x = 0;
    float start_y = 0;
    //도착 위치
    float finish_x = 0;
    float finish_y = 0;
    //채팅
    List chat = null;


}
