package com.markin.app.view.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.markin.app.R;
import com.markin.app.model.Bookmark;
import com.markin.app.model.Category;

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
        archiveWrapper.getFromNameTv();
        archiveWrapper.getArchiveDateTv();
        archiveWrapper.getTitleTv();
        archiveWrapper.getUrlTv();
        archiveWrapper.getLikeNumTv();
        archiveWrapper.getSocketNumTv();
        archiveWrapper.getCommentNumTv();

        return row;
    }

    private class ArchiveWrapper{
        private View base;
        private TextView fromName;
        private TextView archiveDate;
        private TextView title;
        private TextView url;
        private TextView likeNum;
        private TextView socketNum;
        private TextView commentNum;


        public ArchiveWrapper(View base){
            this.base = base;
        }

        public TextView getFromNameTv(){
            if(fromName==null){
                fromName = (TextView)base.findViewById(R.id.from_name);
                fromName.setTextColor(mContext.getResources().getColor(R.color.sub_text_color));
            }
            return fromName;
        }

        public TextView getArchiveDateTv() {
            if(archiveDate==null){
                archiveDate = (TextView)base.findViewById(R.id.archive_date);
                archiveDate.setTextColor(mContext.getResources().getColor(R.color.main_text_color));
            }
            return archiveDate;
        }

        public TextView getTitleTv() {
            if(title==null){
                title = (TextView)base.findViewById(R.id.archive_title);
                title.setTextColor(mContext.getResources().getColor(R.color.main_text_color));
            }
            return title;
        }

        public TextView getUrlTv() {
            if(url==null){
                url = (TextView)base.findViewById(R.id.archive_url);
                url.setTextColor(mContext.getResources().getColor(R.color.main_text_color));
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


    }
}
