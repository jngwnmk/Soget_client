package com.soget.soget_client.connector.user;

import android.os.AsyncTask;
import android.util.Log;

import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.common.RESTAPIManager;
import com.soget.soget_client.model.Comment;
import com.soget.soget_client.model.User;

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
 * Created by wonmook on 15. 5. 23..
 */
public class UserInfoGetTask extends AsyncTask<Void, Void, User> {
    private OnTaskCompleted listener;
    private User user;
    private String token ;
    private String user_id;

    public UserInfoGetTask(OnTaskCompleted listener, String user_id, String token){
        this.listener = listener;
        this.user_id = user_id;
        this.token = token;
    }

    @Override
    protected User doInBackground(Void... params) {
        try{

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            ResponseEntity<User> response;
            HttpHeaders headers = RESTAPIManager.getRestAPIManager().createHeaders(token);
            response = restTemplate.exchange(RESTAPIManager.user_info_url+user_id, HttpMethod.GET, new HttpEntity(headers), User.class);
            user = response.getBody();
            return user;

        } catch (Exception e){
            Log.e("ArchiveMineTask", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(User user){
        listener.onTaskCompleted(user);
    }
}

