package com.markin.app.connector.category;

import android.os.AsyncTask;
import android.util.Log;

import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.common.RESTAPIManager;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by wonmook on 2016. 5. 6..
 */
public class CategoryArchiveCountTask extends AsyncTask<Void, Void, Integer> {
    private OnTaskCompleted listener;
    private String token ;
    private String user_id;
    private String category;

    public CategoryArchiveCountTask(OnTaskCompleted listener, String user_id, String category, String token){
        this.listener = listener;
        this.user_id = user_id;
        this.token = token;
        this.category = category;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        try{

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            HttpHeaders headers = RESTAPIManager.getRestAPIManager().createHeaders(token);
            ResponseEntity<Integer> response = restTemplate.exchange(RESTAPIManager.category_url+user_id+"/"+category, HttpMethod.GET, new HttpEntity(headers), Integer.class);
            return response.getBody();

        } catch (Exception e){
            Log.e("CategoryFeedCountTask", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Integer archiveCount){
        listener.onTaskCompleted(archiveCount);
    }
}
