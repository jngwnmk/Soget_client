package com.markin.app.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.List;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Bookmark{
    private String id;
    private String title;
    private String url;
    private String img_url;
    private String description;
    private String initUserId;
    private String initUserName;
    private String initUserNickName;
    private List<Follower> followers;
    private long date;
    private boolean privacy;
    private List<Comment> comments;
    private List<String> tags;
    private List<String> category;
    private String markinId;

    public Bookmark() {
        id = "";
        title = "";
        url = "";
        img_url = "";
        description = "";
        initUserId = "";
        initUserName = "";
        initUserNickName = "";
        followers = new ArrayList<Follower>();
        date = 0L;
        privacy = false;
        comments = new ArrayList<Comment>();
        tags = new ArrayList<String>();
        category = new ArrayList<String>();
        markinId = "";
    }



    public Bookmark(String id, String title, String url, String img_url, String description, String initUserId, String initUserName, String initUserNickName, List<Follower> followers, long date, boolean privacy, List<Comment> comments, List<String> tags, List<String> category) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.img_url = img_url;
        this.description = description;
        this.initUserId = initUserId;
        this.initUserName = initUserName;
        this.initUserNickName = initUserNickName;
        this.followers = followers;
        this.date = date;
        this.privacy = privacy;
        this.comments = comments;
        this.tags = tags;
        this.category = category;
    }

    public Bookmark(String id, String title, String url, String img_url, String description, String initUserId, String initUserName, String initUserNickName, List<Follower> followers, long date, boolean privacy, List<Comment> comments, List<String> tags, List<String> category, String markinId) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.img_url = img_url;
        this.description = description;
        this.initUserId = initUserId;
        this.initUserName = initUserName;
        this.initUserNickName = initUserNickName;
        this.followers = followers;
        this.date = date;
        this.privacy = privacy;
        this.comments = comments;
        this.tags = tags;
        this.category = category;
        this.markinId = markinId;
    }

    @Override
    public String toString() {
        return "Bookmark{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", img_url='" + img_url + '\'' +
                ", description='" + description + '\'' +
                ", initUserId='" + initUserId + '\'' +
                ", initUserName='" + initUserName + '\'' +
                ", initUserNickName='" + initUserNickName + '\'' +
                ", followers=" + followers +
                ", date=" + date +
                ", privacy=" + privacy +
                ", comments=" + comments +
                ", tags=" + tags +
                ", category=" + category +
                '}';
    }

    private String getTagString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for (int i = 0; i < tags.size(); ++i) {
            sb.append("\"" + tags.get(i) + "\"");
            if (i != tags.size() - 1) {
                sb.append(",");
            }

        }
        sb.append("]");

        return sb.toString();
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInitUserId() {
        return initUserId;
    }

    public void setInitUserId(String initUserId) {
        this.initUserId = initUserId;
    }

    public List<Follower> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Follower> followers) {
        this.followers = followers;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean isPrivacy() {
        return privacy;
    }

    public void setPrivacy(boolean privacy) {
        this.privacy = privacy;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public String getInitUserName() {
        return initUserName;
    }

    public void setInitUserName(String initUserName) {
        this.initUserName = initUserName;
    }

    public String getInitUserNickName() {
        return initUserNickName;
    }

    public void setInitUserNickName(String initUserNickName) {
        this.initUserNickName = initUserNickName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMarkinId() {
        return markinId;
    }

    public void setMarkinId(String markinId) {
        this.markinId = markinId;
    }
}


