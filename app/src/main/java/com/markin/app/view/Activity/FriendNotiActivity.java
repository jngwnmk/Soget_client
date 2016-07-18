package com.markin.app.view.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.markin.app.R;
import com.markin.app.common.StaticValues;

/**
 * Created by wonmook on 2016. 7. 11..
 */
public class FriendNotiActivity extends Activity {

    private TextView invitation_desc_tv;
    private TextView invitation_accept_tv;
    private ImageButton invitation_accept_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.noti_friend_dialog);

        Intent receiveIntent = getIntent();
        if (receiveIntent != null) {
            String invitation_num = receiveIntent.getStringExtra(StaticValues.INVITATIONNUM);
            String invitation_username = receiveIntent.getStringExtra(StaticValues.INVITATIONUSERNAME);
            initLayout(invitation_username, invitation_num);
        }

    }

    private void initLayout(String invitation_username, final String invitation_num) {
        invitation_desc_tv = (TextView) findViewById(R.id.invitation_desc_tv);
        invitation_accept_tv = (TextView) findViewById(R.id.add_friend_accept_tv);
        invitation_desc_tv = (TextView) findViewById(R.id.add_friend_desc_tv);
        invitation_accept_btn = (ImageButton) findViewById(R.id.add_friend_accept_btn);

        String origin = invitation_desc_tv.getText().toString();
        String modified = origin
                .replace("{user_name}", invitation_username);
        invitation_desc_tv.setText(modified);
        invitation_accept_tv.setTextColor(getResources().getColor(R.color.charcol_text_color_66));
        invitation_accept_tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/AppleSDGothicNeo-Medium.otf"));
        invitation_accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(FriendNotiActivity.this, MainActivity.class));

            }
        });
    }

}