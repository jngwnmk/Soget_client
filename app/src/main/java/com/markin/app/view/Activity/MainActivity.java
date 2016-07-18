package com.markin.app.view.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.markin.app.R;
import com.markin.app.callback.FragmentChangeListener;
import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.common.AuthManager;
import com.markin.app.common.StaticValues;
import com.markin.app.connector.friend.FriendCheckWithInvitationTask;
import com.markin.app.connector.invitation.InvitationCodeGetTask;
import com.markin.app.connector.invitation.InvitationCodeMakeTask;
import com.markin.app.connector.manage.ManageResetAllDataTask;
import com.markin.app.connector.manage.ManageResetTrashCanDataTask;
import com.markin.app.connector.user.UserInfoGetTask;
import com.markin.app.model.User;
import com.markin.app.view.Fragment.CategoryFragment;
import com.markin.app.view.Fragment.FriendsFragment;
import com.markin.app.view.Fragment.SettingFragment;

import java.util.ArrayList;

/**
 * Created by wonmook on 2015-03-18.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentChangeListener{

    private String shared_url = "";
    public static boolean autoMarking = false;
    private TextView titleTv = null;
    private ImageButton addBookmarkBtn = null;
    ArrayList<String> invitationNum = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        titleTv = (TextView)findViewById(R.id.toolbar_title);
        titleTv.setTextColor(getResources().getColor(R.color.white));
        titleTv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/FrutigerLTStd-Bold.otf"));


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        setNavigationView();

        addBookmarkBtn = (ImageButton)findViewById(R.id.add_bookmark_btn);
        addBookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (invitationNum.size() != 0) {
                    Intent intent = new Intent(MainActivity.this, InvitatonSendActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(StaticValues.INVITATIONCODE, invitationNum.get(0));
                    bundle.putInt(StaticValues.INVITATIONNUM, invitationNum.size());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                }
            }
        });

        Fragment fragment = null;
        fragment = new CategoryFragment();
        titleTv.setText("SOCKET");
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        Intent receiveIntent = getIntent();
        if(receiveIntent!=null){
            final String invitation_num = receiveIntent.getStringExtra(StaticValues.INVITATIONNUM);
            final String invitation_username = receiveIntent.getStringExtra(StaticValues.INVITATIONUSERNAME);
            String user_id = (AuthManager.getAuthManager().getLoginInfo(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE))).getUserId();
            String token = AuthManager.getAuthManager().getToken(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
            Log.d("InvitatioinSendActivity", user_id + "," + invitation_num);
            if(invitation_num!=null && invitation_username!=null && !"".equals(invitation_num) && !"".equals(invitation_username)){
                new FriendCheckWithInvitationTask(new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(Object object) {
                        if(object!=null && (Boolean)object==false){
                            if(invitation_num!=null && !"".equals(invitation_num)){
                                Intent addFriendIntent = new Intent(MainActivity.this, AddFriendActivity.class);
                                addFriendIntent.putExtra(StaticValues.INVITATIONNUM, invitation_num);
                                addFriendIntent.putExtra(StaticValues.INVITATIONUSERNAME, invitation_username);
                                startActivity(addFriendIntent);
                            }
                        }
                    }
                }, user_id, invitation_num, token).execute();
            }


        }
    }

    private void getInvitation(){
        OnTaskCompleted onTaskCompleted;
        final String user_id = (AuthManager.getAuthManager().getLoginInfo(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE))).getUserId();
        final String token = AuthManager.getAuthManager().getToken(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));

        onTaskCompleted = new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object object) {
                if(object!=null){
                    invitationNum.clear();
                    invitationNum.addAll((ArrayList<String>) object);
                    if(invitationNum.size()==0){
                        new InvitationCodeMakeTask(new OnTaskCompleted() {
                            @Override
                            public void onTaskCompleted(Object object) {
                                if(object!=null){
                                    invitationNum.clear();
                                    invitationNum.addAll((ArrayList<String>) object);
                                }
                            }
                        }, user_id, token).execute();
                    }
                }
            }
        };
        new InvitationCodeGetTask(onTaskCompleted, user_id, token).execute();

    }

    private void setNavigationView(){
        final User user = AuthManager.getAuthManager().getLoginInfo(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        int width = (int)Math.round(getResources().getDisplayMetrics().widthPixels*0.6d);
        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        params.width = width;
        navigationView.setLayoutParams(params);

        navigationView.setNavigationItemSelectedListener(this);

        TextView navigationUsernameTv = (TextView)navigationView.getHeaderView(0).findViewById(R.id.navigator_user_name_tv);
        final TextView navigationSocketNumTv = (TextView)navigationView.getHeaderView(0).findViewById(R.id.navigator_socket_num_tv);
        TextView navigationSocketText = (TextView)navigationView.getHeaderView(0).findViewById(R.id.navigator_socket_text);

        navigationUsernameTv.setTextColor(getResources().getColor(R.color.white));
        navigationUsernameTv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/AppleSDGothicNeo-Medium.otf"));

        navigationSocketNumTv.setTextColor(getResources().getColor(R.color.sub_text_color_80));
        navigationSocketNumTv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/AppleSDGothicNeo-Medium.otf"));

        navigationSocketText.setTextColor(getResources().getColor(R.color.white_33));
        navigationSocketText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/AppleSDGothicNeo-Medium.otf"));

        navigationUsernameTv.setText(user.getUserId());

        String token = AuthManager.getAuthManager().getToken(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        new UserInfoGetTask(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object object) {
                if (object!=null){
                    User user = (User) object;
                    navigationSocketNumTv.setText(user.getBookmarks().size()+"");
                }
            }
        }, user.getUserId(), token).execute();



    }

    @Override
    protected void onResume(){
        super.onResume();
        System.out.println("onResume():MainActivity");
        getInvitation();

    }

    private void showAddDialog() {
        /*FragmentManager fm = getFragmentManager();
        DiscoverFragment discoverFragment = (DiscoverFragment)fm.findFragmentById(R.id.tab_1);
        if(discoverFragment!=null){
            if((!"".equals(shared_url))&&autoMarking){
                discoverFragment.showAddDialog(shared_url,null);
                shared_url = "";
                autoMarking = false;
            }
        }*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_socket) {
            // Create a new fragment and specify the planet to show based on position
            fragment = new CategoryFragment();
            titleTv.setText("SOCKET".toUpperCase());
        } else if (id == R.id.nav_friend) {
            fragment = new FriendsFragment();
            titleTv.setText("CONNECT".toUpperCase());
        } else if (id == R.id.nav_setting) {
            fragment = new SettingFragment();
            titleTv.setText("SETTING".toUpperCase());
        } else if (id == R.id.nav_logout){
            AuthManager.logout(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            finish();
            return true;
        } else if (id == R.id.nav_recover_trashcan){
            new ManageResetTrashCanDataTask(new OnTaskCompleted() {
                @Override
                public void onTaskCompleted(Object object) {
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    getParent().finish();
                }
            }).execute();
            return true;
        } else if (id == R.id.nav_reset_alldata){
            new ManageResetAllDataTask(new OnTaskCompleted() {
                @Override
                public void onTaskCompleted(Object object) {
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    getParent().finish();
                }
            }).execute();
            return true;
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * Callback method when the fragment is changed.
     *
     * @param fragment
     */
    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment, fragment.toString());
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    }
}
