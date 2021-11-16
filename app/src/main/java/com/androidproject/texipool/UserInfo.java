package com.androidproject.texipool;

import android.webkit.MimeTypeMap;

import java.util.List;

public class UserInfo {

    //회원 계정에 관련되 클래스
    public int count = 0;                       //그룹에 참가한 횟수
    public int fail_count = 0;                  //그룹 탈퇴 횟수
    public List stars = null;                   //평점을 전부 저장
    public List estimate_people = null;         //평점을 부여한 사람 저장
    public String img = null;                     //이진 파일로 실제 이미지가 여기 들어 있다.

    public List groups = null;                  //참가한 그룹 리스트
    public List end_groups = null;              //종료된 그룹 리스트

    UserInfo(){

    }


    //이미지 관련 메서드들
    //바이너리 바이트 배열을 스트링 변환
    public static String byteArrayToBinaryString(byte[] b) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < b.length; ++i) {

            sb.append(byteToBinaryString(b[i]));

        }
        return sb.toString();

    }

    // 바이너리 바이트를 스트링으로
    public static String byteToBinaryString(byte n) {

        StringBuilder sb = new StringBuilder("00000000");

        for (int bit = 0; bit < 8; bit++) {

            if (((n >> bit) & 1) > 0) {

                sb.setCharAt(7 - bit, '1');
            }
        }
        return sb.toString();
    }

    //이제 다시 사진으로 바꿔주는 것
    // 스트링을 바이너리 바이트 배열로
    public static byte[] binaryStringToByteArray(String s) {
        int count = s.length() / 8;
        byte[] b = new byte[count];
        for (int i = 1; i < count; ++i) {
            String t = s.substring((i - 1) * 8, i * 8);
            b[i - 1] = binaryStringToByte(t);
        }
        return b;
    }

    // 스트링을 바이너리 바이트로
    public static byte binaryStringToByte(String s) {
        byte ret = 0, total = 0;
        for (int i = 0; i < 8; ++i) {
            ret = (s.charAt(7 - i) == '1') ? (byte) (1 << i) : 0; total = (byte) (ret | total);
        }
        return total;
    }



}
