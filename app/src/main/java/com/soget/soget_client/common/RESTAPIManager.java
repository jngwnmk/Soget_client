package com.soget.soget_client.common;

import android.util.Base64;

import org.springframework.http.HttpHeaders;

/**
 * Created by wonmook on 2015-03-15.
 */
public class RESTAPIManager {

    public final static String base_url = "http://52.68.175.130/";
    public final static String auth_url = base_url+"oauth/token";
    public final static String user_url = base_url+"user/";
    public final static String bookmark_url = base_url+"bookmark/";
    public final static String trashcan_url = bookmark_url+"trashcan/";
    public final static String discover_url = bookmark_url+"home/friends/";//{user_id}/{date}/{page_no}
    public final static String friends_url = user_url+"friends/";
    public final static String friends_sent_url = friends_url+"sent/";
    public final static String friends_receive_url = friends_url+"receive/";

    private static RESTAPIManager restapiManager = new RESTAPIManager();
    private RESTAPIManager(){}
    public static RESTAPIManager getRestAPIManager() {return restapiManager;}

    public static HttpHeaders createHeaders(final String token){
        HttpHeaders headers =  new HttpHeaders(){
            {
                String auth =token;
                String authHeader = "Bearer " +  auth;
                set( "Authorization", authHeader );
            }
        };
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");

        return headers;
    }

    public static HttpHeaders createHeaders(final String username, final String password ){
        HttpHeaders headers =  new HttpHeaders(){
            {
                String auth = username + ":" + password;
                String encodedAuth = Base64.encodeToString(auth.getBytes(), 0);
                String authHeader = "Basic " +  encodedAuth;
                set( "Authorization", authHeader );
            }
        };
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("Accept", "application/json");

        return headers;
    }

    public static String getAuthStr(String user_id, String password){
        return "password="+password+"&username="+user_id+"&grant_type=password&scope=write&client_secret=123456&client_id=soget";
    }
}
