package com.gammaray.entity;

import java.io.Serializable;

public class BoughtArtVo implements Serializable {
    /**
     * id : 25
     * sn : 7PpRSCSouQscCt74i7PDg1y7EYZGw2QS
     * collection_id : 1
     * item_id : 184
     * amount : 1.0
     * price : 0.5
     * total_price : 0.5
     * finished_at : 1618576730
     * pay_type : uart
     * art : {"id":95,"name":"Test92","category_id":1,"theme_id":1,"material_id":1,"produce_at":"1617552000","size_length":123,"size_width":123,"details":"","img_main_file1":{"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/art/img_main_file1/95/03d6a818-aa49-4a74-8c9b-54b71912c8cf.jpeg"},"img_main_file2":{"url":null},"img_main_file3":{"url":null},"img_detail_file1":{"url":null},"img_detail_file1_desc":"","img_detail_file2":{"url":null},"img_detail_file2_desc":"","img_detail_file3":{"url":null},"img_detail_file3_desc":"","img_detail_file4":{"url":null},"img_detail_file4_desc":"","img_detail_file5":{"url":null},"img_detail_file5_desc":"","price":"1.0","fee":null,"position":null,"aasm_state":"online","created_at":"1618576634","collection_id":1,"item_id":184,"member_id":10,"item_hash":"0x782c005f1bc4ae46bb9bc401407978ffa8784e0a","auction_start_time":null,"auction_end_time":null,"liked_by_me":false,"disliked_by_me":false,"favorite_by_me":false,"liked_count":0,"dislike_count":0,"favorite_count":0,"signature_count":1,"royalty":null,"royalty_expired_at":null,"has_royalty":false,"live2d_file":"","live2d_ipfs_url":"","live2d_ipfs_zip_url":"","collection_mode":1,"total_amount":1,"has_amount":1,"is_owner":true,"selling_amount":"0.0","author":{"id":10,"uid":"83p1vVWGpMDWFWxZ4Vrc7uRh","sn":"RZM5665WY6","email":null,"display_name":"Scorpio Cat","token":"72cafd04d6b11abe6976f2b3be34818bde90e836241436cefbeb90cbd66fea6d","phone_number":"15511111111","id_document_validated":false,"app_validated":false,"pay_password_validated":false,"ref_code":"16816028","created_at":"1610417730","expire_at":"2021-04-22 20:46:10","is_read_agreement":null,"is_binding_invitation":false,"address":"5ERmAEibmWT9DLnLqZyfrcTaneHMVikW7mq9QmeV2ovPcM6s","recommend_image":{"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/recommend_image/10/d3e8ef75-ce5c-4d6e-83ed-f6c22b211331.png"},"sex":2,"desc":"I'm a cat","avatar":{"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/avatar/10/8cddc856-3aee-45f4-ba47-f6132146bf51.png"},"is_artist":true,"artist_desc":"I'm a scorpiocat","follow_user_size":3,"following_user_size":3,"follow_by_me":false,"favorite_art_size":0,"art_size":6,"residential_address":null,"college":"Nanjing"}}
     * buyer : {"id":11,"uid":"U9i38dpvcrsydC1vtqWkGZd4","sn":"RPZNCLRCFY","email":null,"display_name":"lucifer","token":"aaeb2e0a5430a96921f362298d92cc217c5dc6cd5ae18ad09bd39233aed2d758","phone_number":"null","id_document_validated":false,"app_validated":false,"pay_password_validated":false,"ref_code":"67749002","created_at":"1610440129","expire_at":"2021-04-23 20:36:24","is_read_agreement":null,"is_binding_invitation":false,"address":"5GNLQHb56SDF6ASRPoNHSVKQxP47cvxhXL5DaRKTLSJJqF1c","recommend_image":{"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/recommend_image/11/de72717a-c952-42f0-a06f-5284bbf2713e.jpg"},"sex":null,"desc":null,"avatar":{"url":null},"is_artist":true,"artist_desc":"whatsfuck","follow_user_size":1,"following_user_size":0,"follow_by_me":false,"favorite_art_size":0,"art_size":5,"residential_address":null,"college":null}
     * seller : {"id":10,"uid":"83p1vVWGpMDWFWxZ4Vrc7uRh","sn":"RZM5665WY6","email":null,"display_name":"Scorpio Cat","token":"72cafd04d6b11abe6976f2b3be34818bde90e836241436cefbeb90cbd66fea6d","phone_number":"15511111111","id_document_validated":false,"app_validated":false,"pay_password_validated":false,"ref_code":"16816028","created_at":"1610417730","expire_at":"2021-04-22 20:46:10","is_read_agreement":null,"is_binding_invitation":false,"address":"5ERmAEibmWT9DLnLqZyfrcTaneHMVikW7mq9QmeV2ovPcM6s","recommend_image":{"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/recommend_image/10/d3e8ef75-ce5c-4d6e-83ed-f6c22b211331.png"},"sex":2,"desc":"I'm a cat","avatar":{"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/avatar/10/8cddc856-3aee-45f4-ba47-f6132146bf51.png"},"is_artist":true,"artist_desc":"I'macat scorpiocat","follow_user_size":3,"following_user_size":3,"follow_by_me":false,"favorite_art_size":0,"art_size":6,"residential_address":null,"college":"Nanjing"}
     */

