package com.markin.app.view.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.markin.app.R;
import com.markin.app.callback.FragmentChangeListener;
import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.callback.PageMove;
import com.markin.app.callback.RecommendViewActionListener;
import com.markin.app.common.AuthManager;
import com.markin.app.common.StaticValues;
import com.markin.app.connector.bookmark.LikeCancelTask;
import com.markin.app.connector.bookmark.LikeTask;
import com.markin.app.connector.recommend.RecommendDiscardTask;
import com.markin.app.connector.recommend.RecommendGetTask;
import com.markin.app.model.Bookmark;
import com.markin.app.model.Comment;
import com.markin.app.model.Follower;
import com.markin.app.model.User;
import com.markin.app.view.Activity.AddBookmarkActivity;
import com.markin.app.view.Activity.CommentActivity;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by wonmook on 2016. 4. 13..
 */
public class RecommendFragment extends Fragment {

    private RelativeLayout recommendLayout = null;
    private SwipeLayout swipeLayout =null;
    private TextView fromNameTv = null;
    private TextView titleTv = null;
    private TextView urlTv = null;
    private TextView descTv = null;
    private TextView continueTv = null;
    private TextView likeNumTv = null;
    private TextView followNumTv = null;
    private TextView commentNumTv = null;
    private ImageView backgroundIv = null;
    private RecommendViewActionListener recommendViewActionListener = null;
    private Bookmark bookmark = null;
    private View under_top_view = null;
    private View under_down_view = null;
    private boolean isOpened = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recommend_list_item, container, false);
        Bundle bundle = this.getArguments();
        bookmark = bundle.getParcelable("bookmark");

        under_top_view = v.findViewById(R.id.bottom_wrapper_up);
        under_down_view = v.findViewById(R.id.bottom_wrapper_down);
        swipeLayout =  (SwipeLayout)v.findViewById(R.id.surfaceview);
        recommendLayout = (RelativeLayout) v.findViewById(R.id.recommend_card_layout);
        fromNameTv = (TextView)v.findViewById(R.id.recommend_user_name);
        titleTv = (TextView)v.findViewById(R.id.recommend_title);
        urlTv = (TextView)v.findViewById(R.id.recommend_url);
        descTv = (TextView)v.findViewById(R.id.recommend_desc);
        likeNumTv = (TextView)v.findViewById(R.id.recommend_like_num);
        followNumTv = (TextView)v.findViewById(R.id.recommend_socket_num);
        commentNumTv  = (TextView)v.findViewById(R.id.recommend_comment_num);
        backgroundIv = (ImageView)v.findViewById(R.id.recommend_background_img);

        initSwipeAction();

        initFromName();

        initTitle();

        initUrl();

        initFirstComment();

        initLike();

        initSocket();

        initComment();

        initBackgroundImg();

        return v;
    }

    private void initSwipeAction(){
        //set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.setRightSwipeEnabled(false);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Bottom, under_down_view);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Top, under_top_view);
        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
                recommendViewActionListener.release();
                if (isOpened) {
                    isOpened = false;
                }
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.

            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

                recommendViewActionListener.swiping();

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
                //recommendViewActionListener.swiping();
                if (!isOpened) {
                    isOpened = true;
                    if (swipeLayout.getCurrentBottomView() == under_top_view) {
                        Log.d(FeedFragment.TAG, "Open Top");
                        //TODO:MarkIn Bookmark
                        Intent addBookmarIntent = new Intent(getActivity(), AddBookmarkActivity.class);
                        addBookmarIntent.putExtra("Bookmark", bookmark);
                        getActivity().startActivityForResult(addBookmarIntent, 0);

                    } else if (swipeLayout.getCurrentBottomView() == under_down_view) {
                        Log.d(FeedFragment.TAG, "Open Down");
                        //TODO:Discard recommend
                        trashBookmark(bookmark.getId());
                    }
                }

            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.

            }
        });
    }

    private void initFromName(){
        fromNameTv.setTextColor(getResources().getColor(R.color.sub_text_color));
        fromNameTv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/AppleSDGothicNeo-Medium.otf"));
        fromNameTv.setText(bookmark.getInitUserName());

    }

    private void initTitle(){
        titleTv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/AppleSDGothicNeo-SemiBold.otf"));
        titleTv.setText(bookmark.getTitle());

    }

    private void initUrl(){
        urlTv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/AppleSDGothicNeo-Regular.otf"));
        try {
            URL simpleURL = new URL(bookmark.getUrl());
            urlTv.setText(simpleURL.getProtocol()+"//"+simpleURL.getHost());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void initFirstComment(){
        descTv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/AppleSDGothicNeo-Regular.otf"));
        descTv.setText(bookmark.getDescription());


    }

    private void initLike(){
        likeNumTv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/AppleSDGothicNeo-Regular.otf"));
        if(bookmark.getLike()!=null){
            try{
                User user = AuthManager.getAuthManager().getLoginInfo(getContext().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
                if(user!=null){
                    String user_id = user.getUserId();
                    if(bookmark.getLike().contains(user_id)){
                        likeNumTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_icon_feed_pressed, 0, 0, 0);
                        likeNumTv.setTag(true);
                    } else {
                        likeNumTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_icon, 0, 0, 0);
                        likeNumTv.setTag(false);
                    }
                }
            } catch (NullPointerException ex){
                ex.printStackTrace();
            }

        }
        likeNumTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((Boolean) likeNumTv.getTag()) {
                    likeCancel();
                } else {
                    like();
                }
            }
        });
        if(bookmark.getLike()!=null){
            likeNumTv.setText(String.valueOf(bookmark.getLike().size()));
        } else {
            likeNumTv.setText("0");
        }
    }

    private void initSocket(){
        followNumTv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/AppleSDGothicNeo-Regular.otf"));
        if(bookmark.getFollowers()!=null){
            followNumTv.setText(String.valueOf(bookmark.getFollowers().size()));
            try{
                User user = AuthManager.getAuthManager().getLoginInfo(getContext().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
                if(user!=null){
                    String user_id = user.getUserId();
                    Follower checkFollower = new Follower();
                    checkFollower.setUserId(user_id);
                    if(bookmark.getFollowers().contains(checkFollower)){
                        followNumTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.socket_icon_pressed, 0, 0, 0);
                        followNumTv.setTag(true);
                    } else {
                        followNumTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.socket_icon, 0, 0, 0);
                        followNumTv.setTag(false);
                    }
                }
            } catch (NullPointerException ex){
                ex.printStackTrace();
            }
        } else {
            followNumTv.setText("0");
        }
    }

    private void initComment(){
        commentNumTv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/AppleSDGothicNeo-Regular.otf"));
        commentNumTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openComment(bookmark.getId(), bookmark.getComments().size());
            }
        });

        if(bookmark.getComments()!=null){
            commentNumTv.setText(String.valueOf(bookmark.getComments().size()));
            try{
                User user = AuthManager.getAuthManager().getLoginInfo(getContext().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
                if(user!=null){
                    String user_id = user.getUserId();
                    Comment checkCommend = new Comment();
                    checkCommend.setUserId(user_id);
                    if(bookmark.getComments().contains(checkCommend)){
                        commentNumTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.comment_icon_pressed, 0, 0, 0);
                        commentNumTv.setTag(true);
                    } else {
                        commentNumTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.comment_icon, 0, 0, 0);
                        commentNumTv.setTag(false);
                    }
                }
            } catch (NullPointerException ex){
                ex.printStackTrace();
            }
        } else {
            commentNumTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.comment_icon, 0, 0, 0);
            commentNumTv.setText("0");
        }
    }

    private void initBackgroundImg(){
        if(bookmark.getImg_url()!=null && !"".equals(bookmark.getImg_url()))
            Picasso.with(getContext()).load(bookmark.getImg_url())
                    .fit().centerCrop()
                    .into(backgroundIv);

    }

    private void like(){
        try{
            User user = AuthManager.getAuthManager().getLoginInfo(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
            if(user!=null){
                String user_id = user.getUserId();
                String token = AuthManager.getAuthManager().getToken(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
                new LikeTask(new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(Object object) {
                        if((Integer)object!=-1){
                            likeNumTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_icon_feed_pressed, 0, 0, 0);
                            likeNumTv.setTag(true);
                            likeNumTv.setText(String.valueOf((Integer)object));
                        }
                    }
                },user_id, bookmark.getId(), token).execute();
            }

        } catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

    private void likeCancel(){
        try{
            User user = AuthManager.getAuthManager().getLoginInfo(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
            if(user!=null){
                String user_id = user.getUserId();
                String token = AuthManager.getAuthManager().getToken(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
                new LikeCancelTask(new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(Object object) {
                        if((Integer)object!=-1){
                            likeNumTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_icon, 0, 0, 0);
                            likeNumTv.setTag(false);
                            likeNumTv.setText(String.valueOf((Integer)object));
                       }
                    }
                }, user_id, bookmark.getId(), token).execute();
            }

        } catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

    private void trashBookmark(final String bookmark_id){
        User user = AuthManager.getAuthManager().getLoginInfo(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        if(user!=null){
            String user_id = user.getUserId();
            String token = AuthManager.getAuthManager().getToken(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
            new RecommendDiscardTask(new OnTaskCompleted() {
                @Override
                public void onTaskCompleted(Object object) {
                    recommendViewActionListener.discard(bookmark_id);
                }
            }, user_id, bookmark_id, token).execute();
        }
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

    public RecommendFragment setSwipeActionListener(RecommendViewActionListener recommendViewActionListener){
        this.recommendViewActionListener = recommendViewActionListener;
        return this;
    }

    @Override
    public void onResume(){
        super.onResume();
        isOpened = false;
    }


}
