package com.markin.app.model;

import java.util.List;

/**
 * Created by wonmook on 2015-03-15.
 */
public class User {

    private String id;
    private String name;
    private String userId;
    private String email;
    private String password;
    private String facebookProfile;
    private String invitationCode;
    private List<String> friends;
    private List<String> friendsRequestReceived;
    private List<String> friendsRequestSent;
    private List<String> bookmarks;
    private List<String> trashcan;
    private List<String> invitation;
    private List<String> invitation_sent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFacebookProfile() {
        return facebookProfile;
    }

    public void setFacebookProfile(String facebookProfile) {
        this.facebookProfile = facebookProfile;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public List<String> getFriendsRequestReceived() {
        return friendsRequestReceived;
    }

    public void setFriendsRequestReceived(List<String> friendsRequestReceived) {
        this.friendsRequestReceived = friendsRequestReceived;
    }

    public List<String> getFriendsRequestSent() {
        return friendsRequestSent;
    }

    public void setFriendsRequestSent(List<String> friendsRequestSent) {
        this.friendsRequestSent = friendsRequestSent;
    }

    public List<String> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(List<String> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public List<String> getInvitation() {
        return invitation;
    }

    public void setInvitation(List<String> invitation) {
        this.invitation = invitation;
    }

    public List<String> getTrashcan() {
        return trashcan;
    }

    public void setTrashcan(List<String> trashcan) {
        this.trashcan = trashcan;
    }

    public List<String> getInvitation_sent() {
        return invitation_sent;
    }

    public void setInvitation_sent(List<String> invitation_sent) {
        this.invitation_sent = invitation_sent;
    }

    @Override
    public String toString() {
        return "{" +
                "\"name\":\"" + name + '"' +
                ", \"userId\":\"" + userId + '"' +
                ", \"email\":\"" + email + '"' +
                ", \"password\":\"" + password + '"' +
                ", \"facebookProfile\":\"" + facebookProfile + '"' +
                ", \"invitationCode\":\"" + invitationCode +'"' +
                ", \"friends\":" + friends +
                ", \"friendsRequestReceived\":" + friendsRequestReceived +
                ", \"friendsRequestSent\":" + friendsRequestSent +
                ", \"bookmarks\":" + bookmarks +
                ", \"trashcan\":" + trashcan+
                ", \"invitation\":" + invitation +
                ", \"invitation_sent\":" + invitation_sent+
                "}";
    }


}
