package com.gammaray.entity;

public class BlindBoxRecordVo {

    /**
     * art_id : 294
     * author : zhubin01
     * address : 5FvC8ong12CCEeYj3gHxemk4h8y4sF8c3BxP92WDXbDnHiYr
     * nft_address : 0xb9a4a4e9a066be6f8b5b9ddf32672c63dfdd58c4
     * name : shu03
     * resource_type : 1
     * img_main_file1 : {"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/art/img_main_file1/294/77e8fc14-0bb7-4e4f-906d-81c911bb0ee5."}
     * special_attr :
     * extrinsic_hash : 0x351b92ed060972967acabfccad9f4b0ca5dafe59f49d780a12fc5037ef24b13c
     * created_at : 1620818754
     */

    private int art_id;
    private String author;
    private String address;
    private String nft_address;
    private String name;
    private int resource_type;
    private ImgMainFile1Bean img_main_file1;
    private String special_attr;
    private String extrinsic_hash;
    private long created_at;

    public int getArt_id() {
        return art_id;
    }

    public void setArt_id(int art_id) {
        this.art_id = art_id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNft_address() {
        return nft_address;
    }

    public void setNft_address(String nft_address) {
        this.nft_address = nft_address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResource_type() {
        return resource_type;
    }

    public void setResource_type(int resource_type) {
        this.resource_type = resource_type;
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

    public String getExtrinsic_hash() {
        return extrinsic_hash;
    }

    public void setExtrinsic_hash(String extrinsic_hash) {
        this.extrinsic_hash = extrinsic_hash;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public static class ImgMainFile1Bean {
        /**
         * url : https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/art/img_main_file1/294/77e8fc14-0bb7-4e4f-906d-81c911bb0ee5.
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
