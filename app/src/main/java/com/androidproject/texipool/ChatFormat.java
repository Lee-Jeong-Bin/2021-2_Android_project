package com.androidproject.texipool;

public class ChatFormat {

    public String nickname;         //닉네임
    public String chatText;         //실제 채팅 내용
    public String img;              //이미지

    ChatFormat(){

    }

    ChatFormat(String nickname, String chatText, String img){

        this.nickname = nickname;
        this.chatText = chatText;
        this.img = img;

    }

    ChatFormat(String nickname, String chatText){

        this.nickname = nickname;
        this.chatText = chatText;

    }

}
