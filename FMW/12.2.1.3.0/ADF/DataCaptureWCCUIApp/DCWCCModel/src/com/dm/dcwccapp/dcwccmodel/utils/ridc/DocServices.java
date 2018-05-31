package com.dm.dcwccapp.dcwccmodel.utils.ridc;

import com.dm.dcwccapp.dcwccmodel.facade.DataCaptureWCCUIFacade;
import com.dm.dcwccapp.dcwccmodel.utils.constants.AttributeConstants;

import java.io.IOException;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.naming.NamingException;

import oracle.stellent.ridc.IdcClient;
import oracle.stellent.ridc.IdcClientException;
import oracle.stellent.ridc.model.DataBinder;
import oracle.stellent.ridc.model.DataObject;
import oracle.stellent.ridc.model.DataResultSet;
import oracle.stellent.ridc.model.DataResultSet.Field;
import oracle.stellent.ridc.model.serialize.HdaBinderSerializer;
import oracle.stellent.ridc.protocol.ServiceResponse;

public class DocServices {

    public static void checkinNew(String profileName, HashMap<String, String> metadataMap) throws IdcClientException,
                                                                                                  NamingException {


        IdcClient idcClient = RIDCUtil.getUCMConnection();

        DataBinder serviceBinder = idcClient.createBinder();

        serviceBinder.putLocal("IdcService", "CHECKIN_NEW"); //CHECKIN_UNIVERSAL
        serviceBinder.putLocal("dDocType", "Document");

        String dDocTitle = UUID.randomUUID().toString();
        serviceBinder.putLocal("dDocTitle", dDocTitle);

        serviceBinder.putLocal("dDocAuthor", RIDCUtil.getUserName());
        //            serviceBinder.putLocal("dSecurityGroup", "Public");
        serviceBinder.putLocal("dSecurityGroup", "DMWCC");

        if ((profileName != null) && !profileName.trim().isEmpty()) {
            serviceBinder.putLocal("xIdcProfile", profileName);
        }

        serviceBinder.putLocal("createPrimaryMetaFile", "true");
        serviceBinder.putLocal("AllowPrimaryMetaFile", "true");

        if (metadataMap != null) {

            for (String key : metadataMap.keySet()) {
                String value = metadataMap.get(key);
                serviceBinder.putLocal(key, value);
            }
        }

        //            serviceBinder.addFile("primaryFile", new TransferFile(in, "File 1", 1024));


        ServiceResponse response = idcClient.sendRequest(RIDCUtil.getUserContext(), serviceBinder);
        DataBinder responseBinder = response.getResponseAsBinder();
        Collection<String> resultSetNames = responseBinder.getResultSetNames();
        DataResultSet resultSet = responseBinder.getResultSet("UserAttribInfo");
        DataResultSet docMetaDefinitionRS = responseBinder.getResultSet("DocMetaDefinition");        
        

        for (DataObject dataObject : resultSet.getRows()) {

            String dName = dataObject.get("dName");
            System.out.println("dName : " + dName);
        }

        DataObject localData = responseBinder.getLocalData();
        if (localData.get("StatusCode").equals("0")) {
            System.out.println(localData.get("StatusMessage"));
        }

    }

    public static HashMap<String, HashMap<String, Object>> checkinNewform(String profileName) throws IdcClientException,
                                                                                                     NamingException {

        HashMap<String, HashMap<String, Object>> attributeMap = new HashMap<String, HashMap<String, Object>>();

        IdcClient idcClient = RIDCUtil.getUCMConnection();

        DataBinder binder = idcClient.createBinder();

        binder.putLocal("IdcService", "CHECKIN_NEW_FORM");
        binder.putLocal("dpTriggerValue", profileName);

        //        dpTriggerValue=Prop38
        //    AssociatedFields:xSSN


        ServiceResponse response = idcClient.sendRequest(RIDCUtil.getUserContext(), binder);
        DataBinder binderResult = response.getResponseAsBinder();


        Collection<String> resultSetNames = binderResult.getResultSetNames();
        String associatedFields = "";
        for (String resultSetName : resultSetNames) {

            if ((resultSetName != null) && !resultSetName.trim().isEmpty()) {
                if (resultSetName.startsWith("AssociatedFields")) {
                    associatedFields = resultSetName;
                }
            }
        }

        DataResultSet resultSet = binderResult.getResultSet(associatedFields);

        for (DataObject dataObject : resultSet.getRows()) {

            String dName = dataObject.get("dName");
            String dType = dataObject.get("dType");
            String dCaption = dataObject.get("dCaption");

            HashMap<String, Object> propertyMao = new HashMap<String, Object>();
            propertyMao.put(AttributeConstants.TYPE, ((dType != null) ? dType.trim().toUpperCase() : null));
            propertyMao.put(AttributeConstants.ATTRIBUTE_NAME, dCaption);
            attributeMap.put(dName, propertyMao);


            //            System.out.println("dType : " + dType + " dCaption : " + dCaption + " dName : " + dName +
            //                               " dpFieldName : " + dpFieldName + " dIsRequired : " + dIsRequired + " dIsEnabled : " +
            //                               dIsEnabl () ed);

        }


        return attributeMap;

    }

