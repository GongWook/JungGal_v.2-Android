package com.gnu_graduate_project_team.junggal_v2;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;



public class AlarmAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AlarmItem> items;
    public AlarmAdapter(List<AlarmItem> items) {
        this.items = items;
    }

    /** Custom Listener **/
    public interface OnItemClickListener
    {
        void onItemClick(int pos);
    }

    static private OnItemClickListener onItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.onItemClickListener = listener;
        Log.d("create onItem listener","make success");
    }

    /** Custom Click Listener 2 -> sharePost 이동 Listener **/
    public interface OnItemClickMoveListner
    {
        void onItemClick(int pos);
    }

    static private OnItemClickMoveListner OnItemClickMoveListner = null;

    public void setOnItemClickMoveListner(OnItemClickMoveListner listener)
    {
        this.OnItemClickMoveListner = listener;
        Log.d("create Move listener","make success");
    }

    /** Custom Click Listener 3 -> review 페이지로 이동 Listener **/
    public interface  OnItemClickMoveToReviewListner
    {
        void onItemClick(int pos);
    }

    static private OnItemClickMoveToReviewListner onItemClickMoveToReviewListner = null;

    public void setOnItemClickMoveToReviewListner(OnItemClickMoveToReviewListner listner)
    {
        this.onItemClickMoveToReviewListner = listner;
        Log.d("create Review listener","make success");
    }



    /** 화면 설정 **/
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //0==model1, 1==model2, 2==model3, 3==model4
        if(viewType == 0)
        {
            return new acceptAlarm(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.alarm_request_activity1,parent,false));
        }
        else if(viewType == 1)
        {
            return new Alarm(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.alarm_request_activity2,parent,false));
        }
        else if(viewType==2)
        {
            return new responseAlarm(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.alarm_response_activity,parent,false));
        }
        else
        {
            return new reviewAlarm(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.alarm_response_activity2,parent,false));
        }

    }

    /** 데이터 설정 **/
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(getItemViewType(position)==0)
        {
            AlarmVO AlarmVO = (AlarmVO) items.get(position).getObject();
            ((acceptAlarm) holder).setAcceptAlarmData(AlarmVO);

        }
        else if(getItemViewType(position)==1)
        {
            AlarmVO AlarmVO = (AlarmVO) items.get(position).getObject();
            ((Alarm) holder).setAlarmData(AlarmVO);
        }
        else if(getItemViewType(position)==2)
        {
            AlarmVO AlarmVO = (AlarmVO) items.get(position).getObject();
            ((responseAlarm) holder).setAlarmData(AlarmVO);
        }
        else
        {
            AlarmVO AlarmVO = (AlarmVO) items.get(position).getObject();
            ((reviewAlarm) holder).setAlarmData(AlarmVO);
        }

    }


    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //화면 1 ( 수락 / 거절 요청 화면 )
    public class acceptAlarm extends RecyclerView.ViewHolder
    {
        /** 레트로 핏 **/
        Retrofit retrofit = ApiClient.getApiClient();
        ApiFcmInterface apiFcmInterface = retrofit.create(ApiFcmInterface.class);

        /** 알람 리스너 등록 **/

        private TextView title;
        private TextView message;
        private TextView time;
        private Button acceptBtn;
        private Button denyBtn;
        private AlarmVO alarmVO;

        public acceptAlarm(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.AcceptAlarmTitle);
            message = itemView.findViewById(R.id.AcceptAlarmMessage);
            time = itemView.findViewById(R.id.AcceptAlarmTime);
            acceptBtn = itemView.findViewById(R.id.AlarmAcceptBtn);
            denyBtn = itemView.findViewById(R.id.AlarmDenyBtn);

            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Call<AlarmVO> call = apiFcmInterface.requestAlarmAccept(alarmVO);
                    call.enqueue(new Callback<AlarmVO>() {
                        @Override
                        public void onResponse(Call<AlarmVO> call, Response<AlarmVO> response) {
                            int position = getAdapterPosition();
                            if(position != RecyclerView.NO_POSITION)
                            {
                                if(onItemClickListener!=null)
                                {
                                    onItemClickListener.onItemClick(position);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<AlarmVO> call, Throwable t) {
                            Log.d("accept request test","failed");
                        }
                    });
                }
            });
            
            denyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Call<AlarmVO> call = apiFcmInterface.requestAlarmDeny(alarmVO);
                    call.enqueue(new Callback<AlarmVO>() {
                        @Override
                        public void onResponse(Call<AlarmVO> call, Response<AlarmVO> response) {
                            int position = getAdapterPosition();
                            if(position != RecyclerView.NO_POSITION)
                            {
                                if(onItemClickListener!=null)
                                {
                                    onItemClickListener.onItemClick(position);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<AlarmVO> call, Throwable t) {
                            Log.d("deny request test","failed");
                        }
                    });

                }
            });

            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION)
                    {
                        if(OnItemClickMoveListner!=null)
                        {
                            OnItemClickMoveListner.onItemClick(position);
                        }
                    }
                }
            });

            message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION)
                    {
                        if(OnItemClickMoveListner!=null)
                        {
                            OnItemClickMoveListner.onItemClick(position);
                        }
                    }
                }
            });


        }

        public void setAcceptAlarmData(AlarmVO AlarmVO)
        {
            this.alarmVO = AlarmVO;
            message.setText(AlarmVO.getMessage());
            time.setText(AlarmVO.calcTime());

        }

    }

    //화면 2 ( 수락 / 거절 요청 화면 )
    public class Alarm extends RecyclerView.ViewHolder
    {

        /** 레트로 핏 **/
        Retrofit retrofit = ApiClient.getApiClient();
        ApiFcmInterface apiFcmInterface = retrofit.create(ApiFcmInterface.class);

        private TextView title;
        private TextView message;
        private TextView time;
        private AlarmVO alarmVO;
        private TextView alarmDelete;
        private Button share_sucess_btn;

        public Alarm(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.requestAlarmTitle);
            message = itemView.findViewById(R.id.requestAlarmMessage);
            time = itemView.findViewById(R.id.requestAlarmTime);
            alarmDelete = itemView.findViewById(R.id.alarm_delete1);
            share_sucess_btn = itemView.findViewById(R.id.share_sucess_btn);

            alarmDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Call<AlarmVO> call = apiFcmInterface.delete_alarm(alarmVO);
                    call.enqueue(new Callback<AlarmVO>() {
                        @Override
                        public void onResponse(Call<AlarmVO> call, Response<AlarmVO> response) {
                            int position = getAdapterPosition();
                            if(position != RecyclerView.NO_POSITION)
                            {
                                if(onItemClickListener!=null)
                                {
                                    onItemClickListener.onItemClick(position);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<AlarmVO> call, Throwable t) {
                            Log.d("Alarm Delete","failed");
                        }
                    });

                }
            });

            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION)
                    {
                        if(OnItemClickMoveListner!=null)
                        {
                            OnItemClickMoveListner.onItemClick(position);
                        }
                    }
                }
            });

            message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION)
                    {
                        if(OnItemClickMoveListner!=null)
                        {
                            OnItemClickMoveListner.onItemClick(position);
                        }
                    }
                }
            });

            /** 나눔 완료 버튼 클릭 이벤트 리스너 **/
            share_sucess_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Call<AlarmVO> call = apiFcmInterface.successShare(alarmVO);
                    call.enqueue(new Callback<AlarmVO>() {
                        @Override
                        public void onResponse(Call<AlarmVO> call, Response<AlarmVO> response) {
                            share_sucess_btn.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onFailure(Call<AlarmVO> call, Throwable t) {

                        }
                    });
                }
            });

        }

        public void setAlarmData(AlarmVO AlarmVO)
        {
            this.alarmVO = AlarmVO;
            message.setText(AlarmVO.getMessage());
            time.setText(AlarmVO.calcTime());
            if(alarmVO.getAcceptFlag()==false || alarmVO.getReviewFlag()!=null && alarmVO.getReviewFlag()==true)
            {
                share_sucess_btn.setVisibility(View.INVISIBLE);
            }
        }
    }

    //화면 3 ( 수락 / 거절 요청 화면 )
    public class responseAlarm extends RecyclerView.ViewHolder
    {

        /** 레트로 핏 **/
        Retrofit retrofit = ApiClient.getApiClient();
        ApiFcmInterface apiFcmInterface = retrofit.create(ApiFcmInterface.class);

        private TextView title;
        private TextView message;
        private TextView time;
        private ImageView postWriter;
        private AlarmVO alarmVO;
        private TextView alarmDelete;

        public responseAlarm(View itemView) {
            super(itemView);

            postWriter = itemView.findViewById(R.id.response_alarm_icon);
            title = itemView.findViewById(R.id.responseAlarmTitle);
            message = itemView.findViewById(R.id.responseAlarmMessage);
            time = itemView.findViewById(R.id.responseAlarmTime);
            alarmDelete = itemView.findViewById(R.id.alarm_delete2);

            alarmDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Call<AlarmVO> call = apiFcmInterface.delete_alarm(alarmVO);
                    call.enqueue(new Callback<AlarmVO>() {
                        @Override
                        public void onResponse(Call<AlarmVO> call, Response<AlarmVO> response) {
                            int position = getAdapterPosition();
                            if(position != RecyclerView.NO_POSITION)
                            {
                                if(onItemClickListener!=null)
                                {
                                    onItemClickListener.onItemClick(position);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<AlarmVO> call, Throwable t) {
                            Log.d("Alarm Delete","failed");
                        }
                    });

                }
            });

            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION)
                    {
                        if(OnItemClickMoveListner!=null)
                        {
                            OnItemClickMoveListner.onItemClick(position);
                        }
                    }
                }
            });

            message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION)
                    {
                        if(OnItemClickMoveListner!=null)
                        {
                            OnItemClickMoveListner.onItemClick(position);
                        }
                    }
                }
            });

        }

        public void setAlarmData(AlarmVO AlarmVO)
        {
            this.alarmVO = AlarmVO;
            if(alarmVO.getAcceptFlag() == true)
            {
                message.setText(AlarmVO.getMessage());
                time.setText(AlarmVO.calcTime());
            }
            else
            {
                title.setText("나눔 신청 실패");
                postWriter.setImageResource(R.drawable.close_custom);
                message.setText(AlarmVO.getMessage());
                time.setText(AlarmVO.calcTime());
            }

        }
    }

    //화면 4 ( 수락 / 거절 요청 화면 )
    public class reviewAlarm extends RecyclerView.ViewHolder
    {

        /** 레트로 핏 **/
        Retrofit retrofit = ApiClient.getApiClient();
        ApiFcmInterface apiFcmInterface = retrofit.create(ApiFcmInterface.class);

        private TextView title;
        private TextView message;
        private TextView time;
        private AlarmVO alarmVO;
        private TextView alarmDelete;
        private Button review_btn;

        public reviewAlarm(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.reviewAlarmTitle);
            message = itemView.findViewById(R.id.reviewAlarmMessage);
            time = itemView.findViewById(R.id.reviewAlarmTime);
            alarmDelete = itemView.findViewById(R.id.reveiw_alarm_delete);
            review_btn = itemView.findViewById(R.id.review_btn);

            alarmDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Call<AlarmVO> call = apiFcmInterface.delete_alarm(alarmVO);
                    call.enqueue(new Callback<AlarmVO>() {
                        @Override
                        public void onResponse(Call<AlarmVO> call, Response<AlarmVO> response) {
                            int position = getAdapterPosition();
                            if(position != RecyclerView.NO_POSITION)
                            {
                                if(onItemClickListener!=null)
                                {
                                    onItemClickListener.onItemClick(position);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<AlarmVO> call, Throwable t) {
                            Log.d("Alarm Delete","failed");
                        }
                    });

                }
            });

            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION)
                    {
                        if(OnItemClickMoveListner!=null)
                        {
                            OnItemClickMoveListner.onItemClick(position);
                        }
                    }
                }
            });

            message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION)
                    {
                        if(OnItemClickMoveListner!=null)
                        {
                            OnItemClickMoveListner.onItemClick(position);
                        }
                    }
                }
            });

            review_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION)
                    {
                        if(onItemClickMoveToReviewListner!=null)
                        {
                            onItemClickMoveToReviewListner.onItemClick(position);
                        }
                    }
                }
            });

        }

        public void setAlarmData(AlarmVO AlarmVO)
        {
            this.alarmVO = AlarmVO;
            message.setText(AlarmVO.getMessage());
            time.setText(AlarmVO.calcTime());
            if(alarmVO.getReviewFlag()!=null && alarmVO.getReviewFlag()==true && alarmVO.getRequestFlag()!=null && alarmVO.getRequestFlag()==true)
            {
                review_btn.setEnabled(false);
                review_btn.setText("작성 완료");
            }
        }
    }

    /** 현재 시간 설정 함수 **/
    public String nowTime()
    {
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH : mm");
        String Time = simpleDateFormat.format(mDate);

        return Time;
    }

}
