package com.dm.dcwccapp.dcwccmodel.dc;

import com.dm.dcwccapp.dcwccmodel.data.Attribute;
import com.dm.dcwccapp.dcwccmodel.facade.DataCaptureWCCUIFacade;
import com.dm.dcwccapp.dcwccmodel.utils.ridc.DocProfileServices;
import com.dm.dcwccapp.dcwccmodel.utils.ridc.DocServices;
import com.dm.dcwccapp.dcwccmodel.utils.ridc.MetaServices;
import com.dm.dcwccapp.dcwccmodel.utils.ridc.RIDCUtil;
import com.dm.dcwccapp.dcwccmodel.utils.ridc.UpdateMetadata;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import javax.naming.NamingException;

import oracle.adf.share.logging.ADFLogger;

import oracle.jbo.JboException;

import oracle.stellent.ridc.IdcClientException;

public class DataCaptureWCCUIDC {


    private static ADFLogger _logger =
        ADFLogger.createADFLogger(DataCaptureWCCUIDC.class);

    private ArrayList<Attribute> attributeList = new ArrayList<Attribute>();

    public ArrayList<Attribute> tester() {

        _logger.info("Inside Tester");
        HashMap<String, HashMap<String, Object>> attributeMap =
            new HashMap<String, HashMap<String, Object>>();

        NamingException nn = null;

        return DataCaptureWCCUIFacade.convertMetaModelToUIModel(attributeMap);
    }

    public String wccUrl() {

        String url = null;
        try {
            url = RIDCUtil.getWCCUrl();
        } catch (NamingException e) {
            e.printStackTrace();
            throw new JboException(e.getMessage());
        }
        return url;
    }

    public ArrayList<Attribute> populateCheckinAttributes(String profileName) throws NamingException {


        HashMap<String, HashMap<String, Object>> attributeMap =
            new HashMap<String, HashMap<String, Object>>();

        if ((profileName != null) && !profileName.trim().isEmpty()) {

            try {
                HashMap<String, HashMap<String, Object>> docMetadataInfoMap = MetaServices.getDocMetadataInfo(null, null);
                attributeMap = DataCaptureWCCUIFacade.mergeAttributeMaps(attributeMap,
                                                                  docMetadataInfoMap);

            } catch (IdcClientException e) {
                e.printStackTrace();
                throw new JboException(e.getMessage());
            }


            try {
                HashMap<String, HashMap<String, Object>> profileAttributeMap = DataCaptureWCCUIFacade.populateAttributesFromProfile(profileName);
                attributeMap = DataCaptureWCCUIFacade.mergeAttributeMaps(attributeMap,
                                                                  profileAttributeMap);

            } catch (IdcClientException e) {
                e.printStackTrace();
                throw new JboException(e.getMessage());
            }


            HashMap<String, HashMap<String, Object>> customAttributeMap = DataCaptureWCCUIFacade.getAttributeProperties();
            attributeMap = DataCaptureWCCUIFacade.mergeAttributeMaps(attributeMap,
                                                              customAttributeMap);

            ArrayList<Attribute> list = DataCaptureWCCUIFacade.convertMetaModelToUIModel(attributeMap);

            attributeList.clear();
            attributeList.addAll(list);

        }

        for (Attribute attribute : attributeList) {
            System.out.println(attribute.getId());
        }


        return attributeList;
    }

    public ArrayList<String[]> populateProfiles() {

        ArrayList<String[]> profileList = new ArrayList<String[]>();

        try {
            profileList = DocProfileServices.getDocProfiles();
        } catch (Exception e) {
            e.printStackTrace();
            throw new JboException(e.getMessage());
        }

        return profileList;

    }

    public ArrayList<Attribute> populateAttributeModel(String docName,
                                                       String bpmCategory,
                                                       String bpmSubCategory) {


        HashMap<String, HashMap<String, Object>> attributeMap =
            new HashMap<String, HashMap<String, Object>>();

        try {
            attributeMap = DataCaptureWCCUIFacade.populateAttributeFromDocName(docName,
                                                                        bpmCategory,
                                                                        bpmSubCategory);
        }  catch (Exception e) {
            e.printStackTrace();
            throw new JboException(e.getMessage());
        }


        ArrayList<Attribute> list = DataCaptureWCCUIFacade.convertMetaModelToUIModel(attributeMap);

        Comparator<Attribute> orderComparator = new Comparator<Attribute>() {

            @Override
            public int compare(Attribute o1, Attribute o2) {

                Integer order1 = o1.getOrder();
                order1 = (order1 != null) ? order1 : new Integer(0);
                Integer order2 = o2.getOrder();
                order2 = (order2 != null) ? order2 : new Integer(0);

                return order1 - order2;
            }

        };

        Collections.sort(list, orderComparator);


        attributeList.clear();
        attributeList.addAll(list);

        return attributeList;

    }

    public void updateMetadata() {


        HashMap<String, String> metadataMap = DataCaptureWCCUIFacade.convertUIModelToMetadataModel(attributeList);

        try {
            UpdateMetadata.update(metadataMap);
        }  catch (Exception e) {
            e.printStackTrace();
            throw new JboException(e.getMessage());
        }

    }

    public void checkinMetadata(String profileName) {

        HashMap<String, String> metadataMap = DataCaptureWCCUIFacade.convertUIModelToMetadataModel(attributeList);

        try {
            DocServices.checkinNew(profileName, metadataMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JboException(e.getMessage());
        }

    }

    public void setAttributeList(ArrayList<Attribute> attributeList) {
        this.attributeList = attributeList;
    }

    public ArrayList<Attribute> getAttributeList() {
        return attributeList;
    }
}
