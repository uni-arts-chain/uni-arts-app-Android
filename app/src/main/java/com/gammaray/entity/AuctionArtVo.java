package com.gammaray.entity;

import java.io.Serializable;

public class AuctionArtVo implements Serializable {

    private int id;

    private String currency_code;

    private String start_price;

    private int amount;

    private String price_increment;

    private String current_price;

    private String win_price;

    private long start_time;

    private long end_time;

    private long created_at;

    private String deposit_amount;

    private boolean can_cancel;

    private long server_timestamp;

    private boolean deposit_paid;

    private boolean buyer_paid;

    private String current_user_highest_price;

    private boolean is_owner;

    private long pay_timeout;

    private boolean is_settlement;

    private boolean is_paying;

    private BuyerBean buyer;

    private SellingArtVo art;

    private String royalty;

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

    public String getStart_price() {
        return start_price;
    }

    public void setStart_price(String start_price) {
        this.start_price = start_price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getPrice_increment() {
        return price_increment;
    }

    public void setPrice_increment(String price_increment) {
        this.price_increment = price_increment;
    }

    public String getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(String current_price) {
        this.current_price = current_price;
    }

    public String getWin_price() {
        return win_price;
    }

    public void setWin_price(String win_price) {
        this.win_price = win_price;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public String getDeposit_amount() {
        return deposit_amount;
    }

    public void setDeposit_amount(String deposit_amount) {
        this.deposit_amount = deposit_amount;
    }

    public boolean isCan_cancel() {
        return can_cancel;
    }

    public void setCan_cancel(boolean can_cancel) {
        this.can_cancel = can_cancel;
    }

    public long getServer_timestamp() {
        return server_timestamp;
    }

    public void setServer_timestamp(long server_timestamp) {
        this.server_timestamp = server_timestamp;
    }

    public boolean isDeposit_paid() {
        return deposit_paid;
    }

    public void setDeposit_paid(boolean deposit_paid) {
        this.deposit_paid = deposit_paid;
    }

    public boolean isIs_owner() {
        return is_owner;
    }

    public void setIs_owner(boolean is_owner) {
        this.is_owner = is_owner;
    }

    public long getPay_timeout() {
        return pay_timeout;
    }

    public void setPay_timeout(long pay_timeout) {
        this.pay_timeout = pay_timeout;
    }

    public boolean isIs_settlement() {
        return is_settlement;
    }

    public void setIs_settlement(boolean is_settlement) {
        this.is_settlement = is_settlement;
    }

    public boolean isIs_paying() {
        return is_paying;
    }

    public void setIs_paying(boolean is_paying) {
        this.is_paying = is_paying;
    }

    public BuyerBean getBuyer() {
        return buyer;
    }

    public void setBuyer(BuyerBean buyer) {
        this.buyer = buyer;
    }

    public SellingArtVo getArt() {
        return art;
    }

    public void setArt(SellingArtVo art) {
        this.art = art;
    }

    public boolean isBuyer_paid() {
        return buyer_paid;
    }

    public void setBuyer_paid(boolean buyer_paid) {
        this.buyer_paid = buyer_paid;
    }

    public String getCurrent_user_highest_price() {
        return current_user_highest_price;
    }

    public void setCurrent_user_highest_price(String current_user_highest_price) {
        this.current_user_highest_price = current_user_highest_price;
    }

    public String getRoyalty() {
        return royalty;
    }

    public void setRoyalty(String royalty) {
        this.royalty = royalty;
    }

    public static class BuyerBean implements Serializable {
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
        private boolean is_read_agreement;
        private boolean is_binding_invitation;
        private String address;
        private RecommendImageBeanX recommend_image;
        private String sex;
        private String desc;
        private AvatarBeanX avatar;
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

        public Boolean getIs_read_agreement() {
            return is_read_agreement;
        }

        public void setIs_read_agreement(Boolean is_read_agreement) {
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

        public RecommendImageBeanX getRecommend_image() {
            return recommend_image;
        }

        public void setRecommend_image(RecommendImageBeanX recommend_image) {
            this.recommend_image = recommend_image;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public AvatarBeanX getAvatar() {
            return avatar;
        }

        public void setAvatar(AvatarBeanX avatar) {
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

        public static class RecommendImageBeanX implements Serializable {
            /**
             * url : https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/recommend_image/11/de72717a-c952-42f0-a06f-5284bbf2713e.jpg
             */

            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class AvatarBeanX implements Serializable {
            /**
             * url : null
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

//    public static class ArtBean implements Serializable {
//
//        private int id;
//
//        private String name;
//
//        private int category_id;
//
//        private String theme_id;
//
//        private String material_id;
//
//        private String produce_at;
//
//        private int size_length;
//
//        private int size_width;
//
//        private String details;
//
//        private String img_detail_file5_desc;
//
//        private ImgMainFile1Bean img_main_file1;
//
//        private ImgMainFile2Bean img_main_file2;
//
//        private ImgMainFile3Bean img_main_file3;
//
//        private ImgDetailFile1Bean img_detail_file1;
//
//        private String img_detail_file1_desc;
//
//        private ImgDetailFile2Bean img_detail_file2;
//
//        private String img_detail_file2_desc;
//
//        private ImgDetailFile3Bean img_detail_file3;
//
//        private String img_detail_file3_desc;
//
//        private ImgDetailFile4Bean img_detail_file4;
//
//        private String img_detail_file4_desc;
//
//        private ImgDetailFile5Bean img_detail_file5;
//
//        private String price;
//
//        private int fee;
//
//        private int position;
//
//        private String aasm_state;
//
//        private String created_at;
//
//        private int collection_id;
//
//        private int item_id;
//
//        private int member_id;
//
//        private String item_hash;
//
//        private int auction_start_time;
//
//        private int auction_end_time;
//
//        private boolean liked_by_me;
//
//        private boolean disliked_by_me;
//
//        private boolean favorite_by_me;
//
//        private int liked_count;
//
//        private int dislike_count;
//
//        private int favorite_count;
//
//        private int signature_count;
//
//        private BigDecimal royalty;
//
//        private long royalty_expired_at;
//
//        private boolean has_royalty;
//
//        private String live2d_file;
//
//        private String live2d_ipfs_url;
//
//        private String live2d_ipfs_zip_url;
//
//        private int collection_mode;
//
//        private int total_amount;
//
//        private int trades_count;
//
//        private String resource_type;
//
//        private String video_url;
//
//        private boolean is_owner;
//
//        private AuthorBean author;
//
//        public static class AuthorBean implements Serializable {
//
//            private int id;
//
//            private String uid;
//
//            private String sn;
//
//            private String email;
//
//            private String display_name;
//
//            private String token;
//
//            private String phone_number;
//
//            private boolean id_document_validated;
//
//            private boolean app_validated;
//
//            private boolean pay_password_validated;
//
//            private String ref_code;
//
//            private String created_at;
//
//            private String expire_at;
//
//            private boolean is_read_agreement;
//
//            private boolean is_binding_invitation;
//
//            private String address;
//
//            private RecommendImageBean recommend_image;
//
//            private int sex;
//
//            private String desc;
//
//            private AvatarBean avatar;
//
//            private boolean is_artist;
//
//            private String artist_desc;
//
//            private int follow_user_size;
//
//            private int following_user_size;
//
//            private boolean follow_by_me;
//
//            private int favorite_art_size;
//
//            private int art_size;
//
//            private String residential_address;
//
//            private String college;
//
//            private WechatImg weixin_img;
//
//            private AlipayImg alipay_img;
//
//            private double cny_amount;
//
//            private boolean have_win_auction;
//
//            public static class RecommendImageBean implements Serializable {
//
//                private String url;
//
//                public String getUrl() {
//                    return url;
//                }
//
//                public void setUrl(String url) {
//                    this.url = url;
//                }
//            }
//
//            public static class AvatarBean implements Serializable {
//
//                private String url;
//
//                public String getUrl() {
//                    return url;
//                }
//
//                public void setUrl(String url) {
//                    this.url = url;
//                }
//            }
//
//            public static class WechatImg implements Serializable {
//                private String url;
//
//                public String getUrl() {
//                    return url;
//                }
//
//                public void setUrl(String url) {
//                    this.url = url;
//                }
//            }
//
//            public static class AlipayImg implements Serializable {
//                private String url;
//
//                public String getUrl() {
//                    return url;
//                }
//
//                public void setUrl(String url) {
//                    this.url = url;
//                }
//            }
//
//            public int getId() {
//                return id;
//            }
//
//            public void setId(int id) {
//                this.id = id;
//            }
//
//            public String getUid() {
//                return uid;
//            }
//
//            public void setUid(String uid) {
//                this.uid = uid;
//            }
//
//            public String getSn() {
//                return sn;
//            }
//
//            public void setSn(String sn) {
//                this.sn = sn;
//            }
//
//            public String getEmail() {
//                return email;
//            }
//
//            public void setEmail(String email) {
//                this.email = email;
//            }
//
//            public String getDisplay_name() {
//                return display_name;
//            }
//
//            public void setDisplay_name(String display_name) {
//                this.display_name = display_name;
//            }
//
//            public String getToken() {
//                return token;
//            }
//
//            public void setToken(String token) {
//                this.token = token;
//            }
//
//            public String getPhone_number() {
//                return phone_number;
//            }
//
//            public void setPhone_number(String phone_number) {
//                this.phone_number = phone_number;
//            }
//
//            public boolean isId_document_validated() {
//                return id_document_validated;
//            }
//
//            public void setId_document_validated(boolean id_document_validated) {
//                this.id_document_validated = id_document_validated;
//            }
//
//            public boolean isApp_validated() {
//                return app_validated;
//            }
//
//            public void setApp_validated(boolean app_validated) {
//                this.app_validated = app_validated;
//            }
//
//            public boolean isPay_password_validated() {
//                return pay_password_validated;
//            }
//
//            public void setPay_password_validated(boolean pay_password_validated) {
//                this.pay_password_validated = pay_password_validated;
//            }
//
//            public String getRef_code() {
//                return ref_code;
//            }
//
//            public void setRef_code(String ref_code) {
//                this.ref_code = ref_code;
//            }
//
//            public String getCreated_at() {
//                return created_at;
//            }
//
//            public void setCreated_at(String created_at) {
//                this.created_at = created_at;
//            }
//
//            public String getExpire_at() {
//                return expire_at;
//            }
//
//            public void setExpire_at(String expire_at) {
//                this.expire_at = expire_at;
//            }
//
//            public boolean isIs_read_agreement() {
//                return is_read_agreement;
//            }
//
//            public void setIs_read_agreement(boolean is_read_agreement) {
//                this.is_read_agreement = is_read_agreement;
//            }
//
//            public boolean isIs_binding_invitation() {
//                return is_binding_invitation;
//            }
//
//            public void setIs_binding_invitation(boolean is_binding_invitation) {
//                this.is_binding_invitation = is_binding_invitation;
//            }
//
//            public String getAddress() {
//                return address;
//            }
//
//            public void setAddress(String address) {
//                this.address = address;
//            }
//
//            public RecommendImageBean getRecommend_image() {
//                return recommend_image;
//            }
//
//            public void setRecommend_image(RecommendImageBean recommend_image) {
//                this.recommend_image = recommend_image;
//            }
//
//            public int getSex() {
//                return sex;
//            }
//
//            public void setSex(int sex) {
//                this.sex = sex;
//            }
//
//            public String getDesc() {
//                return desc;
//            }
//
//            public void setDesc(String desc) {
//                this.desc = desc;
//            }
//
//            public AvatarBean getAvatar() {
//                return avatar;
//            }
//
//            public void setAvatar(AvatarBean avatar) {
//                this.avatar = avatar;
//            }
//
//            public boolean isIs_artist() {
//                return is_artist;
//            }
//
//            public void setIs_artist(boolean is_artist) {
//                this.is_artist = is_artist;
//            }
//
//            public String getArtist_desc() {
//                return artist_desc;
//            }
//
//            public void setArtist_desc(String artist_desc) {
//                this.artist_desc = artist_desc;
//            }
//
//            public int getFollow_user_size() {
//                return follow_user_size;
//            }
//
//            public void setFollow_user_size(int follow_user_size) {
//                this.follow_user_size = follow_user_size;
//            }
//
//            public int getFollowing_user_size() {
//                return following_user_size;
//            }
//
//            public void setFollowing_user_size(int following_user_size) {
//                this.following_user_size = following_user_size;
//            }
//
//            public boolean isFollow_by_me() {
//                return follow_by_me;
//            }
//
//            public void setFollow_by_me(boolean follow_by_me) {
//                this.follow_by_me = follow_by_me;
//            }
//
//            public int getFavorite_art_size() {
//                return favorite_art_size;
//            }
//
//            public void setFavorite_art_size(int favorite_art_size) {
//                this.favorite_art_size = favorite_art_size;
//            }
//
//            public int getArt_size() {
//                return art_size;
//            }
//
//            public void setArt_size(int art_size) {
//                this.art_size = art_size;
//            }
//
//            public String getResidential_address() {
//                return residential_address;
//            }
//
//            public void setResidential_address(String residential_address) {
//                this.residential_address = residential_address;
//            }
//
//            public String getCollege() {
//                return college;
//            }
//
//            public void setCollege(String college) {
//                this.college = college;
//            }
//
//            public WechatImg getWeixin_img() {
//                return weixin_img;
//            }
//
//            public void setWeixin_img(WechatImg weixin_img) {
//                this.weixin_img = weixin_img;
//            }
//
//            public AlipayImg getAlipay_img() {
//                return alipay_img;
//            }
//
//            public void setAlipay_img(AlipayImg alipay_img) {
//                this.alipay_img = alipay_img;
//            }
//
//            public double getCny_amount() {
//                return cny_amount;
//            }
//
//            public void setCny_amount(double cny_amount) {
//                this.cny_amount = cny_amount;
//            }
//
//            public boolean isHave_win_auction() {
//                return have_win_auction;
//            }
//
//            public void setHave_win_auction(boolean have_win_auction) {
//                this.have_win_auction = have_win_auction;
//            }
//        }
//
//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public int getCategory_id() {
//            return category_id;
//        }
//
//        public void setCategory_id(int category_id) {
//            this.category_id = category_id;
//        }
//
//        public String getTheme_id() {
//            return theme_id;
//        }
//
//        public void setTheme_id(String theme_id) {
//            this.theme_id = theme_id;
//        }
//
//        public String getMaterial_id() {
//            return material_id;
//        }
//
//        public void setMaterial_id(String material_id) {
//            this.material_id = material_id;
//        }
//
//        public String getProduce_at() {
//            return produce_at;
//        }
//
//        public void setProduce_at(String produce_at) {
//            this.produce_at = produce_at;
//        }
//
//        public int getSize_length() {
//            return size_length;
//        }
//
//        public void setSize_length(int size_length) {
//            this.size_length = size_length;
//        }
//
//        public int getSize_width() {
//            return size_width;
//        }
//
//        public void setSize_width(int size_width) {
//            this.size_width = size_width;
//        }
//
//        public String getDetails() {
//            return details;
//        }
//
//        public void setDetails(String details) {
//            this.details = details;
//        }
//
//        public String getImg_detail_file5_desc() {
//            return img_detail_file5_desc;
//        }
//
//        public void setImg_detail_file5_desc(String img_detail_file5_desc) {
//            this.img_detail_file5_desc = img_detail_file5_desc;
//        }
//
//        public ImgMainFile1Bean getImg_main_file1() {
//            return img_main_file1;
//        }
//
//        public void setImg_main_file1(ImgMainFile1Bean img_main_file1) {
//            this.img_main_file1 = img_main_file1;
//        }
//
//        public ImgMainFile2Bean getImg_main_file2() {
//            return img_main_file2;
//        }
//
//        public void setImg_main_file2(ImgMainFile2Bean img_main_file2) {
//            this.img_main_file2 = img_main_file2;
//        }
//
//        public ImgMainFile3Bean getImg_main_file3() {
//            return img_main_file3;
//        }
//
//        public void setImg_main_file3(ImgMainFile3Bean img_main_file3) {
//            this.img_main_file3 = img_main_file3;
//        }
//
//        public ImgDetailFile1Bean getImg_detail_file1() {
//            return img_detail_file1;
//        }
//
//        public void setImg_detail_file1(ImgDetailFile1Bean img_detail_file1) {
//            this.img_detail_file1 = img_detail_file1;
//        }
//
//        public String getImg_detail_file1_desc() {
//            return img_detail_file1_desc;
//        }
//
//        public void setImg_detail_file1_desc(String img_detail_file1_desc) {
//            this.img_detail_file1_desc = img_detail_file1_desc;
//        }
//
//        public ImgDetailFile2Bean getImg_detail_file2() {
//            return img_detail_file2;
//        }
//
//        public void setImg_detail_file2(ImgDetailFile2Bean img_detail_file2) {
//            this.img_detail_file2 = img_detail_file2;
//        }
//
//        public String getImg_detail_file2_desc() {
//            return img_detail_file2_desc;
//        }
//
//        public void setImg_detail_file2_desc(String img_detail_file2_desc) {
//            this.img_detail_file2_desc = img_detail_file2_desc;
//        }
//
//        public ImgDetailFile3Bean getImg_detail_file3() {
//            return img_detail_file3;
//        }
//
//        public void setImg_detail_file3(ImgDetailFile3Bean img_detail_file3) {
//            this.img_detail_file3 = img_detail_file3;
//        }
//
//        public String getImg_detail_file3_desc() {
//            return img_detail_file3_desc;
//        }
//
//        public void setImg_detail_file3_desc(String img_detail_file3_desc) {
//            this.img_detail_file3_desc = img_detail_file3_desc;
//        }
//
//        public ImgDetailFile4Bean getImg_detail_file4() {
//            return img_detail_file4;
//        }
//
//        public void setImg_detail_file4(ImgDetailFile4Bean img_detail_file4) {
//            this.img_detail_file4 = img_detail_file4;
//        }
//
//        public String getImg_detail_file4_desc() {
//            return img_detail_file4_desc;
//        }
//
//        public void setImg_detail_file4_desc(String img_detail_file4_desc) {
//            this.img_detail_file4_desc = img_detail_file4_desc;
//        }
//
//        public ImgDetailFile5Bean getImg_detail_file5() {
//            return img_detail_file5;
//        }
//
//        public void setImg_detail_file5(ImgDetailFile5Bean img_detail_file5) {
//            this.img_detail_file5 = img_detail_file5;
//        }
//
//        public String getPrice() {
//            return price;
//        }
//
//        public void setPrice(String price) {
//            this.price = price;
//        }
//
//        public int getFee() {
//            return fee;
//        }
//
//        public void setFee(int fee) {
//            this.fee = fee;
//        }
//
//        public int getPosition() {
//            return position;
//        }
//
//        public void setPosition(int position) {
//            this.position = position;
//        }
//
//        public String getAasm_state() {
//            return aasm_state;
//        }
//
//        public void setAasm_state(String aasm_state) {
//            this.aasm_state = aasm_state;
//        }
//
//        public String getCreated_at() {
//            return created_at;
//        }
//
//        public void setCreated_at(String created_at) {
//            this.created_at = created_at;
//        }
//
//        public int getCollection_id() {
//            return collection_id;
//        }
//
//        public void setCollection_id(int collection_id) {
//            this.collection_id = collection_id;
//        }
//
//        public int getItem_id() {
//            return item_id;
//        }
//
//        public void setItem_id(int item_id) {
//            this.item_id = item_id;
//        }
//
//        public int getMember_id() {
//            return member_id;
//        }
//
//        public void setMember_id(int member_id) {
//            this.member_id = member_id;
//        }
//
//        public String getItem_hash() {
//            return item_hash;
//        }
//
//        public void setItem_hash(String item_hash) {
//            this.item_hash = item_hash;
//        }
//
//        public int getAuction_start_time() {
//            return auction_start_time;
//        }
//
//        public void setAuction_start_time(int auction_start_time) {
//            this.auction_start_time = auction_start_time;
//        }
//
//        public int getAuction_end_time() {
//            return auction_end_time;
//        }
//
//        public void setAuction_end_time(int auction_end_time) {
//            this.auction_end_time = auction_end_time;
//        }
//
//        public boolean isLiked_by_me() {
//            return liked_by_me;
//        }
//
//        public void setLiked_by_me(boolean liked_by_me) {
//            this.liked_by_me = liked_by_me;
//        }
//
//        public boolean isDisliked_by_me() {
//            return disliked_by_me;
//        }
//
//        public void setDisliked_by_me(boolean disliked_by_me) {
//            this.disliked_by_me = disliked_by_me;
//        }
//
//        public boolean isFavorite_by_me() {
//            return favorite_by_me;
//        }
//
//        public void setFavorite_by_me(boolean favorite_by_me) {
//            this.favorite_by_me = favorite_by_me;
//        }
//
//        public int getLiked_count() {
//            return liked_count;
//        }
//
//        public void setLiked_count(int liked_count) {
//            this.liked_count = liked_count;
//        }
//
//        public int getDislike_count() {
//            return dislike_count;
//        }
//
//        public void setDislike_count(int dislike_count) {
//            this.dislike_count = dislike_count;
//        }
//
//        public int getFavorite_count() {
//            return favorite_count;
//        }
//
//        public void setFavorite_count(int favorite_count) {
//            this.favorite_count = favorite_count;
//        }
//
//        public int getSignature_count() {
//            return signature_count;
//        }
//
//        public void setSignature_count(int signature_count) {
//            this.signature_count = signature_count;
//        }
//
//        public BigDecimal getRoyalty() {
//            return royalty;
//        }
//
//        public void setRoyalty(BigDecimal royalty) {
//            this.royalty = royalty;
//        }
//
//        public long getRoyalty_expired_at() {
//            return royalty_expired_at;
//        }
//
//        public void setRoyalty_expired_at(long royalty_expired_at) {
//            this.royalty_expired_at = royalty_expired_at;
//        }
//
//        public boolean isHas_royalty() {
//            return has_royalty;
//        }
//
//        public void setHas_royalty(boolean has_royalty) {
//            this.has_royalty = has_royalty;
//        }
//
//        public String getLive2d_file() {
//            return live2d_file;
//        }
//
//        public void setLive2d_file(String live2d_file) {
//            this.live2d_file = live2d_file;
//        }
//
//        public String getLive2d_ipfs_url() {
//            return live2d_ipfs_url;
//        }
//
//        public void setLive2d_ipfs_url(String live2d_ipfs_url) {
//            this.live2d_ipfs_url = live2d_ipfs_url;
//        }
//
//        public String getLive2d_ipfs_zip_url() {
//            return live2d_ipfs_zip_url;
//        }
//
//        public void setLive2d_ipfs_zip_url(String live2d_ipfs_zip_url) {
//            this.live2d_ipfs_zip_url = live2d_ipfs_zip_url;
//        }
//
//        public int getCollection_mode() {
//            return collection_mode;
//        }
//
//        public void setCollection_mode(int collection_mode) {
//            this.collection_mode = collection_mode;
//        }
//
//        public int getTotal_amount() {
//            return total_amount;
//        }
//
//        public void setTotal_amount(int total_amount) {
//            this.total_amount = total_amount;
//        }
//
//        public int getTrades_count() {
//            return trades_count;
//        }
//
//        public void setTrades_count(int trades_count) {
//            this.trades_count = trades_count;
//        }
//
//        public String getResource_type() {
//            return resource_type;
//        }
//
//        public void setResource_type(String resource_type) {
//            this.resource_type = resource_type;
//        }
//
//        public String getVideo_url() {
//            return video_url;
//        }
//
//        public void setVideo_url(String video_url) {
//            this.video_url = video_url;
//        }
//
//        public AuthorBean getAuthor() {
//            return author;
//        }
//
//        public void setAuthor(AuthorBean author) {
//            this.author = author;
//        }
//
//        public boolean isIs_owner() {
//            return is_owner;
//        }
//
//        public void setIs_owner(boolean is_owner) {
//            this.is_owner = is_owner;
//        }
//
//        public static class ImgMainFile1Bean implements Serializable {
//
//            private String url;
//
//            public String getUrl() {
//                return url;
//            }
//
//            public void setUrl(String url) {
//                this.url = url;
//            }
//        }
//
//        public static class ImgMainFile2Bean implements Serializable {
//
//            private String url;
//
//            public String getUrl() {
//                return url;
//            }
//
//            public void setUrl(String url) {
//                this.url = url;
//            }
//        }
//
//        public static class ImgMainFile3Bean implements Serializable {
//
//            private String url;
//
//            public String getUrl() {
//                return url;
//            }
//
//            public void setUrl(String url) {
//                this.url = url;
//            }
//        }
//
//        public static class ImgDetailFile1Bean implements Serializable {
//
//            private String url;
//
//            public String getUrl() {
//                return url;
//            }
//
//            public void setUrl(String url) {
//                this.url = url;
//            }
//        }
//
//        public static class ImgDetailFile2Bean implements Serializable {
//
//            private String url;
//
//            public String getUrl() {
//                return url;
//            }
//
//            public void setUrl(String url) {
//                this.url = url;
//            }
//        }
//
//        public static class ImgDetailFile3Bean implements Serializable {
//
//            private String url;
//
//            public String getUrl() {
//                return url;
//            }
//
//            public void setUrl(String url) {
//                this.url = url;
//            }
//        }
//
//        public static class ImgDetailFile4Bean implements Serializable {
//
//            private String url;
//
//            public String getUrl() {
//                return url;
//            }
//
//            public void setUrl(String url) {
//                this.url = url;
//            }
//        }
//
//        public static class ImgDetailFile5Bean implements Serializable {
//
//            private String url;
//
//            public String getUrl() {
//                return url;
//            }
//
//            public void setUrl(String url) {
//                this.url = url;
//            }
//        }
//
//    }
}
