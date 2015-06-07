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
    public final static String user_info_url = user_url+"info/";
    public final static String register_url = user_url+"register/";
    public final static String check_user_id_url = register_url +"checkUserId/";
    public final static String check_invitation_code_url = register_url+"checkInvitationCode/";
    public final static String bookmark_url = base_url+"bookmark/";
    public final static String bookmark_friend_url = bookmark_url+"friend/";
    public final static String trashcan_url = bookmark_url+"trashcan/";
    public final static String discover_url = bookmark_url+"home/friends/";//{user_id}/{date}/{page_no}
    public final static String privacy_change_url = bookmark_url+"privacy/";//{user_id}/{markin_id}

    public final static String friends_url = user_url+"friends/";
    public final static String friends_sent_url = friends_url+"sent/";
    public final static String friends_receive_url = friends_url+"receive/";
    public final static String friends_accept_url = friends_url;
    public final static String friends_request_url = friends_url;

    public final static String invitation_url = user_url+"invitation/";
    public final static String invitation_use_url = invitation_url + "send/";

    public final static String comment_url = bookmark_url+"comment/";
    public final static String user_search_url = user_url+"search/";
    private static RESTAPIManager restapiManager = new RESTAPIManager();
    private RESTAPIManager(){}
    public static RESTAPIManager getRestAPIManager() {return restapiManager;}

    public static HttpHeaders createHeaders(){
        HttpHeaders headers = new HttpHeaders(){};
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");

        return headers;

    }
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
