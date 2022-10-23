package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SharePostActivity extends Activity {

    /** PostVO 데이터베이스 정보 담는 변수 **/
    private SharePostVO sharePostVO;

    /** PostWriter 데이터베이스 정보 담는 변수 **/
    private UserVO userVO;

    /** xml 요소 접근 변수 **/
    private ViewPager2 slideView;
    private LinearLayout slidebar;
    private ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
    private Bitmap[] images = new Bitmap[3];

    private TextView sharePostName;
    private TextView sharePostStory;
    private TextView sharePostsharePeople;
    private TextView sharePostsharedPeople;

    private TextView sharePostUser;
    private TextView sharePostUserIntro;
    private ImageView SharePostWriterProfile;

    private ImageView backBtn;
    private TextView deletePostBtn;

    private ImageView sharePostPutInForBtn;
    private ImageView location_btn;

    private ImageView postWriterChat;

    /** Timer **/
    private String postDay;
    private String postHour;
    private String postMin;
    private String nowDay;
    private String nowHour;
    private String nowMin;
    private long shareTime = 0;
    private TextView SharePostTimer;
    private Timer timer;
    private Boolean shareflag = false;

    /** Post ID **/
    public Integer sharePostId;

    /** Latitude Longitude **/
    private String Latitude="";
    private String Longitude="";
    private Boolean geoFlag=false;

    /** Retrofit2 / ApiPostInterfae 호출 **/
    Retrofit retrofit = ApiClient.getApiClient();
    ApiPostInterface apiPostInterface = retrofit.create(ApiPostInterface.class);

    /** Thread 사용 **/
    Handler mHandler = null;

    /** 이미지 데이터 받아오기 위한 변수 **/
    private Integer imageCnt;
    private SharePostImageVO imagedata;

    /** 게시물 삭제 관련 변수 / 게시물 작성자 신청 방지 --> 사용자 계정 **/
    private String user_ID;
    private String user_Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_post_activity);

        //
        user_ID = PreferenceManager.getString(SharePostActivity.this,"user_id");
        user_Name = PreferenceManager.getString(SharePostActivity.this,"user_name");

        //Timer
        Date today = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("H");
        SimpleDateFormat sdf3 = new SimpleDateFormat("m");
        nowDay = sdf1.format(today);
        nowHour = sdf2.format(today);
        nowMin = sdf3.format(today);

        /** xml 요소 초기화 **/
        sharePostName = (TextView) findViewById(R.id.sharePostName);
        sharePostStory = (TextView) findViewById(R.id.sharePostStory);
        sharePostsharePeople = (TextView) findViewById(R.id.sharePostsharePeople);
        sharePostsharedPeople = (TextView) findViewById(R.id.sharePostsharedPeople);
        sharePostUser = (TextView) findViewById(R.id.sharePostUser);
        sharePostUserIntro = (TextView) findViewById(R.id.sharePostUserIntro);
        SharePostWriterProfile = (ImageView) findViewById(R.id.SharePostWriterProfile);
        backBtn = (ImageView) findViewById(R.id.backBtn);
        SharePostTimer = (TextView) findViewById(R.id.SharePostTimer);
        deletePostBtn = (TextView) findViewById(R.id.deletePostBtn);
        sharePostPutInForBtn = (ImageView) findViewById(R.id.sharePostPutInForBtn);
        location_btn = (ImageView)findViewById(R.id.location_btn);
        postWriterChat = (ImageView)findViewById(R.id.postWriterChat);

        /** Thread 사용 **/
        mHandler = new Handler();

        /** 반찬 PostId 값 받기 **/
        Intent intent = getIntent();
        sharePostId = intent.getIntExtra("sharePostID",0);
        MarkerVO marker = new MarkerVO();
        marker.setShare_post_id(sharePostId);

        /** 게시물 load **/
        selectPost(marker);

        /** 게시물 위치 값 받기 **/
        Latitude = intent.getStringExtra("Latitude");
        Longitude = intent.getStringExtra("Longitude");


        /** 반찬 이미지 ViewPager2 코드 **/
        slideView = findViewById(R.id.sliderViewPager);

        /** ViewPager2 아래 Indicator 코드 **/
        slidebar = findViewById(R.id.layoutIndicators);
        slideView.setOffscreenPageLimit(1);
        slideView.setAdapter(new SharePostImgAdapter(SharePostActivity.this, bitmapArrayList));

        /** 뒤로가기 버튼 리스너 **/
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /** 나눔 신청 버튼 리스너 **/
        sharePostPutInForBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(user_ID.equals(sharePostVO.getUser_id()))
                {
                    Toast.makeText(SharePostActivity.this, "작성자는 나눔 신청이 불가능 합니다.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if((sharePostVO.getShare_people() - sharePostVO.getShared_people()) == 0) {
                        Toast.makeText(SharePostActivity.this, "나눔 신청이 종료되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else if(shareflag==false)
                    {
                        Intent putInForIntent = new Intent(SharePostActivity.this,SharePostPutInForActivity.class);
                        putInForIntent.putExtra("remainTime", SharePostTimer.getText().toString().trim());
                        putInForIntent.putExtra("remainPeople",sharePostsharePeople.getText().toString().trim());
                        putInForIntent.putExtra("sharePostId",sharePostVO.getShare_post_id());
                        putInForIntent.putExtra("sharePostName",sharePostVO.getShare_post_name());
                        putInForIntent.putExtra("sharePostWriter",sharePostVO.getUser_id());
                        startActivity(putInForIntent);
                    }
                    else
                    {
                        Toast.makeText(SharePostActivity.this, "나눔이 종료되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        /** 나눔 위치 길 안내 버튼 리스너 **/
        location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(geoFlag)
                {
                    Intent intent = new Intent( SharePostActivity.this, NaverDirection5Activity.class);
                    intent.putExtra("latitude",Double.parseDouble(Latitude));
                    intent.putExtra("longitude",Double.parseDouble(Longitude));
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(SharePostActivity.this, "게시물 로딩중 입니다. 다시 눌러주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        /** 게시물 작성자와 Chatting 하기 **/
        postWriterChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(user_ID.equals(sharePostVO.getUser_id()))
                {
                    Toast.makeText(SharePostActivity.this, "본인과 채팅은 불가능 합니다.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent chatIntent = new Intent(SharePostActivity.this, ChatActivity.class);
                    chatIntent.putExtra("sharePostId",sharePostVO.getShare_post_id());
                    chatIntent.putExtra("sharePostName",sharePostVO.getShare_post_name());
                    chatIntent.putExtra("sharePostWriter",sharePostVO.getUser_id());
                    chatIntent.putExtra("sharePostWriterName",userVO.getName());
                    chatIntent.putExtra("client",user_ID);
                    chatIntent.putExtra("clientName",user_Name);
                    startActivity(chatIntent);
                }

            }
        });

    }

    /** Indicator 세팅 함수 **/
    private void setupIndicators(int count) {

        if(count==1)
            return;

        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.setMargins(12, 8, 12, 8);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.bg_indicator_inactive));
            indicators[i].setLayoutParams(params);
            slidebar.addView(indicators[i]);
        }
        setCurrentIndicator(0);
    }

    private void setCurrentIndicator(int position) {
        int childCount = slidebar.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) slidebar.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_active
                ));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_inactive
                ));
            }
        }
    }

    /** 게시글 서버에서 Select **/
    public void selectPost(MarkerVO marker)
    {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        Call<SharePostVO> call = apiPostInterface.selectPost(marker);
                        call.enqueue(new Callback<SharePostVO>() {
                            @Override
                            public void onResponse(Call<SharePostVO> call, Response<SharePostVO> response) {
                                sharePostVO = response.body();
                                if(sharePostVO.getShare_post_id()==null)
                                {
                                    Toast.makeText(SharePostActivity.this, "삭제된 게시물 입니다.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else
                                {
                                    imageCnt = sharePostVO.getShare_post_img_cnt();

                                    calcTimer();
                                    UserVOGet(sharePostVO.getUser_id());
                                    ImageGet();
                                    settingUI();
                                    if(Latitude==null || Longitude==null)
                                    {
                                        selectGeoPoint();
                                    }
                                    else
                                    {
                                        geoFlag = true;
                                    }

                                    /** 반찬 나눔 종료 버튼 리스너 **/
                                    deletePostBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            AlertDialog.Builder builder = new AlertDialog.Builder(SharePostActivity.this);
                                            builder.setTitle("반찬 나눔 종료").setMessage("반찬 나눔을 종료하시겠습니까?");
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                                                @Override
                                                public void onClick(DialogInterface dialog, int id)
                                                {
                                                    if(user_ID.equals(sharePostVO.getUser_id()))
                                                    {
                                                        deletePost();
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(SharePostActivity.this, "게시글 작성자가 아닙니다.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                                                @Override
                                                public void onClick(DialogInterface dialog, int id)
                                                {

                                                }
                                            });


                                            AlertDialog alertDialog = builder.create();
                                            alertDialog.show();
                                        }
                                    });
                                }

                            }

                            @Override
                            public void onFailure(Call<SharePostVO> call, Throwable t) {
                                Toast.makeText(SharePostActivity.this, "게시물 로드 실패\n네트워크 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        t.start();
    }

    /** Timer 연산 **/
    public void calcTimer()
    {
        String timetmp = sharePostVO.getShare_time();
        String[] strtmp =timetmp.split("T")[0].split("-");
        String[] strtmp2 = timetmp.split("T")[1].split(":");
        postDay = strtmp[2];
        postHour = strtmp2[0];
        postMin = strtmp2[1];

        int checkday = Integer.parseInt(postDay) - Integer.parseInt(nowDay);
        if(checkday==1)
        {
            shareTime += 86400000;
        }

        checkday = Integer.parseInt(postHour) - Integer.parseInt(nowHour);
        shareTime += checkday * 3600000;

        checkday = Integer.parseInt(postMin) - Integer.parseInt(nowMin);
        shareTime += checkday * 60000;

        timer = new Timer(shareTime, 60000);
        timer.start();


    }

    /** 게시글 UI에 적용 **/
    public void settingUI()
    {
        sharePostName.setText("< "+sharePostVO.getShare_post_name()+" >");
        sharePostStory.setText(sharePostVO.getShare_story());
        //나눔 가능 인원 수 : 00명
        String ment = "나눔 가능 인원 수 : ";
        if(sharePostVO.getShare_people()-sharePostVO.getShared_people()<10)
        {
            ment += "0"+(sharePostVO.getShare_people() - sharePostVO.getShared_people());
        }
        else
        {
            ment += (sharePostVO.getShare_people() - sharePostVO.getShared_people());
        }
        sharePostsharePeople.setText(ment);
        //0 / 2
        ment = sharePostVO.getShared_people() +" / " + sharePostVO.getShare_people();
        sharePostsharedPeople.setText(ment);


    }
    
    /** 게시글 작성자 서버에서 Select **/
    public void UserVOGet(String userId)
    {
        UserVO tmp = new UserVO();
        tmp.setId(userId);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        Call<UserVO> call = apiPostInterface.selectWriter(tmp);
                        call.enqueue(new Callback<UserVO>() {
                            @Override
                            public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                                userVO = response.body();

                                sharePostUser.setText(userVO.getName());
                                sharePostUserIntro.setText(userVO.getIntroduce());
                                if(userVO.getProfile_flag()==true)
                                {
                                    byte[] tmp = Base64.decode(userVO.getImagedata(),Base64.DEFAULT);
                                    Bitmap bittmp = BitmapFactory.decodeByteArray(tmp,0,tmp.length);
                                    SharePostWriterProfile.setImageBitmap(bittmp);
                                }


                            }

                            @Override
                            public void onFailure(Call<UserVO> call, Throwable t) {
                                Toast.makeText(SharePostActivity.this, "작성자 정보 로드 실패\n네트워크 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                });
            }
        });

        t.start();
    }

    /** 게시글 이미지 서버에서 Select **/
    public void ImageGet()
    {
        for(int i=1; i<=imageCnt; i++)
        {
            sharePostVO.setImgNumber(i);
            Call<SharePostImageVO> callimg = apiPostInterface.getPostImg(sharePostVO);
            int cnt = i-1;
            callimg.enqueue(new Callback<SharePostImageVO>() {
                @Override
                public void onResponse(Call<SharePostImageVO> call, Response<SharePostImageVO> response) {
                    imagedata = response.body();

                    byte[] tmp = Base64.decode(imagedata.getImagedata(),Base64.DEFAULT);
                    Bitmap bittmp = BitmapFactory.decodeByteArray(tmp,0,tmp.length);
                    bitmapArrayList.add(bittmp);

                    slideView.getAdapter().notifyItemChanged(cnt);


                }

                @Override
                public void onFailure(Call<SharePostImageVO> call, Throwable t) {
                    Toast.makeText(SharePostActivity.this, "게시물 이미지 로드 실패\n네트워크 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("imageload", t.toString());
                }
            });
        }



        /** 화면 이동시 listner **/
        slideView.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });

        setupIndicators(sharePostVO.getShare_post_img_cnt());
    }

    /** 반찬 나눔 종료 **/
    public void deletePost()
    {
        Call<SharePostVO> call = apiPostInterface.deletPost(sharePostVO);
        call.enqueue(new Callback<SharePostVO>() {
            @Override
            public void onResponse(Call<SharePostVO> call, Response<SharePostVO> response) {
                Toast.makeText(SharePostActivity.this, "나눔을 종료합니다.", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<SharePostVO> call, Throwable t) {
                Log.d("fail reason", t.toString());
                Toast.makeText(SharePostActivity.this, "나눔 게시물 삭제 실패\n네트워크 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /** 게시물 좌표값 가져오기 **/
    public void selectGeoPoint()
    {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        SharePostVO tmpVo = new SharePostVO();
                        tmpVo.setShare_post_id(sharePostId);
                        Call<Point> call = apiPostInterface.selectGeoPoint(tmpVo);
                        call.enqueue(new Callback<Point>() {
                            @Override
                            public void onResponse(Call<Point> call, Response<Point> response) {
                                Point point = response.body();
                                Latitude = point.getLatitude();
                                Longitude = point.getLongitude();
                                Log.d("Latitude", Latitude);
                                Log.d("Longitude", Longitude);
                                geoFlag = true;
                            }

                            @Override
                            public void onFailure(Call<Point> call, Throwable t) {
                                Toast.makeText(SharePostActivity.this, "네트워크 상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }

                });
            }
        });

        t.start();
    }

    /** Timer Class **/
    class Timer extends CountDownTimer
    {

        //남은 시간 : 00시간 00분
        String text = "남은 시간 :  ";

        public Timer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            SharePostTimer.setText(text + l/3600000+"시간 " + (l%3600000)/60000 +"분");
        }

        @Override
        public void onFinish() {
            SharePostTimer.setText("나눔이 종료되었습니다.");
            shareflag = true;
        }
    }




}
