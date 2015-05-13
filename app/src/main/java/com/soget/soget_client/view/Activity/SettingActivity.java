package com.soget.soget_client.view.Activity;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.soget.soget_client.R;
import com.soget.soget_client.common.AuthManager;
import com.soget.soget_client.model.Authorization;
import com.soget.soget_client.model.User;

public class SettingActivity extends ActionBarActivity {

    private ImageButton back_btn = null;
    private TextView user_id_tv = null;
    private TextView email_tv = null;
    private ImageButton invitation_btn = null;
    private ImageButton pwd_reset_btn = null;
    private ImageButton feedback_btn = null;
    private ImageButton blog_btn = null;
    private ImageButton privacy_regulation_btn = null;
    private ImageButton app_regulation_btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        initLayout();

        setBackBtnAction();
        setUserInfo();
        setInvitationBtnAction();
        setPwdResetBtnAction();
        setFeedBackBtnAction();
        setBlogBtnAction();
        setPrivacyRegulationBtnAction();
        setAppRegulationBtnAction();

    }

    private void initLayout(){
        back_btn = (ImageButton)findViewById(R.id.back_btn);
        user_id_tv = (TextView)findViewById(R.id.setting_id);
        email_tv = (TextView)findViewById(R.id.setting_email);
        invitation_btn = (ImageButton)findViewById(R.id.setting_invitation_btn);
        pwd_reset_btn = (ImageButton)findViewById(R.id.setting_pwd_change_btn);
        feedback_btn = (ImageButton)findViewById(R.id.setting_feedback_btn);
        blog_btn = (ImageButton)findViewById(R.id.setting_blog_btn);
        privacy_regulation_btn = (ImageButton)findViewById(R.id.setting_privacy_regulation_btn);
        app_regulation_btn = (ImageButton)findViewById(R.id.setting_regulation_btn);
    }

    private void setBackBtnAction(){
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setUserInfo(){
        User userInfo = AuthManager.getAuthManager().getLoginInfo(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        user_id_tv.setText(userInfo.getUserId());
        email_tv.setText(userInfo.getEmail());
    }

    private void setInvitationBtnAction(){
        invitation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"invitation btn",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setPwdResetBtnAction(){
        pwd_reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"pwd reset btn",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setFeedBackBtnAction(){
        feedback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"feedback btn",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setBlogBtnAction(){
        blog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"blog btn",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setPrivacyRegulationBtnAction(){
        privacy_regulation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"privacy regulation btn",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAppRegulationBtnAction(){
        app_regulation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"app regulation btn",Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
