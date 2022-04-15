package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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

    private static Integer pick_from_Multi_album = 1;
    private Integer count = 0;

    private ArrayList<Uri> imageListPath;

    private ArrayList<ImageView> dish_imageArrayList = new ArrayList<>();

    Handler mHandler = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.share_post_write_ativity);

        mHandler = new Handler();

        dish_photo = (ImageView) findViewById(R.id.dish_photo);
        dish_image1 = (ImageView) findViewById(R.id.dish_image1);
        dish_image2 = (ImageView) findViewById(R.id.dish_image2);
        dish_image3 = (ImageView) findViewById(R.id.dish_image3);
        dish_name = (EditText) findViewById(R.id.dish_name);
        person_minus_btn = (Button) findViewById(R.id.person_minus_button);
        person_plush_btn = (Button) findViewById(R.id.person_plus_button);
        person_cnt = (TextView) findViewById(R.id.Person);

        person_count = 0;

        dish_imageArrayList.add(dish_image1);
        dish_imageArrayList.add(dish_image2);
        dish_imageArrayList.add(dish_image3);

        /** 반찬 이미지 등록 **/
        dish_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                count=0;
                imageListPath = new ArrayList<>();
                doTakeMultiAlbumAction();

            }
        });




        /** 나눔 인원 설정 **/
        person_minus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                person_count--;
                if(person_count<0)
                {
                    person_count =0;
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
                        }
                        else if(clipData.getItemCount()>1 && clipData.getItemCount() < 4)
                        {
                            for(int i =0; i<clipData.getItemCount(); i++)
                            {
                                imageListPath.add(clipData.getItemAt(i).getUri()) ;
                                count++;
                            }
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

                                    }
                                });
                            }
                        });

                        t.start();
                    }

                }
            }

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
}
