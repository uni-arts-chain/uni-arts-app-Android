package com.gammaray.eth.repository;


import com.gammaray.eth.entity.Transaction;

import io.reactivex.Single;

public interface TransactionLocalSource {
    Single<Transaction[]> fetchTransaction(String walletAddr, String coin);

    void putTransactions(String walletAddr, Transaction[] transactions);

    Single<Transaction[]> fetchTransaction(String walletAddr, String tokenAddr, String coin);

    void putTransactions(String walletAddr, String symbol, Transaction[] transactions);

    void clear();
}
