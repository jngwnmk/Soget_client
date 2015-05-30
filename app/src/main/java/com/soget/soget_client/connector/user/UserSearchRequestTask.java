package com.soget.soget_client.connector.user;

import android.util.Log;

import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.common.RESTAPIManager;
import com.soget.soget_client.connector.friend.FriendTask;
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
 * Created by wonmook on 15. 5. 16..
 */
public class UserSearchRequestTask extends FriendTask {

    private String keyword;
    public UserSearchRequestTask(OnTaskCompleted listener, String user_id, String token, String keyword) {
        super(listener, user_id, token);
        this.keyword = keyword;
    }


    @Override
    protected ArrayList<User> doInBackground(Void... params) {
        try{

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            ResponseEntity<User[]> response;
            HttpHeaders headers = RESTAPIManager.getRestAPIManager().createHeaders(token);
            response = restTemplate.exchange(RESTAPIManager.user_search_url + user_id+"/"+keyword, HttpMethod.GET, new HttpEntity(headers), User[].class);
            friends = new ArrayList<User>();
            friends.addAll(Arrays.asList(response.getBody()));
            return friends;

        } catch (Exception e){
            Log.e("FriendReceive", e.getMessage(), e);
        }
        return null;
    }
}
