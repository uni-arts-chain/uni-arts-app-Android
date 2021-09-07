package com.gammaray.entity;

import java.util.List;

public class ArtTopicVo {

    /**
     * title : 测试2
     * img_file : {"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/topic/img_file/2/92aaf565-9e87-4a06-966b-643e4460d436.png"}
     * app_img_file : {"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/topic/app_img_file/2/1b0ddf9e-86e2-4d36-bd1d-bf42c5a08306.jpg"}
     * arts : [{"id":1,"name":"rabbit","category_id":2,"theme_id":4,"material_id":1,"produce_at":"1517673600","size_length":45,"size_width":45,"details":"The white rabbit lay quietly on the woman's body and was gently stroked by the woman. The artist Zhu Xingguo's paintings are not smooth and clumsy because they are not known to be due to limitations of external techniques and technical inability, or some hidden conflicts deep in their hearts. This kind of unsmoothness and clumsiness combined with the previous simple and general form should have degraded the expressiveness of the picture, but it accurately conveyed a state of existence of \"although you are in this world, but you don't belong to this world\".","img_main_file1":{"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/art/img_main_file1/1/c0b7fcc0-badc-497f-8a5f-e64837283fc9.png"},"img_main_file2":{"url":null},"img_main_file3":{"url":null},"img_detail_file1":{"url":null},"img_detail_file1_desc":"","img_detail_file2":{"url":null},"img_detail_file2_desc":"","img_detail_file3":{"url":null},"img_detail_file3_desc":"","img_detail_file4":{"url":null},"img_detail_file4_desc":"","img_detail_file5":{"url":null},"img_detail_file5_desc":"","price":"10.0","fee":null,"position":null,"aasm_state":"online","created_at":"1613985462","collection_id":1,"item_id":59,"member_id":10,"item_hash":"0x24d58bf734de10f8f404bd04f1c38bf25e5b2cec","auction_start_time":null,"auction_end_time":null,"liked_by_me":false,"disliked_by_me":false,"favorite_by_me":false,"liked_count":1,"dislike_count":0,"favorite_count":2,"signature_count":1,"royalty":"0.0","royalty_expired_at":null,"has_royalty":false,"live2d_file":null,"live2d_ipfs_url":"","member":{"id":10,"uid":"83p1vVWGpMDWFWxZ4Vrc7uRh","sn":"RZM5665WY6","email":null,"display_name":"Scorpio Cat","token":null,"phone_number":"15511111111","id_document_validated":false,"app_validated":false,"pay_password_validated":false,"ref_code":"16816028","created_at":"1610417730","expire_at":"","is_read_agreement":null,"is_binding_invitation":false,"address":"5ERmAEibmWT9DLnLqZyfrcTaneHMVikW7mq9QmeV2ovPcM6s","recommend_image":{"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/recommend_image/10/d3e8ef75-ce5c-4d6e-83ed-f6c22b211331.png"},"sex":2,"desc":"I'm a cat","avatar":{"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/avatar/10/8cddc856-3aee-45f4-ba47-f6132146bf51.png"},"is_artist":true,"artist_desc":"I'm a scorpiocat","follow_user_size":2,"following_user_size":2,"follow_by_me":false,"favorite_art_size":0,"art_size":2,"residential_address":null,"college":"Nanjing"},"author":{"id":17,"uid":"Bdq2Ne27QeuqR8SHBFBib2UK","sn":"RGGLPB4ISJ","email":"","display_name":"商夏周","token":null,"phone_number":"8618879026320","id_document_validated":false,"app_validated":false,"pay_password_validated":false,"ref_code":"00946973","created_at":"1611024576","expire_at":"","is_read_agreement":null,"is_binding_invitation":false,"address":"5HdtZ1Qyhi6JYkGCvhC72y7D8fm4APD1fkavnE6rDPseKGSE","recommend_image":{"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/recommend_image/17/d50556f3-ead1-4cfa-9f65-4daab12207a4.jpg"},"sex":2,"desc":"个人创作者","avatar":{"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/member/avatar/17/f3dcd31f-6c5d-4f6a-aaff-550d96fd8d54.jpg"},"is_artist":true,"artist_desc":"喜欢纯真自然的东西，所创作的画作都是来自世间万物的灵感","follow_user_size":5,"following_user_size":3,"follow_by_me":false,"favorite_art_size":11,"art_size":6,"residential_address":"南京","college":"南艺"}}]
     */

    private String title;
    private ImgFileBean img_file;
    private AppImgFileBean app_img_file;
    private List<SellingArtVo> arts;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ImgFileBean getImg_file() {
        return img_file;
    }

    public void setImg_file(ImgFileBean img_file) {
        this.img_file = img_file;
    }

    public AppImgFileBean getApp_img_file() {
        return app_img_file;
    }

    public void setApp_img_file(AppImgFileBean app_img_file) {
        this.app_img_file = app_img_file;
    }

    public List<SellingArtVo> getArts() {
        return arts;
    }

    public void setArts(List<SellingArtVo> arts) {
        this.arts = arts;
    }

    public static class ImgFileBean {
        /**
         * url : https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/topic/img_file/2/92aaf565-9e87-4a06-966b-643e4460d436.png
         */

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class AppImgFileBean {
        /**
         * url : https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/topic/app_img_file/2/1b0ddf9e-86e2-4d36-bd1d-bf42c5a08306.jpg
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
