package com.markin.app.view.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.markin.app.R;
import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.common.AuthManager;
import com.markin.app.common.SogetUtil;
import com.markin.app.connector.category.CategoryArchiveCountTask;
import com.markin.app.connector.category.CategoryFeedCountTask;
import com.markin.app.connector.recommend.RecommendGetTask;
import com.markin.app.model.Category;
import com.markin.app.model.Comment;
import com.markin.app.model.User;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by wonmook on 2016. 4. 12..
 */
public class CategoryAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<Category> categories;


    public CategoryAdapter(Context context, ArrayList<Category> categories){
        this.mContext = context;
        this.categories = categories;
        this.inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CategoryWrapper categoryWrapper =null;
        if(row==null){
            row = inflater.inflate(R.layout.category_list_row, null);
            categoryWrapper = new CategoryWrapper(row);
            row.setTag(categoryWrapper);
        } else {
            categoryWrapper = (CategoryWrapper)row.getTag();

        }

        Category item = (Category)getItem(position);
        categoryWrapper.getDescTv().setText(item.getDesc());
        categoryWrapper.getTypeTv().setText(item.getType());
        categoryWrapper.getAuthorTv().setText(item.getAuthor());
        if(item.getImgUrl()!=null && !"".equals(item.getImgUrl()))
            Picasso.with(mContext).load(item.getImgUrl())
                    .fit().centerCrop()
                    .into(categoryWrapper.getBackgroundView());

        try{
            User user = AuthManager.getAuthManager().getLoginInfo(mContext.getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
            if(user!=null){
                String user_id = user.getUserId();
                String token = AuthManager.getAuthManager().getToken(mContext.getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
                long date = System.currentTimeMillis();
                if(!"".equals(item.getType())){
                    final CategoryWrapper finalCategoryWrapper = categoryWrapper;
                    new CategoryArchiveCountTask(new OnTaskCompleted() {
                        @Override
                        public void onTaskCompleted(Object object) {
                            finalCategoryWrapper.getArchiveCountView().setText(""+object);
                        }
                    }, user_id, item.getType(), token).execute();

                    new CategoryFeedCountTask(new OnTaskCompleted() {
                        @Override
                        public void onTaskCompleted(Object object) {
                            if((Integer)object == 0){
                                finalCategoryWrapper.getFeedIndicatorView().setVisibility(View.INVISIBLE);
                            } else {
                                finalCategoryWrapper.getFeedIndicatorView().setVisibility(View.VISIBLE);

                            }
                        }
                    }, user_id, date,item.getType(), token).execute();
                }


            }

        } catch (NullPointerException ex){
            ex.printStackTrace();
        }


        return row;
    }

    private class CategoryWrapper{
        private View base;
        private TextView type;
        private TextView desc;
        private TextView author;
        private ImageView backgroundView;
        private ImageView feedIndicatorView;
        private TextView  archiveCountView;


        public CategoryWrapper(View base){
            this.base = base;
        }

        public TextView getTypeTv(){
            if(type==null){
                type = (TextView)base.findViewById(R.id.category_type_tv);
                type.setTextColor(mContext.getResources().getColor(R.color.category_text_charcol));
            }
            return type;
        }

        public TextView getDescTv(){
            if(desc==null){
                desc = (TextView)base.findViewById(R.id.category_desc_tv);

            }
            return desc;
        }

        public TextView getAuthorTv(){
            if(author==null){
                author = (TextView)base.findViewById(R.id.category_author_tv);

            }
            return author;
        }

        public ImageView getBackgroundView(){
            if(backgroundView==null){
                backgroundView = (ImageView)base.findViewById(R.id.category_frame);
            }
            return backgroundView;
        }

        public ImageView getFeedIndicatorView(){
            if(feedIndicatorView==null){
                feedIndicatorView = (ImageView)base.findViewById(R.id.category_feed_indicator);
            }
            return feedIndicatorView;
        }

        public TextView getArchiveCountView(){
            if(archiveCountView==null){
                archiveCountView = (TextView)base.findViewById(R.id.archive_count);
                archiveCountView.setTextColor(mContext.getResources().getColor(R.color.category_text_charcol));

            }
            return archiveCountView;
        }
    }
}
