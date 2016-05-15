package com.sachin.zhihu.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sachin.imitatezhihu.imitatezhihu.R;
import com.sachin.zhihu.views.RoundImageView;

/**
 * Created by lenovo on 2016/5/15.
 */
public class RoundImageActiviytTest extends AppCompatActivity {

    private RoundImageView roundImageView_1;
    private RoundImageView  roundImageView_4;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.roundimage_layouttest);
        roundImageView_1= (RoundImageView) findViewById(R.id.roundImageView_1);
        roundImageView_4= (RoundImageView) findViewById(R.id.roundImageView_4);


        roundImageView_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(roundImageView_1.getType()==RoundImageView.TYPE_CIRCLE) {
                    roundImageView_1.setType(RoundImageView.TYPE_ROUND);
                }else{
                    roundImageView_1.setType(RoundImageView.TYPE_CIRCLE);
                }
            }
        });

        int base=30;
        roundImageView_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               roundImageView_4.setBorderRadius(90);
            }
        });

    }


}
