package com.gammaray.eth.entity;

import java.util.List;

public class LoginBean {
    /**
     * head : {"code":"1000","msg":"成功。"}
     * body : {"token":"15a38311e285e85e9803962ccd6c426bcf174167d5dbb607c6a35daa488fab27","expired_at":1616745698,"accounts":[{"created_at":1615360649,"updated_at":1615360649,"chain_protocols":["erc20","trc20"],"address_with_chains":[{"address":"0xce7fe2e405980cf81172c41f15bc601652fe8ff0","chain_protocol":"erc20"},{"address":"tuawgzbgzgbmwpw2vj1tpjl5vkqb4bhl3m","chain_protocol":"trc20"}],"address":"0xce7fe2e405980cf81172c41f15bc601652fe8ff0","logo":"https://tatmas.vip/icons/usdt.png","currency":"USDT","currency_key":"Tether","coin_visible":true,"withdraw":true,"tradable":false,"depositable":true,"like_eos":false,"deposit_wallet":"","balance":"100000100.0","locked":"0.0","erc20":false}],"member":{"register_by":"phone","avatar":"https://assets.tatmas.vip/upload/images/1b5357f748253b26efeb99761fa589a5-s8BS2ekLQWEzY1WB.jpg","email":"","phone_number":"8618651090153","display_name":"","trades_count":0,"online":true,"is_merchant":false,"id_verified":true,"otp":true,"fund_password":false},"pay_methods":[]}
     */

    private HeadBean head;
    private BodyBean body;

    public HeadBean getHead() {
        return head;
    }

