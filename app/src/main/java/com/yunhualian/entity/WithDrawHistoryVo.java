package com.yunhualian.entity;

import java.io.Serializable;

/**
 * Synopsis     com.miner.client.entity.WithDrawHistoryVo
 * Author       mosr
 * Version      1.0.0
 * Create       2020/7/14 10:52
 * Email        intimatestranger@sina.cn
 */
public class WithDrawHistoryVo implements Serializable {
    /**
     * sn : 20071400070003
     * fee : 5.0
     * fund_uid : 互动8额家阿胶阿胶
     * chain_name : erc20
     * blockchain : https://etherscan.io/tx/
     * fund_extra : null
     * done_at : null
     * txid : null
     * sum : 1888.0
     * type : null
     * agent_fee_type : null
     * agent_fee : 0.0
     * memo : Internal Server Error
     * aasm_state : suspect
     * channel : {"id":2,"currency_id":2,"key":"tms","fixed":6,"min":"10.0","max":"1000000.0","fee":"5.0","fee_type":"","fee_max":null,"agent_fee":null,"agent_fee_type":"","inuse":true,"created_at":"1593685080","updated_at":"1593685080"}
     * created_at : 1594656431
     * updated_at : 1594656431
     */

    private String sn;
    private String fee;
    private String fund_uid;
    private String chain_name;
    private String blockchain;
    private Object fund_extra;
    private Object done_at;
    private Object txid;
    private String sum;
    private Object type;
    private Object agent_fee_type;
    private String agent_fee;
    private String memo;
    private String aasm_state;
    private ChannelBean channel;
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

    public Object getFund_extra() {
        return fund_extra;
    }

    public void setFund_extra(Object fund_extra) {
        this.fund_extra = fund_extra;
    }

    public Object getDone_at() {
        return done_at;
    }

    public void setDone_at(Object done_at) {
        this.done_at = done_at;
    }

    public Object getTxid() {
        return txid;
    }

    public void setTxid(Object txid) {
        this.txid = txid;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public Object getAgent_fee_type() {
        return agent_fee_type;
    }

    public void setAgent_fee_type(Object agent_fee_type) {
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

    public ChannelBean getChannel() {
        return channel;
    }

    public void setChannel(ChannelBean channel) {
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

    public static class ChannelBean implements Serializable {
        /**
         * id : 2
         * currency_id : 2
         * key : tms
         * fixed : 6
         * min : 10.0
         * max : 1000000.0
         * fee : 5.0
         * fee_type :
         * fee_max : null
         * agent_fee : null
         * agent_fee_type :
         * inuse : true
         * created_at : 1593685080
         * updated_at : 1593685080
         */

        private int id;
        private int currency_id;
        private String key;
        private int fixed;
        private String min;
        private String max;
        private String fee;
        private String fee_type;
        private Object fee_max;
        private Object agent_fee;
        private String agent_fee_type;
        private boolean inuse;
        private String created_at;
        private String updated_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCurrency_id() {
            return currency_id;
        }

        public void setCurrency_id(int currency_id) {
            this.currency_id = currency_id;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getFixed() {
            return fixed;
        }

        public void setFixed(int fixed) {
            this.fixed = fixed;
        }

        public String getMin() {
            return min;
        }

        public void setMin(String min) {
            this.min = min;
        }

        public String getMax() {
            return max;
        }

        public void setMax(String max) {
            this.max = max;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public String getFee_type() {
            return fee_type;
        }

        public void setFee_type(String fee_type) {
            this.fee_type = fee_type;
        }

        public Object getFee_max() {
            return fee_max;
        }

        public void setFee_max(Object fee_max) {
            this.fee_max = fee_max;
        }

        public Object getAgent_fee() {
            return agent_fee;
        }

        public void setAgent_fee(Object agent_fee) {
            this.agent_fee = agent_fee;
        }

        public String getAgent_fee_type() {
            return agent_fee_type;
        }

        public void setAgent_fee_type(String agent_fee_type) {
            this.agent_fee_type = agent_fee_type;
        }

        public boolean isInuse() {
            return inuse;
        }

        public void setInuse(boolean inuse) {
            this.inuse = inuse;
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
}
