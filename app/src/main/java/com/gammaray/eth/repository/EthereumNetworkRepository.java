package com.gammaray.eth.repository;

import android.text.TextUtils;

import com.gammaray.BuildConfig;
import com.gammaray.entity.ReceiverPushBean;
import com.gammaray.eth.entity.NetworkInfo;
import com.gammaray.eth.ui.AddTokenActivity;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.gammaray.eth.base.C.ETC_SYMBOL;
import static com.gammaray.eth.base.C.ETHEREUM_MAIN_NETWORK_NAME;
import static com.gammaray.eth.base.C.ETH_SYMBOL;
import static com.gammaray.eth.base.C.KOVAN_NETWORK_NAME;
import static com.gammaray.eth.base.C.LOCAL_DEV_NETWORK_NAME;
import static com.gammaray.eth.base.C.POA_NETWORK_NAME;
import static com.gammaray.eth.base.C.POA_SYMBOL;
import static com.gammaray.eth.base.C.QTS_MAIN_NETWORK_NAME;
import static com.gammaray.eth.base.C.QTS_SYMBOL;
import static com.gammaray.eth.base.C.ROPSTEN_NETWORK_NAME;


/**
 * Created by Tiny 熊 @ Upchain.pro
 * WeiXin: xlbxiong
 */

public class EthereumNetworkRepository {
    // release https://mainnet.infura.io/v3/7e2855d5896946cb985af8944713a371 ，https://rpc.tatmasglobal.com
    //debug https://ropsten.infura.io/v3/8ddd215139c849559864f7aaf7097307 http://rpc.tatmasglobal.com
//    public static final String qtsRpc = BuildConfig.DEBUG ? "http://rpc.tatmasglobal.com" : "https://rpc.tatmasglobal.com";
    //    public static final String ethRpc = BuildConfig.DEBUG ? "https://ropsten.infura.io/v3/8ddd215139c849559864f7aaf7097307" : "https://mainnet.infura.io/v3/7e2855d5896946cb985af8944713a371";
    public static EthereumNetworkRepository sSelf;
    public static List<AddTokenActivity.TokenItem> tokenItem = new ArrayList<>();
    public static ReceiverPushBean receiverPushBean = new ReceiverPushBean();
    public static final String ETH_PRC_URL = BuildConfig.DEBUG ? "https://rinkeby.infura.io/v3/7e2855d5896946cb985af8944713a371" : "https://mainnet.infura.io/v3/7e2855d5896946cb985af8944713a371";
    public static final int ETH_CHAIN_ID = BuildConfig.DEBUG ? 4 : 61;

    private final NetworkInfo[] NETWORKS = new NetworkInfo[]{
            new NetworkInfo(QTS_MAIN_NETWORK_NAME, QTS_SYMBOL,
                    "https://rpc.tatmasglobal.com",
                    "http://192.168.0.113:8911",
                    "http://192.168.0.113:51067/docs", 1, true),

            new NetworkInfo(ETHEREUM_MAIN_NETWORK_NAME, ETC_SYMBOL,
                    ETH_PRC_URL,
                    "https://classic.trustwalletapp.com",
                    "https://gastracker.io", ETH_CHAIN_ID, true),

            new NetworkInfo(POA_NETWORK_NAME, POA_SYMBOL,
                    "https://core.poa.network",
                    "https://poa.trustwalletapp.com", "poa", 99, false),

            new NetworkInfo(KOVAN_NETWORK_NAME, ETH_SYMBOL,
                    "https://kovan.infura.io/llyrtzQ3YhkdESt2Fzrk",
                    "http://192.168.8.103:8001/",
                    "https://kovan.etherscan.io", 42, false),

            new NetworkInfo(ROPSTEN_NETWORK_NAME, ETH_SYMBOL,
                    "https://ropsten.infura.io/llyrtzQ3YhkdESt2Fzrk",
                    "http://192.168.8.103:8000/",
                    "https://ropsten.etherscan.io", 3, false),

            new NetworkInfo(LOCAL_DEV_NETWORK_NAME, ETH_SYMBOL,
                    "https://ropsten.infura.io/v3/e97146cee9974bbea8b7c459efaff91b",
                    "http://192.168.8.100:8000/",
                    "", 1337, false),

            // fancy muffin song void glide tell abuse census measure sunny together before
//            0x7FAE2cDB24Cbe7e91383d53380c03f3f6c68aF30
    };

    private final SharedPreferenceRepository preferences;
    private NetworkInfo defaultNetwork;
    private final Set<OnNetworkChangeListener> onNetworkChangedListeners = new HashSet<>();
    public NetworkInfo ethNetWork = NETWORKS[1];

    public static EthereumNetworkRepository init(SharedPreferenceRepository sp) {
        if (sSelf == null) {
            sSelf = new EthereumNetworkRepository(sp);
        }
        return sSelf;
    }

    private EthereumNetworkRepository(SharedPreferenceRepository preferenceRepository) {
        this.preferences = preferenceRepository;
        defaultNetwork = getByName(preferences.getDefaultNetwork());
        if (defaultNetwork == null) {
            defaultNetwork = NETWORKS[0];
        }
    }

    public NetworkInfo getByName(String name) {
        if (!TextUtils.isEmpty(name)) {
            for (NetworkInfo NETWORK : NETWORKS) {
                if (name.equals(NETWORK.name)) {
                    return NETWORK;
                }
            }
        }
        return null;
    }

    public String getCurrency() {
        int currencyUnit = preferences.getCurrencyUnit();
        if (currencyUnit == 0) {
            return "CNY";
        } else {
            return "USD";
        }
    }

    public NetworkInfo getDefaultNetwork() {
        return defaultNetwork;
    }

    public NetworkInfo getEthNetWork() {
        return NETWORKS[1];
    }

    public NetworkInfo getQtsNetWork() {
        return NETWORKS[0];
    }

    public void setDefaultNetworkInfo(NetworkInfo networkInfo) {
        defaultNetwork = networkInfo;
        preferences.setDefaultNetwork(defaultNetwork.name);

        for (OnNetworkChangeListener listener : onNetworkChangedListeners) {
            listener.onNetworkChanged(networkInfo.rpcServerUrl);
        }
    }

    public NetworkInfo[] getAvailableNetworkList() {
        return NETWORKS;
    }

    public void addOnChangeDefaultNetwork(OnNetworkChangeListener onNetworkChanged) {
        onNetworkChangedListeners.add(onNetworkChanged);
    }

    public Single<NetworkInfo> find() {
        return Single.just(getEthNetWork())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<BigInteger> getLastTransactionNonce(Web3j web3j, String walletAddress) {
        return Single.fromCallable(() -> {
            EthGetTransactionCount ethGetTransactionCount = web3j
                    .ethGetTransactionCount(walletAddress, DefaultBlockParameterName.PENDING)   // or DefaultBlockParameterName.LATEST
                    .send();
            return ethGetTransactionCount.getTransactionCount();
        });
    }

}
