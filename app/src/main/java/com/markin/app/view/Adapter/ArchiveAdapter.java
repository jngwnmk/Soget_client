package com.markin.app.view.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.markin.app.R;
import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.common.AuthManager;
import com.markin.app.common.SogetUtil;
import com.markin.app.common.StaticValues;
import com.markin.app.connector.bookmark.LikeCancelTask;
import com.markin.app.connector.bookmark.LikeTask;
import com.markin.app.model.Bookmark;
import com.markin.app.model.Category;
import com.markin.app.model.Comment;
import com.markin.app.model.User;
import com.markin.app.view.Activity.CommentActivity;

import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by wonmook on 2016. 4. 13..
 */
public class ArchiveAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<Bookmark> bookmarks;


    public ArchiveAdapter(Context context, ArrayList<Bookmark> bookmarks){
        this.mContext = context;
        this.bookmarks = bookmarks;
        this.inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return bookmarks.size();
    }

    @Override
    public Object getItem(int position) {
        return bookmarks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ArchiveWrapper archiveWrapper =null;
        if(row==null){
            row = inflater.inflate(R.layout.feed_archive_row, null);
            archiveWrapper = new ArchiveWrapper(row);
            row.setTag(archiveWrapper);
        } else {
            archiveWrapper = (ArchiveWrapper)row.getTag();

        }

        final Bookmark bookmark = (Bookmark)getItem(position);
        final ArchiveWrapper finalArchiveWrapper = archiveWrapper;

        archiveWrapper.getViaTv();
        archiveWrapper.getFromNameTv().setText(bookmark.getInitUserName());
        archiveWrapper.getArchiveDateTv().setText(SogetUtil.calDurationTimeForComment(bookmark.getDate()));
        archiveWrapper.getTitleTv().setText(bookmark.getTitle());

        URL simpleURL = null;
        try {
            simpleURL = new URL(bookmark.getUrl());
            archiveWrapper.getUrlTv().setText(simpleURL.getProtocol() + "//" + simpleURL.getHost());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        archiveWrapper.getLikeNumTv().setText(bookmark.getLike().size() + "");
        try{
            User user = AuthManager.getAuthManager().getLoginInfo(mContext.getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
            if(user!=null){
                String user_id = user.getUserId();
                if(bookmark.getLike().contains(user_id)){
                    archiveWrapper.getLikeNumTv().setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_icon_pressed, 0, 0, 0);
                    archiveWrapper.getLikeNumTv().setTag(true);
                } else {
                    archiveWrapper.getLikeNumTv().setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_icon_list, 0, 0, 0);
                    archiveWrapper.getLikeNumTv().setTag(false);
                }
            }
        } catch (NullPointerException ex){
            ex.printStackTrace();
        }
        archiveWrapper.getLikeNumTv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((Boolean) finalArchiveWrapper.getLikeNumTv().getTag()) {
                    likeCancel(finalArchiveWrapper, bookmark);
                } else {
                    like(finalArchiveWrapper, bookmark);
                }
            }
        });

        archiveWrapper.getSocketNumTv().setText(bookmark.getFollowers().size() + "");
        try{
            User user = AuthManager.getAuthManager().getLoginInfo(mContext.getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
            if(user!=null){
                String user_id = user.getUserId();
                if(bookmark.getFollowers().contains(user_id)){
                    archiveWrapper.getSocketNumTv().setCompoundDrawablesWithIntrinsicBounds(R.drawable.socket_icon_pressed, 0, 0, 0);
                    archiveWrapper.getSocketNumTv().setTag(true);
                } else {
                    archiveWrapper.getSocketNumTv().setCompoundDrawablesWithIntrinsicBounds(R.drawable.socket_icon_list, 0, 0, 0);
                    archiveWrapper.getSocketNumTv().setTag(false);
                }
            }
        } catch (NullPointerException ex){
            ex.printStackTrace();
        }

        archiveWrapper.getCommentNumTv().setText(bookmark.getComments().size() + "");
        try{
            User user = AuthManager.getAuthManager().getLoginInfo(mContext.getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
            if(user!=null){
                String user_id = user.getUserId();
                Comment checkCommend = new Comment();
                checkCommend.setUserId(user_id);
                if(bookmark.getComments().contains(checkCommend)){
                    archiveWrapper.getCommentNumTv().setCompoundDrawablesWithIntrinsicBounds(R.drawable.comment_icon_pressed, 0, 0, 0);
                    archiveWrapper.getCommentNumTv().setTag(true);
                } else {
                    archiveWrapper.getCommentNumTv().setCompoundDrawablesWithIntrinsicBounds(R.drawable.comment_icon_list, 0, 0, 0);
                    archiveWrapper.getCommentNumTv().setTag(false);
                }
            }
        } catch (NullPointerException ex){
            ex.printStackTrace();
        }

        archiveWrapper.getCommentNumTv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openComment(bookmark.getId(), bookmark.getComments().size());
            }
        });

        if(position%2==0){
            archiveWrapper.getDivider().setBackground(mContext.getResources().getDrawable(R.drawable.line_1));
        } else {
            archiveWrapper.getDivider().setBackground(mContext.getResources().getDrawable(R.drawable.line_2));
        }
        return row;
    }

    private void like(final ArchiveWrapper archiveWrapper, final Bookmark bookmark){
        try{
            User user = AuthManager.getAuthManager().getLoginInfo(mContext.getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
            if(user!=null){
                String user_id = user.getUserId();
                String token = AuthManager.getAuthManager().getToken(mContext.getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
                new LikeTask(new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(Object object) {
                        if((Integer)object!=-1){
                            archiveWrapper.getLikeNumTv().setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_icon_pressed, 0, 0, 0);
                            archiveWrapper.getLikeNumTv().setTag(true);
                            archiveWrapper.getLikeNumTv().setText(String.valueOf((Integer) object));
                        }
                    }
                },user_id, bookmark.getId(), token).execute();
            }

        } catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

    private void likeCancel(final ArchiveWrapper archiveWrapper, final Bookmark bookmark){
        try{
            User user = AuthManager.getAuthManager().getLoginInfo(mContext.getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
            if(user!=null){
                String user_id = user.getUserId();
                String token = AuthManager.getAuthManager().getToken(mContext.getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
                new LikeCancelTask(new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(Object object) {
                        if((Integer)object!=-1){
                            archiveWrapper.getLikeNumTv().setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_icon_list, 0, 0, 0);
                            archiveWrapper.getLikeNumTv().setTag(false);
                            archiveWrapper.getLikeNumTv().setText(String.valueOf((Integer)object));
                        }
                    }
                }, user_id, bookmark.getId(), token).execute();
            }

        } catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

    private void openComment(String bookmark_id, int size){
        Intent intent = new Intent(mContext, CommentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle extras = new Bundle();
        extras.putString(StaticValues.BOOKMARKID,bookmark_id);
        extras.putInt(StaticValues.MARKINNUM, size);
        intent.putExtras(extras);
        mContext.startActivity(intent);
        ((Activity) mContext).overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    private class ArchiveWrapper{
        private View base;
        private TextView via;
        private TextView fromName;
        private TextView archiveDate;
        private TextView title;
        private TextView url;
        private TextView likeNum;
        private TextView socketNum;
        private TextView commentNum;
        private LinearLayout divider;


        public ArchiveWrapper(View base){
            this.base = base;
        }

        public TextView getViaTv(){
            if(via==null){
                via = (TextView)base.findViewById(R.id.via_tv);
                via.setTextColor(mContext.getResources().getColor(R.color.light_text_color));
                via.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/AppleSDGothicNeo-Regular.otf"));

            }
            return via;
        }
        public TextView getFromNameTv(){
            if(fromName==null){
                fromName = (TextView)base.findViewById(R.id.from_name);
                fromName.setTextColor(mContext.getResources().getColor(R.color.sub_text_color));
                fromName.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/AppleSDGothicNeo-Regular.otf"));
            }
            return fromName;
        }

        public TextView getArchiveDateTv() {
            if(archiveDate==null){
                archiveDate = (TextView)base.findViewById(R.id.archive_date);
                archiveDate.setTextColor(mContext.getResources().getColor(R.color.charcol_text_color_33));
                archiveDate.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/AppleSDGothicNeo-Regular.otf"));

            }
            return archiveDate;
        }

        public TextView getTitleTv() {
            if(title==null){
                title = (TextView)base.findViewById(R.id.archive_title);
                title.setTextColor(mContext.getResources().getColor(R.color.charcol_text_color_80));
                title.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/AppleSDGothicNeo-Medium.otf"));

            }
            return title;
        }

        public TextView getUrlTv() {
            if(url==null){
                url = (TextView)base.findViewById(R.id.archive_url);
                url.setTextColor(mContext.getResources().getColor(R.color.light_text_color));
                url.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/AppleSDGothicNeo-Regular.otf"));

            }
            return url;
        }

        public TextView getLikeNumTv() {
            if(likeNum==null){
                likeNum = (TextView)base.findViewById(R.id.archive_like);
                likeNum.setTextColor(mContext.getResources().getColor(R.color.main_text_color));
            }
            return likeNum;
        }

        public TextView getSocketNumTv() {
            if(socketNum==null){
                socketNum = (TextView)base.findViewById(R.id.archive_socket_num);
                socketNum.setTextColor(mContext.getResources().getColor(R.color.main_text_color));
            }
            return socketNum;
        }

        public TextView getCommentNumTv() {
            if(commentNum==null){
                commentNum = (TextView)base.findViewById(R.id.archive_comment_num);
                commentNum.setTextColor(mContext.getResources().getColor(R.color.main_text_color));
            }
            return commentNum;
        }

        public LinearLayout getDivider(){
            if(divider==null){
                divider = (LinearLayout)base.findViewById(R.id.archive_divider);

            }
            return divider;
        }


    }
}
