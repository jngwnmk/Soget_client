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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.markin.app.R;
import com.markin.app.common.AuthManager;
import com.markin.app.common.SogetUtil;
import com.markin.app.common.StaticValues;
import com.markin.app.connector.bookmark.PrivacyChangeTask;
import com.markin.app.model.Bookmark;
import com.markin.app.model.Follower;
import com.markin.app.view.Activity.CommentActivity;
import com.markin.app.view.component.ConfirmToast;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by wonmook on 2015-03-21.
 */
public class BookmarkAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<Bookmark> bookmarks;
    private String userId;
    private boolean isMyArchive;
    private ConfirmToast toast = null;


    public BookmarkAdapter(Context context, ArrayList<Bookmark> bookmarks, String user_id, boolean isMyArchive){
        this.mContext = context;
        this.bookmarks = bookmarks;
        this.inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.userId = user_id;
        this.isMyArchive = isMyArchive;
        this.toast = new ConfirmToast(mContext);
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
        BookmarkWrapper bookmarkWrapper =null;
        if(row==null){
            row = inflater.inflate(R.layout.archivce_list_row, null);
            bookmarkWrapper = new BookmarkWrapper(row);
            row.setTag(bookmarkWrapper);
        } else {
            bookmarkWrapper = (BookmarkWrapper)row.getTag();

        }
        //Set title
        final Bookmark item = (Bookmark)getItem(position);
        bookmarkWrapper.getTitle().setText(item.getTitle());
        final String bookmark_id = item.getId();
        //Set Thumbnail Image
        if("".equals(item.getImg_url())){
            Picasso.with(mContext).load(R.drawable.archive_noimage).placeholder(R.drawable.archive_noimage)//.into(bookmarkWrapper.getThumbnail());
                    .resizeDimen(R.dimen.list_archive_image_size_w, R.dimen.list_archive_image_size_h)
                    .into(bookmarkWrapper.getThumbnail());

        } else {
            Picasso.with(mContext).load(item.getImg_url()).placeholder(R.drawable.archive_noimage)//.into(bookmarkWrapper.getThumbnail());
                    .resizeDimen(R.dimen.list_archive_image_size_w, R.dimen.list_archive_image_size_h)
                    .into(bookmarkWrapper.getThumbnail());
        }

        ArrayList<String> tags = new ArrayList<String>();
        //final boolean privacy = false;
        tags = (ArrayList<String>)item.getTags();

        /*if(item.getInitUserNickName().equals(userId)){
            //It's a bookmark I created
            //Set Tags
            tags = (ArrayList<String>)item.getTags();
            //Set Privacy
            privacy = item.isPrivacy();

        } else {
            //I am a just follower
            //Let' find a my own tags and privacy setting
            //Set Tags
            tags = findTags((ArrayList<Follower>)(item.getFollowers()));
            //Set Privacy
            privacy = findPrivacy((ArrayList<Follower>)(item.getFollowers()));

        }*/

        //Set Tags
        if(tags.size()!=0){
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < tags.size() ;++i){
                if(i!=tags.size()-1){
                    sb.append(tags.get(i));
                    sb.append(", ");
                } else {
                    sb.append(tags.get(i));
                }
            }
            bookmarkWrapper.getTags().setText(sb.toString());
            bookmarkWrapper.getTags().setVisibility(View.VISIBLE);
        } else {
            bookmarkWrapper.getTags().setVisibility(View.GONE);
        }

        //Set Privacy
        if(isMyArchive){
            if(item.isPrivacy()){
                bookmarkWrapper.getPrivacy().setImageResource(R.drawable.archive_list_lock);
            } else {
                bookmarkWrapper.getPrivacy().setImageResource(R.drawable.archive_list_unlock);
            }
            bookmarkWrapper.getPrivacy().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String token = AuthManager.getAuthManager().getToken(mContext.getApplicationContext().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));

                    if(item.isPrivacy()){
                        new PrivacyChangeTask(userId,item.getMarkinId(),token,false).execute();
                        toast.showToast(mContext.getString(R.string.change_to_public), Toast.LENGTH_SHORT);
                        item.setPrivacy(false);
                        notifyDataSetChanged();
                    } else {
                        new PrivacyChangeTask(userId,item.getMarkinId(),token,true).execute();
                        item.setPrivacy(true);
                        toast.showToast(mContext.getString(R.string.change_to_privacy), Toast.LENGTH_SHORT);
                        notifyDataSetChanged();
                    }
                }
            });
        } else {
            bookmarkWrapper.getPrivacy().setVisibility(View.GONE);
        }


        //Set num of like


        //Set num of get
        bookmarkWrapper.getGet_nums().setText(item.getFollowers().size()+" "+mContext.getString(R.string.archive_row_num_get));
        bookmarkWrapper.getGet_nums().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openComment(bookmark_id,item.getFollowers().size());
            }
        });
        //set num of comment
        bookmarkWrapper.getComment_nums().setText(item.getComments().size()+" "+mContext.getString(R.string.archive_row_num_comment));
        bookmarkWrapper.getComment_nums().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openComment(bookmark_id,item.getFollowers().size());

            }
        });
        return row;
    }
    private void openComment(String bookmark_id, int size){
        Intent intent = new Intent(mContext, CommentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle extras = new Bundle();
        extras.putString(StaticValues.BOOKMARKID, bookmark_id);
        extras.putInt(StaticValues.MARKINNUM, size);
        intent.putExtras(extras);
        mContext.startActivity(intent);
        ((Activity)mContext).overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

    }


    private boolean findPrivacy(ArrayList<Follower> followers){
        for(int i = 0 ; i < followers.size() ;++i){
            if(userId.equals(followers.get(i).getUserId())){
                return followers.get(i).isPrivacy();
            }
        }
        return false;
    }

    private ArrayList<String> findTags(ArrayList<Follower> followers){
        for(int i = 0 ; i < followers.size() ;++i){
            if(userId.equals(followers.get(i).getUserId())){
                return followers.get(i).getTags();
            }
        }
        return new ArrayList<>();
    }




    private class BookmarkWrapper{
        private View base;
        private ImageView thumbnail;
        private TextView tags;
        private TextView title;
        private TextView get_nums;
        private TextView comment_nums;
        private ImageView privacy;

        public BookmarkWrapper(View base){
            this.base = base;
        }

        public ImageView getThumbnail() {
            if(thumbnail==null){
                thumbnail = (ImageView)base.findViewById(R.id.get_thumbnail);
            }
            return thumbnail;
        }

        public TextView getTags() {
            if(tags==null){
                tags = (TextView)base.findViewById(R.id.tag_list);
                tags.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/AppleSDGothicNeo-SemiBold.otf"));

            }
            return tags;
        }

        public TextView getTitle() {
            if(title==null){
                title = (TextView)base.findViewById(R.id.title);
                title.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/AppleSDGothicNeo-SemiBold.otf"));

            }
            return title;
        }

        public TextView getGet_nums() {
            if(get_nums==null){
                get_nums = (TextView)base.findViewById(R.id.get_nums);
                get_nums.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/FrutigerLTStd-Bold.otf"));

            }
            return get_nums;
        }

        public TextView getComment_nums() {
            if(comment_nums==null){
                comment_nums = (TextView)base.findViewById(R.id.comment_nums);
                comment_nums.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/FrutigerLTStd-Bold.otf"));

            }
            return comment_nums;
        }

        public ImageView getPrivacy(){
            if(privacy==null){
                privacy = (ImageView)base.findViewById(R.id.lock_unlock_img);
            }
            return privacy;
        }
    }

}
