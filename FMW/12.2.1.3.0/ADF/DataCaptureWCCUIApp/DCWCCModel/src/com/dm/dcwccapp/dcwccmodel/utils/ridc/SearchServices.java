package com.dm.dcwccapp.dcwccmodel.utils.ridc;

import com.dm.dcwccapp.dcwccmodel.utils.constants.AttributeConstants;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;

import javax.naming.NamingException;

import oracle.stellent.ridc.IdcClient;
import oracle.stellent.ridc.IdcClientException;
import oracle.stellent.ridc.model.DataBinder;
import oracle.stellent.ridc.model.DataObject;
import oracle.stellent.ridc.model.DataResultSet;
import oracle.stellent.ridc.model.DataResultSet.Field;
import oracle.stellent.ridc.model.serialize.HdaBinderSerializer;
import oracle.stellent.ridc.protocol.ServiceResponse;

public class SearchServices {


    public static HashMap<String, HashMap<String, Object>> getSearchResults(String docName) throws IdcClientException,
                                                                                                   IOException,
                                                                                                   NamingException {


        HashMap<String, HashMap<String, Object>> map = new HashMap<String, HashMap<String, Object>>();

        IdcClient idcClient = RIDCUtil.getUCMConnection();
        DataBinder binder = idcClient.createBinder();
        binder.putLocal("IdcService", "GET_SEARCH_RESULTS");
        binder.putLocal("QueryText", "dDocName <matches> `" + docName + "`");
        //        binder.putLocal("dDocName", "WCCD_000423");
        binder.putLocal("ResultCount", "1");

        HdaBinderSerializer serializer = new HdaBinderSerializer("UTF-8", idcClient.getDataFactory());

        serializer.serializeBinder(System.out, binder);

        ServiceResponse response = idcClient.sendRequest(RIDCUtil.getUserContext(), binder);
        DataBinder binderResult = response.getResponseAsBinder();

        DataResultSet resultSet = binderResult.getResultSet("SearchResults");

        if (resultSet != null) {
            List<DataObject> rows = resultSet.getRows();

            for (DataObject dataObject : rows) {

                List<Field> fields = resultSet.getFields();

                for (Field f : fields) {

                    String key = f.getName();
                    String type = f.getType().name();
                    Object value = dataObject.get(f.getName());

                    HashMap<String, Object> propMap = new HashMap<String, Object>();
                    propMap.put(AttributeConstants.VALUE, value);
                    propMap.put(AttributeConstants.TYPE, type);
                    map.put(key, propMap);

                    //                    System.out.println(key + ":" + value + ":" + type);
                }
            }

        }

        return map;
    }
}
