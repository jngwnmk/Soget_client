package com.markin.app.view.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.markin.app.R;
import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.common.AuthManager;
import com.markin.app.common.StaticValues;
import com.markin.app.connector.category.CategoryGetTask;
import com.markin.app.model.Category;
import com.markin.app.model.User;
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
        HashSet<String> categorySet = new HashSet<>();
        for(int i = 0 ; i < categories.size() ; ++i) {
            categorySet.add(categories.get(i).getType());
        }
        SharedPreferences prefs = getActivity().getSharedPreferences(StaticValues.PREFERENCE.CATEGORY.NAME, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(StaticValues.PREFERENCE.CATEGORY.TITLE, categorySet);
        editor.commit();
    }

}
