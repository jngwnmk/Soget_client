package com.soget.soget_client.connector;

import android.os.AsyncTask;

import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.model.User;

import java.util.ArrayList;

/**
 * Created by wonmook on 2015-03-23.
 */
public class FriendRequestTask extends AsyncTask<Void, Void, ArrayList<User>> {
    protected OnTaskCompleted listener;
    protected ArrayList<User> friends;
    protected String token ;
    protected String user_id;

    public FriendRequestTask(OnTaskCompleted listener, String user_id, String token){
        this.listener = listener;
        this.user_id = user_id;
        this.token = token;
    }
    @Override
    protected ArrayList<User> doInBackground(Void... params) {
       /* try{

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            ResponseEntity<User[]> response;
            HttpHeaders headers = RESTAPIManager.getRestAPIManager().createHeaders(token);
            response = restTemplate.exchange(RESTAPIManager.friends_url + "/" + user_id, HttpMethod.GET, new HttpEntity(headers), User[].class);
            friends = new ArrayList<User>();
            friends.addAll(Arrays.asList(response.getBody()));
            return friends;

        } catch (Exception e){
            Log.e("FriendRequestTask", e.getMessage(), e);
        }*/
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<User> friends){
        listener.onTaskCompleted(friends);
    }
}
