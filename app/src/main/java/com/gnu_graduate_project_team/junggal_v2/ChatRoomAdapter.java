package com.gnu_graduate_project_team.junggal_v2;

import android.content.Intent;
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

import de.hdodenhof.circleimageview.CircleImageView;

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
        private CircleImageView chat_room_profile_image;
        private TextView chatRoomReadCnt;

        public ChatRoomHolder(View itemView) {
            super(itemView);

            chatRoomTitle = itemView.findViewById(R.id.chatRoomTitle);
            chatRoomTime = itemView.findViewById(R.id.chatRoomTime);
            chatRoomMessage = itemView.findViewById(R.id.chatRoomMessage);
            chat_room_profile_image = itemView.findViewById(R.id.chat_room_profile_image);
            chatRoomReadCnt = itemView.findViewById(R.id.chatRoomReadCnt);

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
                if(chatRoom.getOwner_cnt()!=0)
                {
                    chatRoomReadCnt.setText(chatRoom.getOwner_cnt()+"개");
                    chatRoomReadCnt.setVisibility(View.VISIBLE);
                }
                else
                {
                    chatRoomReadCnt.setVisibility(View.INVISIBLE);
                }
            }
            else
            {
                chatRoomTitle.setText(chatRoom.getOwner_name());
                if(chatRoom.getClient_cnt()!=0)
                {
                    chatRoomReadCnt.setText(chatRoom.getClient_cnt()+"개");
                    chatRoomReadCnt.setVisibility(View.VISIBLE);
                }
                else
                {
                    chatRoomReadCnt.setVisibility(View.INVISIBLE);
                }
            }

            if(chatRoom.getBitmapFlag())
            {
                chat_room_profile_image.setImageBitmap(chatRoom.getBitmap());
            }
            chatRoomMessage.setText(chatRoom.getShare_post_name());

            //time은 마지막 메세지 받아서 담아주기
            chatRoomTime.setText(nowTime(chatRoom.getUse_time()));
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
