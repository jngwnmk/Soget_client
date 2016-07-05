package com.markin.app.connector.friend;

import android.os.AsyncTask;
import android.util.Log;

import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.common.RESTAPIManager;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by wonmook on 2016. 7. 4..
 */
public class FriendCheckWithInvitationTask extends AsyncTask<Void, Void, Boolean> {
    private String token;
    private String my_id;
    private String invitation_num;
    private OnTaskCompleted listener;

    public FriendCheckWithInvitationTask(OnTaskCompleted listener, String my_id, String invitation_num, String token) {
        this.my_id = my_id;
        this.invitation_num = invitation_num;
        this.token = token;
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            ResponseEntity<Boolean> response;
            HttpHeaders headers = RESTAPIManager.getRestAPIManager().createHeaders(token);
            headers.setContentType(MediaType.APPLICATION_JSON);
            response = restTemplate.exchange(RESTAPIManager.invitation_check_url + my_id + "/" + invitation_num, HttpMethod.GET, new HttpEntity(headers), Boolean.class);
            return response.getBody();

        } catch (Exception e) {
            Log.e("FriendAcceptWInviTask", e.getMessage(), e);
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean isSent){
        listener.onTaskCompleted(isSent);
    }
}
