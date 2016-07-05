package com.markin.app.view.Fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.markin.app.R;

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

        return v;
    }
}
