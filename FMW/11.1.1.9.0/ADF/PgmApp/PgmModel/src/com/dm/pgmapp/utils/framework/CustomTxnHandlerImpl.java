package com.dm.pgmapp.utils.framework;

import java.sql.Connection;

import oracle.jbo.server.DefaultTxnHandlerImpl;


public class CustomTxnHandlerImpl extends DefaultTxnHandlerImpl {
    @Override
    public void handleCommit(Connection conn, boolean autoStart) {
        System.out.println("###> ignoring handleCommit() <###");
    }

    @Override
    public void handleRollback(Connection conn, boolean autoStart) {
        System.out.println("###> ignoring handleRollback() <###");
    }
}
