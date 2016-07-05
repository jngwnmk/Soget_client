package com.markin.app.view.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.markin.app.R;
import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.common.AuthManager;
import com.markin.app.common.StaticValues;
import com.markin.app.connector.category.CategoryGetTask;
import com.markin.app.model.Bookmark;
import com.markin.app.model.Category;
import com.markin.app.model.User;
import com.markin.app.view.Adapter.AddBookmarkPageAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wonmook on 2015-03-22.
 */
public class AddBookmarkDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private ArrayList<String> tags = new ArrayList<String>();
    private String  inputUrl = "";
    //private Spinner privacySpinner = null;
    //private EditText urlEt = null;
    //private EditText tagEt = null;
    private ArrayList<String> privacyType = null;
    private boolean privacy = false;
    //private Button addBookmarkBtn = null;
    private OnTaskCompleted listener =null;
    private Bookmark refBookmark = null;
    private ProgressDialog pDialog;


    private ArrayList<Category> categories = new ArrayList<>();
    private TextView completeTv = null;
    private TextView justAddTv = null;
    private EditText commentEt = null;
    private TextView categoryTv = null;
    private FrameLayout categorySelector = null;

    private FrameLayout addFragmentLayout = null;


    private ViewPager viewPager;
    private AddBookmarkPageAdapter addBookmarkPageAdapter;

    public OnTaskCompleted getListener() {
        return listener;
    }

    public void setListener(OnTaskCompleted listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.add_bookmark_dialog_1, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.popup_black_background);
        /*justAddTv = (TextView)rootView.findViewById(R.id.just_add_tv);
        completeTv = (TextView)rootView.findViewById(R.id.add_complete_tv);
        commentEt = (EditText)rootView.findViewById(R.id.comment_et);
        categoryTv = (TextView)rootView.findViewById(R.id.category_tv);
        categorySelector = (FrameLayout)rootView.findViewById(R.id.category_selector);


        setupView();*/

        viewPager = (ViewPager)rootView.findViewById(R.id.add_dialog_pager);
        addBookmarkPageAdapter = new AddBookmarkPageAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(addBookmarkPageAdapter);


        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.privacy_array, R.layout.privacy_spinner_item);//new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, privacyType);
        /*privacySpinner = (Spinner)rootView.findViewById(R.id.privacy_spinner);

        privacySpinner.setAdapter(adapter);
        privacySpinner.setOnItemSelectedListener(this);

        urlEt = (EditText) rootView.findViewById(R.id.add_bookmark_url);
        urlEt.setText(getInputUrl());
        urlEt.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/AppleSDGothicNeo-Medium.otf"));

        tagEt = (EditText) rootView.findViewById(R.id.add_bookmark_tag);
        tagEt.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/AppleSDGothicNeo-Medium.otf"));

        if(refBookmark!=null){
            //It is an add bookmark. Not making new one.
            urlEt.setEnabled(false);
            StringBuffer tagStr = new StringBuffer();
            setTags((ArrayList<String>) refBookmark.getTags());
            for(int i = 0 ; i < tags.size() ;++i){
                tagStr.append(tags.get(i));
                if(i<tags.size()-1){
                    tagStr.append(",");
                }

            }
            tagEt.setText(tagStr.toString());
        }


        addBookmarkBtn = (Button)rootView.findViewById(R.id.add_bookmark_btn);
        addBookmarkBtn.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/AppleSDGothicNeo-SemiBold.otf"));
        addBookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = urlEt.getText().toString();

                if(!Patterns.WEB_URL.matcher(url).matches()){
                    urlEt.setError("Invalid URL");

                } else {
                    url = ensure_has_protocol(url);
                    System.out.println("URL:"+url);
                    String user_id = (AuthManager.getAuthManager().getLoginInfo(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE))).getUserId();
                    String token = AuthManager.getAuthManager().getToken(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
                    OnTaskCompleted onTaskCompleted = new OnTaskCompleted() {
                        @Override
                        public void onTaskCompleted(Object object) {
                            pDialog.dismiss();
                            getDialog().dismiss();
                            if (listener != null) {
                                listener.onTaskCompleted(object);
                                if(object!=null){
                                    ConfirmToast toast = new ConfirmToast(getActivity());
                                    toast.showToast("MarkIn 되었습니다.", Toast.LENGTH_SHORT);

                                } else {
                                    ConfirmToast toast = new ConfirmToast(getActivity());
                                    toast.showToast("이미 추가된 URL입니다.", Toast.LENGTH_SHORT);
                                }
                            }
                        }
                    };
                    List<String> taglist = new ArrayList<String>();
                    String tags = tagEt.getText().toString();
                    StringTokenizer st = new StringTokenizer(tags, ",");
                    while (st.hasMoreTokens()) {
                        taglist.add(st.nextToken());
                    }
                    if(refBookmark==null){
                        Bookmark new_bookmark = new Bookmark();
                        new_bookmark.setTitle("Default");
                        new_bookmark.setUrl(url);
                        new_bookmark.setInitUserId(AuthManager.getLoginInfo(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE)).getUserId());
                        new_bookmark.setTags(taglist);
                        new_bookmark.setPrivacy(privacy);
                        pDialog.show();
                        new BookmarkMakeTask(onTaskCompleted, new_bookmark, token).execute();
                    } else {
                        refBookmark.setTags(taglist);
                        refBookmark.setPrivacy(privacy);
                        pDialog.show();
                        new BookmarkAddTask(onTaskCompleted,user_id,refBookmark.getId(), refBookmark, token).execute();
                    }

                }
            }
        });*/
        pDialog = new ProgressDialog(this.getActivity());
        pDialog.setMessage("Loading....");
        getGetegoryList();

        return rootView;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        listener.onTaskCompleted(null);
    }

    private void setupView(){

        justAddTv.setTextColor(getActivity().getResources().getColor(R.color.charcol_text_color_33));
        justAddTv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/AppleSDGothicNeo-Medium.otf"));

        completeTv.setTextColor(getActivity().getResources().getColor(R.color.charcol_text_color_66));
        completeTv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/AppleSDGothicNeo-SemiBold.otf"));

        commentEt.setTextColor(getActivity().getResources().getColor(R.color.light_text_color));
        commentEt.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/AppleSDGothicNeo-Medium.otf"));
        //commentEt.getBackground().mutate().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        categoryTv.setTextColor(getActivity().getResources().getColor(R.color.charcol_text_color_80));
        categoryTv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/FrutigerLTStd-Bold.otf"));

        categorySelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Touch Category", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getGetegoryList(){
        OnTaskCompleted onTaskCompleted;
        onTaskCompleted = new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object object) {
                if(object!=null){
                    categories.clear();
                    categories.addAll((ArrayList<Category>) object);
                    if(categories.size()!=0){
                        categoryTv.setText(categories.get(0).getType());
                    }
                }
                pDialog.dismiss();
            }
        };

        try{
            User user = AuthManager.getAuthManager().getLoginInfo(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
            if(user!=null){
                String user_id = user.getUserId();
                String token = AuthManager.getAuthManager().getToken(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
                pDialog.show();
                new CategoryGetTask(onTaskCompleted,user_id, token).execute();
            }

        } catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }


    private String ensure_has_protocol(String a_url)
    {
        if (a_url.startsWith("http://")||a_url.startsWith("https://"))
        {
            return a_url;
        } else {
            return "http://"+a_url;
        }
        //return a_url;
    }

    @Override
    public void onStart(){
        super.onStart();
        if(getDialog()==null){
            return ;
        }

        int width = getActivity().getResources().getDimensionPixelSize(R.dimen.popup_size_width);
        int height = getActivity().getResources().getDimensionPixelSize(R.dimen.popup_size_height);
        getDialog().getWindow().setLayout(width, height);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position==0){
            privacy = false;
        } else {
            privacy = true;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void updateInputUrl(String url){
        setInputUrl(url);
        //if(urlEt!=null){
          //  urlEt.setText(getInputUrl());
        //}
    }

    public String getInputUrl() {
        return inputUrl;
    }

    public void setInputUrl(String inputUrl) {
        this.inputUrl = inputUrl;
    }

    public Bookmark getRefBookmark() {
        return refBookmark;
    }

    public void setRefBookmark(Bookmark refBookmark) {
        this.refBookmark = refBookmark;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
}
