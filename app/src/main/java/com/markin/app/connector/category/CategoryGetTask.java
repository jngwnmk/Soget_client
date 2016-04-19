package com.markin.app.connector.category;

import android.os.AsyncTask;
import android.util.Log;

import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.common.RESTAPIManager;
import com.markin.app.model.Bookmark;
import com.markin.app.model.Category;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by wonmook on 2016. 4. 12..
 */
public class CategoryGetTask extends AsyncTask<Void, Void, ArrayList<Category>> {
    private OnTaskCompleted listener;
    private ArrayList<Category> categories;
    private String token ;
    private String user_id;

    public CategoryGetTask(OnTaskCompleted listener, String user_id, String token){
        this.listener = listener;
        this.user_id = user_id;
        this.token = token;
    }

    @Override
    protected ArrayList<Category> doInBackground(Void... params) {
        try{

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            HttpHeaders headers = RESTAPIManager.getRestAPIManager().createHeaders(token);
            ResponseEntity<Category[]> response = restTemplate.exchange(RESTAPIManager.category_url, HttpMethod.GET, new HttpEntity(headers), Category[].class);
            categories = new ArrayList<>();
            categories.addAll(Arrays.asList(response.getBody()));
            return categories;

        } catch (Exception e){
            Log.e("CategoryGetTask", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Category> categories){
        listener.onTaskCompleted(categories);
    }
}
