package com.markin.app.connector.invitation;

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
 * Created by wonmook on 15. 5. 23..
 */
public class InvitationCodeUseTask extends AsyncTask<Void, Void, Boolean> {
    private OnTaskCompleted listener;
    private String token ;
    private String user_id;
    private String invitation_code;

    public InvitationCodeUseTask(OnTaskCompleted listener, String user_id, String invitation_code, String token){
        this.listener = listener;
        this.user_id = user_id;
        this.token = token;
        this.invitation_code = invitation_code;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try{

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            ResponseEntity<Boolean> response;
            HttpHeaders headers = RESTAPIManager.getRestAPIManager().createHeaders(token);
            headers.setContentType(MediaType.APPLICATION_JSON);
            response = restTemplate.exchange(RESTAPIManager.invitation_use_url +  user_id, HttpMethod.PUT, new HttpEntity<String>(invitation_code, headers), Boolean.class);
            Log.d("InvitatioinSendActivity",response.toString());
            return response.getBody();

        } catch (Exception e){
            Log.e("InvitationCodeUseTask", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Boolean isSent){
        listener.onTaskCompleted(isSent);
    }
}
