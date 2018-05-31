package com.dm.dcwccapp.dcwccmodel.utils.ridc;

import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;

import oracle.stellent.ridc.IdcClient;
import oracle.stellent.ridc.IdcClientException;
import oracle.stellent.ridc.model.DataBinder;
import oracle.stellent.ridc.protocol.ServiceResponse;

public class UpdateMetadata {

    public static void update(HashMap<String, String> map) throws IdcClientException, NamingException {

        IdcClient idcClient = RIDCUtil.getUCMConnection();

        DataBinder binder = idcClient.createBinder();
        binder.putLocal("IdcService", "UPDATE_DOCINFO");
        //        binder.putLocal("dID", "223");
        //        binder.putLocal("dDocName", "WCCD_000423");
        //        binder.putLocal("xFirstName", name);


        //        for (String key : map.keySet()) {
        //
        //            binder.putLocal(key, map.get(key));
        //           System.out.println("key: " + key + " value: " + map.get(key));
        //        }


        for (Map.Entry entry : map.entrySet()) {

            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            binder.putLocal(key, value);
            System.out.println("Updating" + key + ":" + value);
        }


        //        binder.putLocal("dSecurityGroup", "");
        //        binder.putLocal("dDocAccount", "");
        //        binder.putLocal("dRevLabel", "2");
        //        binder.putLocal("xLastName", "brain");
        //        binder.putLocal("xFirstName", "elizabeth");
        //        binder.putLocal("xSysDocID", "970211");

        //        binder.putLocal("IsUpdateMetaOnly", "true");
        //        binder.putLocal("createPrimaryMetaFile", "true");

        ServiceResponse response = idcClient.sendRequest(RIDCUtil.getUserContext(), binder);

        System.out.println(response);


    }
}
