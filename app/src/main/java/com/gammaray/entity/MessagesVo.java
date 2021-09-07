package com.gammaray.entity;

public class MessagesVo {

    /**
     * id : 88
     * title : New Message
     * body : The work you uploaded has passed the review, Click for details.
     * read : false
     * created_at : 1619064054
     * updated_at : 1619064054
     */

    private int id;
    private String title;
    private String body;
    private boolean read;
    private long created_at;
    private long updated_at;
    private String resource_type;
    private String resource_id;
    private String action_str;

    public String getResource_type() {
        return resource_type;
    }

    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }

    public String getResource_id() {
        return resource_id;
    }

    public void setResource_id(String resource_id) {
        this.resource_id = resource_id;
    }

    public String getAction_str() {
        return action_str;
    }

    public void setAction_str(String action_str) {
        this.action_str = action_str;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at = updated_at;
    }
}
