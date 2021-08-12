package com.yunhualian.entity;

import java.io.Serializable;

public class WithDrawsBean implements Serializable {

    private String sn;

    private String fee;

    private String fund_uid;

    private String chain_name;

    private String blockchain;

    private String fund_extra;

    private String done_at;

    private String txid;

    private String sum;

    private boolean type;

    private String agent_fee_type;

    private String agent_fee;

    private String memo;

    private String aasm_state;

    private String channel;

    private String created_at;

    private String updated_at;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getFund_uid() {
        return fund_uid;
    }

    public void setFund_uid(String fund_uid) {
        this.fund_uid = fund_uid;
    }

    public String getChain_name() {
        return chain_name;
    }

    public void setChain_name(String chain_name) {
        this.chain_name = chain_name;
    }

    public String getBlockchain() {
        return blockchain;
    }

    public void setBlockchain(String blockchain) {
        this.blockchain = blockchain;
    }

    public String getFund_extra() {
        return fund_extra;
    }

    public void setFund_extra(String fund_extra) {
        this.fund_extra = fund_extra;
    }

    public String getDone_at() {
        return done_at;
    }

    public void setDone_at(String done_at) {
        this.done_at = done_at;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getAgent_fee_type() {
        return agent_fee_type;
    }

    public void setAgent_fee_type(String agent_fee_type) {
        this.agent_fee_type = agent_fee_type;
    }

    public String getAgent_fee() {
        return agent_fee;
    }

    public void setAgent_fee(String agent_fee) {
        this.agent_fee = agent_fee;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getAasm_state() {
        return aasm_state;
    }

    public void setAasm_state(String aasm_state) {
        this.aasm_state = aasm_state;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
