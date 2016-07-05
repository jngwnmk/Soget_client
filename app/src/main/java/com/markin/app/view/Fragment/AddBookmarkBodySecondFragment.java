package com.markin.app.view.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.markin.app.R;
import com.markin.app.callback.AddBookmarkListener;
import com.markin.app.common.StaticValues;
import com.markin.app.model.Category;
import com.markin.app.view.Adapter.SimpleCategoryAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wonmook on 2016. 4. 28..
 */
public class AddBookmarkBodySecondFragment extends Fragment{

    private ListView categorylv = null;
    private SimpleCategoryAdapter simpleCategoryAdapter = null;
    private ArrayList<String> categories = new ArrayList<>();
    private AddBookmarkListener addBookmarkListener = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_bookmark_dialog_sub_second, container, false);
        categorylv = (ListView)rootView.findViewById(R.id.category_list);
        categories.addAll(loadCategory());
        simpleCategoryAdapter = new SimpleCategoryAdapter(getActivity(), categories);
        categorylv.setAdapter(simpleCategoryAdapter);
        categorylv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                addBookmarkListener.selectCategory(categories.get(i));
            }
        });
        return rootView;

    }

    private List<String> loadCategory(){
        SharedPreferences prefs = getActivity().getSharedPreferences(StaticValues.PREFERENCE.CATEGORY.NAME, getActivity().MODE_PRIVATE);
        String categoryTitles = prefs.getString(StaticValues.PREFERENCE.CATEGORY.TITLE, "");
        return Arrays.asList(categoryTitles.split(","));
    }

    @Override
    public void onAttach(Context context){

        super.onAttach(context);
        if (!(context instanceof AddBookmarkListener)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        addBookmarkListener = (AddBookmarkListener) context;
    }

}
