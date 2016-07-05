package com.markin.app.view.Fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.markin.app.R;
import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.common.AuthManager;
import com.markin.app.common.SogetUtil;
import com.markin.app.common.StaticValues;
import com.markin.app.connector.category.CategoryGetTask;
import com.markin.app.model.Category;
import com.markin.app.model.User;
import com.markin.app.view.Activity.FeedActivity;
import com.markin.app.view.Activity.SettingActivity;
import com.markin.app.view.Activity.WebViewActivity;
import com.markin.app.view.Adapter.CategoryAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wonmook on 2016. 4. 12..
 */
public class CategoryFragment extends Fragment {
    private ArrayList<Category> categories = new ArrayList<Category>();
    private ListView categoryListView = null;
    private CategoryAdapter categoryAdapter =null;
    private ProgressDialog pDialog = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.category_layout,container, false);

        //Setup Category ListView
        categories = new ArrayList<>();
        categories.add(new Category());
        categories.add(new Category());
        categories.add(new Category());

        categoryListView = (ListView)rootView.findViewById(R.id.category_list);
        categoryAdapter = new CategoryAdapter(getActivity(), categories);
        categoryListView.setAdapter(categoryAdapter);
        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(),FeedActivity.class);
                Bundle extras = new Bundle();
                extras.putString(StaticValues.CATEGORY, categories.get(i).getType());
                intent.putExtras(extras);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

            }
        });


        pDialog = new ProgressDialog(this.getActivity());
        pDialog.setMessage("Loading....");
        getGetegoryList();
        return rootView;
    }

    public void getGetegoryList(){
        OnTaskCompleted onTaskCompleted;
        onTaskCompleted = new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object object) {
                if(object!=null){
                    categories.clear();
                    categories.addAll((ArrayList<Category>) object);
                    saveCategory((ArrayList<Category>)object);
                    categoryListView.setAdapter(categoryAdapter);
                    categoryAdapter.notifyDataSetChanged();
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

    private void saveCategory(ArrayList<Category> categories){

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < categories.size(); i++) {
            sb.append(categories.get(i).getType()).append(",");
        }
        if(getActivity()!=null){
            SharedPreferences prefs = getActivity().getSharedPreferences(StaticValues.PREFERENCE.CATEGORY.NAME, getActivity().MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(StaticValues.PREFERENCE.CATEGORY.TITLE, sb.toString());
            editor.commit();
        }

    }

}
