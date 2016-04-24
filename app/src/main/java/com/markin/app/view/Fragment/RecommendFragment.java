package com.markin.app.view.Fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.markin.app.R;
import com.markin.app.callback.FragmentChangeListener;
import com.markin.app.callback.PageMove;
import com.markin.app.common.StaticValues;
import com.markin.app.model.Bookmark;
import com.markin.app.view.Activity.CommentActivity;
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

    private Bookmark bookmark = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recommend_list_item, container, false);
        Bundle bundle = this.getArguments();
        bookmark = bundle.getParcelable("bookmark");

        SwipeLayout swipeLayout =  (SwipeLayout)v.findViewById(R.id.surfaceview);

        //set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.setRightSwipeEnabled(false);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Bottom, v.findViewById(R.id.bottom_wrapper_down));
        swipeLayout.addDrag(SwipeLayout.DragEdge.Top, v.findViewById(R.id.bottom_wrapper_up));

        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });

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
        commentNumTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openComment(bookmark.getId(), bookmark.getComments().size());
            }
        });


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

    private void openComment(String bookmark_id, int size){
        Intent intent = new Intent(getContext(), CommentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle extras = new Bundle();
        extras.putString(StaticValues.BOOKMARKID,bookmark_id);
        extras.putInt(StaticValues.MARKINNUM, size);
        intent.putExtras(extras);
        getActivity().startActivity(intent);
    }
}
