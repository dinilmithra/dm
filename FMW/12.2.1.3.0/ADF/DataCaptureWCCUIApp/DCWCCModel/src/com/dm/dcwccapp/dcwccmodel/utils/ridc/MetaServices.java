package com.dm.dcwccapp.dcwccmodel.utils.ridc;

import com.dm.dcwccapp.dcwccmodel.utils.constants.AttributeConstants;

import java.util.HashMap;

import javax.naming.NamingException;

import oracle.stellent.ridc.IdcClient;
import oracle.stellent.ridc.IdcClientException;
import oracle.stellent.ridc.model.DataBinder;
import oracle.stellent.ridc.model.DataObject;
import oracle.stellent.ridc.model.DataResultSet;
import oracle.stellent.ridc.protocol.ServiceResponse;

public class MetaServices {


    public static HashMap<String, HashMap<String, Object>> getDocMetadataInfo(String dID,
                                                                              String dDocName) throws IdcClientException,
                                                                                                      NamingException {

        HashMap<String, HashMap<String, Object>> attributeMap = new HashMap<String, HashMap<String, Object>>();

        IdcClient idcClient = RIDCUtil.getUCMConnection();

        DataBinder binder = idcClient.createBinder();

        binder.putLocal("IdcService", "GET_DOC_METADATA_INFO");
        
        if ((dID != null) && !dID.trim().isEmpty()) {
            binder.putLocal("dID", dID);
        }
        
        if ((dDocName != null) && !dDocName.trim().isEmpty()) {
            binder.putLocal("dDocName", dDocName);
        }

        ServiceResponse response = idcClient.sendRequest(RIDCUtil.getUserContext(), binder);
        DataBinder binderResult = response.getResponseAsBinder();

        DataResultSet resultSet = binderResult.getResultSet("DocMetaDefinition");
        //        List<Field> fields = resultSet.getFields();

        if (resultSet != null) {
            for (DataObject dataObject : resultSet.getRows()) {

                String dName = dataObject.get("dName");

                String dType = dataObject.get("dType");
                String dIsRequired = dataObject.get("dIsRequired");
                //                String dIsEnabled = dataObject.get("dIsEnabled");
                String dCaption = dataObject.get("dCaption");
                String dOrder = dataObject.get("dOrder");

                HashMap<String, Object> propertyMap = new HashMap<String, Object>();
                propertyMap.put(AttributeConstants.TYPE, dType.toUpperCase());
                propertyMap.put(AttributeConstants.REQUIRED,
                                ((dIsRequired == null) ? Boolean.FALSE :
                                 ((dIsRequired.equals("1") ? Boolean.TRUE : Boolean.FALSE))));
                //                propertyMap.put(AttributeConstants.IS_VISIBLE, dIsEnabled);
                propertyMap.put(AttributeConstants.ATTRIBUTE_NAME, dCaption);
                propertyMap.put(AttributeConstants.ATTRIBUTE_ORDER, dOrder);

                attributeMap.put(dName, propertyMap);

            }


        }

        return attributeMap;

    }
}
