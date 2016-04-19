package com.markin.app.connector.recommend;

import android.os.AsyncTask;
import android.util.Log;

import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.common.RESTAPIManager;
import com.markin.app.model.Bookmark;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by wonmook on 15. 5. 13..
 */
public class RecommendDiscardTask extends AsyncTask<Void, Void, Void> {
    private OnTaskCompleted listener;
    private String token ;
    private String user_id;
    private String bookmark_id;
    ResponseEntity<Bookmark> response;
    public RecommendDiscardTask(OnTaskCompleted listener, String user_id, String bookmark_id, String token){
        this.listener = listener;
        this.user_id = user_id;
        this.bookmark_id = bookmark_id;
        this.token = token;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try{

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            HttpHeaders headers = RESTAPIManager.getRestAPIManager().createHeaders(token);
            headers.setContentType(MediaType.APPLICATION_JSON);
            restTemplate.exchange(RESTAPIManager.trashcan_url+user_id+"/"+bookmark_id, HttpMethod.PUT, new HttpEntity(headers), String.class);


        } catch (Exception e){
            Log.e("TrashBookmarkRequest", e.getMessage(), e);
        }
        return null;
    }

}
