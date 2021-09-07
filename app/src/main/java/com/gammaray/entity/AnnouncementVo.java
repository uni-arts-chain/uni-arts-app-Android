package com.gammaray.entity;

import java.io.Serializable;

/**
 * Synopsis     com.miner.client.entity.AnnouncementVo
 * Author       mosr
 * Version      1.0.0
 * Create       2020/7/15 14:03
 * Email        intimatestranger@sina.cn
 */
public class AnnouncementVo implements Serializable {

    /**
     * id : 2
     * type : New::New
     * title : 新闻稿件
     * from : 广告人
     * label : 广告
     * summary : 广告说明
     * content : <p>在文件上传的场景（非图片），你可能需要给上传的文件设置&nbsp;<code>Content-Disposition</code>&nbsp;以便于用户直接访问 URL 的时候能够用你期望的文件名或原文件名来下载并保存。</p>
     *
     * <p>In some case, you may need change the&nbsp;<code>Content-Disposition</code>&nbsp;for your uploaded files for allow users visit URL with direct download, and get the original filename.</p>
     *
     * <p>这个时候你需要给 Uploader 实现&nbsp;<code>content_disposition</code>&nbsp;函数，例如：</p>
     *
     * <p>So, you need implement a&nbsp;<code>content_disposition</code>&nbsp;method for your CarrierWave Uploader, for example:</p>
     * cover :
     * link : https://github.com/huacnlee/carrierwave-aliyun
     * created_at : 1593746734
     */

    private int id;
    private String type;
    private String title;
    private String from;
    private String label;
    private String summary;
    private String content;
    private String cover;
    private String link;
    private long created_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(int created_at) {
        this.created_at = created_at;
    }
}
