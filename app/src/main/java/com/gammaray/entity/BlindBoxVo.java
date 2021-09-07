package com.gammaray.entity;

import java.io.Serializable;
import java.util.List;

public class BlindBoxVo implements Serializable {

    /**
     * id : 3
     * title : test
     * price : 0.0
     * desc :
     * img_path : https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/blind_box/img/3/269f1e5f-755b-4e30-aba0-a630f8841f5c.jpg
     * background_img_path : null
     * rule :
     * start_time : 1618848000
     * end_time : 1619712000
     * special_attr : null
     * card_groups : [{"id":1,"box_id":2,"seller_id":5,"amount":1,"remain_amount":1,"art":{"id":105,"name":"qqq","category_id":2,"theme_id":1,"material_id":2,"produce_at":"1617638400","size_length":100,"size_width":100,"details":"","img_main_file1":{"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/art/img_main_file1/105/d66198b8-5017-4626-8380-7e27333ec06c.jpg"},"img_main_file2":{"url":null},"img_main_file3":{"url":null},"img_detail_file1":{"url":null},"img_detail_file1_desc":"","img_detail_file2":{"url":null},"img_detail_file2_desc":"","img_detail_file3":{"url":null},"img_detail_file3_desc":"","img_detail_file4":{"url":null},"img_detail_file4_desc":"","img_detail_file5":{"url":null},"img_detail_file5_desc":"","price":null,"fee":null,"position":null,"aasm_state":"online","created_at":"1618911631","collection_id":1,"item_id":188,"member_id":5,"item_hash":"0x119b2b8b622581c3a352474964aa8bf031f440d2","auction_start_time":null,"auction_end_time":null,"liked_by_me":false,"disliked_by_me":false,"favorite_by_me":false,"liked_count":0,"dislike_count":0,"favorite_count":0,"signature_count":0,"royalty":null,"royalty_expired_at":null,"has_royalty":false,"live2d_file":"","live2d_ipfs_url":"","live2d_ipfs_zip_url":"","collection_mode":1,"total_amount":1,"has_amount":0,"is_owner":false,"selling_amount":0,"trades_count":0,"author":{"id":5,"uid":"Zby8V9CEFveSYKzr1DcCrWVJ","sn":"BPPVE6PAGS","email":null,"display_name":null,"token":null,"phone_number":null,"id_document_validated":false,"app_validated":false,"pay_password_validated":false,"ref_code":"26449644","created_at":"1607655600","expire_at":"","is_read_agreement":null,"is_binding_invitation":false,"address":"5CqQgVQVf8tFR6gN2TL2pHg5YprjuMS4siSceFutffNBAyfy","recommend_image":{"url":null},"sex":null,"desc":null,"avatar":{"url":null},"is_artist":false,"artist_desc":null,"follow_user_size":1,"following_user_size":2,"follow_by_me":false,"favorite_art_size":1,"art_size":1,"residential_address":null,"college":null}}}]
     */

    private int id;
    private String title;
    private String price;
    private String desc;
    private String img_path;
    private String background_img_path;
    private String rule;
    private String start_time;
    private String end_time;
    private String special_attr;
    private String app_cover_img_path;
    private String app_img_path;
    private String app_background_img_path;
    private String app_background_1x_img_path;
    private String app_background_10x_img_path;
    private String app_body_background_img_path;
    private int background_color;

    public String getApp_background_1x_img_path() {
        return app_background_1x_img_path;
    }

    public void setApp_background_1x_img_path(String app_background_1x_img_path) {
        this.app_background_1x_img_path = app_background_1x_img_path;
    }

    public String getApp_background_10x_img_path() {
        return app_background_10x_img_path;
    }

    public void setApp_background_10x_img_path(String app_background_10x_img_path) {
        this.app_background_10x_img_path = app_background_10x_img_path;
    }

    public String getApp_body_background_img_path() {
        return app_body_background_img_path;
    }

