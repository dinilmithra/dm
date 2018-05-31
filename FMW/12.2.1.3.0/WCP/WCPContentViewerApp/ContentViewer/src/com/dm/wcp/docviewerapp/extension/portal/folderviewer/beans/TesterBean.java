package com.dm.wcp.docviewerapp.extension.portal.folderviewer.beans;

import oracle.adf.share.logging.ADFLogger;

public class TesterBean {

    private static ADFLogger _logger = ADFLogger.createADFLogger(TesterBean.class);

    public void printFlowMessage() {

        _logger.severe("Passing through PrintFlowMessage");
    }
}
