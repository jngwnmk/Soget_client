package com.soget.soget_client.connector.common;

import android.os.AsyncTask;
import android.util.Log;

import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.model.Greeting;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by wonmook on 2015-03-15.
 */
public class HttpRequestTask extends AsyncTask<Void, Void, Greeting> {

    private OnTaskCompleted listener;
    public HttpRequestTask(OnTaskCompleted listener){
        this.listener = listener;
    }

    @Override
    protected Greeting doInBackground(Void... params){
        try{
            final String url = "http://rest-service.guides.spring.io/greeting";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            Greeting greeting = restTemplate.getForObject(url, Greeting.class);
            return greeting;
        } catch (Exception e){
            Log.e("HttpRequestTask", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Greeting greeting){
        listener.onTaskCompleted(greeting);
    }
}
