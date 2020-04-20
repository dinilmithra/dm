package com.dm.pgmapp.utils.framework;

import oracle.jbo.server.DefaultTxnHandlerFactoryImpl;
import oracle.jbo.server.TransactionHandler;

public class CustomTxnHandlerFactoryImpl extends DefaultTxnHandlerFactoryImpl {
    public CustomTxnHandlerFactoryImpl() {
        super();
    }

    @Override
    public TransactionHandler createTransactionHandler() {
        return new CustomTxnHandlerImpl();
    }
}
