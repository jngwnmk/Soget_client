package com.soget.soget_client.view.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.soget.soget_client.R;
import com.soget.soget_client.common.AuthManager;
import com.soget.soget_client.common.StaticValues;

import org.springframework.core.io.Resource;

/**
 * Created by wonmook on 15. 5. 17..
 */
public class InvitatonSendActivity extends Activity {

    private TextView currentInvitationNumTv = null;
    private TextView senderInfoTv           = null;
    private TextView invitationCodeTv       = null;
    private EditText receiverPhoneEt        = null;
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

        currentInviationNum = getIntent().getExtras().getInt(StaticValues.INVITATIONNUM);
        invitationCode = getIntent().getExtras().getString(StaticValues.INVITATIONCODE);
        phoneNum = getIntent().getExtras().getString(StaticValues.PHONENUM);
        setLayout();
    }

    private void setLayout(){

        currentInvitationNumTv = (TextView)findViewById(R.id.invitation_num_tv);
        currentInvitationNumTv.setText(getString(R.string.invitation_send_current_code)+currentInviationNum+"개");

        senderInfoTv = (TextView)findViewById(R.id.invitation_sender_tv);
        senderInfoTv.setText(userName+" ("+userId+")님이");

        invitationCodeTv = (TextView)findViewById(R.id.invitation_code_tv);
        invitationCodeTv.setText(getString(R.string.invitation_code)+invitationCode);

        receiverPhoneEt = (EditText)findViewById(R.id.invitation_receiver_et);
        if(!"".equals(phoneNum)){
            receiverPhoneEt.setText(phoneNum);
        }
        receiverPhoneEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ContactListActivity.class);
                Bundle extra = new Bundle();
                extra.putInt(StaticValues.INVITATIONNUM,currentInviationNum);
                extra.putString(StaticValues.INVITATIONCODE,invitationCode);
                intent.putExtras(extra);
                startActivity(intent);
                finish();
            }
        });

        invitationSendBtn = (ImageButton)findViewById(R.id.invitation_send_btn);
        invitationSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Sent Invitation MSG!!!",Toast.LENGTH_SHORT).show();
                StringBuffer message = new StringBuffer();
                message.append(userName+" ("+userId+")님이 ");
                message.append(getString(R.string.invitation_sms_message)+" ");
                message.append(getString(R.string.invitation_code)+invitationCode+" ");
                message.append(getString(R.string.invitation_app));
                sendSMS(receiverPhoneEt.getText().toString(), message.toString());
                finish();
            }
        });

    }

    private void sendSMS(String phoneNumber, String message)
    {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

}