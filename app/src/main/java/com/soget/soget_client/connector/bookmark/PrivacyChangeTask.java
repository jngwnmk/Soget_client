package com.soget.soget_client.connector.bookmark;

import android.os.AsyncTask;
import android.util.Log;

import com.soget.soget_client.common.RESTAPIManager;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by wonmook on 15. 6. 4..
 */
public class PrivacyChangeTask extends AsyncTask<Void, Void, Void> {
    private String token;
    private String user_id;
    private String markin_id;
    private boolean privacy;

    public PrivacyChangeTask(String user_id, String markin_id, String token, boolean privacy) {
        this.user_id = user_id;
        this.markin_id = markin_id;
        this.token = token;
        this.privacy = privacy;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            HttpHeaders headers = RESTAPIManager.getRestAPIManager().createHeaders(token);
            headers.setContentType(MediaType.APPLICATION_JSON);
            restTemplate.exchange(RESTAPIManager.privacy_change_url + user_id + "/" + markin_id, HttpMethod.PUT, new HttpEntity(privacy,headers), String.class);


        } catch (Exception e) {
            Log.e("PrivacyChangeTask", e.getMessage(), e);
        }
        return null;
    }
}