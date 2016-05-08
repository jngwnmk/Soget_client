package com.markin.app.connector.bookmark;

import android.os.AsyncTask;
import android.util.Log;

import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.common.RESTAPIManager;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by wonmook on 2016. 5. 8..
 */
public class LikeCancelTask extends AsyncTask<Void, Void, Integer> {
    private String token;
    private String user_id;
    private String bookmark_id;
    private OnTaskCompleted listener;

    public LikeCancelTask(OnTaskCompleted listener , String user_id, String bookmark_id, String token) {
        this.user_id = user_id;
        this.bookmark_id = bookmark_id;
        this.token = token;
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        try {

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            HttpHeaders headers = RESTAPIManager.getRestAPIManager().createHeaders(token);
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<Integer> response = restTemplate.exchange(RESTAPIManager.like_cancel_url + bookmark_id + "/" + user_id, HttpMethod.PUT, new HttpEntity(headers), Integer.class);
            return  response.getBody();
        } catch (Exception e) {
            Log.e("LikeCancelTask", e.getMessage(), e);
        }
        return -1;
    }

    @Override
    protected void onPostExecute(Integer success){
        listener.onTaskCompleted(success);
    }
}
