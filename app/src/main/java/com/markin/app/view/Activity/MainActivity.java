package com.markin.app.view.Activity;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.markin.app.R;
import com.markin.app.callback.FragmentChangeListener;
import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.view.Fragment.AddBookmarkDialog;
import com.markin.app.view.Fragment.CategoryFragment;
import com.markin.app.view.Fragment.DiscoverFragment;
import com.markin.app.view.Fragment.FeedFragment;
import com.markin.app.view.Fragment.FriendsFragment;

import java.util.HashMap;

/**
 * Created by wonmook on 2015-03-18.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentChangeListener{

    private String shared_url = "";
    public static boolean autoMarking = false;
    private TextView titleTv = null;
    private ImageButton addBookmarkBtn = null;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        /*//Change to Archive Tab
        FragmentManager fm = getFragmentManager();
        TabsFragment tabsFragment =(TabsFragment) fm.findFragmentById(R.id.tabs_fragment);
        if (tabsFragment != null)
            tabsFragment.onTabChanged(TabsFragment.TAB_HOME);*/


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        titleTv = (TextView)findViewById(R.id.toolbar_title);

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        addBookmarkBtn = (ImageButton)findViewById(R.id.add_bookmark_btn);
        addBookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*android.app.FragmentManager fm = getFragmentManager();
                AddBookmarkDialog addBookmarkDialog = new AddBookmarkDialog();
                addBookmarkDialog.show(fm, "add_bookmark_dialog");*/
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        System.out.println("onResume():MainActivity");

        Fragment fragment = null;
        fragment = new CategoryFragment();
        titleTv.setText("Category");
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
/*
        Intent intent = getIntent();
        if(intent!=null){
            if(intent.getExtras()!=null){
                shared_url = intent.getExtras().getString("SHARED_URL");
            }
        }
        showAddDialog();
*/

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
            titleTv.setText("Category");
        } else if (id == R.id.nav_friend) {
            fragment = new FriendsFragment();
            titleTv.setText("Connect");
        } else if (id == R.id.nav_setting) {
            fragment = new FriendsFragment();
            titleTv.setText("Connect");
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
