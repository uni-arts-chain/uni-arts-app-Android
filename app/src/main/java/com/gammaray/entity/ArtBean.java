package com.gammaray.entity;

public class ArtBean {


    /**
     * id : 16
     * name : One by one
     * category_id : 4
     * theme_id : 6
     * material_id : 3
     * produce_at : 1487001600
     * size_length : 50
     * size_width : 40
     * details : The dream always loves to show its unique affinity and literary friends in the unreachable distance. In the picture, that extends to the end of the world and the sea of clouds, and the purest and primitive hope has sprouted, calm and insignificant. Amidst the bluish-purple romantic tones, the freedom is most free.
     * img_main_file1 : {"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/art/img_main_file1/16/959daf4e-d080-4aa9-97a7-3d415e24d954.png"}
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
     * price : 1.1
     * fee : null
     * position : null
     * aasm_state : auctioning
     * created_at : 1614067726
     * collection_id : 1
     * item_id : 73
     * member_id : 17
     * item_hash : 0x55dd15413149f97f221679ae0d4903287bdec2fe
     * auction_start_time : 1117696
     * auction_end_time : 1348096
     * liked_by_me : false
     * disliked_by_me : false
     * favorite_by_me : false
     * liked_count : 2
     * dislike_count : 0
     * favorite_count : 1
     * signature_count : 0
     * royalty : 0.0
     * royalty_expired_at : null
     * has_royalty : false
     * live2d_file : null
     * live2d_ipfs_url :
     * member : {"id":17,"uid":"Bdq2Ne27QeuqR8SHBFBib2UK","sn":"RGGLPB4ISJ","email":"","display_name":"商夏周","token":"c9281425d23b6fb08fc468090f512c0e618fff3a0d87ce8e437221dea0a26864","phone_number":"8618879026320","id_document_validated":false,"app_validated":false,"pay_password_validated":false,"ref_code":"00946973","created_at":"1611024576","expire_at":"2021-04-07 14:36:56","is_read_agreement":null,"is_binding_invitation":false,"address":"5HdtZ1Qyhi6JYkGCvhC72y7D8fm4APD1fkavnE6rDPseKGSE","recommend_image":{"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/recommend_image/17/d50556f3-ead1-4cfa-9f65-4daab12207a4.jpg"},"sex":2,"desc":"个人创作者","avatar":{"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/avatar/17/f3dcd31f-6c5d-4f6a-aaff-550d96fd8d54.jpg"},"is_artist":true,"artist_desc":"喜欢纯真自然的东西，所创作的画作都是来自世间万物的灵感","follow_user_size":5,"following_user_size":3,"follow_by_me":true,"favorite_art_size":11,"art_size":6,"residential_address":"南京","college":"南艺"}
     * author : {"id":17,"uid":"Bdq2Ne27QeuqR8SHBFBib2UK","sn":"RGGLPB4ISJ","email":"","display_name":"商夏周","token":"c9281425d23b6fb08fc468090f512c0e618fff3a0d87ce8e437221dea0a26864","phone_number":"8618879026320","id_document_validated":false,"app_validated":false,"pay_password_validated":false,"ref_code":"00946973","created_at":"1611024576","expire_at":"2021-04-07 14:36:56","is_read_agreement":null,"is_binding_invitation":false,"address":"5HdtZ1Qyhi6JYkGCvhC72y7D8fm4APD1fkavnE6rDPseKGSE","recommend_image":{"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/recommend_image/17/d50556f3-ead1-4cfa-9f65-4daab12207a4.jpg"},"sex":2,"desc":"个人创作者","avatar":{"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/avatar/17/f3dcd31f-6c5d-4f6a-aaff-550d96fd8d54.jpg"},"is_artist":true,"artist_desc":"喜欢纯真自然的东西，所创作的画作都是来自世间万物的灵感","follow_user_size":5,"following_user_size":3,"follow_by_me":true,"favorite_art_size":11,"art_size":6,"residential_address":"南京","college":"南艺"}
     */

    private int id;
    private String name;
    private int category_id;
    private int theme_id;
    private int material_id;
    private String material_des;

    public String getMaterial_des() {
        return material_des;
    }

    public void setMaterial_des(String material_des) {
        this.material_des = material_des;
    }

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
    private Object fee;
    private Object position;
    private String aasm_state;
    private String created_at;
    private int collection_id;
    private int item_id;
    private int member_id;
    private String item_hash;
    private int auction_start_time;
    private int auction_end_time;
    private boolean liked_by_me;
    private boolean disliked_by_me;
    private boolean favorite_by_me;
    private int liked_count;
    private int dislike_count;
    private int favorite_count;
    private int signature_count;
    private String royalty;
    private Object royalty_expired_at;
    private boolean has_royalty;
    private Object live2d_file;
    private String live2d_ipfs_url;
    private MemberBean member;
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

