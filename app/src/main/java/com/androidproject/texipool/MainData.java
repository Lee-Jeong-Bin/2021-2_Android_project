package com.androidproject.texipool;

import java.util.ArrayList;

public class MainData {

    String destination;
    String now_people;
    String start_day;
    String start_time;

    //그룹 리스트 조회에서는 몇개 더필요.

    ArrayList<String> ret = new ArrayList<String>();

    MainData(){

        this.destination = "목적지";
        this.now_people = "인원";
        this.start_day = "출발날짜";
        this.start_time = "출발시간";

    }

    MainData(String destination, String now_people, String start_day, String start_time){

        this.destination = destination;
        this.now_people = now_people;
        this.start_day = start_day;
        this.start_time = start_time;


    }

    ArrayList<String> returnME(){

        ret.add(destination);
        ret.add(now_people);
        ret.add(start_day);
        ret.add(start_time);

        return ret;

    }



}