    public void setHead(HeadBean head) {
        this.head = head;
    }

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class HeadBean {
        /**
         * code : 1000
         * msg : 成功。
         */

        private String code;
        private String msg;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public static class BodyBean {
        /**
         * token : 15a38311e285e85e9803962ccd6c426bcf174167d5dbb607c6a35daa488fab27
         * expired_at : 1616745698
         * accounts : [{"created_at":1615360649,"updated_at":1615360649,"chain_protocols":["erc20","trc20"],"address_with_chains":[{"address":"0xce7fe2e405980cf81172c41f15bc601652fe8ff0","chain_protocol":"erc20"},{"address":"tuawgzbgzgbmwpw2vj1tpjl5vkqb4bhl3m","chain_protocol":"trc20"}],"address":"0xce7fe2e405980cf81172c41f15bc601652fe8ff0","logo":"https://tatmas.vip/icons/usdt.png","currency":"USDT","currency_key":"Tether","coin_visible":true,"withdraw":true,"tradable":false,"depositable":true,"like_eos":false,"deposit_wallet":"","balance":"100000100.0","locked":"0.0","erc20":false}]
         * member : {"register_by":"phone","avatar":"https://assets.tatmas.vip/upload/images/1b5357f748253b26efeb99761fa589a5-s8BS2ekLQWEzY1WB.jpg","email":"","phone_number":"8618651090153","display_name":"","trades_count":0,"online":true,"is_merchant":false,"id_verified":true,"otp":true,"fund_password":false}
         * pay_methods : []
         */

        private String token;
        private int expired_at;
        private MemberBean member;
        private List<AccountsBean> accounts;
        private List<?> pay_methods;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getExpired_at() {
            return expired_at;
        }

        public void setExpired_at(int expired_at) {
            this.expired_at = expired_at;
        }

        public MemberBean getMember() {
            return member;
        }

        public void setMember(MemberBean member) {
            this.member = member;
        }

        public List<AccountsBean> getAccounts() {
            return accounts;
        }

        public void setAccounts(List<AccountsBean> accounts) {
            this.accounts = accounts;
        }

        public List<?> getPay_methods() {
            return pay_methods;
        }

        public void setPay_methods(List<?> pay_methods) {
            this.pay_methods = pay_methods;
        }

        public static class MemberBean {
            /**
             * register_by : phone
             * avatar : https://assets.tatmas.vip/upload/images/1b5357f748253b26efeb99761fa589a5-s8BS2ekLQWEzY1WB.jpg
             * email :
             * phone_number : 8618651090153
             * display_name :
             * trades_count : 0
             * online : true
             * is_merchant : false
             * id_verified : true
             * otp : true
             * fund_password : false
             */

            private String register_by;
            private String avatar;
            private String email;
            private String phone_number;
            private String display_name;
            private int trades_count;
            private boolean online;
            private boolean is_merchant;
            private boolean id_verified;
            private boolean otp;
            private boolean fund_password;

            public String getRegister_by() {
                return register_by;
            }

            public void setRegister_by(String register_by) {
                this.register_by = register_by;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getPhone_number() {
                return phone_number;
            }

            public void setPhone_number(String phone_number) {
                this.phone_number = phone_number;
            }

            public String getDisplay_name() {
                return display_name;
            }

            public void setDisplay_name(String display_name) {
                this.display_name = display_name;
            }

            public int getTrades_count() {
                return trades_count;
            }

            public void setTrades_count(int trades_count) {
                this.trades_count = trades_count;
            }

            public boolean isOnline() {
                return online;
            }

            public void setOnline(boolean online) {
                this.online = online;
            }

            public boolean isIs_merchant() {
                return is_merchant;
            }

            public void setIs_merchant(boolean is_merchant) {
                this.is_merchant = is_merchant;
            }

            public boolean isId_verified() {
                return id_verified;
            }

            public void setId_verified(boolean id_verified) {
                this.id_verified = id_verified;
            }

            public boolean isOtp() {
                return otp;
            }

            public void setOtp(boolean otp) {
                this.otp = otp;
            }

            public boolean isFund_password() {
                return fund_password;
            }

            public void setFund_password(boolean fund_password) {
                this.fund_password = fund_password;
            }
        }

        public static class AccountsBean {
            /**
             * created_at : 1615360649
             * updated_at : 1615360649
             * chain_protocols : ["erc20","trc20"]
             * address_with_chains : [{"address":"0xce7fe2e405980cf81172c41f15bc601652fe8ff0","chain_protocol":"erc20"},{"address":"tuawgzbgzgbmwpw2vj1tpjl5vkqb4bhl3m","chain_protocol":"trc20"}]
             * address : 0xce7fe2e405980cf81172c41f15bc601652fe8ff0
             * logo : https://tatmas.vip/icons/usdt.png
             * currency : USDT
             * currency_key : Tether
             * coin_visible : true
             * withdraw : true
             * tradable : false
             * depositable : true
             * like_eos : false
             * deposit_wallet :
             * balance : 100000100.0
             * locked : 0.0
             * erc20 : false
             */

            private int created_at;
            private int updated_at;
            private String address;
            private String logo;
            private String currency;
            private String currency_key;
            private boolean coin_visible;
            private boolean withdraw;
            private boolean tradable;
            private boolean depositable;
            private boolean like_eos;
            private String deposit_wallet;
            private String balance;
            private String locked;
            private boolean erc20;
            private List<String> chain_protocols;
            private List<AddressWithChainsBean> address_with_chains;

            public int getCreated_at() {
                return created_at;
            }

            public void setCreated_at(int created_at) {
                this.created_at = created_at;
            }

            public int getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(int updated_at) {
                this.updated_at = updated_at;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public String getCurrency_key() {
                return currency_key;
            }

            public void setCurrency_key(String currency_key) {
                this.currency_key = currency_key;
            }

            public boolean isCoin_visible() {
                return coin_visible;
            }

            public void setCoin_visible(boolean coin_visible) {
                this.coin_visible = coin_visible;
            }

            public boolean isWithdraw() {
                return withdraw;
            }

            public void setWithdraw(boolean withdraw) {
                this.withdraw = withdraw;
            }

            public boolean isTradable() {
                return tradable;
            }

            public void setTradable(boolean tradable) {
                this.tradable = tradable;
            }

            public boolean isDepositable() {
                return depositable;
            }

            public void setDepositable(boolean depositable) {
                this.depositable = depositable;
            }

            public boolean isLike_eos() {
                return like_eos;
            }

            public void setLike_eos(boolean like_eos) {
                this.like_eos = like_eos;
            }

            public String getDeposit_wallet() {
                return deposit_wallet;
            }

            public void setDeposit_wallet(String deposit_wallet) {
                this.deposit_wallet = deposit_wallet;
            }

            public String getBalance() {
                return balance;
            }

            public void setBalance(String balance) {
                this.balance = balance;
            }

            public String getLocked() {
                return locked;
            }

            public void setLocked(String locked) {
                this.locked = locked;
            }

            public boolean isErc20() {
                return erc20;
            }

            public void setErc20(boolean erc20) {
                this.erc20 = erc20;
            }

            public List<String> getChain_protocols() {
                return chain_protocols;
            }

            public void setChain_protocols(List<String> chain_protocols) {
                this.chain_protocols = chain_protocols;
            }

            public List<AddressWithChainsBean> getAddress_with_chains() {
                return address_with_chains;
            }

            public void setAddress_with_chains(List<AddressWithChainsBean> address_with_chains) {
                this.address_with_chains = address_with_chains;
            }

            public static class AddressWithChainsBean {
                /**
                 * address : 0xce7fe2e405980cf81172c41f15bc601652fe8ff0
                 * chain_protocol : erc20
                 */

                private String address;
                private String chain_protocol;

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public String getChain_protocol() {
                    return chain_protocol;
                }

                public void setChain_protocol(String chain_protocol) {
                    this.chain_protocol = chain_protocol;
                }
            }
        }
    }

    /**
     * head : {"code":"1000","msg":"成功。"}
     * body : {"token":"5c13e554949f354a31061a4c3aa2b5845cdf5a7855f0c1caf3461f0317087204","accounts":[{"currency":"USDT","balance":"0.0","locked":"0.0"}],"member":{"avatar":"","email":"","phone_number":"18651090153","display_name":"","trades_count":0,"online":false,"is_merchant":false,"id_verified":false},"pay_methods":[]}
     */


}
