package com.yunhualian.entity;

public class ArtAuctionVo {

    /**
     * id : 1
     * topic : Special auction in Nanjing University of the Arts
     * desc : Special auction in Nanjing University of the Arts
     * aasm_state : online
     * start_at : 1612972800
     * end_at : 1620403200
     * img_file : {"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/auction_meeting/img_file/1/6bdc63e9-fe2b-44b5-bee3-7786e14a4b11.jpg"}
     * owner_id : 1
     * art_size : 4
     */

    private int id;
    private String topic;
    private String desc;
    private String aasm_state;
    private String start_at;
    private String end_at;
    private ImgFileBean img_file;
    private int owner_id;
    private int art_size;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAasm_state() {
        return aasm_state;
    }

    public void setAasm_state(String aasm_state) {
        this.aasm_state = aasm_state;
    }

    public String getStart_at() {
        return start_at;
    }

    public void setStart_at(String start_at) {
        this.start_at = start_at;
    }

    public String getEnd_at() {
        return end_at;
    }

    public void setEnd_at(String end_at) {
        this.end_at = end_at;
    }

    public ImgFileBean getImg_file() {
        return img_file;
    }

    public void setImg_file(ImgFileBean img_file) {
        this.img_file = img_file;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public int getArt_size() {
        return art_size;
    }

    public void setArt_size(int art_size) {
        this.art_size = art_size;
    }

    public static class ImgFileBean {
        /**
         * url : https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/auction_meeting/img_file/1/6bdc63e9-fe2b-44b5-bee3-7786e14a4b11.jpg
         */

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
