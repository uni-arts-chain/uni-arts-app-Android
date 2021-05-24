package com.yunhualian.entity;

public class CollectArtVo {

    /**
     * id : 64
     * created_at : 1619777794
     * favoritable : {"id":180,"name":"shubiao","category_id":12,"theme_id":null,"material_id":null,"produce_at":null,"size_length":null,"size_width":null,"details":"shubiao","img_main_file1":{"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/art/img_main_file1/180/4f6c9e9b-9142-49b8-8449-746483f76872."},"img_main_file2":{"url":null},"img_main_file3":{"url":null},"img_detail_file1":{"url":null},"img_detail_file1_desc":null,"img_detail_file2":{"url":null},"img_detail_file2_desc":null,"img_detail_file3":{"url":null},"img_detail_file3_desc":null,"img_detail_file4":{"url":null},"img_detail_file4_desc":null,"img_detail_file5":{"url":null},"img_detail_file5_desc":null,"price":"0.0","fee":null,"position":null,"aasm_state":"bidding","created_at":"1619751793","collection_id":5,"item_id":10,"member_id":13,"item_hash":"0xb6e7e45f6f6a82d2a3b42962a7e9c9281821479e","auction_start_time":null,"auction_end_time":null,"liked_by_me":false,"disliked_by_me":false,"favorite_by_me":true,"liked_count":0,"dislike_count":0,"favorite_count":1,"signature_count":0,"royalty":null,"royalty_expired_at":null,"has_royalty":false,"live2d_file":null,"live2d_ipfs_url":"","live2d_ipfs_zip_url":"","collection_mode":3,"total_amount":1000,"has_amount":0,"is_owner":false,"selling_amount":"0.0","trades_count":6,"author":{"id":13,"uid":"qS3r14ymq95hm2oRWxTcDFhb","sn":"MUNV3MGXOT","email":null,"display_name":"zhubin","token":"a1ff60089c02a4d9277f623f9ba20087136876504f54fb9326fde4542a0dd23a","phone_number":null,"id_document_validated":false,"app_validated":false,"pay_password_validated":false,"ref_code":"31397878","created_at":"1610526303","expire_at":"2021-05-06 14:48:12","is_read_agreement":null,"is_binding_invitation":false,"address":"5FvC8ong12CCEeYj3gHxemk4h8y4sF8c3BxP92WDXbDnHiYr","recommend_image":{"url":null},"sex":null,"desc":"测试结果啦啦啦","avatar":{"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/avatar/13/14bcf4aa-fae3-46bd-8aba-354f4df1a6d2."},"is_artist":false,"artist_desc":null,"follow_user_size":2,"following_user_size":6,"follow_by_me":true,"favorite_art_size":6,"art_size":6,"residential_address":"南京","college":"测试"}}
     * favoritor : {"id":78,"uid":"jt6ffzdt7UoZGdHGHuzbg2aV","sn":"O73LGI2AWR","email":null,"display_name":"哈哈哈哈","token":"ae0c91ac37c567182541db373b8136ba5a33720ed9bd95ce3b98d7147be7b5e4","phone_number":"8618651090153","id_document_validated":false,"app_validated":false,"pay_password_validated":false,"ref_code":"26271084","created_at":"1619492473","expire_at":"2021-05-04 11:01:14","is_read_agreement":null,"is_binding_invitation":false,"address":"5GC1TZ4GUcimKuu33dfWF9Dwi5F6RxzT53SV1L5MzkhgJqWZ","recommend_image":{"url":null},"sex":null,"desc":"艺术就是爆炸 fuck","avatar":{"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/avatar/78/c25762cb-5e72-4aaa-ab98-b22666e25b7e."},"is_artist":false,"artist_desc":"艺术就是爆炸","follow_user_size":0,"following_user_size":2,"follow_by_me":false,"favorite_art_size":1,"art_size":12,"residential_address":null,"college":null}
     */

    private int id;
    private String created_at;
    private FavoritableBean favoritable;
    private FavoritorBean favoritor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public FavoritableBean getFavoritable() {
        return favoritable;
    }

    public void setFavoritable(FavoritableBean favoritable) {
        this.favoritable = favoritable;
    }

    public FavoritorBean getFavoritor() {
        return favoritor;
    }

    public void setFavoritor(FavoritorBean favoritor) {
        this.favoritor = favoritor;
    }

