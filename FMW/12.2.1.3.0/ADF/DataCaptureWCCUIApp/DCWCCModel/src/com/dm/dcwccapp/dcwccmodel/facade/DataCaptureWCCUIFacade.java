package com.dm.dcwccapp.dcwccmodel.facade;

import com.dm.dcwccapp.dcwccmodel.data.Attribute;
import com.dm.dcwccapp.dcwccmodel.utils.DateUtil;
import com.dm.dcwccapp.dcwccmodel.utils.constants.AttributeConstants;
import com.dm.dcwccapp.dcwccmodel.utils.constants.Component;
import com.dm.dcwccapp.dcwccmodel.utils.ridc.DocProfileServices;
import com.dm.dcwccapp.dcwccmodel.utils.ridc.MetaServices;
import com.dm.dcwccapp.dcwccmodel.utils.ridc.SearchServices;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.naming.NamingException;

import oracle.adf.share.logging.ADFLogger;

import oracle.stellent.ridc.IdcClientException;


public class DataCaptureWCCUIFacade {

    private static ADFLogger _logger = ADFLogger.createADFLogger(DataCaptureWCCUIFacade.class);

    public static HashMap<String, HashMap<String, Object>> getAttributeProperties() {

        HashMap<String, HashMap<String, Object>> attributeMap = new HashMap<String, HashMap<String, Object>>();


        HashMap<String, Object> xSignatureProp = new HashMap<String, Object>();
        //        xSignatureProp.put(AttributeConstants.ATTRIBUTE_NAME, "Signature");
        //        xSignatureProp.put(AttributeConstants.VALIDATION_MESSAGE, "Signature is required");
        xSignatureProp.put(AttributeConstants.COMPONENT, Component.CHECK_BOX);
        attributeMap.put("xSignature", xSignatureProp);


        return attributeMap;

    }

    public static HashMap<String, HashMap<String, Object>> mergeAttributeMaps(HashMap<String, HashMap<String, Object>> map1,
                                                                              HashMap<String, HashMap<String, Object>> map2) {

        for (String key : map2.keySet()) {

            HashMap<String, Object> map2Prop = map2.get(key);
            HashMap<String, Object> map1Prop = map1.get(key);
            if (map1Prop != null) {
                map1Prop.putAll(map2Prop);
            } else {
                map1.put(key, map2Prop);
            }
        }
        return map1;
    }

    public static HashMap<String, HashMap<String, Object>> populateAttributeFromDocName(String docName,
                                                                                        String bpmCategory,
                                                                                        String bpmSubCategory) throws IdcClientException,
                                                                                                                      IOException,
                                                                                                                      NamingException {


        HashMap<String, HashMap<String, Object>> attributeMap = new HashMap<String, HashMap<String, Object>>();

        HashMap<String, HashMap<String, Object>> seatchAttributeMap = new HashMap<String, HashMap<String, Object>>();
        seatchAttributeMap = SearchServices.getSearchResults(docName);


        if ((seatchAttributeMap != null) && (seatchAttributeMap.size() > 0)) {

            attributeMap = mergeAttributeMaps(attributeMap, seatchAttributeMap);

            HashMap<String, Object> dIDMap = attributeMap.get("dID");
            String dID = (String) dIDMap.get(AttributeConstants.VALUE);

            HashMap<String, Object> dDocNameMap = attributeMap.get("dDocName");
            String dDocName = (String) dDocNameMap.get(AttributeConstants.VALUE);

            HashMap<String, HashMap<String, Object>> docMetadataInfoMap = MetaServices.getDocMetadataInfo(dID, dDocName);
            attributeMap = mergeAttributeMaps(attributeMap, docMetadataInfoMap);


            HashMap<String, Object> profAttrMap = attributeMap.get("xIdcProfile");
            String profileName = (String) profAttrMap.get(AttributeConstants.VALUE);

            HashMap<String, Object> categoryAttrMap = attributeMap.get("xCategory");
            String category = (String) categoryAttrMap.get(AttributeConstants.VALUE);

            HashMap<String, Object> subCategoryAttrMap = attributeMap.get("xSubCategory");
            String subCategory = (String) subCategoryAttrMap.get(AttributeConstants.VALUE);

            if ((bpmCategory != null) && !bpmCategory.trim().isEmpty() && (bpmSubCategory != null) &&
                !bpmSubCategory.trim().isEmpty()) {

                if ((category != null) && !category.trim().isEmpty() && (subCategory != null) &&
                    !subCategory.trim().isEmpty()) {
                    if (bpmCategory.equals(category) && bpmSubCategory.equals(subCategory)) {

                        profileName = bpmSubCategory.replaceAll("\\s+", "");

                        HashMap<String, HashMap<String, Object>> profileAttributeMap =
                            populateAttributesFromProfile(profileName);
                        attributeMap = mergeAttributeMaps(attributeMap, profileAttributeMap);
                    }
                }
            }

            HashMap<String, HashMap<String, Object>> customAttributeMap = getAttributeProperties();
            attributeMap = mergeAttributeMaps(attributeMap, customAttributeMap);

        }


        return attributeMap;

    }

