package com.markin.app.connector.manage;

import android.os.AsyncTask;
import android.util.Log;

import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.common.RESTAPIManager;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by wonmook on 2016. 5. 16..
 */
public class ManageResetTrashCanDataTask extends AsyncTask<Void, Void, Void> {
    private OnTaskCompleted listener;

    public ManageResetTrashCanDataTask(OnTaskCompleted listener){
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try{

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            HttpHeaders headers = RESTAPIManager.getRestAPIManager().createHeaders();
            ResponseEntity<Boolean> response= restTemplate.exchange(RESTAPIManager.manage_reset_trashcan_url, HttpMethod.DELETE, new HttpEntity(headers), Boolean.class);

        } catch (Exception e){
            Log.e("ManageResetTrashCanDataTask", e.getMessage(), e);
        }

        return null;
    }


}

