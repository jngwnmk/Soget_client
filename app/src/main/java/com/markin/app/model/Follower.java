package com.markin.app.model;

import java.util.ArrayList;

public class Follower{
	private String userKeyId;        //Unique Object Id
	private String userName;      		//User name
	private String userId;  			//User id in Markin
	private ArrayList<String> tags;
	private boolean privacy;

    public Follower(){
        userKeyId ="";
        userName = "";
        userId = "";
        tags = new ArrayList<String>();
        privacy = false;
    }


    public Follower(String userKeyId, String userName, String userId, ArrayList<String> tags, boolean privacy) {
        this.userKeyId = userKeyId;
        this.userName = userName;
        this.userId = userId;
        this.tags = tags;
        this.privacy = privacy;
    }

    public String getUserKeyId() {
		return userKeyId;
	}
	public void setUserKeyId(String userKeyId) {
		this.userKeyId = userKeyId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public ArrayList<String> getTags() {
		return tags;
	}
	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	public boolean isPrivacy() {
		return privacy;
	}
	public void setPrivacy(boolean privacy) {
		this.privacy = privacy;
	}


}
