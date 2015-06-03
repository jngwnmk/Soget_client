package com.soget.soget_client.view.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.soget.soget_client.R;
import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.common.AuthManager;
import com.soget.soget_client.common.StaticValues;
import com.soget.soget_client.connector.bookmark.ArchiveFriendTask;
import com.soget.soget_client.model.Bookmark;
import com.soget.soget_client.model.Follower;
import com.soget.soget_client.view.Adapter.BookmarkAdapter;

import java.util.ArrayList;

/**
 * Created by wonmook on 15. 5. 14..
 */
public class FriendArchiveActivity extends ActionBarActivity {

    private ImageButton backBtn = null;
    private TextView friendNameTv = null;
    private ArrayList<Bookmark> bookmarks = new ArrayList<Bookmark>();
    private ListView bookmarkListView = null;
    private BookmarkAdapter bookmarkAdapter =null;
    private ProgressDialog pDialog;
    private String friend_user_id;
    public final static String FRIENDID = "friend_user_id";
    private int page_num = 0;
    private boolean isLastPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_archive_layout);

        Intent intent = getIntent();
        friend_user_id = intent.getExtras().getString(FRIENDID);
        initLayout();
        setBackBtnAction();
    }

    private void initLayout(){
        backBtn = (ImageButton)findViewById(R.id.back_btn);
        friendNameTv = (TextView)findViewById(R.id.friend_user_id_tv);
        friendNameTv.setText(friend_user_id);
        bookmarkListView = (ListView)findViewById(R.id.archive_list);
        bookmarkAdapter = new BookmarkAdapter(getApplicationContext(),bookmarks,friend_user_id,false);
        bookmarkListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = bookmarks.get(position).getUrl();
                //우선은 웹브라우저에서 여는 것으로 하고, 나중에 Webview의 필요성이 있으면 변경
                //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                //startActivity(intent);
                Intent intent = new Intent(getApplicationContext(),WebViewActivity.class);
                Bundle extras = new Bundle();
                extras.putString(WebViewActivity.WEBVIEWURL,url);
                extras.putString(StaticValues.BOOKMARKID,bookmarks.get(position).getId());
                //extras.putParcelable(StaticValues.BOOKMARK, bookmarks.get(position));
                extras.putBoolean(StaticValues.ISMYBOOKMARK, false);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        bookmarkListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            boolean loadMore = false;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (loadMore && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    System.out.println("load more");
                    if(!isLastPage()) {
                        page_num++;
                        getFriendArchive();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                System.out.println("firstVisibleItem:" + firstVisibleItem + ",visibleItemCount:" + visibleItemCount + "," + totalItemCount);
                loadMore = (firstVisibleItem + visibleItemCount == totalItemCount);

            }
        });
        bookmarkListView.setAdapter(bookmarkAdapter);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading....");
    }

    private void setBackBtnAction(){
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateBookmarkList(){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                bookmarkAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getFriendArchive(){
        OnTaskCompleted onTaskCompleted;
        onTaskCompleted = new OnTaskCompleted(){
            @Override
            public void onTaskCompleted(Object object) {

                Log.d("FriendArchiveActivity", ((ArrayList<Bookmark>) object).toString());
                if (object!=null){
                    Log.d("ArchiveFragment", ((ArrayList<Bookmark>) object).toString());
                    if(page_num==0){
                        bookmarks.clear();
                    }
                    ArrayList<Bookmark> moreBookmarks = (ArrayList<Bookmark>) object;
                    if(moreBookmarks!=null){
                        if(moreBookmarks.size()==0){
                            setLastPage(true);
                        } else {
                            setLastPage(false);
                            bookmarks.addAll(moreBookmarks);
                            updateBookmarkList();

                        }
                    }

                    pDialog.dismiss();

                }
            }
        };
        //String user_id = (AuthManager.getAuthManager().getLoginInfo(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE))).getUserId();
        String token = AuthManager.getAuthManager().getToken(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        pDialog.show();
        new ArchiveFriendTask(onTaskCompleted,friend_user_id, token,page_num).execute();
    }

    @Override
    public void onResume(){
        super.onResume();
        getFriendArchive();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }
}
