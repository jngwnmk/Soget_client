package com.markin.app.connector.bookmark;

import android.os.AsyncTask;
import android.util.Log;

import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.common.RESTAPIManager;
import com.markin.app.model.Bookmark;

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
 * Created by wonmook on 15. 5. 14..
 */
public class ArchiveFriendTask extends AsyncTask<Void, Void, ArrayList<Bookmark>> {
    private OnTaskCompleted listener;
    private ArrayList<Bookmark> bookmarks;
    private String token ;
    private String user_id;
    private int page_num=0;

    public ArchiveFriendTask(OnTaskCompleted listener, String user_id, String token, int page_num){
        this.listener = listener;
        this.user_id = user_id;
        this.token = token;
        this.page_num = page_num;
    }

    @Override
    protected ArrayList<Bookmark> doInBackground(Void... params) {
        try{

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            ResponseEntity<Bookmark[]> response;
            HttpHeaders headers = RESTAPIManager.getRestAPIManager().createHeaders(token);
            response = restTemplate.exchange(RESTAPIManager.bookmark_friend_url +  user_id+"/"+page_num, HttpMethod.GET, new HttpEntity(headers), Bookmark[].class);
            //Bookmark[] bookmark = (Bookmark[])(response.getBody().getContent());
            bookmarks = new ArrayList<Bookmark>();
            bookmarks.addAll(Arrays.asList(response.getBody()));
            return bookmarks;

        } catch (Exception e){
            Log.e("FriendArchiveRequest", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Bookmark> bookmarks){
        listener.onTaskCompleted(bookmarks);
    }
}

