package com.markin.app.view.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.markin.app.R;
import com.markin.app.common.StaticValues;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wonmook on 15. 6. 13..
 */
public class ConditionActivity extends Activity{

    private TextView conditionTv = null;
    private ImageButton backBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.condition_layout);
        backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        boolean condition = getIntent().getExtras().getBoolean(StaticValues.CONDITION,false);
        boolean privacy = getIntent().getExtras().getBoolean(StaticValues.PRIVACY,false);


        conditionTv = (TextView) findViewById(R.id.condition_tv);
        if(condition){
            conditionTv.setText(readTxt(R.raw.condition));
        } else if(privacy){
            conditionTv.setText(readTxt(R.raw.privacy));
        }

    }
    private String readTxt(int text) {
        String data = null;
        InputStream inputStream = getResources().openRawResource(text);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int i;
        try {
            i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }

            data = new String(byteArrayOutputStream.toByteArray(),"UTF-8");
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