    public static HashMap<String, HashMap<String, Object>> docInfo(String dID,
                                                                   String dDocName) throws IdcClientException,
                                                                                           NamingException {


        HashMap<String, HashMap<String, Object>> attributeMap = new HashMap<String, HashMap<String, Object>>();


        IdcClient idcClient = RIDCUtil.getUCMConnection();

        DataBinder binder = idcClient.createBinder();

        binder.putLocal("IdcService", "DOC_INFO");
        binder.putLocal("dID", dID);
        binder.putLocal("dDocName", dDocName);

        ServiceResponse response = idcClient.sendRequest(RIDCUtil.getUserContext(), binder);
        DataBinder binderResult = response.getResponseAsBinder();

        DataResultSet resultSet = binderResult.getResultSet("DOC_INFO");
        //        List<Field> fields = resultSet.getFields();

        if (resultSet != null) {

            HashMap<String, Object> dIDMap = new HashMap<String, Object>();
            dIDMap.put(AttributeConstants.VALUE, dID);
            attributeMap.put("dID", dIDMap);

            HashMap<String, Object> dDocNameMap = new HashMap<String, Object>();
            dDocNameMap.put(AttributeConstants.VALUE, dDocName);

            attributeMap.put("dDocName", dDocNameMap);

            List<DataObject> rows = resultSet.getRows();
            int i = rows.size();

            for (DataObject dataObject : rows) {

                //                List<Field> fields = resultSet.getFields();
                //
                ////                String dID1 = dataObject.get("dID");
                ////                String dDocName1 = dataObject.get("dDocName");
                //
                //                for (Field field : fields) {
                //
                //                    String key = field.getName();
                //                    Object value = dataObject.get(field.getName());
                //
                //                    HashMap<String, Object> propMap = new HashMap<String, Object>();
                //                    propMap.put(AttributeConstants.VALUE, value);
                //
                //                    attributeMap.put(key, propMap);
                //
                //                    //                    System.out.println(key + " : " + value);
                //                }

                for (String key : dataObject.keySet()) {

                    String value = dataObject.get(key);

                    HashMap<String, Object> propMap = new HashMap<String, Object>();
                    propMap.put(AttributeConstants.VALUE, value);
                    attributeMap.put(key, propMap);
                    //                    System.out.println("key : " + key + " value : " + value);

                }
            }

        }


        HashMap<String, HashMap<String, Object>> associatedFieldsMap = new HashMap<String, HashMap<String, Object>>();

        Collection<String> resultSetNames = binderResult.getResultSetNames();
        String associatedFields = "";
        for (String resultSetName : resultSetNames) {

            if ((resultSetName != null) && !resultSetName.trim().isEmpty()) {
                if (resultSetName.startsWith("AssociatedFields")) {
                    associatedFields = resultSetName;
                }
            }
        }

        resultSet = binderResult.getResultSet(associatedFields);
        for (DataObject dataObject : resultSet.getRows()) {

            String dCaption = dataObject.get("dCaption");

            String dType = dataObject.get("dType");
            if ((dType != null) && !dType.trim().isEmpty()) {
                dType = dType.toUpperCase();
            }

            String dIsRequired = dataObject.get("dIsRequired");
            if ((dIsRequired != null) && !dIsRequired.trim().isEmpty()) {
                if ("1".equals(dIsRequired)) {
                    dIsRequired = "true";
                } else {
                    dIsRequired = "false";
                }
            }

            String dIsEnabled = dataObject.get("dIsEnabled");
            if ((dIsEnabled != null) && !dIsEnabled.trim().isEmpty()) {
                if ("1".equals(dIsEnabled)) {
                    dIsEnabled = "true";
                } else {
                    dIsEnabled = "false";
                }
            }

            String dName = dataObject.get("dName");


            HashMap<String, Object> propertyMap = new HashMap<String, Object>();
            propertyMap.put(AttributeConstants.ATTRIBUTE_NAME, dCaption);
            propertyMap.put(AttributeConstants.TYPE, dType);
            propertyMap.put(AttributeConstants.REQUIRED, Boolean.valueOf(dIsRequired));
            propertyMap.put(AttributeConstants.IS_VISIBLE, Boolean.valueOf(dIsEnabled));

            associatedFieldsMap.put(dName, propertyMap);

            System.out.println("dName : " + dName + " dCaption : " + dCaption + " dType : " + dType);


        }

        attributeMap = DataCaptureWCCUIFacade.mergeAttributeMaps(attributeMap, associatedFieldsMap);


        return attributeMap;

    }
}
