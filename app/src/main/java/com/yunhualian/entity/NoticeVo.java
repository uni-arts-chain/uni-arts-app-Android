package com.yunhualian.entity;


import java.io.Serializable;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/12/03 上午10:26
 * Desc   : description
 */
public class NoticeVo implements Serializable {


    /**
     * id : 226
     * created_at : 1543564323
     * updated_at : 1543564323
     * title : Rfinex暂停BPX充提的通知
     * content :
     * auth_id : 90533
     * start_at : 0
     * end_at : 0
     * disabled : false
     * locale : zh-CN
     * notice_url : https://rfinex.zendesk.com/hc/zh-cn/articles/360020412491
     */

    private long id;
    private long created_at;
    private long updated_at;
    private String title;
    private String content;
    private long auth_id;
    private long start_at;
    private long end_at;
    private boolean disabled;
    private String locale;
    private String notice_url;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getAuth_id() {
        return auth_id;
    }

    public void setAuth_id(long auth_id) {
        this.auth_id = auth_id;
    }

    public long getStart_at() {
        return start_at;
    }

    public void setStart_at(long start_at) {
        this.start_at = start_at;
    }

    public long getEnd_at() {
        return end_at;
    }

    public void setEnd_at(long end_at) {
        this.end_at = end_at;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getNotice_url() {
        return notice_url;
    }

    public void setNotice_url(String notice_url) {
        this.notice_url = notice_url;
    }

}
