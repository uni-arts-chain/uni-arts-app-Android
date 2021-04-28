package com.yunhualian.entity;

import java.io.Serializable;

/**
 * Synopsis     com.miner.client.entity.LivenessVerifyVo
 * Author		Mosr
 * Version		1.0.0
 * Create 	    2020/7/11 23:00
 * Email  		intimatestranger@sina.cn
 */
public class LivenessVerifyVo implements Serializable {

    /**
     * verification_score : 0.73
     * approve_flag : false
     */

    private String verification_score;
    private boolean approve_flag;

    public String getVerification_score() {
        return verification_score;
    }

    public void setVerification_score(String verification_score) {
        this.verification_score = verification_score;
    }

    public boolean isApprove_flag() {
        return approve_flag;
    }

    public void setApprove_flag(boolean approve_flag) {
        this.approve_flag = approve_flag;
    }
}
