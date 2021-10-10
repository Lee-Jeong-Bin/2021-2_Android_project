package com.androidproject.texipool;

import java.util.List;

public class Group {

    public String aid = "";        //고유번호
    public List users = null;      //참여자 리스트
    public int start_hours = 0;    //출발 시
    public int start_minutes = 0;  //출발 분
    //출발 위치
    public float start_x = 0;
    public float start_y = 0;
    //도착 위치
    public float finish_x = 0;
    public float finish_y = 0;
    //채팅
    public List chat = null;

    //
    public int end = 0;        //그룹이 진행 중이라면 0, 그룹이 종료되었다면 1


}
