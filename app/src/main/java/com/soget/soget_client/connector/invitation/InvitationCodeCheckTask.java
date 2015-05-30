package com.soget.soget_client.connector.invitation;

import android.os.AsyncTask;
import android.util.Log;

import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.common.RESTAPIManager;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by wonmook on 15. 5. 22..
 */
public class InvitationCodeCheckTask extends AsyncTask<Void, Void, Boolean> {

        private OnTaskCompleted listener;
        private String invitation_code;

        public InvitationCodeCheckTask(OnTaskCompleted listener, String invitation_code){
                this.listener = listener;
                this.invitation_code = invitation_code;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            ResponseEntity<Boolean> response;
            HttpHeaders headers = RESTAPIManager.getRestAPIManager().createHeaders();
            response = restTemplate.exchange(RESTAPIManager.check_invitation_code_url+invitation_code, HttpMethod.GET, new HttpEntity(headers), Boolean.class);
            return response.getBody();

            } catch (Exception e){
            Log.e("InvitationCodeCheckTask", e.getMessage(), e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean valid){
                 listener.onTaskCompleted(valid);
        }
}