    public int getAuction_start_time() {
        return auction_start_time;
    }

    public void setAuction_start_time(int auction_start_time) {
        this.auction_start_time = auction_start_time;
    }

    public int getAuction_end_time() {
        return auction_end_time;
    }

    public void setAuction_end_time(int auction_end_time) {
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

    public Object getLive2d_file() {
        return live2d_file;
    }

    public void setLive2d_file(Object live2d_file) {
        this.live2d_file = live2d_file;
    }

    public String getLive2d_ipfs_url() {
        return live2d_ipfs_url;
    }

    public void setLive2d_ipfs_url(String live2d_ipfs_url) {
        this.live2d_ipfs_url = live2d_ipfs_url;
    }

    public MemberBean getMember() {
        return member;
    }

    public void setMember(MemberBean member) {
        this.member = member;
    }

    public AuthorBean getAuthor() {
        return author;
    }

    public void setAuthor(AuthorBean author) {
        this.author = author;
    }

    public static class ImgMainFile1Bean {
        /**
         * url : https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/art/img_main_file1/16/959daf4e-d080-4aa9-97a7-3d415e24d954.png
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

    public static class MemberBean {
        /**
         * id : 17
         * uid : Bdq2Ne27QeuqR8SHBFBib2UK
         * sn : RGGLPB4ISJ
         * email :
         * display_name : 商夏周
         * token : c9281425d23b6fb08fc468090f512c0e618fff3a0d87ce8e437221dea0a26864
         * phone_number : 8618879026320
         * id_document_validated : false
         * app_validated : false
         * pay_password_validated : false
         * ref_code : 00946973
         * created_at : 1611024576
         * expire_at : 2021-04-07 14:36:56
         * is_read_agreement : null
         * is_binding_invitation : false
         * address : 5HdtZ1Qyhi6JYkGCvhC72y7D8fm4APD1fkavnE6rDPseKGSE
         * recommend_image : {"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/recommend_image/17/d50556f3-ead1-4cfa-9f65-4daab12207a4.jpg"}
         * sex : 2
         * desc : 个人创作者
         * avatar : {"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/avatar/17/f3dcd31f-6c5d-4f6a-aaff-550d96fd8d54.jpg"}
         * is_artist : true
         * artist_desc : 喜欢纯真自然的东西，所创作的画作都是来自世间万物的灵感
         * follow_user_size : 5
         * following_user_size : 3
         * follow_by_me : true
         * favorite_art_size : 11
         * art_size : 6
         * residential_address : 南京
         * college : 南艺
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

    public static class AuthorBean {
        /**
         * id : 17
         * uid : Bdq2Ne27QeuqR8SHBFBib2UK
         * sn : RGGLPB4ISJ
         * email :
         * display_name : 商夏周
         * token : c9281425d23b6fb08fc468090f512c0e618fff3a0d87ce8e437221dea0a26864
         * phone_number : 8618879026320
         * id_document_validated : false
         * app_validated : false
         * pay_password_validated : false
         * ref_code : 00946973
         * created_at : 1611024576
         * expire_at : 2021-04-07 14:36:56
         * is_read_agreement : null
         * is_binding_invitation : false
         * address : 5HdtZ1Qyhi6JYkGCvhC72y7D8fm4APD1fkavnE6rDPseKGSE
         * recommend_image : {"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/recommend_image/17/d50556f3-ead1-4cfa-9f65-4daab12207a4.jpg"}
         * sex : 2
         * desc : 个人创作者
         * avatar : {"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/avatar/17/f3dcd31f-6c5d-4f6a-aaff-550d96fd8d54.jpg"}
         * is_artist : true
         * artist_desc : 喜欢纯真自然的东西，所创作的画作都是来自世间万物的灵感
         * follow_user_size : 5
         * following_user_size : 3
         * follow_by_me : true
         * favorite_art_size : 11
         * art_size : 6
         * residential_address : 南京
         * college : 南艺
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
        private Object is_read_agreement;
        private boolean is_binding_invitation;
        private String address;
        private RecommendImageBeanX recommend_image;
        private int sex;
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

        public static class RecommendImageBeanX {
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

        public static class AvatarBeanX {
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
