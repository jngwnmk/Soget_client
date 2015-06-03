package com.soget.soget_client.view.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.soget.soget_client.R;
import com.soget.soget_client.common.SogetUtil;
import com.soget.soget_client.common.StaticValues;
import com.soget.soget_client.model.Bookmark;
import com.soget.soget_client.view.Activity.CommentActivity;
import com.soget.soget_client.view.Activity.FriendArchiveActivity;
import com.soget.soget_client.view.Activity.WebViewActivity;
import com.soget.soget_client.view.component.SwipeTouchListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by saadati on 10/4/14.
 */
public class DiscoverAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<Bookmark> bookmarks;

    public DiscoverAdapter(Context context, ArrayList<Bookmark> bookmarks){
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        DiscoverWrapper discoverWrapper = null;
        if(convertView==null){
            convertView  = inflater.inflate(R.layout.home_list_row, null);
            discoverWrapper = new DiscoverWrapper(convertView);
            convertView.setTag(discoverWrapper);
        } else {
            discoverWrapper = (DiscoverWrapper)convertView.getTag();
        }

        final Bookmark item = (Bookmark)getItem(position);
        System.out.println("Discover" + item.toString());
        final String bookmark_id = item.getId();

        //Set User name & User id
        discoverWrapper.getUserId().setText(item.getInitUserName() + "(" + item.getInitUserNickName() + ")");
        discoverWrapper.getUserId().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id = item.getInitUserNickName();
                Intent intent = new Intent(mContext, FriendArchiveActivity.class);
                intent.putExtra(FriendArchiveActivity.FRIENDID, user_id);
                mContext.startActivity(intent);
            }
        });
        //Set title
        discoverWrapper.getTitle().setText(item.getTitle());
        discoverWrapper.getTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = item.getUrl();
                openWebLink(url, item.getId(),item);
            }
        });

        //Set elapsed Time
        discoverWrapper.getDate().setText(SogetUtil.calDurationTime(item.getDate()));

        //Set Thumbnail Image
        if("".equals(item.getImg_url())){
            Picasso.with(mContext).load(R.drawable.picture_no_image)
                    .placeholder(R.drawable.picture_no_image).fit().centerCrop()//.into(bookmarkWrapper.getThumbnail());
                    //.resizeDimen(R.dimen.list_discover_image_size_w, R.dimen.list_discover_image_size_h)
                    .into(discoverWrapper.getCoverImg());

        } else {
            Picasso.with(mContext).load(item.getImg_url())
                    .placeholder(R.drawable.picture_no_image).fit().centerCrop()//.into(bookmarkWrapper.getThumbnail());
                    //.resizeDimen(R.dimen.list_discover_image_size_w, R.dimen.list_discover_image_size_h)
                    .into(discoverWrapper.getCoverImg());

        }

        //Set Tags
        if(item.getTags().size()!=0){
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < item.getTags().size() ;++i){
                if(i!=item.getTags().size()-1){
                    sb.append(item.getTags().get(i));
                    sb.append(", ");
                } else {
                    sb.append(item.getTags().get(i));
                }

            }
            discoverWrapper.getTags().setText(sb.toString());
        } else {
            discoverWrapper.getTags().setText(mContext.getResources().getString(R.string.blank));
            discoverWrapper.getTags().setCompoundDrawables(null,null,null,null);
        }
        //Set num of get
        discoverWrapper.getGet_nums().setText(item.getFollowers().size()+" "+mContext.getString(R.string.archive_row_num_get));
        discoverWrapper.getGet_nums().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openComment(bookmark_id,item.getFollowers().size());
            }
        });
        //set num of comment
        discoverWrapper.getComment_nums().setText(item.getComments().size()+" "+mContext.getString(R.string.archive_row_num_comment));
        discoverWrapper.getComment_nums().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openComment(bookmark_id, item.getFollowers().size());
            }
        });

        discoverWrapper.getDesc().setText(item.getDescription());
        discoverWrapper.getDesc().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = item.getUrl();
                openWebLink(url, item.getId(),item);
            }
        });
        return convertView;
    }

    private void openWebLink(String url, String bookmark_id, Bookmark ref_bookmark){
        Intent intent = new Intent(mContext,WebViewActivity.class);
        Bundle extras = new Bundle();
        extras.putString(WebViewActivity.WEBVIEWURL,url);
        extras.putString(StaticValues.BOOKMARKID,bookmark_id);
        //extras.putParcelable(StaticValues.BOOKMARK, ref_bookmark);
        extras.putBoolean(StaticValues.ISMYBOOKMARK,false);
        intent.putExtras(extras);
        mContext.startActivity(intent);
    }

    private void openComment(String bookmark_id, int size){
        Intent intent = new Intent(mContext, CommentActivity.class);
        Bundle extras = new Bundle();
        extras.putString(StaticValues.BOOKMARKID,bookmark_id);
        extras.putInt(StaticValues.MARKINNUM, size);
        intent.putExtras(extras);
        mContext.startActivity(intent);
    }

    private class DiscoverWrapper {
        private View base;
        private ImageView coverImg;
        private TextView userId;
        private TextView date;
        private TextView tags;
        private TextView title;
        private TextView get_nums;
        private TextView comment_nums;
        private TextView desc;

        public DiscoverWrapper(View base){
            this.base = base;
        }

        public ImageView getCoverImg() {
            if(coverImg==null){
                coverImg = (ImageView)base.findViewById(R.id.discover_cover_img);
            }
            return coverImg;
        }

        public TextView getUserId(){
            if(userId==null){
                userId = (TextView)base.findViewById(R.id.discover_id);
                userId.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/AppleSDGothicNeo-SemiBold.otf"));

            }
            return userId;
        }

        public TextView getDate(){
            if(date==null){
                date = (TextView)base.findViewById(R.id.discover_date);
                date.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/FrutigerLTStd-Bold.otf"));

            }
            return date;
        }

        public TextView getTags() {
            if(tags==null){
                tags = (TextView)base.findViewById(R.id.discover_tags);
                tags.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/AppleSDGothicNeo-SemiBold.otf"));

            }
            return tags;
        }

        public TextView getTitle() {
            if(title==null){
                title = (TextView)base.findViewById(R.id.discover_title);
                title.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/AppleSDGothicNeo-SemiBold.otf"));

            }
            return title;
        }

        public TextView getGet_nums() {
            if(get_nums==null){
                get_nums = (TextView)base.findViewById(R.id.discover_get);
                get_nums.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/FrutigerLTStd-Bold.otf"));

            }
            return get_nums;
        }

        public TextView getComment_nums() {
            if(comment_nums==null){
                comment_nums = (TextView)base.findViewById(R.id.discover_comment);
                comment_nums.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/FrutigerLTStd-Bold.otf"));

            }
            return comment_nums;
        }


        public TextView getDesc(){
            if(desc==null){
                desc = (TextView)base.findViewById(R.id.discover_desc);
                desc.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/AppleSDGothicNeo-SemiBold.otf"));

            }
            return desc;
        }
    }
}
