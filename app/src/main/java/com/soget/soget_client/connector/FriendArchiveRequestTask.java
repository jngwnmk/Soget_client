package com.soget.soget_client.connector;

import android.os.AsyncTask;
import android.util.Log;

import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.common.RESTAPIManager;
import com.soget.soget_client.model.Bookmark;
import com.soget.soget_client.model.Page;

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
public class FriendArchiveRequestTask extends AsyncTask<Void, Void, ArrayList<Bookmark>> {
    private OnTaskCompleted listener;
    private ArrayList<Bookmark> bookmarks;
    private String token ;
    private String user_id;
    private int page_num=0;

    public FriendArchiveRequestTask(OnTaskCompleted listener, String user_id,String token, int page_num){
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
            ResponseEntity<Page> response;
            HttpHeaders headers = RESTAPIManager.getRestAPIManager().createHeaders(token);
            response = restTemplate.exchange(RESTAPIManager.bookmark_friend_url +  user_id+"/"+page_num, HttpMethod.GET, new HttpEntity(headers), Page.class);
            Bookmark[] bookmark = (Bookmark[])(response.getBody().getContent());
            bookmarks = new ArrayList<Bookmark>();
            bookmarks.addAll(Arrays.asList(bookmark));
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

