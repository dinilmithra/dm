package com.dm.taskflowrefreshapp.view.beans;

import oracle.adf.share.logging.ADFLogger;

public class HomeBean {

    private static ADFLogger _logger =
        ADFLogger.createADFLogger(HomeBean.class);

    public HomeBean() {
        super();
        _logger.info("Initializing " + this);
    }

    String firstName;

    public void setFirstName(String firstName) {
        _logger.info(firstName);
        this.firstName = firstName;
    }

    public String getFirstName() {

        _logger.info(firstName);
        return firstName;
    }

    @Override
    protected void finalize() throws Throwable {
        _logger.info("Destroying " + this);
        super.finalize();
    }
}
