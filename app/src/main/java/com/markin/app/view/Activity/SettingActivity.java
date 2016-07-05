package com.markin.app.view.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.markin.app.R;
import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.common.AuthManager;
import com.markin.app.common.StaticValues;
import com.markin.app.connector.user.UserInfoGetTask;
import com.markin.app.connector.invitation.InvitationCodeGetTask;
import com.markin.app.model.User;

import java.util.ArrayList;

public class SettingActivity extends ActionBarActivity {

    private ImageButton back_btn = null;
    private TextView user_id_tv = null;
    private TextView email_tv = null;
    private ImageButton invitation_btn = null;
    private TextView invitation_tv = null;
    private ImageButton pwd_reset_btn = null;
    private ImageButton feedback_btn = null;
    private ImageButton blog_btn = null;
    private ImageButton privacy_regulation_btn = null;
    private ImageButton app_regulation_btn = null;
    private ArrayList<String> invitationNum = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout_old);
        initLayout();

        setBackBtnAction();
        setUserInfo();
        setInvitationInfo();
        setInvitationBtnAction();
        setPwdResetBtnAction();
        setFeedBackBtnAction();
        setBlogBtnAction();
        setPrivacyRegulationBtnAction();
        setAppRegulationBtnAction();

    }

    @Override
    protected void onResume(){
        super.onResume();

    }

    private void initLayout(){
        back_btn = (ImageButton)findViewById(R.id.back_btn);
        user_id_tv = (TextView)findViewById(R.id.setting_id);
        email_tv = (TextView)findViewById(R.id.setting_email);
        invitation_tv = (TextView)findViewById(R.id.setting_invitation_tv);
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
        if(userInfo.getEmail().equals("")){
            //Request
            requestUserInfo(userInfo.getUserId());
        } else {
            email_tv.setText(userInfo.getEmail());
        }

    }

    private void setInvitationInfo(){
        OnTaskCompleted onTaskCompleted;
        onTaskCompleted = new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object object) {
                invitationNum = (ArrayList<String>)object;
                invitation_tv.setText("초대장: "+ invitationNum.size()+"장");
            }
        };
        String user_id = (AuthManager.getAuthManager().getLoginInfo(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE))).getUserId();
        String token = AuthManager.getAuthManager().getToken(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        new InvitationCodeGetTask(onTaskCompleted, user_id, token).execute();
    }
    private void setInvitationBtnAction(){

        invitation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(invitationNum.size()!=0) {
                    Intent intent = new Intent(getApplicationContext(), InvitatonSendActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(StaticValues.INVITATIONCODE, invitationNum.get(0));
                    bundle.putInt(StaticValues.INVITATIONNUM, invitationNum.size());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    private void setPwdResetBtnAction(){
        pwd_reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToMarkInWeb();
            }
        });
    }

    private void setFeedBackBtnAction(){
        feedback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMarkInWeb();
            }
        });
    }

    private void setBlogBtnAction(){
        blog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMarkInWeb();
            }
        });
    }

    private void goToMarkInWeb(){
        Intent intent = new Intent(SettingActivity.this, NormalWebViewActivity.class);
        intent.putExtra(NormalWebViewActivity.WEBVIEWURL,"https://medium.com/@MarkIn_app/markin-01-c043b4d4f1cd");
        startActivity(intent);

    }

    private void setPrivacyRegulationBtnAction(){
        privacy_regulation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ConditionActivity.class);
                intent.putExtra(StaticValues.PRIVACY,true);
                startActivity(intent);
            }
        });
    }

    private void setAppRegulationBtnAction(){
        app_regulation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ConditionActivity.class);
                intent.putExtra(StaticValues.CONDITION,true);
                startActivity(intent);
            }
        });
    }

    private void requestUserInfo(String user_id){
        String token = AuthManager.getAuthManager().getToken(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        new UserInfoGetTask(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object object) {
                    if(object!=null){
                        User user = (User)object;
                        email_tv.setText(user.getEmail());
                    }
            }
        }, user_id, token).execute();

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
