package com.dm.pgmapp.utils.framework;
import oracle.jbo.server.SessionImpl;
import oracle.jbo.server.TransactionHandlerFactory;

public class CustomSessionImpl extends SessionImpl {
    public CustomSessionImpl() {
        super();
    }

    private TransactionHandlerFactory mTxnHandlerFactory = new CustomTxnHandlerFactoryImpl();

    @Override
    public TransactionHandlerFactory getTransactionHandlerFactory() {
        return mTxnHandlerFactory;
    }
}
