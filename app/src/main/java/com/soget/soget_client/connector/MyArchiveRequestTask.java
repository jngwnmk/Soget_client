package com.soget.soget_client.connector;

import android.os.AsyncTask;
import android.util.Log;

import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.model.Bookmark;

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
 * Created by wonmook on 2015-03-21.
 */
public class MyArchiveRequestTask extends AsyncTask<Void, Void, ArrayList<Bookmark>> {
    private OnTaskCompleted listener;
    private ArrayList<Bookmark> bookmarks;
    private String token ;
    private String user_id;
    public MyArchiveRequestTask(OnTaskCompleted listener, String user_id,String token){
        this.listener = listener;
        this.user_id = user_id;
        this.token = token;
    }
    private HttpHeaders createHeaders(){
        HttpHeaders headers =  new HttpHeaders(){
            {
                String auth =token;
                String authHeader = "Bearer " +  auth;
                set( "Authorization", authHeader );
            }
        };
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");

        return headers;
    }
    @Override
    protected ArrayList<Bookmark> doInBackground(Void... params) {
        try{

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            ResponseEntity<Bookmark[]> response;
            HttpHeaders headers = createHeaders();
            response = restTemplate.exchange(SogetAPI.bookmark_url + "/" + user_id, HttpMethod.GET, new HttpEntity(headers), Bookmark[].class);
            bookmarks = new ArrayList<Bookmark>();
            bookmarks.addAll(Arrays.asList(response.getBody()));
            return bookmarks;

        } catch (Exception e){
            Log.e("RegisterRequestTask", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Bookmark> bookmarks){
        listener.onTaskCompleted(bookmarks);
    }
}
