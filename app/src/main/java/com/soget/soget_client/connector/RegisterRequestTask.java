package com.soget.soget_client.connector;

import android.os.AsyncTask;
import android.util.Log;

import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.common.RESTAPIManager;
import com.soget.soget_client.model.User;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


/**
 * Created by wonmook on 2015-03-15.
 */
public class RegisterRequestTask extends AsyncTask<Void, Void, User> {
    private OnTaskCompleted listener;
    private User user;

    public RegisterRequestTask(OnTaskCompleted listener, User user){
        this.listener = listener;
        this.user = user;
    }

    @Override
    protected User doInBackground(Void... params){
        try{
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            User registered_user = restTemplate.postForObject(RESTAPIManager.user_url+"register", user, User.class);
            return registered_user;
        } catch (Exception e){
            Log.e("RegisterRequestTask", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(User user){
        listener.onTaskCompleted(user);
    }
}
