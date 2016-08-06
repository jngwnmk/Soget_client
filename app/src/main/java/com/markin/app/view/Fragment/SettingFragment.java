package com.markin.app.view.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.markin.app.R;
import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.common.AuthManager;
import com.markin.app.common.StaticValues;
import com.markin.app.connector.user.UserInfoGetTask;
import com.markin.app.model.User;
import com.markin.app.view.Activity.ConditionActivity;
import com.markin.app.view.Activity.NormalWebViewActivity;

import org.w3c.dom.Text;

/**
 * Created by wonmook on 2016. 5. 31..
 */
public class SettingFragment extends Fragment{

    private TextView idTv = null;
    private TextView emailTv = null;
    private TextView pwdChangeTv = null;
    private TextView feedBackTv  = null;
    private TextView blogTv = null;
    private TextView privacyTv = null;
    private TextView termTv = null;

    private ImageButton pwdChangeBtn = null;
    private ImageButton feedBackBtn = null;
    private ImageButton blogBtn = null;
    private ImageButton privacyBtn = null;
    private ImageButton termBtn = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.setting_layout, container, false);
        idTv = (TextView)v.findViewById(R.id.setting_id);
        emailTv = (TextView)v.findViewById(R.id.setting_email);
        pwdChangeTv = (TextView)v.findViewById(R.id.setting_pwd_change_tv);
        feedBackTv = (TextView)v.findViewById(R.id.setting_feedback_tv);
        blogTv = (TextView)v.findViewById(R.id.setting_blog_tv);
        privacyTv = (TextView)v.findViewById(R.id.setting_privacy_tv);
        termTv = (TextView)v.findViewById(R.id.setting_term_tv);

        pwdChangeBtn = (ImageButton)v.findViewById(R.id.setting_pwd_change_btn);
        feedBackBtn = (ImageButton)v.findViewById(R.id.setting_feedback_btn);
        blogBtn = (ImageButton)v.findViewById(R.id.setting_blog_btn);
        privacyBtn = (ImageButton)v.findViewById(R.id.setting_privacy_regulation_btn);
        termBtn = (ImageButton)v.findViewById(R.id.setting_regulation_btn);

        idTv.setTextColor(getResources().getColor(R.color.charcol_text_color_80));
        emailTv.setTextColor(getResources().getColor(R.color.charcol_text_color_80));
        pwdChangeTv.setTextColor(getResources().getColor(R.color.charcol_text_color_80));
        feedBackTv.setTextColor(getResources().getColor(R.color.charcol_text_color_80));
        blogTv.setTextColor(getResources().getColor(R.color.charcol_text_color_80));
        privacyTv.setTextColor(getResources().getColor(R.color.charcol_text_color_80));
        termTv.setTextColor(getResources().getColor(R.color.charcol_text_color_80));

        idTv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/AppleSDGothicNeo-Medium.otf"));
        emailTv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/AppleSDGothicNeo-Medium.otf"));
        pwdChangeTv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/AppleSDGothicNeo-Medium.otf"));
        feedBackTv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/AppleSDGothicNeo-Medium.otf"));
        blogTv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/AppleSDGothicNeo-Medium.otf"));
        privacyTv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/AppleSDGothicNeo-Medium.otf"));
        termTv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/AppleSDGothicNeo-Medium.otf"));


        pwdChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSocketWeb();
            }
        });

        feedBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSocketWeb();
            }
        });

        blogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSocketWeb();
            }
        });

        privacyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ConditionActivity.class);
                intent.putExtra(StaticValues.PRIVACY,true);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

            }
        });

        termBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ConditionActivity.class);
                intent.putExtra(StaticValues.CONDITION,true);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

            }
        });

        String user_id = (AuthManager.getAuthManager().getLoginInfo(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE))).getUserId();
        String token = AuthManager.getAuthManager().getToken(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));

        idTv.setText(user_id);

        new UserInfoGetTask(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object object) {
                if(object!=null){
                    User user = (User)object;
                    emailTv.setText(user.getEmail());
                }
            }
        }, user_id, token).execute();

        return v;
    }

    private void goToSocketWeb(){
        Intent intent = new Intent(getActivity(), NormalWebViewActivity.class);
        intent.putExtra(NormalWebViewActivity.WEBVIEWURL,StaticValues.BLOG_URL);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);


    }


}
