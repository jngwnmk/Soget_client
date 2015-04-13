package com.soget.soget_client.view.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import com.soget.soget_client.R;

/**
 * Created by wonmook on 2015-04-01.
 */
public class CommentActivity extends ActionBarActivity{
    private Button closeBtn =null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_layout);
        closeBtn = (Button)findViewById(R.id.close_btn);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
