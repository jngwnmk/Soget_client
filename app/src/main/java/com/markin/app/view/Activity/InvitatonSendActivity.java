package com.markin.app.view.Activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
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

/**
 * Created by wonmook on 15. 5. 17..
 */
public class InvitatonSendActivity extends Activity {

    private TextView invitationDescTv       = null;
    private TextView senderInfoTv           = null;
    private TextView senderInfo1Tv          = null;
    private TextView invitationCodeTv       = null;
    //private TextView invitationAppTv        = null;
    private TextView inviationNoteTv        = null;
    private ImageButton invitationSendBtn   = null;
    private int currentInviationNum = 0;
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
        currentInviationNum = getIntent().getExtras().getInt(StaticValues.INVITATIONNUM);
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
                    senderInfoTv.setText(user.getName()+" ("+user.getUserId()+")님이");
                }
            }
        }, user_id, token).execute();

    }

    private void setLayout(){

        invitationDescTv = (TextView)findViewById(R.id.invitation_desc_tv);
        invitationDescTv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/AppleSDGothicNeo-SemiBold.otf"));


        senderInfoTv = (TextView)findViewById(R.id.invitation_sender_tv);
        senderInfoTv.setText(userName+" ("+userId+")님이");
        senderInfoTv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/AppleSDGothicNeo-Regular.otf"));

        senderInfo1Tv = (TextView)findViewById(R.id.invitation_sender_1_tv);
        senderInfo1Tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/AppleSDGothicNeo-Regular.otf"));

        invitationCodeTv = (TextView)findViewById(R.id.invitation_code_tv);
        invitationCodeTv.setText(getString(R.string.invitation_code)+" "+invitationCode);
        invitationCodeTv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/AppleSDGothicNeo-Regular.otf"));

        //invitationAppTv = (TextView)findViewById(R.id.invitation_app_tv);
        //invitationAppTv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/AppleSDGothicNeo-Regular.otf"));
        inviationNoteTv = (TextView)findViewById(R.id.invitation_note_tv);
        inviationNoteTv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/AppleSDGothicNeo-Medium.otf"));

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
                        }

                    }
                },user_id,invitationCode,token).execute();

            }
        });

    }

    private void sendKakaoTalk(){

        StringBuffer message = new StringBuffer();
        message.append(userName+" ("+userId+")님이 모은");
        message.append(getString(R.string.invitation_sms_message)+" ");
        message.append(getString(R.string.invitation_code)+" "+invitationCode+" ");
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
                            .addActionInfo(AppActionInfoBuilder
                                    .createAndroidActionInfoBuilder()
                                    .build())

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

}