    public static class FavoritableBean {
        /**
         * id : 180
         * name : shubiao
         * category_id : 12
         * theme_id : null
         * material_id : null
         * produce_at : null
         * size_length : null
         * size_width : null
         * details : shubiao
         * img_main_file1 : {"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/art/img_main_file1/180/4f6c9e9b-9142-49b8-8449-746483f76872."}
         * img_main_file2 : {"url":null}
         * img_main_file3 : {"url":null}
         * img_detail_file1 : {"url":null}
         * img_detail_file1_desc : null
         * img_detail_file2 : {"url":null}
         * img_detail_file2_desc : null
         * img_detail_file3 : {"url":null}
         * img_detail_file3_desc : null
         * img_detail_file4 : {"url":null}
         * img_detail_file4_desc : null
         * img_detail_file5 : {"url":null}
         * img_detail_file5_desc : null
         * price : 0.0
         * fee : null
         * position : null
         * aasm_state : bidding
         * created_at : 1619751793
         * collection_id : 5
         * item_id : 10
         * member_id : 13
         * item_hash : 0xb6e7e45f6f6a82d2a3b42962a7e9c9281821479e
         * auction_start_time : null
         * auction_end_time : null
         * liked_by_me : false
         * disliked_by_me : false
         * favorite_by_me : true
         * liked_count : 0
         * dislike_count : 0
         * favorite_count : 1
         * signature_count : 0
         * royalty : null
         * royalty_expired_at : null
         * has_royalty : false
         * live2d_file : null
         * live2d_ipfs_url :
         * live2d_ipfs_zip_url :
         * collection_mode : 3
         * total_amount : 1000
         * has_amount : 0
         * is_owner : false
         * selling_amount : 0.0
         * trades_count : 6
         * author : {"id":13,"uid":"qS3r14ymq95hm2oRWxTcDFhb","sn":"MUNV3MGXOT","email":null,"display_name":"zhubin","token":"a1ff60089c02a4d9277f623f9ba20087136876504f54fb9326fde4542a0dd23a","phone_number":null,"id_document_validated":false,"app_validated":false,"pay_password_validated":false,"ref_code":"31397878","created_at":"1610526303","expire_at":"2021-05-06 14:48:12","is_read_agreement":null,"is_binding_invitation":false,"address":"5FvC8ong12CCEeYj3gHxemk4h8y4sF8c3BxP92WDXbDnHiYr","recommend_image":{"url":null},"sex":null,"desc":"测试结果啦啦啦","avatar":{"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/avatar/13/14bcf4aa-fae3-46bd-8aba-354f4df1a6d2."},"is_artist":false,"artist_desc":null,"follow_user_size":2,"following_user_size":6,"follow_by_me":true,"favorite_art_size":6,"art_size":6,"residential_address":"南京","college":"测试"}
         */

        private int id;
        private String name;
        private int category_id;
        private Object theme_id;
        private Object material_id;
        private Object produce_at;
        private Object size_length;
        private Object size_width;
        private String details;
        private ImgMainFile1Bean img_main_file1;
        private ImgMainFile2Bean img_main_file2;
        private ImgMainFile3Bean img_main_file3;
        private ImgDetailFile1Bean img_detail_file1;
        private Object img_detail_file1_desc;
        private ImgDetailFile2Bean img_detail_file2;
        private Object img_detail_file2_desc;
        private ImgDetailFile3Bean img_detail_file3;
        private Object img_detail_file3_desc;
        private ImgDetailFile4Bean img_detail_file4;
        private Object img_detail_file4_desc;
        private ImgDetailFile5Bean img_detail_file5;
        private Object img_detail_file5_desc;
        private String price;
        private Object fee;
        private Object position;
        private String aasm_state;
        private String created_at;
        private int collection_id;
        private int item_id;
        private int member_id;
        private String item_hash;
        private Object auction_start_time;
        private Object auction_end_time;
        private boolean liked_by_me;
        private boolean disliked_by_me;
        private boolean favorite_by_me;
        private int liked_count;
        private int dislike_count;
        private int favorite_count;
        private int signature_count;
        private Object royalty;
        private Object royalty_expired_at;
        private boolean has_royalty;
        private String live2d_file;
        private String live2d_ipfs_url;
        private String live2d_ipfs_zip_url;
        private int collection_mode;
        private int total_amount;
        private int has_amount;
        private boolean is_owner;
        private String selling_amount;
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

