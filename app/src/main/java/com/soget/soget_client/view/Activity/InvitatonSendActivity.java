package com.soget.soget_client.view.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.AppActionBuilder;
import com.kakao.AppActionInfoBuilder;
import com.kakao.KakaoLink;
import com.kakao.KakaoParameterException;
import com.kakao.KakaoTalkLinkMessageBuilder;
import com.soget.soget_client.R;
import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.common.AuthManager;
import com.soget.soget_client.common.StaticValues;
import com.soget.soget_client.connector.user.UserInfoGetTask;
import com.soget.soget_client.connector.invitation.InvitationCodeUseTask;
import com.soget.soget_client.model.User;

/**
 * Created by wonmook on 15. 5. 17..
 */
public class InvitatonSendActivity extends Activity {

    private TextView currentInvitationNumTv = null;
    private TextView senderInfoTv           = null;
    private TextView invitationCodeTv       = null;
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

        currentInvitationNumTv = (TextView)findViewById(R.id.invitation_num_tv);
        currentInvitationNumTv.setText(getString(R.string.invitation_send_current_code)+currentInviationNum+"개");

        senderInfoTv = (TextView)findViewById(R.id.invitation_sender_tv);
        senderInfoTv.setText(userName+" ("+userId+")님이");

        invitationCodeTv = (TextView)findViewById(R.id.invitation_code_tv);
        invitationCodeTv.setText(getString(R.string.invitation_code)+" "+invitationCode);

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
                            Toast.makeText(getApplicationContext(), object.toString(),Toast.LENGTH_SHORT).show();
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
        message.append(userName+" ("+userId+")님이 ");
        message.append(getString(R.string.invitation_sms_message)+" ");
        message.append(getString(R.string.invitation_code)+" "+invitationCode+" ");
        message.append(getString(R.string.invitation_app));

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
            /*kakaoTalkLinkMessageBuilder.addAppLink("앱으로 바로가기",
                    new AppActionBuilder()
                            .addActionInfo(AppActionInfoBuilder.createAndroidActionInfoBuilder().setExecuteParam("bo_table=test&wr_id=11").setMarketParam("referrer=kakaotalklink").build())
                            .addActionInfo(AppActionInfoBuilder.createiOSActionInfoBuilder(AppActionBuilder.DEVICE_TYPE.PHONE).setExecuteParam("execparamkey1=1111").build()).build()
            );*/
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