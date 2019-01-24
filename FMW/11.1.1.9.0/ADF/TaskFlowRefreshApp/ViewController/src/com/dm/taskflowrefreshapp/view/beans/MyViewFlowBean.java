package com.dm.taskflowrefreshapp.view.beans;

import oracle.adf.share.logging.ADFLogger;

public class MyViewFlowBean {

    private static ADFLogger _logger =
        ADFLogger.createADFLogger(MyViewFlowBean.class);

    public MyViewFlowBean() {
        super();
        _logger.info("Initializing " + this);
    }

    public void setup() {
        _logger.info("");
    }

    public void initilizer() {
        _logger.info("" + this);
    }

    public void finalizer() {
        _logger.info("" + this);
    }

    @Override
    protected void finalize() throws Throwable {
        _logger.info("Destroying " + this);
        super.finalize();
    }
}
