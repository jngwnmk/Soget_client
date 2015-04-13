package com.soget.soget_client.view.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.soget.soget_client.R;
import com.soget.soget_client.model.Bookmark;
import com.soget.soget_client.view.Activity.CommentActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by wonmook on 2015-03-21.
 */
public class BookmarkAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<Bookmark> bookmarks;

    public BookmarkAdapter(Context context, ArrayList<Bookmark> bookmarks){
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
        BookmarkWrapper bookmarkWrapper =null;
        if(bookmarkWrapper==null){
            row = inflater.inflate(R.layout.archivce_list_row, null);
            bookmarkWrapper = new BookmarkWrapper(row);
            row.setTag(bookmarkWrapper);
        } else {
            bookmarkWrapper = (BookmarkWrapper)row.getTag();

        }
        //Set title
        Bookmark item = (Bookmark)getItem(position);
        bookmarkWrapper.getTitle().setText(item.getTitle());

        //Set Thumbnail Image
        Picasso.with(mContext).load(item.getImg_url())
                .resizeDimen(R.dimen.list_archive_image_size, R.dimen.list_archive_image_size)
                .centerInside().into(bookmarkWrapper.getThumbnail());

        //Set Tags
        if(item.getTags().size()!=0){
            bookmarkWrapper.getTags().setText(item.getTags().toString());
        } else {
            bookmarkWrapper.getTags().setText(mContext.getResources().getString(R.string.blank));
        }

        //Set num of get
        bookmarkWrapper.getGet_nums().setText(item.getFollowers().size()+mContext.getString(R.string.archive_row_num_get));
        //set num of comment
        bookmarkWrapper.getComment_nums().setText(item.getComments().size()+mContext.getString(R.string.archive_row_num_comment));
        bookmarkWrapper.getComment_nums().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                mContext.startActivity(intent);
            }
        });
        return row;
    }


    private class BookmarkWrapper{
        private View base;
        private ImageView thumbnail;
        private TextView tags;
        private TextView title;
        private TextView get_nums;
        private TextView comment_nums;

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
            }
            return tags;
        }

        public TextView getTitle() {
            if(title==null){
                title = (TextView)base.findViewById(R.id.title);
            }
            return title;
        }

        public TextView getGet_nums() {
            if(get_nums==null){
                get_nums = (TextView)base.findViewById(R.id.get_nums);
            }
            return get_nums;
        }

        public TextView getComment_nums() {
            if(comment_nums==null){
                comment_nums = (TextView)base.findViewById(R.id.comment_nums);
            }
            return comment_nums;
        }
    }

}
