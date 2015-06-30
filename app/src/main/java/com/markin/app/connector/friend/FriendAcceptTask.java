package com.markin.app.connector.friend;

import android.os.AsyncTask;
import android.util.Log;

import com.markin.app.common.RESTAPIManager;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by wonmook on 15. 5. 26..
 */
public class FriendAcceptTask extends AsyncTask<Void, Void, Void> {
    private String token ;
    private String my_id;
    private String friend_id;

    public FriendAcceptTask( String my_id, String friend_id, String token){
        this.my_id = my_id;
        this.friend_id = friend_id;
        this.token = token;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try{

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            HttpHeaders headers = RESTAPIManager.getRestAPIManager().createHeaders(token);
            headers.setContentType(MediaType.APPLICATION_JSON);
            restTemplate.exchange(RESTAPIManager.friends_accept_url+my_id+"/"+friend_id, HttpMethod.PUT, new HttpEntity(headers), String.class);

        } catch (Exception e){
            Log.e("FriendAcceptTask", e.getMessage(), e);
        }
        return null;
    }

}
