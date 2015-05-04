package com.soget.soget_client.view.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.soget.soget_client.R;
import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.common.AuthManager;
import com.soget.soget_client.connector.MyArchiveRequestTask;
import com.soget.soget_client.connector.WebExtractor;
import com.soget.soget_client.model.Bookmark;
import com.soget.soget_client.view.Activity.WebViewActivity;
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
    private ImageButton settingBtn = null;
    private ImageButton addBtn = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.archive_layout,container, false);
        settingBtn = (ImageButton)rootView.findViewById(R.id.setting_btn);
        addBtn = (ImageButton)rootView.findViewById(R.id.add_get_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Show Add Dialog
                FragmentManager fm = getFragmentManager();
                AddBookmarkDialog addBookmarkDialog = new AddBookmarkDialog();
                addBookmarkDialog.setListener(new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(Object object) {
                        getMyArchive();
                    }
                });
                addBookmarkDialog.show(fm,"add_bookmark_dialog");
            }
        });

        bookmarkListView = (ListView)rootView.findViewById(R.id.archive_list);
        bookmarkAdapter = new BookmarkAdapter(inflater.getContext(),bookmarks);
        bookmarkListView.setAdapter(bookmarkAdapter);
        bookmarkListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = bookmarks.get(position).getUrl();
                //우선은 웹브라우저에서 여는 것으로 하고, 나중에 Webview의 필요성이 있으면 변경
                //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                //startActivity(intent);
                Intent intent = new Intent(getActivity(),WebViewActivity.class);
                Bundle extras = new Bundle();
                extras.putString(WebViewActivity.WEBVIEWURL,url);
                intent.putExtras(extras);
                startActivity(intent);

            }
        });
        pDialog = new ProgressDialog(this.getActivity());
        pDialog.setMessage("Loading....");
        pDialog.show();
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

    private void getMyArchive(){
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
    }

    @Override
    public void onResume(){
        super.onResume();
        getMyArchive();
    }


}
