package com.dm.taskflowrefreshapp.view.beans;

import oracle.adf.share.logging.ADFLogger;

public class MyViewFlowBean {

    private static ADFLogger _logger =
        ADFLogger.createADFLogger(MyViewFlowBean.class);

    public MyViewFlowBean() {
        super();
        _logger.info("Initializing " + this);
    }

    public void initilize() {
        _logger.info("Calling");
    }

    @Override
    protected void finalize() throws Throwable {
        _logger.info("Destroying " + this);
        super.finalize();
    }
}
