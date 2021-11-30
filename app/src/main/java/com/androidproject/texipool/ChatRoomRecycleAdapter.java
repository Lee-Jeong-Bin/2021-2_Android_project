package com.androidproject.texipool;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;



public class ChatRoomRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    int i = 0;          //0이면 원래 목적지만 1이면 출발지도 나오게
    private  ArrayList<Group> groupArrayList = new ArrayList<>();

    ChatRoomRecycleAdapter(int i){

        this.i = i;

    }


    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ  어뎁터 눌렸울 경우 ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    public interface OnItemClickListener            //아이템이 눌린
    {
        void onItemClick(View v, int pos);
    }

    private OnItemClickListener mListener = null;  //클릭 리스너 변수

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }
    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 여기까지 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(i == 1) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.destination_item, parent, false);
            return new ViewHolderChatRoom1(view);


        }else{      //0일 경우가 원래

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatroomitem, parent, false);
            return new ViewHolderChatRoom(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(i == 1) {

            ((ViewHolderChatRoom1)holder).onBind(groupArrayList.get(position));


        }else{      //0일 경우가 원래

            ((ViewHolderChatRoom)holder).onBind(groupArrayList.get(position));

        }

    }

    @Override
    public int getItemCount() {     //들어간 아이템 개수 체크

        return groupArrayList.size();

    }

    void addItem(Group data) {      //아이템을 여기다가 집어넣는다.

        groupArrayList.add(data);
    }

    //아이템 넣기
    public class ViewHolderChatRoom extends RecyclerView.ViewHolder {

        private View view;

        TextView destination;
        TextView people_count;
        TextView date;
        TextView time;

        public ViewHolderChatRoom(@NonNull View itemView) {
            super(itemView);

            destination = (TextView)itemView.findViewById(R.id.destination);
            destination.setSelected(true);      //이거 넣어야 긴 문장이 흘러서 보여준다.
            people_count = (TextView)itemView.findViewById(R.id.count);
            date = (TextView)itemView.findViewById(R.id.date);
            time = (TextView)itemView.findViewById(R.id.time);

            itemView.setOnClickListener(new View.OnClickListener() {            //클릭은 여기다가 한다.
                @Override
                public void onClick(View v) {

                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION)            //눌렸을 경우
                    {
                        // click event
                        mListener.onItemClick(v, pos);
                    }

                }
            });

        }

        public void onBind(Group gp){

            destination.setText(gp.destination);
            people_count.setText(Integer.toString(gp.users.size()));
            date.setText(Integer.toString(gp.year) + "-" + Integer.toString(gp.month) + "-" + Integer.toString(gp.day));
            time.setText(Integer.toString(gp.start_hours) + "시 " + Integer.toString(gp.start_minutes) +"분");

        }

    }

    //아이템 넣기
    public class ViewHolderChatRoom1 extends RecyclerView.ViewHolder {

        private View view;

        TextView start;
        TextView destination;
        TextView people_count;
        TextView date;
        TextView time;

        public ViewHolderChatRoom1(@NonNull View itemView) {
            super(itemView);

            start = (TextView)itemView.findViewById(R.id.start);
            start.setSelected(true);      //이거 넣어야 긴 문장이 흘러서 보여준다.
            destination = (TextView)itemView.findViewById(R.id.destination);
            destination.setSelected(true);      //이거 넣어야 긴 문장이 흘러서 보여준다.
            people_count = (TextView)itemView.findViewById(R.id.count);
            date = (TextView)itemView.findViewById(R.id.date);
            time = (TextView)itemView.findViewById(R.id.time);

            itemView.setOnClickListener(new View.OnClickListener() {            //클릭은 여기다가 한다.
                @Override
                public void onClick(View v) {

                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION)            //눌렸을 경우
                    {
                        // click event
                        mListener.onItemClick(v, pos);
                    }

                }
            });

        }

        public void onBind(Group gp){

            start.setText(gp.start_address);
            destination.setText(gp.destination);
            people_count.setText(Integer.toString(gp.users.size()));
            date.setText(Integer.toString(gp.year) + "-" + Integer.toString(gp.month) + "-" + Integer.toString(gp.day));
            time.setText(Integer.toString(gp.start_hours) + "시 " + Integer.toString(gp.start_minutes) +"분");

        }

    }

}



