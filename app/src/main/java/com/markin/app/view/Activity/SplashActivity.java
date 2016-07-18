package com.markin.app.view.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.kakao.helper.Logger;
import com.markin.app.R;
import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.common.AuthManager;
import com.markin.app.common.StaticValues;
import com.markin.app.connector.user.UserInfoGetTask;
import com.markin.app.connector.user.UserLoginTask;
import com.markin.app.model.Authorization;
import com.markin.app.model.User;

/**
 * Created by wonmook on 15. 5. 20..
 */
public class SplashActivity extends Activity implements OnTaskCompleted{

    private User user = null;
    private String shared_url = "";
    private String invitation_num = "";
    private String invitation_username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.splash_layout);
        Intent intent = getIntent();
        if (intent != null) {
            Uri uri = intent.getData();
            Log.d("INTENT", "URI : " + uri);

            if (uri != null) {
                Log.d("INTENT","scheme : " + uri.getScheme());
                Log.d("INTENT","host : " + uri.getHost());
                Log.d("INTENT", "path : " + uri.getPath());
                Log.d("INTENT", "query : " + uri.getQuery());
                invitation_num = uri.getQueryParameter(StaticValues.INVITATIONNUM);
                invitation_username = uri.getQueryParameter(StaticValues.INVITATIONUSERNAME);
                Log.d("INTENT", "invitation_num : " + invitation_num);
                Log.d("INTENT", "invitation_username : " + invitation_username);

            }
        }


        if(isTutorialNeed()){
            finish();
            Intent tutorialIntent = new Intent(SplashActivity.this, TutorialActivity.class);
            tutorialIntent.putExtra(StaticValues.INVITATIONNUM, invitation_num);
            tutorialIntent.putExtra(StaticValues.INVITATIONUSERNAME, invitation_username);
            startActivity(tutorialIntent);
        } else {
            autoLogin();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        //When shared url is received.
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            }
        }
    }

    private boolean isTutorialNeed(){
        SharedPreferences sharedPreferences = getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE);
        boolean needTutorial = sharedPreferences.getBoolean("tutorial", true);
        return needTutorial;

    }

    private void handleSendText(Intent intent) {
        String shared_text = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (shared_text != null) {
            shared_url = shared_text;
            Toast.makeText(getApplicationContext(), shared_url, Toast.LENGTH_SHORT).show();
            MainActivity.autoMarking = true;

        }
    }

    private void autoLogin(){
        user = AuthManager.getAuthManager().getLoginInfo(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        //Login info has
        if(user!=null){
            new UserLoginTask(SplashActivity.this, user).execute();
        }
        //Doesn't
        else {
            finish();
            Intent intent = new Intent(SplashActivity.this,IntroActivity.class);
            intent.putExtra(StaticValues.INVITATIONNUM, invitation_num);
            intent.putExtra(StaticValues.INVITATIONUSERNAME, invitation_username);
            startActivity(intent);
        }
    }

    @Override
    public void onTaskCompleted(Object authorization) {
        //Login sucess
        if(authorization!=null){
            Log.d("MainActivity", ((Authorization) authorization).toString());
            //Save authorization info to shared preference
            getUserInfo(user.getUserId(),  ((Authorization) authorization).getAccess_token());
        } else {

            //Login fail
            finish();
            startActivity(new Intent(SplashActivity.this,IntroActivity.class));
        }
    }

    private void getUserInfo(String user_id, final String token){
        //String token = AuthManager.getAuthManager().getToken(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        new UserInfoGetTask(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object object) {
                if(object!=null){
                    user = (User)object;
                    AuthManager.getAuthManager().login(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE), user.getUserId(), user.getPassword(), user.getName(), user.getEmail(), token);
                    finish();

                    Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                    Bundle extra = new Bundle();
                    extra.putString("SHARED_URL",shared_url);
                    extra.putString(StaticValues.INVITATIONNUM, invitation_num);
                    extra.putString(StaticValues.INVITATIONUSERNAME, invitation_username);
                    intent.putExtras(extra);
                    startActivity(intent);
                }
            }
        }, user_id, token).execute();
    }
}