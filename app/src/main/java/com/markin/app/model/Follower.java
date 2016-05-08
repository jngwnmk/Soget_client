package com.markin.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Follower implements Parcelable{
	private String userKeyId;        //Unique Object Id
	private String userName;      		//User name
	private String userId;  			//User id in Markin
	private ArrayList<String> tags;
	private boolean privacy;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(userKeyId);
		parcel.writeString(userName);
		parcel.writeString(userId);
		parcel.writeStringList(tags);
		parcel.writeByte((byte) (privacy ? 1 : 0));
	}

	private void readFromParcel(Parcel in){
		userKeyId = in.readString();
		userName = in.readString();
		userId = in.readString();
		in.readStringList(tags);
		privacy = in.readByte() != 0;
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Follower createFromParcel(Parcel in) {
			return new Follower(in);
		}

		public Follower[] newArray(int size) {
			return new Follower[size];
		}
	};

    public Follower(){
        userKeyId ="";
        userName = "";
        userId = "";
        tags = new ArrayList<String>();
        privacy = false;
    }

	public Follower(Parcel in){

		this();
		readFromParcel(in);
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Follower follower = (Follower) o;

		return !(userId != null ? !userId.equals(follower.userId) : follower.userId != null);

	}

	@Override
	public int hashCode() {
		return userId != null ? userId.hashCode() : 0;
	}
}
