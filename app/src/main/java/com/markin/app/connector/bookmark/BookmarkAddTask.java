package com.markin.app.connector.bookmark;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.markin.app.R;
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
public class BookmarkAddTask extends AsyncTask<Void, Void, Bookmark> {
    private OnTaskCompleted listener;
    private String token ;
    private String user_id;
    private String bookmark_id;
    private Bookmark ref_bookmark;
    ResponseEntity<Bookmark> response;
    private Context context;
    public BookmarkAddTask(Context context, OnTaskCompleted listener, String user_id, String bookmark_id, Bookmark ref_bookmark, String token){
        this.listener = listener;
        this.user_id = user_id;
        this.bookmark_id = bookmark_id;
        this.ref_bookmark = ref_bookmark;
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
            response = restTemplate.exchange(RESTAPIManager.bookmark_url+user_id+"/"+bookmark_id, HttpMethod.PUT, new HttpEntity(ref_bookmark,headers), Bookmark.class);
            return response.getBody();

        } catch (Exception e){
            Log.e("BookmarkAddTask", e.getMessage(), e);
            Toast.makeText(context, context.getResources().getString(R.string.bookmark_duplicate), Toast.LENGTH_SHORT).show();

        }
        return null;
    }

    @Override
    protected void onPostExecute(Bookmark bookmarks){
        listener.onTaskCompleted(bookmarks);
    }
}
