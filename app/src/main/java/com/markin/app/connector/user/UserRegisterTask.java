package com.markin.app.connector.user;

import android.os.AsyncTask;
import android.util.Log;

import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.common.RESTAPIManager;
import com.markin.app.model.User;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


/**
 * Created by wonmook on 2015-03-15.
 */
public class UserRegisterTask extends AsyncTask<Void, Void, User> {
    private OnTaskCompleted listener;
    private User user;

    public UserRegisterTask(OnTaskCompleted listener, User user){
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
            Log.e("UserRegisterTask", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(User user){
        listener.onTaskCompleted(user);
    }
}
