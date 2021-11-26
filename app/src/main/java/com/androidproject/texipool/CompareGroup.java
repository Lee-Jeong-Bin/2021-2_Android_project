package com.androidproject.texipool;

import java.time.LocalTime;
import java.util.Comparator;

public class CompareGroup <T extends Group> implements Comparator<T>{
    //Arraylist에서 sort함수를 쓰기 위한 클래스이다.

    @Override
    public int compare(T o1, T o2) {
        //양수이면 o2 즉, 자기 자신이 더 크다.
        //음수이면 o1 즉, 비교치가 더 크다.

        //변환
        String time1 = Integer.toString(o1.year);
        int month = o1.month;
        if (month < 10){
            time1 = time1 + '0' + Integer.toString(month);
        }else{
            time1 = time1 + Integer.toString(month);
        }
        int day = o1.day;
        if (day < 10){
            time1 = time1 + '0' + Integer.toString(day);
        }else{
            time1 = time1 + Integer.toString(day);
        }

        //시분 저장장
        int hour = o1.start_hours;
        if (hour < 10){
            time1 = time1 + '0' + Integer.toString(hour);
        }else{
            time1 = time1 + Integer.toString(hour);
        }
        int minute = o1.start_minutes;
        if (minute < 10){
            time1 = time1 + '0' + Integer.toString(minute);
        }else{
            time1 = time1 + Integer.toString(minute);
        }
        long l1 = Long.parseLong(time1);


        //변환
        String time2 = Integer.toString(o2.year);
        int month2 = o2.month;
        if (month2 < 10){
            time2 = time2 + '0' + Integer.toString(month2);
        }else{
            time2 = time2 + Integer.toString(month2);
        }
        int day2 = o2.day;
        if (day2 < 10){
            time2 = time2 + '0' + Integer.toString(day2);
        }else{
            time2 = time2 + Integer.toString(day2);
        }

        //시분 저장장
        int hour2 = o2.start_hours;
        if (hour2 < 10){
            time2 = time2 + '0' + Integer.toString(hour2);
        }else{
            time2 = time2 + Integer.toString(hour2);
        }
        int minute2 = o2.start_minutes;
        if (minute2 < 10){
            time2 = time2 + '0' + Integer.toString(minute2);
        }else{
            time2 = time2 + Integer.toString(minute2);
        }
        long l2 = Long.parseLong(time2);

        System.out.println(l1);
        System.out.println(l2);


        return (l1 < l2 ? -1 : (l1 == l2) ? 0 : 1);
    }
}
