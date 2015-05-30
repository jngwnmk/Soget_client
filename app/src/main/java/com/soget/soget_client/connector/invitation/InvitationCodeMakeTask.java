package com.soget.soget_client.connector.invitation;

import android.os.AsyncTask;
import android.util.Log;

import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.common.RESTAPIManager;
import com.soget.soget_client.model.Bookmark;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by wonmook on 15. 5. 15..
 */
public class InvitationCodeMakeTask extends AsyncTask<Void, Void, ArrayList<String>> {
    private OnTaskCompleted listener;
    private String token ;
    private String user_id;
    ResponseEntity<String> response;

    public InvitationCodeMakeTask(OnTaskCompleted listener, String user_id, String token){
        this.listener = listener;
        this.user_id = user_id;
        this.token = token;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... params) {
        try{
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            ResponseEntity<String[]> response;
            HttpHeaders headers = RESTAPIManager.getRestAPIManager().createHeaders(token);
            response = restTemplate.exchange(RESTAPIManager.invitation_url +  user_id, HttpMethod.PUT, new HttpEntity(headers), String[].class);
            return (ArrayList<String>)Arrays.asList(response.getBody());

        } catch (Exception e){
            Log.e("InvitationCodeGetTask", e.getMessage(), e);
        }
        return null;

    }

    @Override
    protected void onPostExecute(ArrayList<String> invitations){
        listener.onTaskCompleted(invitations);
    }
}

