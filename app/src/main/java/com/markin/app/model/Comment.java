package com.markin.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Comment implements Parcelable{
    private long date;
    private String userKeyId;        //Unique Object Id
    private String userName;      		//User name
    private String userId;  			//User id in Markin
    private String content;


    public Comment(){
        date = 0L;
        userKeyId = "";
        userName = "";
        userId = "";
        content = "";
    }

    public Comment(Parcel in){
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(date);
        parcel.writeString(userKeyId);
        parcel.writeString(userName);
        parcel.writeString(userId);
        parcel.writeString(content);
    }

    private void readFromParcel(Parcel in){
        date = in.readLong();
        userKeyId = in.readString();
        userName = in.readString();
        userId = in.readString();
        content = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public Comment(long date, String userKeyId, String userName, String userId, String content) {

        this.date = date;
        this.userKeyId = userKeyId;
        this.userName = userName;
        this.userId = userId;
        this.content = content;
    }

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

    public String getUserKeyId() {
        return userKeyId;
    }

    public void setUserKeyId(String userKeyId) {
        this.userKeyId = userKeyId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
