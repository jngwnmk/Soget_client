package com.soget.soget_client.view.Fragment;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import com.soget.soget_client.R;
import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.common.AuthManager;
import com.soget.soget_client.connector.AddBookmarkRequestTask;
import com.soget.soget_client.model.Bookmark;

import java.util.ArrayList;

/**
 * Created by wonmook on 2015-03-22.
 */
public class AddBookmarkDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {
    private Spinner privacySpinner = null;
    private EditText urlEt = null;
    private EditText tagEt = null;
    private ArrayList<String> privacyType = null;
    private boolean privacy = false;
    private Button addBookmarkBtn = null;
    private OnTaskCompleted listener =null;

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
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(R.color.transparent));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.privacy_array, R.layout.privacy_spinner_item);//new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, privacyType);
        privacySpinner = (Spinner)rootView.findViewById(R.id.privacy_spinner);

        privacySpinner.setAdapter(adapter);
        privacySpinner.setOnItemSelectedListener(this);

        urlEt = (EditText) rootView.findViewById(R.id.add_bookmark_url);
        tagEt = (EditText) rootView.findViewById(R.id.add_bookmark_tag);
        addBookmarkBtn = (Button)rootView.findViewById(R.id.add_bookmark_btn);
        addBookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bookmark new_bookmark = new Bookmark();
                new_bookmark.setTitle("Default");
                new_bookmark.setUrl(urlEt.getText().toString());
                new_bookmark.setInitUserId(AuthManager.getLoginInfo(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE)).getUserId());
                new_bookmark.setPrivacy(privacy);
                OnTaskCompleted onTaskCompleted;
                onTaskCompleted = new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(Object object) {
                        Toast.makeText(getActivity(), "Completed",Toast.LENGTH_SHORT).show();
                        getDialog().dismiss();
                        if(listener!=null){
                            listener.onTaskCompleted(object);
                        }
                    }
                };
                String token = AuthManager.getAuthManager().getToken(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
                new AddBookmarkRequestTask(onTaskCompleted,new_bookmark, token).execute();
            }
        });
        return rootView;
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
}