    private int id;
    private String sn;
    private int collection_id;
    private int item_id;
    private String amount;
    private String price;
    private String total_price;
    private long finished_at;
    private String pay_type;
    private String royalty;
    private String deposit;
    private String assm_state;
    private String created_at;
    private String trade_refer;
    private ArtBean art;
    private BuyerBean buyer;
    private SellerBean seller;
    private AuctionArtVo auction;

    public String getRoyalty() {
        return royalty;
    }

    public void setRoyalty(String royalty) {
        this.royalty = royalty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public long getFinished_at() {
        return finished_at;
    }

    public void setFinished_at(long finished_at) {
        this.finished_at = finished_at;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public ArtBean getArt() {
        return art;
    }

    public void setArt(ArtBean art) {
        this.art = art;
    }

    public BuyerBean getBuyer() {
        return buyer;
    }

    public void setBuyer(BuyerBean buyer) {
        this.buyer = buyer;
    }

    public SellerBean getSeller() {
        return seller;
    }

    public void setSeller(SellerBean seller) {
        this.seller = seller;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getAssm_state() {
        return assm_state;
    }

    public void setAssm_state(String assm_state) {
        this.assm_state = assm_state;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTrade_refer() {
        return trade_refer;
    }

    public void setTrade_refer(String trade_refer) {
        this.trade_refer = trade_refer;
    }

    public AuctionArtVo getAuction() {
        return auction;
    }

    public void setAuction(AuctionArtVo auction) {
        this.auction = auction;
    }

    public static class ArtBean implements Serializable {
        /**
         * id : 95
         * name : Test92
         * category_id : 1
         * theme_id : 1
         * material_id : 1
         * produce_at : 1617552000
         * size_length : 123
         * size_width : 123
         * details :
         * img_main_file1 : {"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/art/img_main_file1/95/03d6a818-aa49-4a74-8c9b-54b71912c8cf.jpeg"}
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
         * price : 1.0
         * fee : null
         * position : null
         * aasm_state : online
         * created_at : 1618576634
         * collection_id : 1
         * item_id : 184
         * member_id : 10
         * item_hash : 0x782c005f1bc4ae46bb9bc401407978ffa8784e0a
         * auction_start_time : null
         * auction_end_time : null
         * liked_by_me : false
         * disliked_by_me : false
         * favorite_by_me : false
         * liked_count : 0
         * dislike_count : 0
         * favorite_count : 0
         * signature_count : 1
         * royalty : null
         * royalty_expired_at : null
         * has_royalty : false
         * live2d_file :
         * live2d_ipfs_url :
         * live2d_ipfs_zip_url :
         * collection_mode : 1
         * total_amount : 1
         * has_amount : 1
         * is_owner : true
         * selling_amount : 0.0
         * author : {"id":10,"uid":"83p1vVWGpMDWFWxZ4Vrc7uRh","sn":"RZM5665WY6","email":null,"display_name":"Scorpio Cat","token":"72cafd04d6b11abe6976f2b3be34818bde90e836241436cefbeb90cbd66fea6d","phone_number":"15511111111","id_document_validated":false,"app_validated":false,"pay_password_validated":false,"ref_code":"16816028","created_at":"1610417730","expire_at":"2021-04-22 20:46:10","is_read_agreement":null,"is_binding_invitation":false,"address":"5ERmAEibmWT9DLnLqZyfrcTaneHMVikW7mq9QmeV2ovPcM6s","recommend_image":{"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/recommend_image/10/d3e8ef75-ce5c-4d6e-83ed-f6c22b211331.png"},"sex":2,"desc":"I'm a cat","avatar":{"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/avatar/10/8cddc856-3aee-45f4-ba47-f6132146bf51.png"},"is_artist":true,"artist_desc":"I'm a scorpiocat","follow_user_size":3,"following_user_size":3,"follow_by_me":false,"favorite_art_size":0,"art_size":6,"residential_address":null,"college":"Nanjing"}
         */

        private int id;
        private String name;
        private int category_id;
        private int theme_id;
        private int material_id;
        private String produce_at;
        private int size_length;
        private int size_width;
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
        private long created_at;
        private int collection_id;
        private int item_id;
        private int member_id;
        private String item_hash;
        private long auction_start_time;
        private long auction_end_time;
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
        private String selling_amount;
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

        public int getSize_length() {
            return size_length;
        }

        public void setSize_length(int size_length) {
            this.size_length = size_length;
        }

        public int getSize_width() {
            return size_width;
        }

        public void setSize_width(int size_width) {
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

        public long getCreated_at() {
            return created_at;
        }

        public void setCreated_at(long created_at) {
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

        public long getAuction_start_time() {
            return auction_start_time;
        }

        public void setAuction_start_time(long auction_start_time) {
            this.auction_start_time = auction_start_time;
        }

        public long getAuction_end_time() {
            return auction_end_time;
        }

        public void setAuction_end_time(long auction_end_time) {
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

        public String getSelling_amount() {
            return selling_amount;
        }

        public void setSelling_amount(String selling_amount) {
            this.selling_amount = selling_amount;
        }

        public AuthorBean getAuthor() {
            return author;
        }

        public void setAuthor(AuthorBean author) {
            this.author = author;
        }

        public static class ImgMainFile1Bean implements Serializable {
            /**
             * url : https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/art/img_main_file1/95/03d6a818-aa49-4a74-8c9b-54b71912c8cf.jpeg
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

            private Object url;

            public Object getUrl() {
                return url;
            }

            public void setUrl(Object url) {
                this.url = url;
            }
        }

        public static class ImgMainFile3Bean implements Serializable {
            /**
             * url : null
             */

            private Object url;

            public Object getUrl() {
                return url;
            }

            public void setUrl(Object url) {
                this.url = url;
            }
        }

        public static class ImgDetailFile1Bean implements Serializable {
            /**
             * url : null
             */

            private Object url;

            public Object getUrl() {
                return url;
            }

            public void setUrl(Object url) {
                this.url = url;
            }
        }

        public static class ImgDetailFile2Bean implements Serializable {
            /**
             * url : null
             */

            private Object url;

            public Object getUrl() {
                return url;
            }

            public void setUrl(Object url) {
                this.url = url;
            }
        }

        public static class ImgDetailFile3Bean implements Serializable {
            /**
             * url : null
             */

            private Object url;

            public Object getUrl() {
                return url;
            }

            public void setUrl(Object url) {
                this.url = url;
            }
        }

        public static class ImgDetailFile4Bean implements Serializable {
            /**
             * url : null
             */

            private Object url;

            public Object getUrl() {
                return url;
            }

            public void setUrl(Object url) {
                this.url = url;
            }
        }

        public static class ImgDetailFile5Bean implements Serializable {
            /**
             * url : null
             */

            private Object url;

            public Object getUrl() {
                return url;
            }

            public void setUrl(Object url) {
                this.url = url;
            }
        }

        public static class AuthorBean implements Serializable {
            /**
             * id : 10
             * uid : 83p1vVWGpMDWFWxZ4Vrc7uRh
             * sn : RZM5665WY6
             * email : null
             * display_name : Scorpio Cat
             * token : 72cafd04d6b11abe6976f2b3be34818bde90e836241436cefbeb90cbd66fea6d
             * phone_number : 15511111111
             * id_document_validated : false
             * app_validated : false
             * pay_password_validated : false
             * ref_code : 16816028
             * created_at : 1610417730
             * expire_at : 2021-04-22 20:46:10
             * is_read_agreement : null
             * is_binding_invitation : false
             * address : 5ERmAEibmWT9DLnLqZyfrcTaneHMVikW7mq9QmeV2ovPcM6s
             * recommend_image : {"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/recommend_image/10/d3e8ef75-ce5c-4d6e-83ed-f6c22b211331.png"}
             * sex : 2
             * desc : I'm a cat
             * avatar : {"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/avatar/10/8cddc856-3aee-45f4-ba47-f6132146bf51.png"}
             * is_artist : true
             * artist_desc : I'm a scorpiocat
             * follow_user_size : 3
             * following_user_size : 3
             * follow_by_me : false
             * favorite_art_size : 0
             * art_size : 6
             * residential_address : null
             * college : Nanjing
             */

            private int id;
            private String uid;
            private String sn;
            private Object email;
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
            private Object residential_address;
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

            public Object getEmail() {
                return email;
            }

            public void setEmail(Object email) {
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

            public Object getResidential_address() {
                return residential_address;
            }

            public void setResidential_address(Object residential_address) {
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
                 * url : https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/recommend_image/10/d3e8ef75-ce5c-4d6e-83ed-f6c22b211331.png
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
                 * url : https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/avatar/10/8cddc856-3aee-45f4-ba47-f6132146bf51.png
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

    public static class BuyerBean implements Serializable {
        /**
         * id : 11
         * uid : U9i38dpvcrsydC1vtqWkGZd4
         * sn : RPZNCLRCFY
         * email : null
         * display_name : lucifer
         * token : aaeb2e0a5430a96921f362298d92cc217c5dc6cd5ae18ad09bd39233aed2d758
         * phone_number : null
         * id_document_validated : false
         * app_validated : false
         * pay_password_validated : false
         * ref_code : 67749002
         * created_at : 1610440129
         * expire_at : 2021-04-23 20:36:24
         * is_read_agreement : null
         * is_binding_invitation : false
         * address : 5GNLQHb56SDF6ASRPoNHSVKQxP47cvxhXL5DaRKTLSJJqF1c
         * recommend_image : {"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/recommend_image/11/de72717a-c952-42f0-a06f-5284bbf2713e.jpg"}
         * sex : null
         * desc : null
         * avatar : {"url":null}
         * is_artist : true
         * artist_desc : whatsfuck
         * follow_user_size : 1
         * following_user_size : 0
         * follow_by_me : false
         * favorite_art_size : 0
         * art_size : 5
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

    public static class SellerBean implements Serializable {
        /**
         * id : 10
         * uid : 83p1vVWGpMDWFWxZ4Vrc7uRh
         * sn : RZM5665WY6
         * email : null
         * display_name : Scorpio Cat
         * token : 72cafd04d6b11abe6976f2b3be34818bde90e836241436cefbeb90cbd66fea6d
         * phone_number : 15511111111
         * id_document_validated : false
         * app_validated : false
         * pay_password_validated : false
         * ref_code : 16816028
         * created_at : 1610417730
         * expire_at : 2021-04-22 20:46:10
         * is_read_agreement : null
         * is_binding_invitation : false
         * address : 5ERmAEibmWT9DLnLqZyfrcTaneHMVikW7mq9QmeV2ovPcM6s
         * recommend_image : {"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/recommend_image/10/d3e8ef75-ce5c-4d6e-83ed-f6c22b211331.png"}
         * sex : 2
         * desc : I'm a cat
         * avatar : {"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/avatar/10/8cddc856-3aee-45f4-ba47-f6132146bf51.png"}
         * is_artist : true
         * artist_desc : I'macat scorpiocat
         * follow_user_size : 3
         * following_user_size : 3
         * follow_by_me : false
         * favorite_art_size : 0
         * art_size : 6
         * residential_address : null
         * college : Nanjing
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
        private boolean is_read_agreement;
        private boolean is_binding_invitation;
        private String address;
        private RecommendImageBeanXX recommend_image;
        private int sex;
        private String desc;
        private AvatarBeanXX avatar;
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

        public boolean getIs_read_agreement() {
            return is_read_agreement;
        }

        public void setIs_read_agreement(boolean is_read_agreement) {
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

        public RecommendImageBeanXX getRecommend_image() {
            return recommend_image;
        }

        public void setRecommend_image(RecommendImageBeanXX recommend_image) {
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

        public AvatarBeanXX getAvatar() {
            return avatar;
        }

        public void setAvatar(AvatarBeanXX avatar) {
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

        public static class RecommendImageBeanXX implements Serializable {
            /**
             * url : https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/recommend_image/10/d3e8ef75-ce5c-4d6e-83ed-f6c22b211331.png
             */

            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class AvatarBeanXX implements Serializable {
            /**
             * url : https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/avatar/10/8cddc856-3aee-45f4-ba47-f6132146bf51.png
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

    public static class AuctionArtVo implements Serializable {

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

        private BuyerBean buyer;

        private String royalty;

        private ArtBean art;

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

        public BuyerBean getBuyer() {
            return buyer;
        }

        public void setBuyer(BuyerBean buyer) {
            this.buyer = buyer;
        }

        public ArtBean getArt() {
            return art;
        }

        public void setArt(ArtBean art) {
            this.art = art;
        }

        public String getRoyalty() {
            return royalty;
        }

        public void setRoyalty(String royalty) {
            this.royalty = royalty;
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
    }
}
