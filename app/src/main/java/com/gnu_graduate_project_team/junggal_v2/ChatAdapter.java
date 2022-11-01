package com.gnu_graduate_project_team.junggal_v2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ChatItem> items;

    public ChatAdapter(List<ChatItem> items)
    {
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //0==model1, 1==model2
        if(viewType ==0)
        {
            return new ChatAdapter.senderHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.activity_chat_right,parent,false));
        }
        else
        {
            return new ChatAdapter.readerHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.activity_chat_left,parent,false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(getItemViewType(position)==0)
        {
            ChatVO chatVO = (ChatVO) items.get(position).getObject();
            ((ChatAdapter.senderHolder) holder).setData(chatVO);
        }
        else
        {
            ChatVO chatVO = (ChatVO) items.get(position).getObject();
            ((ChatAdapter.readerHolder) holder).setData(chatVO);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }

    //화면1
    static class senderHolder extends RecyclerView.ViewHolder
    {

        private TextView content;
        private TextView time;
        private ChatVO chatVO;

        public senderHolder(View itemView) {
            super(itemView);

            content = itemView.findViewById(R.id.content);
            time = itemView.findViewById(R.id.time);

        }

        public void setData(ChatVO chatVO)
        {
            this.chatVO = chatVO;
            time.setText(nowTime(chatVO.getChat_time()));
            content.setText(chatVO.getContent());
        }
    }

    //화면1
    static class readerHolder extends RecyclerView.ViewHolder
    {

        private TextView content;
        private TextView name;
        private TextView time;
        private ChatVO chatVO;

        public readerHolder(View itemView) {
            super(itemView);

            content = itemView.findViewById(R.id.content);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);

        }

        public void setData(ChatVO chatVO)
        {
            this.chatVO = chatVO;
            name.setText(chatVO.getSender());
            time.setText(nowTime(chatVO.getChat_time()));
            content.setText(chatVO.getContent());
        }
    }

    /** 현재 시간 설정 함수 **/
    public static String nowTime(String dbTime)
    {
        String tmp[] = dbTime.split("T");

        long now = System.currentTimeMillis();
        Date mDate = new Date(now);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        String Time = simpleDateFormat.format(mDate);

        if(Time.equals(tmp[0]))
        {
            return tmp[1];
        }
        else
        {
            return Time;
        }
    }
}
