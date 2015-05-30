package com.soget.soget_client.connector.discover;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.common.AuthManager;
import com.soget.soget_client.common.RESTAPIManager;
import com.soget.soget_client.common.SettingManager;
import com.soget.soget_client.model.Bookmark;
import com.soget.soget_client.model.Page;

import org.json.JSONArray;
import org.json.JSONObject;
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
 * Created by wonmook on 15. 5. 13..
 */
public class DiscoverGetTask extends AsyncTask<Void, Void, ArrayList<Bookmark>> {
    private OnTaskCompleted listener;
    private ArrayList<Bookmark> bookmarks;
    private String token ;
    private String user_id;
    private long date;

    public DiscoverGetTask(OnTaskCompleted listener, String user_id, String token, long date){
        this.listener = listener;
        this.user_id = user_id;
        this.token = token;
        this.date = date;
    }

    @Override
    protected ArrayList<Bookmark> doInBackground(Void... params) {
        try{

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            HttpHeaders headers = RESTAPIManager.getRestAPIManager().createHeaders(token);
            System.out.println("currentTime:"+date);
            ResponseEntity<Page> response = restTemplate.exchange(RESTAPIManager.discover_url +  user_id + "/" +date+"/0", HttpMethod.GET, new HttpEntity(headers), Page.class);
            System.out.println(response.getBody());
            Bookmark[] bookmark = (Bookmark[])(response.getBody().getContent());
            bookmarks = new ArrayList<Bookmark>();
            bookmarks.addAll(Arrays.asList(bookmark));
            return bookmarks;

        } catch (Exception e){
            Log.e("DiscoverGetTask", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Bookmark> bookmarks){
        listener.onTaskCompleted(bookmarks);
    }
}