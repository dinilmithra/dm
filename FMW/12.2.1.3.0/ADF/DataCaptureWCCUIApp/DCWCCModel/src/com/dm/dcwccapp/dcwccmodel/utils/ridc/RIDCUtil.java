package com.dm.dcwccapp.dcwccmodel.utils.ridc;

import javax.naming.NamingException;

import oracle.adf.model.connection.url.URLConnectionProxy;
import oracle.adf.share.ADFContext;
import oracle.adf.share.logging.ADFLogger;

import oracle.stellent.ridc.IdcClient;
import oracle.stellent.ridc.IdcClientException;
import oracle.stellent.ridc.IdcClientManager;
import oracle.stellent.ridc.IdcContext;

public class RIDCUtil {


    private static ADFLogger _logger =
        ADFLogger.createADFLogger(RIDCUtil.class);

    public RIDCUtil() {

        //
        //        try {
        //
        //            WccConnection wccconn = (WccConnection) ADFContext.getCurrent()
        //                                                              .getConnectionsContext()
        //                                                              .lookup("dcdocs-wcc-conn");
        //            wccurl = wccconn.getPropConnectionUrl();
        //
        //        } catch (NamingException e) {
        //            e.printStackTrace();
        //        }


    }


    public static IdcClient getUCMConnection() throws IdcClientException,
                                                      NamingException {

        //        WccConnection idcconn =
        //            (WccConnection)ADFContext.getCurrent().getConnectionsContext().lookup("dcdocs-idc-conn");

        String idcconnUrl = null;
        //            idcconn.getPropConnectionUrl();

        IdcClientManager clientManager = new IdcClientManager();
        IdcClient client = clientManager.createClient(idcconnUrl);
        return client;

    }

    public static IdcContext getUserContext() throws NamingException {

        IdcContext userContext = new IdcContext(getUserName());
        return userContext;
    }

    public static String getWCCUrl() throws NamingException {

        URLConnectionProxy wcconn =
            (URLConnectionProxy)ADFContext.getCurrent().getConnectionsContext().lookup("dcdocs-wccurl-conn");
        String url = wcconn.getURL().toString();
        return url;
    }

    public static String getUserName() throws NamingException {


        //        WccConnection idcconn =
        //            (WccConnection)ADFContext.getCurrent().getConnectionsContext().lookup("dcdocs-idc-conn");
        String ridcUsername = null;
        //            idcconn.getPropCredentialUsername();

        return ridcUsername;
    }


}
