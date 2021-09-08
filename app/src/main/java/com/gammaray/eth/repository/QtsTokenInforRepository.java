package com.gammaray.eth.repository;

import com.gammaray.R;
import com.gammaray.base.YunApplication;
import com.gammaray.eth.entity.TokenInfo;
import com.gammaray.eth.ui.AddTokenActivity;

import java.util.ArrayList;
import java.util.List;


public class QtsTokenInforRepository {
    public static QtsTokenInforRepository qtsToken;
    public static final int QTS_INDEX = 0;
    public static final int TMS_INDEX = 2;
    private static EthereumNetworkRepository ethereumNetworkRepository = YunApplication.repositoryFactory().ethereumNetworkRepository;
    private final AddTokenActivity.TokenItem[] tokenItems = new AddTokenActivity.TokenItem[]{
//            mItems.add(new AddTokenActivity.TokenItem(new TokenInfo("0xAE98b4B3Eaf3537374b0784294868ED21db6328F", "Future Farm Token", "FFT", 6), false, R.drawable.icon_qts));
//        mItems.add(new TokenItem(new TokenInfo("0xF015Ef3c817AA70DD55417A7C09E3d751e998aa4", "Hera", "Hera", 18), false, R.drawable.wallet_logo_demo));
//        mItems.add(new TokenItem(new TokenInfo("0xb204DfB4386e605eCe4E87Bd97Ab4b71D5AFF3c7", "Tatmas Coin", "TMS", 6), false, R.drawable.wallet_logo_demo));
//        mItems.add(new TokenItem(new TokenInfo("0x46c5567eB6D20bAf86dD05c4947188d13666B0D3", "Elf Pool Coin", "EPC", 6), false, R.drawable.wallet_logo_demo));
//        mItems.add(new TokenItem(new TokenInfo("0x57374C1D046B64Fa94c21a2Ab53F436B34fC8b77", "Thhc", "THHC", 6), false, R.drawable.wallet_logo_demo));
//        mItems.add(new TokenItem(new TokenInfo("0x57374C1D046B64Fa94c21a2Ab53F436B34fC8b77", "Future Farm Super Token", "FFST", 6), false, R.drawable.wallet_logo_demo));
//        mItems.add(new TokenItem(new TokenInfo("0xAF3161a001Cb771E38BF8cE100a6e7d949Eac1f0", "KUANGJIQUAN", "KJQ", 6), false, R.drawable.wallet_logo_demo));
//        mItems.add(new TokenItem(new TokenInfo("0x9597319E138215D5CA3328B17a9eF843412e93be", "Future Farm Super Token", "FFST", 6), false, R.drawable.wallet_logo_demo));
//        mItems.add(new TokenItem(new TokenInfo("0x57374C1D046B64Fa94c21a2Ab53F436B34fC8b77", "Future Farm Token2", "FFT2", 6), false, R.drawable.wallet_logo_demo));
            new AddTokenActivity.TokenItem(new TokenInfo("", "QTS", "QTS", 18, ethereumNetworkRepository.getDefaultNetwork().rpcServerUrl, ""), false, R.drawable.icon_audio),
            new AddTokenActivity.TokenItem(new TokenInfo("", "ETH", "ETH", 18, ethereumNetworkRepository.ethNetWork.rpcServerUrl, ""), false, R.drawable.icon_audio),
            new AddTokenActivity.TokenItem(new TokenInfo("0xb204DfB4386e605eCe4E87Bd97Ab4b71D5AFF3c7", "Tatmas Coin", "TMS", 6, ethereumNetworkRepository.getDefaultNetwork().rpcServerUrl, ""), false, R.drawable.icon_audio),
            new AddTokenActivity.TokenItem(new TokenInfo("0xF015Ef3c817AA70DD55417A7C09E3d751e998aa4", "Hera", "Hera", 18, ethereumNetworkRepository.getDefaultNetwork().rpcServerUrl, ""), false, R.drawable.icon_audio),
            new AddTokenActivity.TokenItem(new TokenInfo("0x46c5567eB6D20bAf86dD05c4947188d13666B0D3", "Elf Pool Coin", "EPC", 6, ethereumNetworkRepository.getDefaultNetwork().rpcServerUrl, ""), false, R.drawable.icon_audio),
            new AddTokenActivity.TokenItem(new TokenInfo("0x57374C1D046B64Fa94c21a2Ab53F436B34fC8b78", "Thhc", "THHC", 6, ethereumNetworkRepository.getDefaultNetwork().rpcServerUrl, ""), false, R.drawable.icon_audio),
            new AddTokenActivity.TokenItem(new TokenInfo("0x57374C1D046B64Fa94c21a2Ab53F436B34fC8b77", "Future Farm Super Token", "FFST", 6, ethereumNetworkRepository.getDefaultNetwork().rpcServerUrl, ""), false, R.drawable.icon_audio),
            new AddTokenActivity.TokenItem(new TokenInfo("0xAF3161a001Cb771E38BF8cE100a6e7d949Eac1f0", "KUANGJIQUAN", "KJQ", 6, ethereumNetworkRepository.getDefaultNetwork().rpcServerUrl, ""), false, R.drawable.icon_audio),
            new AddTokenActivity.TokenItem(new TokenInfo("0x9597319E138215D5CA3328B17a9eF843412e93be", "Future Farm Super Token", "FFST", 6, ethereumNetworkRepository.getDefaultNetwork().rpcServerUrl, ""), false, R.drawable.icon_audio),
            new AddTokenActivity.TokenItem(new TokenInfo("0x57374C1D046B64Fa94c21a2Ab53F436B34fC8b67", "Future Farm Token2", "FFT2", 6, ethereumNetworkRepository.getDefaultNetwork().rpcServerUrl, ""), false, R.drawable.icon_audio),
    };

    public QtsTokenInforRepository() {
    }

    public static QtsTokenInforRepository init() {
        if (qtsToken == null) {
            qtsToken = new QtsTokenInforRepository();
        }
        return qtsToken;
    }

    public AddTokenActivity.TokenItem[] getTokenItems() {
        return tokenItems;
    }

    public List<AddTokenActivity.TokenItem> getQtsTokenItems() {
        List<AddTokenActivity.TokenItem> list = new ArrayList<>();

        for (int i = 0; i < tokenItems.length; i++) {
            if (!tokenItems[i].tokenInfo.symbol.equalsIgnoreCase("ETH")) {
                list.add(tokenItems[i]);
            }
        }


        return list;
    }

    public List<AddTokenActivity.TokenItem> getEthTokenItems() {
        List<AddTokenActivity.TokenItem> list = new ArrayList<>();

        for (int i = 0; i < tokenItems.length; i++) {
            if (tokenItems[i].tokenInfo.symbol.equalsIgnoreCase("ETH")) {
                list.add(tokenItems[i]);
            }
        }


        return list;
    }

    public AddTokenActivity.TokenItem getDefaultToken() {
        return tokenItems[0];
    }

    public AddTokenActivity.TokenItem getPerTokens(int index) {
        if (index > tokenItems.length)
            return null;
        else
            return tokenItems[index];
    }
}
