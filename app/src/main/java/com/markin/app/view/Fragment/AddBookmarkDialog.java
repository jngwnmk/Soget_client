package com.markin.app.view.Fragment;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.markin.app.R;
import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.common.AuthManager;
import com.markin.app.connector.bookmark.BookmarkAddTask;
import com.markin.app.connector.bookmark.BookmarkMakeTask;
import com.markin.app.model.Bookmark;
import com.markin.app.view.component.ConfirmToast;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by wonmook on 2015-03-22.
 */
public class AddBookmarkDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private ArrayList<String> tags = new ArrayList<String>();
    private String  inputUrl = "";
    private Spinner privacySpinner = null;
    private EditText urlEt = null;
    private EditText tagEt = null;
    private ArrayList<String> privacyType = null;
    private boolean privacy = false;
    private Button addBookmarkBtn = null;
    private OnTaskCompleted listener =null;
    private Bookmark refBookmark = null;
    private ProgressDialog pDialog;


    public OnTaskCompleted getListener() {
        return listener;
    }

    public void setListener(OnTaskCompleted listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.add_bookmark_dialog,container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.popup_black_background);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.privacy_array, R.layout.privacy_spinner_item);//new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, privacyType);
        privacySpinner = (Spinner)rootView.findViewById(R.id.privacy_spinner);

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
        });
        pDialog = new ProgressDialog(this.getActivity());
        pDialog.setMessage("Loading....");

        return rootView;
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
        if(urlEt!=null){
            urlEt.setText(getInputUrl());
        }
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
