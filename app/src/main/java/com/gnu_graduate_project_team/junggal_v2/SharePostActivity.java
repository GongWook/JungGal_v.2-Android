package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

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


    /** Retrofit2 / ApiPostInterfae 호출 **/
    Retrofit retrofit = ApiClient.getApiClient();
    ApiPostInterface apiPostInterface = retrofit.create(ApiPostInterface.class);

    /** Thread 사용 **/
    Handler mHandler = null;

    /** 이미지 데이터 받아오기 위한 변수 **/
    private Integer imageCnt;
    private SharePostImageVO imagedata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_post_activity);

        /** xml 요소 초기화 **/
        sharePostName = (TextView) findViewById(R.id.sharePostName);
        sharePostStory = (TextView) findViewById(R.id.sharePostStory);
        sharePostsharePeople = (TextView) findViewById(R.id.sharePostsharePeople);
        sharePostsharedPeople = (TextView) findViewById(R.id.sharePostsharedPeople);
        sharePostUser = (TextView) findViewById(R.id.sharePostUser);
        sharePostUserIntro = (TextView) findViewById(R.id.sharePostUserIntro);
        SharePostWriterProfile = (ImageView) findViewById(R.id.SharePostWriterProfile);
        backBtn = (ImageView) findViewById(R.id.backBtn);

        /** Thread 사용 **/
        mHandler = new Handler();

        /** 반찬 PostId 값 받기 **/
        Intent intent = getIntent();
        Integer sharePostId = intent.getIntExtra("sharePostID",0);
        MarkerVO marker = new MarkerVO();
        marker.setShare_post_id(sharePostId);

        /** 게시물 load **/
        selectPost(marker);

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
                                imageCnt = sharePostVO.getShare_post_img_cnt();

                                UserVOGet(sharePostVO.getUser_id());
                                ImageGet();
                                settingUI();

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

    /** 게시글 UI에 적용 **/
    public void settingUI()
    {
        sharePostName.setText("< "+sharePostVO.getShare_post_name()+" >");
        sharePostStory.setText(sharePostVO.getShare_story());
        //나눔 가능 인원 수 : 00명
        String ment = "나눔 가능 인원 수 : ";
        if(sharePostVO.getShare_people()<10)
        {
            ment += (sharePostVO.getShare_people() - sharePostVO.getShared_people());
        }
        else
        {
            ment += "0" + (sharePostVO.getShare_people() - sharePostVO.getShared_people());
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

}
