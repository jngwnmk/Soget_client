package com.soget.soget_client.view.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.soget.soget_client.R;
import com.soget.soget_client.common.SogetUtil;
import com.soget.soget_client.model.Bookmark;
import com.soget.soget_client.model.Comment;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by wonmook on 15. 5. 16..
 */
public class CommentAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<Comment> comments;

    public CommentAdapter(Context context, ArrayList<Comment> comments){
        this.mContext = context;
        this.comments = comments;
        this.inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CommentWrapper commentWrapper =null;
        if(row==null)
        {
            row = inflater.inflate(R.layout.comment_list_row, null);
            commentWrapper = new CommentWrapper(row);
            row.setTag(commentWrapper);
        } else {
            commentWrapper = (CommentWrapper)row.getTag();

        }
        //Set title
        Comment item = (Comment)getItem(position);
        commentWrapper.getCommentTv().setText(item.getContent());
        commentWrapper.getUserInfoTv().setText(item.getUserName()+"("+item.getUserId()+")");
        commentWrapper.getDateTv().setText(SogetUtil.calDurationTime(item.getDate()));

        return row;
    }

    private class CommentWrapper {
        private View base;
        private TextView userInfoTv;
        private TextView commentTv;
        private TextView dateTv;

        public CommentWrapper(View base){
            this.base = base;
        }

        public TextView getUserInfoTv(){
            if(userInfoTv==null){
                userInfoTv = (TextView)base.findViewById(R.id.comment_user_info_tv);
            }
            return userInfoTv;
        }

        public TextView getDateTv(){
            if(dateTv==null){
                dateTv = (TextView)base.findViewById(R.id.comment_date_tv);
            }
            return dateTv;
        }

        public TextView getCommentTv(){
            if(commentTv==null){
                commentTv = (TextView)base.findViewById(R.id.comment_content_tv);
            }
            return commentTv;
        }
    }
}
