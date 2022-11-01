package com.gnu_graduate_project_team.junggal_v2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naver.maps.map.overlay.OverlayImage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SearchItem> items;

    public SearchAdapter(List<SearchItem> items) {
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
        if(viewType==0)
        {
            return  new SearchAdapter.nullHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_activity2, parent, false));
        }
        else
        {
            return new SearchAdapter.SearchHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_activity, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==0)
        {

        }
        else
        {
            SharePostVO sharePostVO = (SharePostVO) items.get(position).getObject();
            ((SearchHolder)holder).setData(sharePostVO);
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

    static class nullHolder extends RecyclerView.ViewHolder
    {

        public nullHolder(View itemView) {
            super(itemView);
        }
    }

    static class SearchHolder extends RecyclerView.ViewHolder
    {

        private SharePostVO sharePostVO;
        private TextView searchTitle;
        private TextView searchContent;
        private TextView searchTime;
        private ImageView searchMarker;
        private Integer imageFlag;

        public SearchHolder(View itemView) {
            super(itemView);

            searchTitle = itemView.findViewById(R.id.searchTitle);
            searchContent = itemView.findViewById(R.id.searchContent);
            searchTime = itemView.findViewById(R.id.searchTime);
            searchMarker = itemView.findViewById(R.id.searchMarker);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position!= RecyclerView.NO_POSITION)
                    {
                        if(onItemClickListener!=null)
                        {
                            onItemClickListener.onItemClick(position);
                        }
                    }
                }
            });
        }

        public void setData(SharePostVO sharePostVO)
        {
            this.sharePostVO = sharePostVO;

            /** 게시글 내용이 길 시에 **/
            String tmp = sharePostVO.getShare_story();
            if(tmp.length()>30)
            {
                tmp = tmp.substring(0,30)+"...";
            }

            /** 마커 이미지 등록 **/
            imageFlag = sharePostVO.getShare_post_icon();

            switch(imageFlag)
            {
                case 1:
                    searchMarker.setImageResource(R.drawable.korean_food_custom);
                    break;
                case 2:
                    searchMarker.setImageResource(R.drawable.chinese_food_custom);
                    break;
                case 3:
                    searchMarker.setImageResource(R.drawable.japanese_food_custom);
                    break;
                case 4:
                    searchMarker.setImageResource(R.drawable.western_food_custom);
                    break;
                case 5:
                    searchMarker.setImageResource(R.drawable.meat_food_custom);
                    break;
                case 6:
                    searchMarker.setImageResource(R.drawable.seafood_custom);
                    break;
                case 7:
                    searchMarker.setImageResource(R.drawable.healthy_food_custom);
                    break;
                case 8:
                    searchMarker.setImageResource(R.drawable.snack_food_custom);
                    break;
                case 9:
                    searchMarker.setImageResource(R.drawable.food_ingredients_custom);
                    break;

            }

            searchTitle.setText("<"+sharePostVO.getShare_post_name()+">");
            searchContent.setText(tmp);
            searchTime.setText(calcTime(sharePostVO.getShare_time()));


        }

        /** 게시물 남은 시간 연산 **/
        public String calcTime(String share_time)
        {

            /** 현재 시간 **/
            long now = System.currentTimeMillis();
            Date mDate = new Date(now);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd HH:mm");
            String Time = simpleDateFormat.format(mDate);


            String[] share_time_split = share_time.split("T");
            String share_time_day = share_time_split[0].split("-")[2];
            String share_time_hour = share_time_split[1].split(":")[0];
            String share_time_min = share_time_split[1].split(":")[1];

            String[] now_time_split = Time.split(" ");
            String now_time_day = now_time_split[0];
            String now_time_hour = now_time_split[1].split(":")[0];
            String now_time_min = now_time_split[1].split(":")[1];

            Integer remain_day = Integer.parseInt(share_time_day) - Integer.parseInt(now_time_day);
            Integer remain_hour = 0;
            Integer remain_min = 0;

            if(remain_day==1)
            {
                remain_hour=24;
                remain_hour+= Integer.parseInt(share_time_hour) - Integer.parseInt(now_time_hour);
            }
            else
            {
                remain_hour=0;
                remain_hour+= Integer.parseInt(share_time_hour) - Integer.parseInt(now_time_hour);
            }

            remain_min = Integer.parseInt(share_time_min) - Integer.parseInt(now_time_min);

            if(remain_min<0)
            {
                remain_hour--;
                remain_min+=60;
            }

            String ment = "남은 시간 : "+remain_hour+"시간 "+remain_min+"분";

            return ment;


        }
    }


}
