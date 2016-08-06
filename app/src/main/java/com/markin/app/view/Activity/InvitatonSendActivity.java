package com.markin.app.view.Activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kakao.AppActionBuilder;
import com.kakao.AppActionInfoBuilder;
import com.kakao.KakaoLink;
import com.kakao.KakaoParameterException;
import com.kakao.KakaoTalkLinkMessageBuilder;
import com.markin.app.R;
import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.common.AuthManager;
import com.markin.app.common.StaticValues;
import com.markin.app.connector.invitation.InvitationCodeUseTask;
import com.markin.app.connector.user.UserInfoGetTask;
import com.markin.app.model.User;
import com.markin.app.view.Fragment.FriendsFragment;

import org.w3c.dom.Text;

/**
 * Created by wonmook on 15. 5. 17..
 */
public class InvitatonSendActivity extends Activity {

    private TextView    invitationTitleTv   = null;
    private TextView    invitationDescTv    = null;
    private TextView    invitationContentTv = null;
    private ImageButton invitationSendBtn   = null;
    private ImageButton prevBtn = null;

    private String invitationCode = "";
    private String userId = "";
    private String userName = "";
    private String phoneNum = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invitation_send_layout);

        userId = (AuthManager.getAuthManager().getLoginInfo(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE))).getUserId();
        userName = (AuthManager.getAuthManager().getLoginInfo(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE))).getName();
        if(userName.equals("")){
              getUserInfo(userId);
        }
        invitationCode = getIntent().getExtras().getString(StaticValues.INVITATIONCODE);
        phoneNum = getIntent().getExtras().getString(StaticValues.PHONENUM);
        setLayout();
    }

    private void getUserInfo(String user_id){
        String token = AuthManager.getAuthManager().getToken(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        new UserInfoGetTask(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object object) {
                if(object!=null){
                    User user = (User)object;
                    userName = user.getName();
                    userId = user.getUserId();

                    String origin = invitationContentTv.getText().toString();
                    String modified = origin
                            .replace("{user_name}",userName)
                            .replace("{user_id}",userId)
                            .replace("{code}",invitationCode);
                    invitationContentTv.setText(modified);
                }
            }
        }, user_id, token).execute();
    }

    private void setLayout(){

        prevBtn = (ImageButton)findViewById(R.id.back_btn);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

            }
        });

        invitationTitleTv = (TextView)findViewById(R.id.invitation_title_tv);
        invitationDescTv  = (TextView)findViewById(R.id.invitation_desc_tv);
        invitationContentTv = (TextView)findViewById(R.id.invitation_content_tv);

        invitationTitleTv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/AppleSDGothicNeo-SemiBold.otf"));
        invitationDescTv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/AppleSDGothicNeo-Medium.otf"));
        invitationContentTv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/AppleSDGothicNeo-Medium.otf"));

        String origin = invitationContentTv.getText().toString();
        String modified = origin
                .replace("{user_name}",userName)
                .replace("{user_id}",userId)
                .replace("{code}",invitationCode);
        invitationContentTv.setText(modified);

        invitationSendBtn = (ImageButton)findViewById(R.id.invitation_send_btn);
        invitationSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_id = (AuthManager.getAuthManager().getLoginInfo(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE))).getUserId();
                String token = AuthManager.getAuthManager().getToken(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
                Log.d("InvitatioinSendActivity", user_id + "," + invitationCode);
                new InvitationCodeUseTask(new OnTaskCompleted(){
                    @Override
                    public void onTaskCompleted(Object object) {
                        if(object!=null){
                            sendKakaoTalk();
                            finish();
                            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

                        }

                    }
                },user_id,invitationCode,token).execute();

            }
        });

    }

    private void sendKakaoTalk(){

        StringBuffer message = new StringBuffer();
        message.append(userName+" ("+userId+")님이 발견한");
        message.append(getString(R.string.invitation_sms_message) + " ");
        message.append(getString(R.string.invitation_code) + " " + invitationCode + " ");
//        message.append(getString(R.string.invitation_app));

        KakaoLink kakaoLink = null;
        KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = null;
        try {
            kakaoLink = KakaoLink.getKakaoLink(getApplicationContext());
            kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            kakaoTalkLinkMessageBuilder.addText(message.toString());
            kakaoTalkLinkMessageBuilder.addAppButton("앱으로 바로가기",
                    new AppActionBuilder()
                            .addActionInfo(AppActionInfoBuilder.createAndroidActionInfoBuilder().setExecuteParam("invitationNum=" + invitationCode + "&invitationUserName=" + userName).build())
                            .addActionInfo(AppActionInfoBuilder.createiOSActionInfoBuilder().setExecuteParam("invitationNum=" + invitationCode + "&invitationUserName=" + userName).build())
                            .build()
            );

            kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder.build(), this);

        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }
    }

    private void sendSMS(String phoneNumber, String message)
    {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

    }


}