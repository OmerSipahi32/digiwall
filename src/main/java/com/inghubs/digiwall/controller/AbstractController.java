package com.inghubs.digiwall.controller;

public abstract class AbstractController {

    // --------------- WalletController ---------------//
    /* default */ static final String WALLET_BASE = "/wallet";
    /* default */ static final String CREATE_WALLET = WALLET_BASE + "/create";
    /* default */ static final String LIST_WALLETS = WALLET_BASE + "/list";
    /* default */ static final String DEPOSIT = WALLET_BASE + "/deposit";
    /* default */ static final String WITHDRAW = WALLET_BASE + "/withdraw";

    // --------------- TransactionController ---------------//
    /* default */ static final String TRANSACTION_BASE = "/transaction";
    /* default */ static final String LIST_TRANSACTIONS = TRANSACTION_BASE + "/list/{walletId}";
    /* default */ static final String APPROVE_TRANSACTION = TRANSACTION_BASE + "/approve";

    // --------------- AuthController ---------------//
    /* default */ static final String AUTH_BASE = "/auth";
    /* default */ static final String LOGIN = AUTH_BASE + "/login";
}