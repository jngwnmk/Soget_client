package com.markin.app.connector.bookmark;

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
 * Created by wonmook on 2015-03-22.
 */
public class BookmarkMakeTask extends AsyncTask<Void, Void, Bookmark> {
    private OnTaskCompleted listener;
    private String token ;
    private String user_id;
    private Bookmark new_bookmark;
    ResponseEntity<Bookmark> response;
    public BookmarkMakeTask(OnTaskCompleted listener, Bookmark bookmark, String token){
        this.listener = listener;
        this.new_bookmark = bookmark;
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
            System.out.println("bookmark:"+new_bookmark.toString());
            response = restTemplate.exchange(RESTAPIManager.bookmark_url, HttpMethod.POST, new HttpEntity(new_bookmark,headers), Bookmark.class);
            return response.getBody();

        } catch (Exception e){
            Log.e("BookmarkMakeTask", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bookmark bookmarks){
        listener.onTaskCompleted(bookmarks);
    }
}