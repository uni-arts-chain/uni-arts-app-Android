package com.yunhualian.entity;

import java.io.Serializable;

/**
 * Synopsis     com.miner.client.entity.RegisterVo
 * Author       mosr
 * Version      1.0.0
 * Create       2020.7.2 10:45
 * Email        intimatestranger@sina.cn
 */
public class UserVo implements Serializable {
    /**
     * id : 2
     * uid : duPP1E8jZBEfhwRwXbqSfEKD
     * sn : SNAKEEI2NQ
     * email : null
     * display_name : null
     * jwt_token : null
     * token : null
     * session_expiry : 1593658546
     * phone_number : 8613002502070
     * register_type : 1
     * id_document_validated : false
     * app_validated : false
     * pay_password_validated : false
     * is_large_customer : false
     * off : 1.0
     * electricity_off : 1.0
     * ref_code : 94634748
     * created_at : 1593656746
     * paid : false
     * expire_at :
     * is_read_agreement : null
     */

    private int id;
    private String uid;
    private String sn;
    private String email;
    private String display_name;
    private String jwt_token;
    private String token;
    private long session_expiry;
    private String phone_number;
    private int register_type;
    private boolean id_document_validated;
    private boolean app_validated;
    private boolean pay_password_validated;
    private boolean is_large_customer;
    private String off;
    private String electricity_off;
    private String ref_code;
    private String created_at;
    private boolean paid;
    private String expire_at;
    private String is_read_agreement;
    private String total_power_count;
    /**
     * email : null
     * display_name : null
     * session_expiry : 1597134447
     * total_power_count : 78.0
     * is_read_agreement : null
     * mining_level : 4
     * invitation_level : null
     * mining_level_desc : 钻石矿工
     * mining_level_limit : 50
     * is_binding_invitation : true
     */

    private int mining_level;
    private Object invitation_level;
    private String mining_level_desc;
    private String mining_level_limit;
    private boolean is_binding_invitation;

    public String getTotal_power_count() {
        return total_power_count;
    }

    public void setTotal_power_count(String total_power_count) {
        this.total_power_count = total_power_count;
    }

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

    public UserVo setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getJwt_token() {
        return jwt_token;
    }

    public void setJwt_token(String jwt_token) {
        this.jwt_token = jwt_token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getSession_expiry() {
        return session_expiry;
    }

    public void setSession_expiry(long session_expiry) {
        this.session_expiry = session_expiry;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public UserVo setPhone_number(String phone_number) {
        this.phone_number = phone_number;
        return this;
    }

    public int getRegister_type() {
        return register_type;
    }

    public void setRegister_type(int register_type) {
        this.register_type = register_type;
    }

    public boolean isId_document_validated() {
        return id_document_validated;
    }

    public UserVo setId_document_validated(boolean id_document_validated) {
        this.id_document_validated = id_document_validated;
        return this;
    }

    public boolean isApp_validated() {
        return app_validated;
    }

    public UserVo setApp_validated(boolean app_validated) {
        this.app_validated = app_validated;
        return this;
    }

    public boolean isPay_password_validated() {
        return pay_password_validated;
    }

    public void setPay_password_validated(boolean pay_password_validated) {
        this.pay_password_validated = pay_password_validated;
    }

    public boolean isIs_large_customer() {
        return is_large_customer;
    }

    public void setIs_large_customer(boolean is_large_customer) {
        this.is_large_customer = is_large_customer;
    }

    public String getOff() {
        return off;
    }

    public void setOff(String off) {
        this.off = off;
    }

    public String getElectricity_off() {
        return electricity_off;
    }

    public void setElectricity_off(String electricity_off) {
        this.electricity_off = electricity_off;
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

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
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

    public int getMining_level() {
        return mining_level;
    }

    public void setMining_level(int mining_level) {
        this.mining_level = mining_level;
    }

    public Object getInvitation_level() {
        return invitation_level;
    }

    public void setInvitation_level(Object invitation_level) {
        this.invitation_level = invitation_level;
    }

    public String getMining_level_desc() {
        return mining_level_desc;
    }

    public void setMining_level_desc(String mining_level_desc) {
        this.mining_level_desc = mining_level_desc;
    }

    public String getMining_level_limit() {
        return mining_level_limit;
    }

    public void setMining_level_limit(String mining_level_limit) {
        this.mining_level_limit = mining_level_limit;
    }

    public boolean isIs_binding_invitation() {
        return is_binding_invitation;
    }

    public UserVo setIs_binding_invitation(boolean is_binding_invitation) {
        this.is_binding_invitation = is_binding_invitation;
        return this;
    }
}
