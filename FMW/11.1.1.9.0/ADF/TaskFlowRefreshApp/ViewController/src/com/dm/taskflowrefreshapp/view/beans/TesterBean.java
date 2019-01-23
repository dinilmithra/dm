package com.dm.taskflowrefreshapp.view.beans;

import oracle.adf.share.logging.ADFLogger;

public class TesterBean {

    private static ADFLogger _logger =
        ADFLogger.createADFLogger(TesterBean.class);

    public TesterBean() {
        super();
        _logger.info("Inside TesterBean Constructor" + this);
    }

    private String username;

    @Override
    protected void finalize() throws Throwable {
        _logger.info("Inside TesterBean finalize: " + this);
        super.finalize();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
