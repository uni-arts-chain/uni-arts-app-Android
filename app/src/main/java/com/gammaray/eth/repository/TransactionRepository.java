package com.gammaray.eth.repository;


import android.text.TextUtils;

import com.gammaray.eth.domain.ETHWallet;
import com.gammaray.eth.entity.Transaction;
import com.gammaray.eth.service.BlockExplorerClientType;

import io.reactivex.Maybe;
import io.reactivex.Observable;
public class TransactionRepository implements TransactionRepositoryType {

    private final EthereumNetworkRepository networkRepository;
    private final TransactionLocalSource transactionLocalSource;
    private final BlockExplorerClientType blockExplorerClient;

    public TransactionRepository(
            EthereumNetworkRepository networkRepository,
            TransactionLocalSource inMemoryCache,
            TransactionLocalSource inDiskCache,
            BlockExplorerClientType blockExplorerClient) {
        this.networkRepository = networkRepository;
        this.blockExplorerClient = blockExplorerClient;
        this.transactionLocalSource = inMemoryCache;

        this.networkRepository.addOnChangeDefaultNetwork(this::onNetworkChanged);
    }

    @Override
    public Observable<Transaction[]> fetchTransaction(ETHWallet walletAddr, String tokenAddr, String coin) {
        return Observable.create(e -> {
            Transaction[] transactions;
            if (TextUtils.isEmpty(tokenAddr)) {
                transactions = transactionLocalSource.fetchTransaction(walletAddr.address,coin).blockingGet();
            } else {
                transactions = transactionLocalSource.fetchTransaction(walletAddr.address, tokenAddr).blockingGet();
            }

            if (transactions != null && transactions.length > 0) {
                e.onNext(transactions);
            }
            transactions = blockExplorerClient.fetchTransactions(walletAddr, tokenAddr,coin).blockingFirst();
            transactionLocalSource.clear();
            if (TextUtils.isEmpty(tokenAddr)) {
                transactionLocalSource.putTransactions(walletAddr.address, transactions);
            } else {
                transactionLocalSource.putTransactions(walletAddr.address, tokenAddr, transactions);
            }
            e.onNext(transactions);
            e.onComplete();
        });
    }
    @Override
    public Maybe<Transaction> findTransaction(ETHWallet walletAddr, String transactionHash,String coin) {
        return fetchTransaction(walletAddr, null,coin)
                .firstElement()
                .flatMap(transactions -> {
                    for (Transaction transaction : transactions) {
                        if (transaction.hash.equals(transactionHash)) {
                            return Maybe.just(transaction);
                        }
                    }
                    return null;
                });
    }

    private void onNetworkChanged(String networkInfo) {
        transactionLocalSource.clear();
    }
}
