package com.soget.soget_client.view.Fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.soget.soget_client.R;
import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.common.AuthManager;
import com.soget.soget_client.connector.MyArchiveRequestTask;
import com.soget.soget_client.connector.WebExtractor;
import com.soget.soget_client.model.Bookmark;
import com.soget.soget_client.view.Adapter.BookmarkAdapter;

import java.util.ArrayList;

/**
 * Created by wonmook on 2015-03-18.
 */
public class ArchiveFragment extends Fragment {

    private ArrayList<Bookmark> bookmarks = new ArrayList<Bookmark>();
    private ListView bookmarkListView = null;
    private BookmarkAdapter bookmarkAdapter =null;
    private ProgressDialog pDialog;

    //Github Push Test
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.archive_layout,container, false);
        bookmarkListView = (ListView)rootView.findViewById(R.id.archive_list);
        bookmarkAdapter = new BookmarkAdapter(inflater.getContext(),bookmarks);
        bookmarkListView.setAdapter(bookmarkAdapter);

        pDialog = new ProgressDialog(this.getActivity());
        pDialog.setMessage("Loading....");
        System.out.println("Test");
        pDialog.show();
        OnTaskCompleted onTaskCompleted;
        onTaskCompleted = new OnTaskCompleted(){
            @Override
            public void onTaskCompleted(Object object) {

                Log.d("ArchiveFragment", ((ArrayList<Bookmark>) object).toString());
                ArrayList<Bookmark> raw_bookmark = ((ArrayList<Bookmark>) object);
                OnTaskCompleted webExtractTaskComplete = new OnTaskCompleted(){
                    @Override
                    public void onTaskCompleted(Object object) {
                        bookmarks.clear();
                        bookmarks.addAll((ArrayList<Bookmark>) object);
                        updateBookmarkList();
                        pDialog.dismiss();
                    }
                };
                new WebExtractor(webExtractTaskComplete,raw_bookmark).execute();
            }
        };
        String user_id = (AuthManager.getAuthManager().getLoginInfo(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE))).getUserId();
        String token = AuthManager.getAuthManager().getToken(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        new MyArchiveRequestTask(onTaskCompleted,user_id, token).execute();
        return rootView;
    }

    private void updateBookmarkList(){
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                bookmarkAdapter.notifyDataSetChanged();

            }
        });



    }
}
