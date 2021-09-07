package com.gammaray.entity;

import java.io.Serializable;

public class OfferPriceBean implements Serializable {

    private int id;

    private String currency_code;

    private String price;

    private Long created_at;

    private MemberBean member;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }

    public MemberBean getMember() {
        return member;
    }

    public void setMember(MemberBean member) {
        this.member = member;
    }

    public static class MemberBean implements Serializable{

        private int id;

        private String uid;

        private String sn;

        private String email;

        private String display_name;

        private String token;

        private String phone_number;

        private boolean id_document_validated;

        private boolean app_validated;

        private boolean pay_password_validated;

        private String ref_code;

        private String created_at;

        private String expire_at;

        private Object is_read_agreement;

        private boolean is_binding_invitation;

        private String address;

        private RecommendImageBean recommend_image;

        private int sex;

        private String desc;

        private AvatarBean avatar;

        private boolean is_artist;

        private String artist_desc;

        private int follow_user_size;

        private int following_user_size;

        private boolean follow_by_me;

        private int favorite_art_size;

        private int art_size;

        private String residential_address;

        private String college;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getDisplay_name() {
            return display_name;
        }

        public void setDisplay_name(String display_name) {
            this.display_name = display_name;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }

        public boolean isId_document_validated() {
            return id_document_validated;
        }

        public void setId_document_validated(boolean id_document_validated) {
            this.id_document_validated = id_document_validated;
        }

        public boolean isApp_validated() {
            return app_validated;
        }

        public void setApp_validated(boolean app_validated) {
            this.app_validated = app_validated;
        }

        public boolean isPay_password_validated() {
            return pay_password_validated;
        }

        public void setPay_password_validated(boolean pay_password_validated) {
            this.pay_password_validated = pay_password_validated;
        }

        public String getRef_code() {
            return ref_code;
        }

        public void setRef_code(String ref_code) {
            this.ref_code = ref_code;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getExpire_at() {
            return expire_at;
        }

        public void setExpire_at(String expire_at) {
            this.expire_at = expire_at;
        }

        public Object getIs_read_agreement() {
            return is_read_agreement;
        }

        public void setIs_read_agreement(Object is_read_agreement) {
            this.is_read_agreement = is_read_agreement;
        }

        public boolean isIs_binding_invitation() {
            return is_binding_invitation;
        }

        public void setIs_binding_invitation(boolean is_binding_invitation) {
            this.is_binding_invitation = is_binding_invitation;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public RecommendImageBean getRecommend_image() {
            return recommend_image;
        }

        public void setRecommend_image(RecommendImageBean recommend_image) {
            this.recommend_image = recommend_image;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public AvatarBean getAvatar() {
            return avatar;
        }

        public void setAvatar(AvatarBean avatar) {
            this.avatar = avatar;
        }

        public boolean isIs_artist() {
            return is_artist;
        }

        public void setIs_artist(boolean is_artist) {
            this.is_artist = is_artist;
        }

        public String getArtist_desc() {
            return artist_desc;
        }

        public void setArtist_desc(String artist_desc) {
            this.artist_desc = artist_desc;
        }

        public int getFollow_user_size() {
            return follow_user_size;
        }

        public void setFollow_user_size(int follow_user_size) {
            this.follow_user_size = follow_user_size;
        }

        public int getFollowing_user_size() {
            return following_user_size;
        }

        public void setFollowing_user_size(int following_user_size) {
            this.following_user_size = following_user_size;
        }

        public boolean isFollow_by_me() {
            return follow_by_me;
        }

        public void setFollow_by_me(boolean follow_by_me) {
            this.follow_by_me = follow_by_me;
        }

        public int getFavorite_art_size() {
            return favorite_art_size;
        }

        public void setFavorite_art_size(int favorite_art_size) {
            this.favorite_art_size = favorite_art_size;
        }

        public int getArt_size() {
            return art_size;
        }

        public void setArt_size(int art_size) {
            this.art_size = art_size;
        }

        public String getResidential_address() {
            return residential_address;
        }

        public void setResidential_address(String residential_address) {
            this.residential_address = residential_address;
        }

        public String getCollege() {
            return college;
        }

        public void setCollege(String college) {
            this.college = college;
        }

        public static class RecommendImageBean {
            /**
             * url : https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/recommend_image/17/d50556f3-ead1-4cfa-9f65-4daab12207a4.jpg
             */

            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class AvatarBean {
            /**
             * url : https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/avatar/17/f3dcd31f-6c5d-4f6a-aaff-550d96fd8d54.jpg
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
}
