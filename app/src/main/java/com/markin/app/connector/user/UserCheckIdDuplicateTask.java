package com.markin.app.connector.user;

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
 * Created by wonmook on 15. 5. 22..
 */
public class UserCheckIdDuplicateTask extends AsyncTask<Void, Void, Boolean> {
    private OnTaskCompleted listener;
    private String user_id;

    public UserCheckIdDuplicateTask(OnTaskCompleted listener, String user_id){
        this.listener = listener;
        this.user_id = user_id;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try{

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            ResponseEntity<Boolean> response;
            HttpHeaders headers = RESTAPIManager.getRestAPIManager().createHeaders();
            response = restTemplate.exchange(RESTAPIManager.check_user_id_url+user_id, HttpMethod.GET, new HttpEntity(headers), Boolean.class);
            return response.getBody();

        } catch (Exception e){
            Log.e("UserCheckIdDuplicateTask", e.getMessage(), e);
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean isDuplicated){
        listener.onTaskCompleted(isDuplicated);
    }
}
