package com.androidproject.texipool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<ChatFormat> cf = new ArrayList<>();
    public String mynickname;




    ChatAdapter(String mynickname){         //이거 있어야 자기자신인지 아닌지 구분이 가능하다.

        this.mynickname = mynickname;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ViewHolderChat(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {       //여기만 하면 끝난다.

        //여기서 배치 다르게 한다.(왼쪽 정렬은 동승자들, 오른쪽 정렬은 자기 자신)
        if(cf.get(position).nickname.equals(mynickname)){           //자기 자신일 경우

            ((ViewHolderChat)holder).onBind(cf.get(position));
            ((ViewHolderChat) holder).ltotal.setGravity(Gravity.RIGHT);
            ((ViewHolderChat) holder).ltext.setGravity(Gravity.RIGHT);
            ((ViewHolderChat) holder).luser.setVisibility(View.GONE);


        }else{                                                      //동승자일 경우우

            ((ViewHolderChat)holder).onBind(cf.get(position));

        }




    }

    @Override
    public int getItemCount() {
        return cf.size();
    }

    void addItem(ChatFormat data) {      //아이템을 여기다가 집어넣는다.

        cf.add(data);
    }


    //아이템 넣기
    public class ViewHolderChat extends RecyclerView.ViewHolder {

        private View view;

        TextView real_chat;
        TextView name;
        ImageView iv;

        LinearLayout ltotal;
        LinearLayout luser;
        LinearLayout ltext;
        LinearLayout lempty;

        public ViewHolderChat(@NonNull View itemView) {                     //여기까지 완성
            super(itemView);

            real_chat = (TextView)itemView.findViewById(R.id.real_text);
            iv = (ImageView)itemView.findViewById(R.id.userimg);
            name = (TextView)itemView.findViewById(R.id.name);
            ltotal = (LinearLayout)itemView.findViewById(R.id.total_layout);
            luser = (LinearLayout) itemView.findViewById(R.id.user_layout);
            ltext = (LinearLayout) itemView.findViewById(R.id.text_layout);


        }

        public void onBind(ChatFormat cf){       //들어가는 형식 맞혀주기

            if(cf.img != null){         //정상적으로 프로필 사진도 있는 경우

                real_chat.setText("  "+cf.chatText+"  ");
                name.setText(cf.nickname);
                //이제 여기다가 이미지 넣으면 된다.
                Bitmap bmp;
                byte[] bytes = UserInfo.binaryStringToByteArray(cf.img);
                bmp = BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
                iv.setImageBitmap(bmp);
                iv.setClipToOutline(true);              //모양에 맞게 사진 자르기


            }else{                      //프로필 사진이 없는 경우

                real_chat.setText("  "+cf.chatText+"  ");
                name.setText(cf.nickname);

            }

        }

    }

}