    public static HashMap<String, HashMap<String, Object>> populateAttributesFromProfile(String profileName) throws IdcClientException,
                                                                                                                    NamingException {

        HashMap<String, HashMap<String, Object>> attributeMap = new HashMap<String, HashMap<String, Object>>();

        ArrayList<String> rules = DocProfileServices.getDocProfile(profileName);

        for (String ruleName : rules) {

            HashMap<String, HashMap<String, Object>> ruleAttributeMap = DocProfileServices.getDocRule(ruleName);
            attributeMap = mergeAttributeMaps(attributeMap, ruleAttributeMap);
        }

        return attributeMap;

    }

    public static ArrayList<Attribute> convertMetaModelToUIModel(HashMap<String, HashMap<String, Object>> attributeMap) {

        ArrayList<Attribute> attributeList = new ArrayList<Attribute>();

        int i = 0;
        for (String attributeKey : attributeMap.keySet()) {

            Attribute attribute = new Attribute();

            HashMap<String, Object> propertyMap = attributeMap.get(attributeKey);

            attribute.setId(attributeKey);

            for (String propertyKey : propertyMap.keySet()) {

                if (AttributeConstants.ATTRIBUTE_NAME.equals(propertyKey)) {
                    String name = (String) propertyMap.get(propertyKey);
                    attribute.setName(name);

                } else if (AttributeConstants.EDIT.equals(propertyKey)) {  
                    Boolean visible = (Boolean) propertyMap.get(propertyKey);
                    attribute.setVisible(visible);

                } else if (AttributeConstants.REQUIRED.equals(propertyKey)) {
                    Boolean required = (Boolean) propertyMap.get(propertyKey);
                    attribute.setRequired(required);

                } else if (AttributeConstants.VALUE.equals(propertyKey)) {
                    String value = (String) propertyMap.get(propertyKey);
                    attribute.setValue(value);

                } else if (AttributeConstants.VALIDATION_MESSAGE.equals(propertyKey)) {
                    String message = (String) propertyMap.get(propertyKey);
                    attribute.setValidationMessage(message);

                } else if (AttributeConstants.TYPE.equals(propertyKey)) {
                    String type = (String) propertyMap.get(propertyKey);
                    attribute.setAttributeType(type);

                } else if (AttributeConstants.COMPONENT.equals(propertyKey)) {
                    String value = (String) propertyMap.get(propertyKey);
                    attribute.setComponent(value);
                } else if (AttributeConstants.ATTRIBUTE_ORDER.equals(propertyKey)) {

                    Object value = propertyMap.get(propertyKey);
                    Integer order = (value != null) ? Integer.valueOf(String.valueOf(value).trim()) : null;
                    attribute.setOrder(order);
                }


            }

            String attributeType = attribute.getAttributeType();
            String component = attribute.getComponent();

            if ((component != null) && !component.trim().isEmpty() && Component.CHECK_BOX.equals(component)) {
                String value = (String) attribute.getValue();
                attribute.setValue(Boolean.valueOf(value));

            } else if ((attributeType != null) && !attributeType.trim().isEmpty() && "DATE".equals(attributeType)) {

                String ucmDate = (String) attribute.getValue();
                Date date = null;

                if ((ucmDate != null) && !ucmDate.trim().isEmpty()) {
                    date = DateUtil.ucmDateToUtilDate(ucmDate);
                }

                attribute.setValue(date);
            }

            attributeList.add(attribute);
        }

        return attributeList;
    }

    public static HashMap<String, String> convertUIModelToMetadataModel(ArrayList<Attribute> uiModel) {

        HashMap<String, String> metadateModel = new HashMap<String, String>();

        for (Attribute attribute : uiModel) {

            String id = attribute.getId();
            Boolean visible = attribute.getVisible();
            visible = (visible != null) ? visible : Boolean.FALSE;

            if ("dID".equals(id)) {

                Object value = attribute.getValue();
                if (value == null) {
                    value = "";
                }
                metadateModel.put(id, String.valueOf(value));

            } else if ("dDocName".equals(id)) {

                Object value = attribute.getValue();
                if (value == null) {
                    value = "";
                }
                metadateModel.put(id, String.valueOf(value));

            } else if (visible) {

                String attributeType = attribute.getAttributeType();
                String component = attribute.getComponent();

                if (Component.CHECK_BOX.equals(component)) {
                    Boolean value = (Boolean) attribute.getValue();
                    metadateModel.put(id, String.valueOf(Boolean.valueOf(value)));

                } else if ("DATE".equals(attributeType)) {
                    Date utilDate = (Date) attribute.getValue();
                    String ucmDate = DateUtil.utilDateToUcmDate(utilDate);
                    metadateModel.put(id, ucmDate);

                } else {
                    Object value = attribute.getValue();
                    if (value == null) {
                        value = "";
                    }
                    metadateModel.put(id, String.valueOf(value));
                }
            }
        }

        return metadateModel;

    }


}
