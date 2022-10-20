package com.gnu_graduate_project_team.junggal_v2;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ChatRoomItem> items;

    public ChatRoomAdapter(List<ChatRoomItem> items) {
        this.items = items;
    }

    /** Custom Listener **/
    public interface OnItemClickListener
    {
        void onItemClick(int pos);
    }

    static private AlarmAdapter.OnItemClickListener onItemClickListener = null;

    public void setOnItemClickListener(AlarmAdapter.OnItemClickListener listener)
    {
        this.onItemClickListener = listener;
        Log.d("create onItem listener","make success");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChatRoomHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.chat_room_activity,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatRoomVO chatRoomVO = (ChatRoomVO) items.get(position).getObject();
        ((ChatRoomHolder)holder).setChatRoomData(chatRoomVO);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }

    static class ChatRoomHolder extends RecyclerView.ViewHolder
    {
        private TextView chatRoomTitle;
        private TextView chatRoomTime;
        private TextView chatRoomMessage;
        private ChatRoomVO chatRoomVO;

        public ChatRoomHolder(View itemView) {
            super(itemView);

            chatRoomTitle = itemView.findViewById(R.id.chatRoomTitle);
            chatRoomTime = itemView.findViewById(R.id.chatRoomTime);
            chatRoomMessage = itemView.findViewById(R.id.chatRoomMessage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION)
                    {
                        if(onItemClickListener != null)
                        {
                            onItemClickListener.onItemClick(position);
                        }
                    }
                }
            });
        }

        public void setChatRoomData(ChatRoomVO chatRoom)
        {
            this.chatRoomVO = chatRoom;
            if(chatRoom.getUser_id().equals(chatRoom.getOwner_id()))
            {
                chatRoomTitle.setText(chatRoom.getClient_name());
            }
            else
            {
                chatRoomTitle.setText(chatRoom.getOwner_name());
            }
            chatRoomMessage.setText(chatRoom.getShare_post_name());
            //time은 마지막 메세지 받아서 담아주기
        }
    }

}
