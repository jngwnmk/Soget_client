package com.soget.soget_client.connector;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.model.Authorization;
import com.soget.soget_client.model.User;

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
public class LoginRequestTask extends AsyncTask<Void, Void, Authorization> {

    private OnTaskCompleted listener;
    private User user;

    public LoginRequestTask(OnTaskCompleted listener, User user){
        this.listener = listener;
        this.user = user;
    }

    private HttpHeaders createHeaders(final String username, final String password ){
        HttpHeaders headers =  new HttpHeaders(){
            {
                String auth = username + ":" + password;
                String encodedAuth = Base64.encodeToString(auth.getBytes(),0);
                String authHeader = "Basic " +  encodedAuth;
                set( "Authorization", authHeader );
            }
        };
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("Accept", "application/json");

        return headers;
    }

    @Override
    protected Authorization doInBackground(Void... params) {
        try{
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            ResponseEntity<Authorization> response;
            HttpHeaders headers = createHeaders("soget","123456");
            String auth_str = "password=1234&username=jngwnmk&grant_type=password&scope=write&client_secret=123456&client_id=soget";
            response = restTemplate.exchange(SogetAPI.auth_url, HttpMethod.POST, new HttpEntity<String>(auth_str,headers), Authorization.class);
            return response.getBody();
        } catch (Exception e){
            Log.e("LoginRequestTask", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Authorization authorization){
        listener.onTaskCompleted(authorization);
    }
}
