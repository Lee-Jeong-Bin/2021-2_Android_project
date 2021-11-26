package com.androidproject.texipool;

import java.util.List;

public class Group {

    public String aid = "";                 //고유번호
    public List<String> users = null;       //참여자 리스트
    public int limit_person = 0;            //제한 인원
    public int year=0;                      //출발 년도
    public int month = 0;                   //출발 월
    public int day = 0;                     //출발 일
    public int start_hours = 0;             //출발 시
    public int start_minutes = 0;           //출발 분
    public String start_address = "";       //출발지 명
    public String destination = "";         //도착지 명
    public int cash = 0;                    //총 금액
    public String text = "";                //소개말

    //출발 위치
    public double start_x = 0;              //경도
    public double start_y = 0;              //위도
    //도착 위치
    public double finish_x = 0;
    public double finish_y = 0;
    //채팅
    public List<ChatFormat> chat = null;

    //그룹 종료 여부
    public int end = 0;        //그룹이 진행 중이라면 0, 그룹이 종료되었다면 1

    Group(){

    }

    Group(int limit_person, int year, int month, int day, int start_hours, int start_minutes,
            String start_address, String destination, double start_x, double start_y, double
          finish_x, double finish_y,int cash, String text){

        this.limit_person = limit_person;
        this.year = year;
        this.month = month;
        this.day = day;
        this.start_hours = start_hours;
        this.start_minutes = start_minutes;
        this.start_address = start_address;
        this.destination = destination;
        this.start_x = start_x;
        this.start_y = start_y;
        this.finish_x = finish_x;
        this.finish_y = finish_y;
        this.cash = cash;
        this.text = text;

    }




}
