package com.gammaray.entity;

import java.io.Serializable;
import java.util.List;

public class NetworkInfos implements Serializable {

    private String title;

    private List<ChainNetWork> chain_networks;

    public static final class ChainNetWork implements Serializable{

        private String name;

        private int chain_id;

        private int network_id;

        private String rpc_url;

        private boolean isChoose;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getChain_id() {
            return chain_id;
        }

        public void setChain_id(int chain_id) {
            this.chain_id = chain_id;
        }

        public int getNetwork_id() {
            return network_id;
        }

        public void setNetwork_id(int network_id) {
            this.network_id = network_id;
        }

        public String getRpc_url() {
            return rpc_url;
        }

        public void setRpc_url(String rpc_url) {
            this.rpc_url = rpc_url;
        }

        public boolean isChoose() {
            return isChoose;
        }

        public void setChoose(boolean choose) {
            isChoose = choose;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ChainNetWork> getChain_networks() {
        return chain_networks;
    }

    public void setChain_networks(List<ChainNetWork> chain_networks) {
        this.chain_networks = chain_networks;
    }
}
