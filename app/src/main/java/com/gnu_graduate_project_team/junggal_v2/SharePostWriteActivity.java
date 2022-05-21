package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SharePostWriteActivity extends Activity {

    private ImageView dish_photo;
    private ImageView dish_image1;
    private ImageView dish_image2;
    private ImageView dish_image3;
    private EditText dish_name;
    private Button person_minus_btn;
    private Button person_plush_btn;
    private TextView person_cnt;
    private Integer person_count;
    private Button max_time_minus_btn;
    private Button max_time_plus_btn;
    private Integer share_time;
    private TextView share_time_cnt;
    private TextView share_geo_point;
    private TextView popup_Choose;
    private Integer share_icon_number;
    private TextView sharePost_submit;
    private EditText share_post_story;

    /** return result intent **/
    private static Integer pick_from_Multi_album = 1;
    private static Integer share_geo_region = 2;
    private static Integer share_icon = 3;

    /** 반찬 나눔 이미지 관련 **/
    private Integer count = 0;
    private ArrayList<Uri> imageListPath;
    private ArrayList<ImageView> dish_imageArrayList = new ArrayList<>();
    private Boolean image_flag = false;

    /** 반찬 나눔 위치 및 좌표 **/
    private SharePostGeoInfo sharePostGeoInfo;

    /** Thread 사용 **/
    Handler mHandler = null;

    /** Server와 통신 관련 **/
    private Map<String, RequestBody> share_post = new HashMap<>();
    private ArrayList<MultipartBody.Part> sharePostImage;
    private SharePostVO sharePost;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.share_post_write_ativity);

        /** 사용자 id 받아오기 **/
        String user_id = PreferenceManager.getString(SharePostWriteActivity.this,"user_id");

        mHandler = new Handler();

        dish_photo = (ImageView) findViewById(R.id.dish_photo);
        dish_image1 = (ImageView) findViewById(R.id.dish_image1);
        dish_image2 = (ImageView) findViewById(R.id.dish_image2);
        dish_image3 = (ImageView) findViewById(R.id.dish_image3);
        dish_name = (EditText) findViewById(R.id.dish_name);
        person_minus_btn = (Button) findViewById(R.id.person_minus_button);
        person_plush_btn = (Button) findViewById(R.id.person_plus_button);
        person_cnt = (TextView) findViewById(R.id.Person);
        max_time_minus_btn = (Button) findViewById(R.id.max_time_minus_btn);
        max_time_plus_btn = (Button) findViewById(R.id.max_time_plus_btn);
        share_time_cnt = (TextView) findViewById(R.id.share_time);
        share_geo_point = (TextView) findViewById(R.id.share_geo_point);
        popup_Choose = (TextView) findViewById(R.id.popup_choose);
        sharePost_submit = (TextView) findViewById(R.id.sharePost_submit);
        share_post_story = (EditText) findViewById(R.id.share_post_story);

        person_count = 1;
        share_time = 1;

        dish_imageArrayList.add(dish_image1);
        dish_imageArrayList.add(dish_image2);
        dish_imageArrayList.add(dish_image3);

        /** Retrofit2 / ApiPostInterfae 호출 **/
        Retrofit retrofit = ApiClient.getApiClient();
        ApiPostInterface apiPostInterface = retrofit.create(ApiPostInterface.class);

        /** 반찬 이미지 등록 **/
        dish_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                count=0;
                imageListPath = new ArrayList<>();
                sharePostImage = new ArrayList<>();
                doTakeMultiAlbumAction();

            }
        });

        /** 반찬 나눔 위치 공유 **/
        share_geo_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SharePostWriteActivity.this,SharePostlocationActivity.class);
                startActivityForResult(intent, share_geo_region);
            }
        });

        /** 팝업 아이콘 설정 **/
        popup_Choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SharePostWriteActivity.this, SharePostIconActivity.class);
                startActivityForResult(intent, share_icon);

            }
        });

        /** 나눔 인원 설정 **/
        person_minus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                person_count--;
                if(person_count<1)
                {
                    person_count =1;
                }


                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                person_cnt.setText(person_count.toString());
                            }
                        });
                    }
                });

                t.start();
            }
        });

        person_plush_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                person_count++;

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                person_cnt.setText(person_count.toString());
                            }
                        });
                    }
                });

                t.start();
            }
        });


        /** 나눔 시간 설정 **/
        max_time_minus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share_time --;
                if(share_time<1)
                {
                    share_time=1;
                }

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                share_time_cnt.setText(share_time.toString());
                            }
                        });
                    }
                });

                t.start();
            }
        });

        max_time_plus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                share_time ++;

                if(share_time>12)
                {
                    share_time=12;
                }

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                share_time_cnt.setText(share_time.toString());
                            }
                        });
                    }
                });

                t.start();

            }
        });


        sharePost_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String dish_name_tmp = dish_name.getText().toString().trim();
                String story_tmp = share_post_story.getText().toString().trim();

                if ( !image_flag )
                {
                    Toast.makeText(getApplicationContext(),"사진을 등록해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(dish_name_tmp.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"반찬 이름을 등록해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(sharePostGeoInfo==null)
                {
                    Toast.makeText(getApplicationContext(),"나눔 위치를 등록해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(share_icon_number==null || share_icon_number == 9999)
                {
                    Toast.makeText(getApplicationContext(),"팝업 모양 등록해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(story_tmp.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"나눔 이야기를 작성해주세요.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {

                                    RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), user_id);
                                    RequestBody shareName = RequestBody.create(MediaType.parse("text/plain"),dish_name_tmp );
                                    RequestBody sharePostImgCnt = RequestBody.create(MediaType.parse("text/plain"),count.toString());
                                    RequestBody sharePostRegion = RequestBody.create(MediaType.parse("text/plain"),sharePostGeoInfo.getGeoRegion());
                                    RequestBody sharePostPoint = RequestBody.create(MediaType.parse("text/plain"),sharePostGeoInfo.getLatLng().longitude+" " + sharePostGeoInfo.getLatLng().latitude);
                                    RequestBody shareIcon = RequestBody.create(MediaType.parse("text/plain"),share_icon_number.toString());
                                    RequestBody sharePeople = RequestBody.create(MediaType.parse("text/plain"),person_count.toString());
                                    RequestBody shareTime = RequestBody.create(MediaType.parse("text/plain"),share_time.toString());
                                    RequestBody shareStory = RequestBody.create(MediaType.parse("text/plain"), story_tmp);

                                    share_post.put("user_id",userId);
                                    share_post.put("share_post_name",shareName);
                                    share_post.put("share_post_img_cnt",sharePostImgCnt);
                                    share_post.put("share_post_region",sharePostRegion);
                                    share_post.put("share_post_point",sharePostPoint);
                                    share_post.put("share_post_icon", shareIcon);
                                    share_post.put("share_people",sharePeople);
                                    share_post.put("share_time_int",shareTime);
                                    share_post.put("share_story",shareStory);


                                    Call<SharePostVO> call = apiPostInterface.sharePostUpload(share_post,sharePostImage);

                                    call.enqueue(new Callback<SharePostVO>() {
                                        @Override
                                        public void onResponse(Call<SharePostVO> call, Response<SharePostVO> response) {

                                            SharePostVO checkVO = response.body();
                                            if(checkVO.getShare_post_id()!=null)
                                            {
                                                Log.d("upload_test", "test성공");
                                                finish();
                                            }
                                            else
                                            {
                                                Toast.makeText(getApplicationContext(),"게시글 등록이 실패하였습니다.\n 네트워크 상태를 확인해 주세요." , Toast.LENGTH_SHORT).show();
                                            }


                                        }

                                        @Override
                                        public void onFailure(Call<SharePostVO> call, Throwable t) {
                                            Toast.makeText(getApplicationContext(),"게시글 등록이 실패하였습니다.\n 네트워크 상태를 확인해 주세요." , Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                }
                            });
                        }
                    });

                    t.start();


                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == pick_from_Multi_album)
        {
            if(data==null)
            {

            }
            else
            {
                if(data.getClipData() == null)
                {
                    Toast.makeText(this, "다중 선택이 불가능한 기기입니다.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ClipData clipData = data.getClipData();

                    if(clipData.getItemCount()>3)
                    {
                        Toast.makeText(this, "사진은 3장까지 선택 가능합니다.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if(clipData.getItemCount()==1)
                        {
                            imageListPath.add(clipData.getItemAt(0).getUri());
                            count=1;
                            image_flag = true;

                        }
                        else if(clipData.getItemCount()>1 && clipData.getItemCount() < 4)
                        {
                            for(int i =0; i<clipData.getItemCount(); i++)
                            {
                                imageListPath.add(clipData.getItemAt(i).getUri()) ;
                                count++;
                            }
                            image_flag = true;
                        }

                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        Collections.sort(imageListPath,Collections.reverseOrder());

                                        for(int i=0; i<3; i++)
                                        {
                                            dish_imageArrayList.get(i).setImageBitmap(null);
                                        }



                                        for(int i=0; i<count; i++)
                                        {
                                            Glide.with(getApplicationContext()).load(imageListPath.get(i)).override(500,500).into(dish_imageArrayList.get(i));
                                        }


                                        imageCompressJpeg(count);
                                    }
                                });
                            }
                        });

                        t.start();
                    }

                }
            }

        }
        else if( requestCode == share_geo_region )
        {
            if(data.getParcelableExtra("sharePostGeoInfo")!=null)
            {
                sharePostGeoInfo = data.getParcelableExtra("sharePostGeoInfo");
                Log.d("shareRegion",sharePostGeoInfo.getGeoRegion());
                Log.d("sharegeoPoint",sharePostGeoInfo.getLatLng().toString());
            }

        }
        else if( requestCode == share_icon)
        {
            share_icon_number = data.getIntExtra("sharePostIconNumber",9999);
            Log.d("sharePostIconNumber",share_icon_number.toString());
        }
        else if( requestCode == 9999)
        {
            Log.d("Backpress",":9999");
        }
    }

    public void doTakeMultiAlbumAction()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, pick_from_Multi_album);
    }

    public void imageCompressJpeg(int count)
    {
        MultipartBody.Part imagetmp;
        RequestBody imgtmp;

        if (count ==1 )
        {

            InputStream inputStream = null;

            try {
                inputStream = getApplicationContext().getContentResolver().openInputStream(imageListPath.get(0));
            }catch (IOException e)
            {
                e.printStackTrace();
            }

            Bitmap tmp = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            tmp.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);

            imgtmp = RequestBody.create(MediaType.parse("image/jpg"),byteArrayOutputStream.toByteArray());
            sharePostImage.add(MultipartBody.Part.createFormData("files","file"+count,imgtmp));

            Log.d("image","compress-success");
        }
        else
        {
            for(int i=0;i<count;i++)
            {

                InputStream inputStream = null;

                try {
                    inputStream = getApplicationContext().getContentResolver().openInputStream(imageListPath.get(i));
                }catch (IOException e)
                {
                    e.printStackTrace();
                }

                Bitmap tmp = BitmapFactory.decodeStream(inputStream);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                tmp.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);

                imgtmp = RequestBody.create(MediaType.parse("image/jpg"),byteArrayOutputStream.toByteArray());
                sharePostImage.add(MultipartBody.Part.createFormData("files","file"+count,imgtmp));

                Log.d("image","compress-success");
            }
        }

    }
}
