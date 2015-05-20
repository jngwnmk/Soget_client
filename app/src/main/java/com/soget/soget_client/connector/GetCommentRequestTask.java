package com.soget.soget_client.connector;

import android.os.AsyncTask;
import android.util.Log;

import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.common.RESTAPIManager;
import com.soget.soget_client.model.Bookmark;
import com.soget.soget_client.model.Comment;

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
 * Created by wonmook on 15. 5. 16..
 */
public class GetCommentRequestTask extends AsyncTask<Void, Void, ArrayList<Comment>> {
    private OnTaskCompleted listener;
    private ArrayList<Comment> comments;
    private String token ;
    private String bookmark_id;

    public GetCommentRequestTask(OnTaskCompleted listener, String bookmark_id,String token){
        this.listener = listener;
        this.bookmark_id = bookmark_id;
        this.token = token;
    }

    @Override
    protected ArrayList<Comment> doInBackground(Void... params) {
        try{

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            ResponseEntity<Comment[]> response;
            HttpHeaders headers = RESTAPIManager.getRestAPIManager().createHeaders(token);
            response = restTemplate.exchange(RESTAPIManager.comment_url+bookmark_id, HttpMethod.GET, new HttpEntity(headers), Comment[].class);
            comments = new ArrayList<Comment>();
            comments.addAll(Arrays.asList(response.getBody()));
            return comments;

        } catch (Exception e){
            Log.e("MyArchiveRequestTask", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Comment> comments){
        listener.onTaskCompleted(comments);
    }
}