    public void setApp_body_background_img_path(String app_body_background_img_path) {
        this.app_body_background_img_path = app_body_background_img_path;
    }

    public int getBackground_color() {
        return background_color;
    }

    public void setBackground_color(int background_color) {
        this.background_color = background_color;
    }

    public List<CardGroupsBean> getOnchain_card_groups() {
        return onchain_card_groups;
    }

    public void setOnchain_card_groups(List<CardGroupsBean> onchain_card_groups) {
        this.onchain_card_groups = onchain_card_groups;
    }

    public String getApp_background_img_path() {
        return app_background_img_path;
    }

    public void setApp_background_img_path(String app_background_img_path) {
        this.app_background_img_path = app_background_img_path;
    }

    public String getApp_cover_img_path() {
        return app_cover_img_path;
    }

    public void setApp_cover_img_path(String app_cover_img_path) {
        this.app_cover_img_path = app_cover_img_path;
    }

    public String getApp_img_path() {
        return app_img_path;
    }

    public void setApp_img_path(String app_img_path) {
        this.app_img_path = app_img_path;
    }

    private List<CardGroupsBean> onchain_card_groups;

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getBackground_img_path() {
        return background_img_path;
    }

    public void setBackground_img_path(String background_img_path) {
        this.background_img_path = background_img_path;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getSpecial_attr() {
        return special_attr;
    }

    public void setSpecial_attr(String special_attr) {
        this.special_attr = special_attr;
    }

    public List<CardGroupsBean> getCard_groups() {
        return onchain_card_groups;
    }

    public void setCard_groups(List<CardGroupsBean> onchain_card_groups) {
        this.onchain_card_groups = onchain_card_groups;
    }

    public static class CardGroupsBean implements Serializable {
        /**
         * id : 1
         * box_id : 2
         * seller_id : 5
         * amount : 1
         * remain_amount : 1
         * art : {"id":105,"name":"qqq","category_id":2,"theme_id":1,"material_id":2,"produce_at":"1617638400","size_length":100,"size_width":100,"details":"","img_main_file1":{"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/art/img_main_file1/105/d66198b8-5017-4626-8380-7e27333ec06c.jpg"},"img_main_file2":{"url":null},"img_main_file3":{"url":null},"img_detail_file1":{"url":null},"img_detail_file1_desc":"","img_detail_file2":{"url":null},"img_detail_file2_desc":"","img_detail_file3":{"url":null},"img_detail_file3_desc":"","img_detail_file4":{"url":null},"img_detail_file4_desc":"","img_detail_file5":{"url":null},"img_detail_file5_desc":"","price":null,"fee":null,"position":null,"aasm_state":"online","created_at":"1618911631","collection_id":1,"item_id":188,"member_id":5,"item_hash":"0x119b2b8b622581c3a352474964aa8bf031f440d2","auction_start_time":null,"auction_end_time":null,"liked_by_me":false,"disliked_by_me":false,"favorite_by_me":false,"liked_count":0,"dislike_count":0,"favorite_count":0,"signature_count":0,"royalty":null,"royalty_expired_at":null,"has_royalty":false,"live2d_file":"","live2d_ipfs_url":"","live2d_ipfs_zip_url":"","collection_mode":1,"total_amount":1,"has_amount":0,"is_owner":false,"selling_amount":0,"trades_count":0,"author":{"id":5,"uid":"Zby8V9CEFveSYKzr1DcCrWVJ","sn":"BPPVE6PAGS","email":null,"display_name":null,"token":null,"phone_number":null,"id_document_validated":false,"app_validated":false,"pay_password_validated":false,"ref_code":"26449644","created_at":"1607655600","expire_at":"","is_read_agreement":null,"is_binding_invitation":false,"address":"5CqQgVQVf8tFR6gN2TL2pHg5YprjuMS4siSceFutffNBAyfy","recommend_image":{"url":null},"sex":null,"desc":null,"avatar":{"url":null},"is_artist":false,"artist_desc":null,"follow_user_size":1,"following_user_size":2,"follow_by_me":false,"favorite_art_size":1,"art_size":1,"residential_address":null,"college":null}}
         */

        private int id;
        private int box_id;
        private int seller_id;
        private int amount;
        private int remain_amount;
        private ArtBean art;
        private String special_attr;

        public String getSpecial_attr() {
            return special_attr;
        }

        public void setSpecial_attr(String special_attr) {
            this.special_attr = special_attr;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getBox_id() {
            return box_id;
        }

        public void setBox_id(int box_id) {
            this.box_id = box_id;
        }

        public int getSeller_id() {
            return seller_id;
        }

        public void setSeller_id(int seller_id) {
            this.seller_id = seller_id;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getRemain_amount() {
            return remain_amount;
        }

        public void setRemain_amount(int remain_amount) {
            this.remain_amount = remain_amount;
        }

        public ArtBean getArt() {
            return art;
        }

        public void setArt(ArtBean art) {
            this.art = art;
        }

        public static class ArtBean implements Serializable {
            /**
             * id : 105
             * name : qqq
             * category_id : 2
             * theme_id : 1
             * material_id : 2
             * produce_at : 1617638400
             * size_length : 100.0
             * size_width : 100.0
             * details :
             * img_main_file1 : {"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/art/img_main_file1/105/d66198b8-5017-4626-8380-7e27333ec06c.jpg"}
             * img_main_file2 : {"url":null}
             * img_main_file3 : {"url":null}
             * img_detail_file1 : {"url":null}
             * img_detail_file1_desc :
             * img_detail_file2 : {"url":null}
             * img_detail_file2_desc :
             * img_detail_file3 : {"url":null}
             * img_detail_file3_desc :
             * img_detail_file4 : {"url":null}
             * img_detail_file4_desc :
             * img_detail_file5 : {"url":null}
             * img_detail_file5_desc :
             * price : null
             * fee : null
             * position : null
             * aasm_state : online
             * created_at : 1618911631
             * collection_id : 1
             * item_id : 188
             * member_id : 5
             * item_hash : 0x119b2b8b622581c3a352474964aa8bf031f440d2
             * auction_start_time : null
             * auction_end_time : null
             * liked_by_me : false
             * disliked_by_me : false
             * favorite_by_me : false
             * liked_count : 0
             * dislike_count : 0
             * favorite_count : 0
             * signature_count : 0
             * royalty : null
             * royalty_expired_at : null
             * has_royalty : false
             * live2d_file :
             * live2d_ipfs_url :
             * live2d_ipfs_zip_url :
             * collection_mode : 1
             * total_amount : 1
             * has_amount : 0
             * is_owner : false
             * selling_amount : 0
             * trades_count : 0
             * author : {"id":5,"uid":"Zby8V9CEFveSYKzr1DcCrWVJ","sn":"BPPVE6PAGS","email":null,"display_name":null,"token":null,"phone_number":null,"id_document_validated":false,"app_validated":false,"pay_password_validated":false,"ref_code":"26449644","created_at":"1607655600","expire_at":"","is_read_agreement":null,"is_binding_invitation":false,"address":"5CqQgVQVf8tFR6gN2TL2pHg5YprjuMS4siSceFutffNBAyfy","recommend_image":{"url":null},"sex":null,"desc":null,"avatar":{"url":null},"is_artist":false,"artist_desc":null,"follow_user_size":1,"following_user_size":2,"follow_by_me":false,"favorite_art_size":1,"art_size":1,"residential_address":null,"college":null}
             */

            private int id;
            private String name;
            private int category_id;
            private int theme_id;
            private int material_id;
            private String produce_at;
            private double size_length;
            private double size_width;
            private String details;
            private ImgMainFile1Bean img_main_file1;
            private ImgMainFile2Bean img_main_file2;
            private ImgMainFile3Bean img_main_file3;
            private ImgDetailFile1Bean img_detail_file1;
            private String img_detail_file1_desc;
            private ImgDetailFile2Bean img_detail_file2;
            private String img_detail_file2_desc;
            private ImgDetailFile3Bean img_detail_file3;
            private String img_detail_file3_desc;
            private ImgDetailFile4Bean img_detail_file4;
            private String img_detail_file4_desc;
            private ImgDetailFile5Bean img_detail_file5;
            private String img_detail_file5_desc;
            private String price;
            private String fee;
            private String position;
            private String aasm_state;
            private String created_at;
            private int collection_id;
            private int item_id;
            private int member_id;
            private String item_hash;
            private String auction_start_time;
            private String auction_end_time;
            private boolean liked_by_me;
            private boolean disliked_by_me;
            private boolean favorite_by_me;
            private int liked_count;
            private int dislike_count;
            private int favorite_count;
            private int signature_count;
            private String royalty;
            private String royalty_expired_at;
            private boolean has_royalty;
            private String live2d_file;
            private String live2d_ipfs_url;
            private String live2d_ipfs_zip_url;
            private int collection_mode;
            private int total_amount;
            private int has_amount;
            private boolean is_owner;
            private int selling_amount;
            private int trades_count;
            private AuthorBean author;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getCategory_id() {
                return category_id;
            }

            public void setCategory_id(int category_id) {
                this.category_id = category_id;
            }

            public int getTheme_id() {
                return theme_id;
            }

            public void setTheme_id(int theme_id) {
                this.theme_id = theme_id;
            }

            public int getMaterial_id() {
                return material_id;
            }

            public void setMaterial_id(int material_id) {
                this.material_id = material_id;
            }

            public String getProduce_at() {
                return produce_at;
            }

            public void setProduce_at(String produce_at) {
                this.produce_at = produce_at;
            }

            public double getSize_length() {
                return size_length;
            }

            public void setSize_length(double size_length) {
                this.size_length = size_length;
            }

            public double getSize_width() {
                return size_width;
            }

            public void setSize_width(double size_width) {
                this.size_width = size_width;
            }

            public String getDetails() {
                return details;
            }

            public void setDetails(String details) {
                this.details = details;
            }

            public ImgMainFile1Bean getImg_main_file1() {
                return img_main_file1;
            }

            public void setImg_main_file1(ImgMainFile1Bean img_main_file1) {
                this.img_main_file1 = img_main_file1;
            }

            public ImgMainFile2Bean getImg_main_file2() {
                return img_main_file2;
            }

            public void setImg_main_file2(ImgMainFile2Bean img_main_file2) {
                this.img_main_file2 = img_main_file2;
            }

            public ImgMainFile3Bean getImg_main_file3() {
                return img_main_file3;
            }

            public void setImg_main_file3(ImgMainFile3Bean img_main_file3) {
                this.img_main_file3 = img_main_file3;
            }

            public ImgDetailFile1Bean getImg_detail_file1() {
                return img_detail_file1;
            }

            public void setImg_detail_file1(ImgDetailFile1Bean img_detail_file1) {
                this.img_detail_file1 = img_detail_file1;
            }

            public String getImg_detail_file1_desc() {
                return img_detail_file1_desc;
            }

            public void setImg_detail_file1_desc(String img_detail_file1_desc) {
                this.img_detail_file1_desc = img_detail_file1_desc;
            }

            public ImgDetailFile2Bean getImg_detail_file2() {
                return img_detail_file2;
            }

            public void setImg_detail_file2(ImgDetailFile2Bean img_detail_file2) {
                this.img_detail_file2 = img_detail_file2;
            }

            public String getImg_detail_file2_desc() {
                return img_detail_file2_desc;
            }

            public void setImg_detail_file2_desc(String img_detail_file2_desc) {
                this.img_detail_file2_desc = img_detail_file2_desc;
            }

            public ImgDetailFile3Bean getImg_detail_file3() {
                return img_detail_file3;
            }

            public void setImg_detail_file3(ImgDetailFile3Bean img_detail_file3) {
                this.img_detail_file3 = img_detail_file3;
            }

            public String getImg_detail_file3_desc() {
                return img_detail_file3_desc;
            }

            public void setImg_detail_file3_desc(String img_detail_file3_desc) {
                this.img_detail_file3_desc = img_detail_file3_desc;
            }

            public ImgDetailFile4Bean getImg_detail_file4() {
                return img_detail_file4;
            }

            public void setImg_detail_file4(ImgDetailFile4Bean img_detail_file4) {
                this.img_detail_file4 = img_detail_file4;
            }

            public String getImg_detail_file4_desc() {
                return img_detail_file4_desc;
            }

            public void setImg_detail_file4_desc(String img_detail_file4_desc) {
                this.img_detail_file4_desc = img_detail_file4_desc;
            }

            public ImgDetailFile5Bean getImg_detail_file5() {
                return img_detail_file5;
            }

            public void setImg_detail_file5(ImgDetailFile5Bean img_detail_file5) {
                this.img_detail_file5 = img_detail_file5;
            }

            public String getImg_detail_file5_desc() {
                return img_detail_file5_desc;
            }

            public void setImg_detail_file5_desc(String img_detail_file5_desc) {
                this.img_detail_file5_desc = img_detail_file5_desc;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getFee() {
                return fee;
            }

            public void setFee(String fee) {
                this.fee = fee;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public String getAasm_state() {
                return aasm_state;
            }

            public void setAasm_state(String aasm_state) {
                this.aasm_state = aasm_state;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public int getCollection_id() {
                return collection_id;
            }

            public void setCollection_id(int collection_id) {
                this.collection_id = collection_id;
            }

            public int getItem_id() {
                return item_id;
            }

            public void setItem_id(int item_id) {
                this.item_id = item_id;
            }

            public int getMember_id() {
                return member_id;
            }

            public void setMember_id(int member_id) {
                this.member_id = member_id;
            }

            public String getItem_hash() {
                return item_hash;
            }

            public void setItem_hash(String item_hash) {
                this.item_hash = item_hash;
            }

            public String getAuction_start_time() {
                return auction_start_time;
            }

            public void setAuction_start_time(String auction_start_time) {
                this.auction_start_time = auction_start_time;
            }

            public String getAuction_end_time() {
                return auction_end_time;
            }

            public void setAuction_end_time(String auction_end_time) {
                this.auction_end_time = auction_end_time;
            }

            public boolean isLiked_by_me() {
                return liked_by_me;
            }

            public void setLiked_by_me(boolean liked_by_me) {
                this.liked_by_me = liked_by_me;
            }

            public boolean isDisliked_by_me() {
                return disliked_by_me;
            }

            public void setDisliked_by_me(boolean disliked_by_me) {
                this.disliked_by_me = disliked_by_me;
            }

            public boolean isFavorite_by_me() {
                return favorite_by_me;
            }

            public void setFavorite_by_me(boolean favorite_by_me) {
                this.favorite_by_me = favorite_by_me;
            }

            public int getLiked_count() {
                return liked_count;
            }

            public void setLiked_count(int liked_count) {
                this.liked_count = liked_count;
            }

            public int getDislike_count() {
                return dislike_count;
            }

            public void setDislike_count(int dislike_count) {
                this.dislike_count = dislike_count;
            }

            public int getFavorite_count() {
                return favorite_count;
            }

            public void setFavorite_count(int favorite_count) {
                this.favorite_count = favorite_count;
            }

            public int getSignature_count() {
                return signature_count;
            }

            public void setSignature_count(int signature_count) {
                this.signature_count = signature_count;
            }

            public String getRoyalty() {
                return royalty;
            }

            public void setRoyalty(String royalty) {
                this.royalty = royalty;
            }

            public String getRoyalty_expired_at() {
                return royalty_expired_at;
            }

            public void setRoyalty_expired_at(String royalty_expired_at) {
                this.royalty_expired_at = royalty_expired_at;
            }

            public boolean isHas_royalty() {
                return has_royalty;
            }

            public void setHas_royalty(boolean has_royalty) {
                this.has_royalty = has_royalty;
            }

            public String getLive2d_file() {
                return live2d_file;
            }

            public void setLive2d_file(String live2d_file) {
                this.live2d_file = live2d_file;
            }

            public String getLive2d_ipfs_url() {
                return live2d_ipfs_url;
            }

            public void setLive2d_ipfs_url(String live2d_ipfs_url) {
                this.live2d_ipfs_url = live2d_ipfs_url;
            }

            public String getLive2d_ipfs_zip_url() {
                return live2d_ipfs_zip_url;
            }

            public void setLive2d_ipfs_zip_url(String live2d_ipfs_zip_url) {
                this.live2d_ipfs_zip_url = live2d_ipfs_zip_url;
            }

            public int getCollection_mode() {
                return collection_mode;
            }

            public void setCollection_mode(int collection_mode) {
                this.collection_mode = collection_mode;
            }

            public int getTotal_amount() {
                return total_amount;
            }

            public void setTotal_amount(int total_amount) {
                this.total_amount = total_amount;
            }

            public int getHas_amount() {
                return has_amount;
            }

            public void setHas_amount(int has_amount) {
                this.has_amount = has_amount;
            }

            public boolean isIs_owner() {
                return is_owner;
            }

            public void setIs_owner(boolean is_owner) {
                this.is_owner = is_owner;
            }

            public int getSelling_amount() {
                return selling_amount;
            }

            public void setSelling_amount(int selling_amount) {
                this.selling_amount = selling_amount;
            }

            public int getTrades_count() {
                return trades_count;
            }

            public void setTrades_count(int trades_count) {
                this.trades_count = trades_count;
            }

            public AuthorBean getAuthor() {
                return author;
            }

            public void setAuthor(AuthorBean author) {
                this.author = author;
            }

            public static class ImgMainFile1Bean implements Serializable {
                /**
                 * url : https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/art/img_main_file1/105/d66198b8-5017-4626-8380-7e27333ec06c.jpg
                 */

                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }

            public static class ImgMainFile2Bean implements Serializable {
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

            public static class ImgMainFile3Bean implements Serializable {
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

            public static class ImgDetailFile1Bean implements Serializable {
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

            public static class ImgDetailFile2Bean implements Serializable {
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

            public static class ImgDetailFile3Bean implements Serializable {
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

            public static class ImgDetailFile4Bean implements Serializable {
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

            public static class ImgDetailFile5Bean implements Serializable {
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

            public static class AuthorBean implements Serializable {
                /**
                 * id : 5
                 * uid : Zby8V9CEFveSYKzr1DcCrWVJ
                 * sn : BPPVE6PAGS
                 * email : null
                 * display_name : null
                 * token : null
                 * phone_number : null
                 * id_document_validated : false
                 * app_validated : false
                 * pay_password_validated : false
                 * ref_code : 26449644
                 * created_at : 1607655600
                 * expire_at :
                 * is_read_agreement : null
                 * is_binding_invitation : false
                 * address : 5CqQgVQVf8tFR6gN2TL2pHg5YprjuMS4siSceFutffNBAyfy
                 * recommend_image : {"url":null}
                 * sex : null
                 * desc : null
                 * avatar : {"url":null}
                 * is_artist : false
                 * artist_desc : null
                 * follow_user_size : 1
                 * following_user_size : 2
                 * follow_by_me : false
                 * favorite_art_size : 1
                 * art_size : 1
                 * residential_address : null
                 * college : null
                 */

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
                private String is_read_agreement;
                private boolean is_binding_invitation;
                private String address;
                private RecommendImageBean recommend_image;
                private String sex;
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

                public String getIs_read_agreement() {
                    return is_read_agreement;
                }

                public void setIs_read_agreement(String is_read_agreement) {
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

                public static class RecommendImageBean implements Serializable {
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

                public static class AvatarBean implements Serializable {
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
        }
    }
}
