package com.markin.app.view.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.markin.app.R;
import com.markin.app.common.SogetUtil;
import com.markin.app.model.Comment;

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
        commentWrapper.getUserInfoTv().setText(item.getUserName()+" ("+item.getUserId()+")");
        commentWrapper.getDateTv().setText(SogetUtil.calDurationTimeForComment(item.getDate()));
        if("YES".equals(item.getComma())){
            commentWrapper.getCommentTv().setCompoundDrawablesWithIntrinsicBounds(R.drawable.comment_quote, 0, 0, 0);
        } else {
            commentWrapper.getCommentTv().setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }

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
                userInfoTv.setTextColor(mContext.getResources().getColor(R.color.sub_text_color_80));
                userInfoTv.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/AppleSDGothicNeo-SemiBold.otf"));

            }
            return userInfoTv;
        }

        public TextView getDateTv(){
            if(dateTv==null){
                dateTv = (TextView)base.findViewById(R.id.comment_date_tv);
                dateTv.setTextColor(mContext.getResources().getColor(R.color.charcol_text_color_33));
                dateTv.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/AppleSDGothicNeo-SemiBold.otf"));

            }
            return dateTv;
        }

        public TextView getCommentTv(){
            if(commentTv==null){
                commentTv = (TextView)base.findViewById(R.id.comment_content_tv);
                commentTv.setTextColor(mContext.getResources().getColor(R.color.charcol_text_color_66));
                commentTv.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/AppleSDGothicNeo-Regular.otf"));

            }
            return commentTv;
        }
    }
}
