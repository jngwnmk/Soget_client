package com.markin.app.connector.user;

import android.os.AsyncTask;
import android.util.Log;

import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.common.RESTAPIManager;
import com.markin.app.model.Authorization;
import com.markin.app.model.User;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by wonmook on 2015-03-22.
 */
public class UserLoginTask extends AsyncTask<Void, Void, Authorization> {

    private OnTaskCompleted listener;
    private User user;

    public UserLoginTask(OnTaskCompleted listener, User user){
        this.listener = listener;
        this.user = user;
    }

    @Override
    protected Authorization doInBackground(Void... params) {
        try{
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            ResponseEntity<Authorization> response;
            HttpHeaders headers = RESTAPIManager.getRestAPIManager().createHeaders("soget","123456");
            String auth_str = RESTAPIManager.getRestAPIManager().getAuthStr(user.getUserId(),user.getPassword());
            System.out.println("Login(auth_str):"+auth_str);
            response = restTemplate.exchange(RESTAPIManager.auth_url, HttpMethod.POST, new HttpEntity<String>(auth_str,headers), Authorization.class);
            return response.getBody();
        } catch (Exception e){
            Log.e("UserLoginTask", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Authorization authorization){
        listener.onTaskCompleted(authorization);
    }
}
