package com.soget.soget_client.view.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.soget.soget_client.R;
import com.soget.soget_client.model.Bookmark;
import com.soget.soget_client.view.Activity.CommentActivity;
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
        View row = convertView;
        DiscoverkWrapper discoverkWrapper =null;
        if(discoverkWrapper==null){
            row = inflater.inflate(R.layout.home_list_row, null);
            discoverkWrapper = new DiscoverkWrapper(row);
            row.setTag(discoverkWrapper);
        } else {
            discoverkWrapper = (DiscoverkWrapper)row.getTag();

        }
        //Set title
        Bookmark item = (Bookmark)getItem(position);
        discoverkWrapper.getTitle().setText(item.getTitle());

        //Set Thumbnail Image
        Picasso.with(mContext).load(item.getImg_url())//.into(bookmarkWrapper.getThumbnail());
                .resizeDimen(R.dimen.list_discover_image_size_w, R.dimen.list_discover_image_size_h)
                .into(discoverkWrapper.getCoverImg());

        //Set Tags
        if(item.getTags().size()!=0){
            discoverkWrapper.getTags().setText(item.getTags().toString());
        } else {
            discoverkWrapper.getTags().setText(mContext.getResources().getString(R.string.blank));
        }


        //Set num of get
        discoverkWrapper.getGet_nums().setText(item.getFollowers().size()+" "+mContext.getString(R.string.archive_row_num_get));
        //set num of comment
        discoverkWrapper.getComment_nums().setText(item.getComments().size()+" "+mContext.getString(R.string.archive_row_num_comment));
        discoverkWrapper.getComment_nums().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                mContext.startActivity(intent);
            }
        });
        return row;
    }

    private class DiscoverkWrapper{
        private View base;
        private ImageView coverImg;
        private TextView tags;
        private TextView title;
        private TextView get_nums;
        private TextView comment_nums;
        private TextView desc;

        public DiscoverkWrapper(View base){
            this.base = base;
        }

        public ImageView getCoverImg() {
            if(coverImg==null){
                coverImg = (ImageView)base.findViewById(R.id.discover_cover_img);
            }
            return coverImg;
        }

        public TextView getTags() {
            if(tags==null){
                tags = (TextView)base.findViewById(R.id.discover_tags);
            }
            return tags;
        }

        public TextView getTitle() {
            if(title==null){
                title = (TextView)base.findViewById(R.id.discover_title);
            }
            return title;
        }

        public TextView getGet_nums() {
            if(get_nums==null){
                get_nums = (TextView)base.findViewById(R.id.discover_get);
            }
            return get_nums;
        }

        public TextView getComment_nums() {
            if(comment_nums==null){
                comment_nums = (TextView)base.findViewById(R.id.discover_comment);
            }
            return comment_nums;
        }


        public TextView getDesc(){
            if(desc==null){
                desc = (TextView)base.findViewById(R.id.discover_desc);
            }
            return desc;
        }
    }
}