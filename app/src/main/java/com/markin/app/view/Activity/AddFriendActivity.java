package com.markin.app.view.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.markin.app.R;
import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.common.AuthManager;
import com.markin.app.common.StaticValues;
import com.markin.app.connector.friend.FriendAcceptWithInvitationTask;
import com.markin.app.connector.friend.FriendCheckWithInvitationTask;
import com.markin.app.connector.invitation.InvitationCodeUseTask;

/**
 * Created by wonmook on 2016. 7. 4..
 */
public class AddFriendActivity extends Activity {

    private TextView invitation_desc_tv;
    private TextView invitation_accept_tv;
    private TextView invitation_decline_tv;
    private ImageButton invitation_accept_btn;
    private ImageButton invitation_decline_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_friend_dialog);

        Intent receiveIntent = getIntent();
        if(receiveIntent!=null){
            String invitation_num = receiveIntent.getStringExtra(StaticValues.INVITATIONNUM);
            String invitation_username = receiveIntent.getStringExtra(StaticValues.INVITATIONUSERNAME);
            initLayout(invitation_username, invitation_num);
        }

    }

    private void initLayout(String invitation_username, final String invitation_num){
        invitation_desc_tv = (TextView)findViewById(R.id.invitation_desc_tv);
        invitation_accept_tv = (TextView)findViewById(R.id.add_friend_accept_tv);
        invitation_desc_tv = (TextView)findViewById(R.id.add_friend_desc_tv);
        invitation_accept_btn = (ImageButton)findViewById(R.id.add_friend_accept_btn);
        invitation_decline_btn = (ImageButton)findViewById(R.id.add_friend_decline_btn);

        //invitation_desc_tv.setTextColor(getResources().getColor(R.color.white));
        //invitation_desc_tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/AppleSDGothicNeo-Medium.otf"));

        String origin = invitation_desc_tv.getText().toString();
        String modified = origin
                .replace("{user_name}", invitation_username);
        invitation_desc_tv.setText(modified);
        invitation_accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_id = (AuthManager.getAuthManager().getLoginInfo(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE))).getUserId();
                String token = AuthManager.getAuthManager().getToken(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
                Log.d("InvitatioinSendActivity", user_id + "," + invitation_num);

                new FriendAcceptWithInvitationTask(new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(Object object) {

                        finish();
                        if (object != null && (Boolean) object == true) {
                            Toast.makeText(getApplicationContext(), "Added New Frined!!!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error!!!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, user_id, invitation_num, token).execute();
            }
        });

        invitation_decline_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}