        public Object getTheme_id() {
            return theme_id;
        }

        public void setTheme_id(Object theme_id) {
            this.theme_id = theme_id;
        }

        public Object getMaterial_id() {
            return material_id;
        }

        public void setMaterial_id(Object material_id) {
            this.material_id = material_id;
        }

        public Object getProduce_at() {
            return produce_at;
        }

        public void setProduce_at(Object produce_at) {
            this.produce_at = produce_at;
        }

        public Object getSize_length() {
            return size_length;
        }

        public void setSize_length(Object size_length) {
            this.size_length = size_length;
        }

        public Object getSize_width() {
            return size_width;
        }

        public void setSize_width(Object size_width) {
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

        public Object getImg_detail_file1_desc() {
            return img_detail_file1_desc;
        }

        public void setImg_detail_file1_desc(Object img_detail_file1_desc) {
            this.img_detail_file1_desc = img_detail_file1_desc;
        }

        public ImgDetailFile2Bean getImg_detail_file2() {
            return img_detail_file2;
        }

        public void setImg_detail_file2(ImgDetailFile2Bean img_detail_file2) {
            this.img_detail_file2 = img_detail_file2;
        }

        public Object getImg_detail_file2_desc() {
            return img_detail_file2_desc;
        }

        public void setImg_detail_file2_desc(Object img_detail_file2_desc) {
            this.img_detail_file2_desc = img_detail_file2_desc;
        }

        public ImgDetailFile3Bean getImg_detail_file3() {
            return img_detail_file3;
        }

        public void setImg_detail_file3(ImgDetailFile3Bean img_detail_file3) {
            this.img_detail_file3 = img_detail_file3;
        }

        public Object getImg_detail_file3_desc() {
            return img_detail_file3_desc;
        }

        public void setImg_detail_file3_desc(Object img_detail_file3_desc) {
            this.img_detail_file3_desc = img_detail_file3_desc;
        }

        public ImgDetailFile4Bean getImg_detail_file4() {
            return img_detail_file4;
        }

        public void setImg_detail_file4(ImgDetailFile4Bean img_detail_file4) {
            this.img_detail_file4 = img_detail_file4;
        }

        public Object getImg_detail_file4_desc() {
            return img_detail_file4_desc;
        }

        public void setImg_detail_file4_desc(Object img_detail_file4_desc) {
            this.img_detail_file4_desc = img_detail_file4_desc;
        }

        public ImgDetailFile5Bean getImg_detail_file5() {
            return img_detail_file5;
        }

        public void setImg_detail_file5(ImgDetailFile5Bean img_detail_file5) {
            this.img_detail_file5 = img_detail_file5;
        }

        public Object getImg_detail_file5_desc() {
            return img_detail_file5_desc;
        }

        public void setImg_detail_file5_desc(Object img_detail_file5_desc) {
            this.img_detail_file5_desc = img_detail_file5_desc;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public Object getFee() {
            return fee;
        }

        public void setFee(Object fee) {
            this.fee = fee;
        }

        public Object getPosition() {
            return position;
        }

        public void setPosition(Object position) {
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

        public Object getAuction_start_time() {
            return auction_start_time;
        }

        public void setAuction_start_time(Object auction_start_time) {
            this.auction_start_time = auction_start_time;
        }

        public Object getAuction_end_time() {
            return auction_end_time;
        }

        public void setAuction_end_time(Object auction_end_time) {
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

        public Object getRoyalty() {
            return royalty;
        }

        public void setRoyalty(Object royalty) {
            this.royalty = royalty;
        }

        public Object getRoyalty_expired_at() {
            return royalty_expired_at;
        }

        public void setRoyalty_expired_at(Object royalty_expired_at) {
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

        public static class ImgMainFile1Bean {
            /**
             * url : https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/art/img_main_file1/180/4f6c9e9b-9142-49b8-8449-746483f76872.
             */

            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class ImgMainFile2Bean {
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

        public static class ImgMainFile3Bean {
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

        public static class ImgDetailFile1Bean {
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

        public static class ImgDetailFile2Bean {
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

        public static class ImgDetailFile3Bean {
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

        public static class ImgDetailFile4Bean {
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

        public static class ImgDetailFile5Bean {
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

        public static class AuthorBean {
            /**
             * id : 13
             * uid : qS3r14ymq95hm2oRWxTcDFhb
             * sn : MUNV3MGXOT
             * email : null
             * display_name : zhubin
             * token : a1ff60089c02a4d9277f623f9ba20087136876504f54fb9326fde4542a0dd23a
             * phone_number : null
             * id_document_validated : false
             * app_validated : false
             * pay_password_validated : false
             * ref_code : 31397878
             * created_at : 1610526303
             * expire_at : 2021-05-06 14:48:12
             * is_read_agreement : null
             * is_binding_invitation : false
             * address : 5FvC8ong12CCEeYj3gHxemk4h8y4sF8c3BxP92WDXbDnHiYr
             * recommend_image : {"url":null}
             * sex : null
             * desc : 测试结果啦啦啦
             * avatar : {"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/avatar/13/14bcf4aa-fae3-46bd-8aba-354f4df1a6d2."}
             * is_artist : false
             * artist_desc : null
             * follow_user_size : 2
             * following_user_size : 6
             * follow_by_me : true
             * favorite_art_size : 6
             * art_size : 6
             * residential_address : 南京
             * college : 测试
             */

            private int id;
            private String uid;
            private String sn;
            private Object email;
            private String display_name;
            private String token;
            private Object phone_number;
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
            private Object sex;
            private String desc;
            private AvatarBean avatar;
            private boolean is_artist;
            private Object artist_desc;
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

            public Object getPhone_number() {
                return phone_number;
            }

            public void setPhone_number(Object phone_number) {
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

            public Object getSex() {
                return sex;
            }

            public void setSex(Object sex) {
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

            public Object getArtist_desc() {
                return artist_desc;
            }

            public void setArtist_desc(Object artist_desc) {
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

            public static class AvatarBean {
                /**
                 * url : https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/avatar/13/14bcf4aa-fae3-46bd-8aba-354f4df1a6d2.
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

    public static class FavoritorBean {
        /**
         * id : 78
         * uid : jt6ffzdt7UoZGdHGHuzbg2aV
         * sn : O73LGI2AWR
         * email : null
         * display_name : 哈哈哈哈
         * token : ae0c91ac37c567182541db373b8136ba5a33720ed9bd95ce3b98d7147be7b5e4
         * phone_number : 8618651090153
         * id_document_validated : false
         * app_validated : false
         * pay_password_validated : false
         * ref_code : 26271084
         * created_at : 1619492473
         * expire_at : 2021-05-04 11:01:14
         * is_read_agreement : null
         * is_binding_invitation : false
         * address : 5GC1TZ4GUcimKuu33dfWF9Dwi5F6RxzT53SV1L5MzkhgJqWZ
         * recommend_image : {"url":null}
         * sex : null
         * desc : 艺术就是爆炸 fuck
         * avatar : {"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/avatar/78/c25762cb-5e72-4aaa-ab98-b22666e25b7e."}
         * is_artist : false
         * artist_desc : 艺术就是爆炸
         * follow_user_size : 0
         * following_user_size : 2
         * follow_by_me : false
         * favorite_art_size : 1
         * art_size : 12
         * residential_address : null
         * college : null
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
        private RecommendImageBeanX recommend_image;
        private Object sex;
        private String desc;
        private AvatarBeanX avatar;
        private boolean is_artist;
        private String artist_desc;
        private int follow_user_size;
        private int following_user_size;
        private boolean follow_by_me;
        private int favorite_art_size;
        private int art_size;
        private Object residential_address;
        private Object college;

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

        public RecommendImageBeanX getRecommend_image() {
            return recommend_image;
        }

        public void setRecommend_image(RecommendImageBeanX recommend_image) {
            this.recommend_image = recommend_image;
        }

        public Object getSex() {
            return sex;
        }

        public void setSex(Object sex) {
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

        public Object getResidential_address() {
            return residential_address;
        }

        public void setResidential_address(Object residential_address) {
            this.residential_address = residential_address;
        }

        public Object getCollege() {
            return college;
        }

        public void setCollege(Object college) {
            this.college = college;
        }

        public static class RecommendImageBeanX {
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

        public static class AvatarBeanX {
            /**
             * url : https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/avatar/78/c25762cb-5e72-4aaa-ab98-b22666e25b7e.
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
