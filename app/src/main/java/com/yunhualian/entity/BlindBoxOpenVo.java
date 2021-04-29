package com.yunhualian.entity;

public class BlindBoxOpenVo {

    /**
     * art_id : 154
     * name : Test137
     * img_main_file1 : {"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/art/img_main_file1/154/fe952397-9974-4230-97ff-18acacec2187.jpeg"}
     * special_attr :
     * opened : 1
     * extrinsic_hash : 0xd5b394e1de3c222b5de42c43b8f93eb6a8c0df530e9b3c52d0f4b4481f116f87
     * left : 9
     * total : 10
     * created_at : 1619582712
     */

    private int art_id;
    private String name;
    private ImgMainFile1Bean img_main_file1;
    private String special_attr;
    private int opened;
    private String extrinsic_hash;
    private int left;
    private int total;
    private int created_at;
    private String resource_type;

    public String getResource_type() {
        return resource_type;
    }

    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }

    public int getArt_id() {
        return art_id;
    }

    public void setArt_id(int art_id) {
        this.art_id = art_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImgMainFile1Bean getImg_main_file1() {
        return img_main_file1;
    }

    public void setImg_main_file1(ImgMainFile1Bean img_main_file1) {
        this.img_main_file1 = img_main_file1;
    }

    public String getSpecial_attr() {
        return special_attr;
    }

    public void setSpecial_attr(String special_attr) {
        this.special_attr = special_attr;
    }

    public int getOpened() {
        return opened;
    }

    public void setOpened(int opened) {
        this.opened = opened;
    }

    public String getExtrinsic_hash() {
        return extrinsic_hash;
    }

    public void setExtrinsic_hash(String extrinsic_hash) {
        this.extrinsic_hash = extrinsic_hash;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCreated_at() {
        return created_at;
    }

    public void setCreated_at(int created_at) {
        this.created_at = created_at;
    }

    public static class ImgMainFile1Bean {
        /**
         * url : https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/art/img_main_file1/154/fe952397-9974-4230-97ff-18acacec2187.jpeg
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
