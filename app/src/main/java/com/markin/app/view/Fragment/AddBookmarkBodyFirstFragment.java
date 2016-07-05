package com.markin.app.view.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.markin.app.R;
import com.markin.app.callback.AddBookmarkListener;
import com.markin.app.common.StaticValues;

import org.w3c.dom.Text;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by wonmook on 2016. 4. 27..
 */

public class AddBookmarkBodyFirstFragment extends Fragment{
    private FrameLayout categoryFrame;
    private EditText commentEt;
    private TextView categoryTv;
    private String cateogry="Listening";
    private AddBookmarkListener addBookmarkListener = null;
    private static AddBookmarkBodyFirstFragment instance = null;

    public static AddBookmarkBodyFirstFragment getInstance(){
        if(instance==null){
            instance = new AddBookmarkBodyFirstFragment();
        }
        return instance;
    }

    public AddBookmarkBodyFirstFragment(){

    }

    public String getCateogry() {
        return cateogry;
    }

    public AddBookmarkBodyFirstFragment setCateogry(String cateogry) {
        this.cateogry = cateogry;
        return this;

    }

    public EditText getCommentEt() {
        return commentEt;
    }

    public void setCommentEt(EditText commentEt) {
        this.commentEt = commentEt;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_bookmark_dialog_sub_first, container, false);

        commentEt = (EditText)rootView.findViewById(R.id.comment_et);
        commentEt.setTextColor(getResources().getColor(R.color.charcol_text_color_80));
        commentEt.setHintTextColor(getResources().getColor(R.color.charcol_text_color_33));
        commentEt.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/AppleSDGothicNeo-Medium.otf"));

        categoryTv = (TextView)rootView.findViewById(R.id.category_tv);
        categoryTv.setTextColor(getResources().getColor(R.color.charcol_text_color_80));
        categoryTv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/FrutigerLTStd-Bold.otf"));

        categoryFrame = (FrameLayout)rootView.findViewById(R.id.category_selector);
        categoryFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBookmarkListener.gotoCategoryList();
            }
        });

        if(categoryTv!=null){
            categoryTv.setText(this.cateogry);
        }
        return rootView;

    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (!(context instanceof AddBookmarkListener)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        addBookmarkListener = (AddBookmarkListener) context;

    }
}
