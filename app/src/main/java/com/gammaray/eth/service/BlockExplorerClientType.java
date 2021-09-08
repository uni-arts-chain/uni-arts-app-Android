package com.gammaray.eth.service;


import com.gammaray.eth.domain.ETHWallet;
import com.gammaray.eth.entity.Transaction;

import io.reactivex.Observable;

public interface BlockExplorerClientType {
    Observable<Transaction[]> fetchTransactions(ETHWallet forAddress, String forToken, String coin);
}
