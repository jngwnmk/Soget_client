package com.soget.soget_client.connector;

import android.os.AsyncTask;
import android.util.Log;

import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.common.RESTAPIManager;
import com.soget.soget_client.model.Bookmark;

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
public class TrashBookmarkRequestTask extends AsyncTask<Void, Void, Bookmark> {
    private OnTaskCompleted listener;
    private String token ;
    private String user_id;
    private String bookmark_id;
    ResponseEntity<Bookmark> response;
    public TrashBookmarkRequestTask(OnTaskCompleted listener, String user_id,String bookmark_id, String token){
        this.listener = listener;
        this.user_id = user_id;
        this.bookmark_id = bookmark_id;
        this.token = token;
    }

    @Override
    protected Bookmark doInBackground(Void... params) {
        try{

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            HttpHeaders headers = RESTAPIManager.getRestAPIManager().createHeaders(token);
            headers.setContentType(MediaType.APPLICATION_JSON);
            response = restTemplate.exchange(RESTAPIManager.trashcan_url+user_id+"/"+bookmark_id, HttpMethod.PUT, new HttpEntity(headers), Bookmark.class);
            return response.getBody();

        } catch (Exception e){
            Log.e("TrashBookmarkRequestTask", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bookmark bookmarks){
        listener.onTaskCompleted(bookmarks);
    }
}
