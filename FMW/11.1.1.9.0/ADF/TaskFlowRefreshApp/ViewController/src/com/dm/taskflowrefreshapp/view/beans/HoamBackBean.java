package com.dm.taskflowrefreshapp.view.beans;

import oracle.adf.share.logging.ADFLogger;

public class HoamBackBean {

    private static ADFLogger _logger =
        ADFLogger.createADFLogger(HoamBackBean.class);

    public HoamBackBean() {
        super();
        _logger.info("Inside HoamBackBean Constructor" + this);
    }

    private String lastName;

    @Override
    protected void finalize() throws Throwable {
        _logger.info("Inside HoamBackBean finalize" + this);
        super.finalize();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }
}
