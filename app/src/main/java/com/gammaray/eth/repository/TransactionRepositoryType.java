package com.gammaray.eth.repository;


import com.gammaray.eth.domain.ETHWallet;
import com.gammaray.eth.entity.Transaction;


import io.reactivex.Maybe;
import io.reactivex.Observable;

public interface TransactionRepositoryType {
    Observable<Transaction[]> fetchTransaction(ETHWallet walletAddr, String token, String coin);

    Maybe<Transaction> findTransaction(ETHWallet walletAddr, String transactionHash, String coin);

}
