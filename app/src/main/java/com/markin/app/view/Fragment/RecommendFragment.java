package com.markin.app.view.Fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.markin.app.R;
import com.markin.app.callback.PageMove;
import com.markin.app.model.Bookmark;
import com.squareup.picasso.Picasso;

/**
 * Created by wonmook on 2016. 4. 13..
 */
public class RecommendFragment extends Fragment {
    private TextView fromNameTv = null;
    private TextView titleTv = null;
    private TextView urlTv = null;
    private TextView descTv = null;
    private TextView continueTv = null;
    private TextView likeNumTv = null;
    private TextView followNumTv = null;
    private TextView commentNumTv = null;
    private ImageView backgroundIv = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recommend_list_item, container, false);
        Bundle bundle = this.getArguments();
        Bookmark bookmark = bundle.getParcelable("bookmark");

        fromNameTv = (TextView)v.findViewById(R.id.recommend_user_name);
        fromNameTv.setTextColor(getResources().getColor(R.color.sub_text_color));
        fromNameTv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/AppleSDGothicNeo-Medium.otf"));

        titleTv = (TextView)v.findViewById(R.id.recommend_title);
        titleTv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/AppleSDGothicNeo-SemiBold.otf"));

        urlTv = (TextView)v.findViewById(R.id.recommend_url);
        urlTv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/AppleSDGothicNeo-Regular.otf"));

        descTv = (TextView)v.findViewById(R.id.recommend_desc);
        descTv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/AppleSDGothicNeo-Regular.otf"));

        likeNumTv = (TextView)v.findViewById(R.id.recommend_like_num);
        likeNumTv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/AppleSDGothicNeo-Regular.otf"));

        followNumTv = (TextView)v.findViewById(R.id.recommend_socket_num);
        followNumTv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/AppleSDGothicNeo-Regular.otf"));

        commentNumTv  = (TextView)v.findViewById(R.id.recommend_comment_num);
        commentNumTv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/AppleSDGothicNeo-Regular.otf"));

        backgroundIv = (ImageView)v.findViewById(R.id.recommend_background_img);

        fromNameTv.setText(bookmark.getInitUserName());
        titleTv.setText(bookmark.getTitle());
        urlTv.setText(bookmark.getUrl());
        descTv.setText(bookmark.getDescription());

        if(bookmark.getFollowers()!=null){
            followNumTv.setText(String.valueOf(bookmark.getFollowers().size()));
        } else {
            followNumTv.setText("0");
        }

        if(bookmark.getComments()!=null){
            commentNumTv.setText(String.valueOf(bookmark.getComments().size()));
        } else {
            commentNumTv.setText("0");
        }

        if(bookmark.getImg_url()!=null && !"".equals(bookmark.getImg_url()))
            Picasso.with(getContext()).load(bookmark.getImg_url())
                    .fit().centerCrop()
                    .into(backgroundIv);


        return v;
    }
}
