package com.dm.dcwccapp.dcwccmodel.utils.ridc;

import com.dm.dcwccapp.dcwccmodel.utils.constants.AttributeConstants;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;

import javax.naming.NamingException;

import oracle.stellent.ridc.IdcClient;
import oracle.stellent.ridc.IdcClientException;
import oracle.stellent.ridc.model.DataBinder;
import oracle.stellent.ridc.model.DataObject;
import oracle.stellent.ridc.model.DataResultSet;
import oracle.stellent.ridc.protocol.ServiceResponse;


public class DocProfileServices {
    public DocProfileServices() {
        super();
    }

    public static ArrayList<String> getDocProfile(String profileName) throws IdcClientException, NamingException {
        
        ArrayList<String> profileList = new ArrayList<String>();
        
        IdcClient  idcClient = RIDCUtil.getUCMConnection();        

        DataBinder binder = idcClient.createBinder();
        binder.putLocal("IdcService", "GET_DOCPROFILE");

//        binder.putLocal("dpName", "Prop58");
        binder.putLocal("dpName", profileName);

          ServiceResponse  response = idcClient.sendRequest(RIDCUtil.getUserContext(), binder);
          DataBinder  binderResult = response.getResponseAsBinder();
          
        DataResultSet resultSet = binderResult.getResultSet("ProfileRules");

        HashMap<String, Object> map = new HashMap<String, Object>();

        for (DataObject dataObject : resultSet.getRows()) {
            
          String dpRuleName =  dataObject.get("dpRuleName");
            
            profileList.add(dpRuleName);
         
        }

        return profileList;

    }
    
    public static HashMap<String,  HashMap<String, Object> > getDocRule(String ruleName) throws IdcClientException,
                                                                                              NamingException {
       HashMap<String,  HashMap<String, Object> > ruleAttributeMap = new HashMap<String,  HashMap<String, Object> >();
       
        IdcClient  idcClient = RIDCUtil.getUCMConnection();
        

        DataBinder binder = idcClient.createBinder();
        binder.putLocal("IdcService", "GET_DOCRULE");

        //        binder.putLocal("dpRuleName", "Prop58_Edit");
        binder.putLocal("dpRuleName", ruleName);

          ServiceResponse  response = idcClient.sendRequest(RIDCUtil.getUserContext(), binder);
          DataBinder  binderResult = response.getResponseAsBinder();
          
        DataResultSet resultSet = binderResult.getResultSet("RuleFields");

        HashMap<String, Object> map = new HashMap<String, Object>();

        for (DataObject dataObject : resultSet.getRows()) {
           
          String dpRuleFieldName =  dataObject.get("dpRuleFieldName");           
          String dpRuleFieldType =  dataObject.get("dpRuleFieldType");
          
            HashMap<String, Object>  rule = new  HashMap<String, Object> ();  
            boolean ruleflag=Boolean.FALSE;
            
            if(AttributeConstants.EDIT.equals(dpRuleFieldType)){
                    rule.put(AttributeConstants.EDIT, Boolean.TRUE);
                    ruleflag=Boolean.TRUE;
                }else  if(AttributeConstants.REQUIRED.equals(dpRuleFieldType)){
                    rule.put(AttributeConstants.REQUIRED, Boolean.TRUE);
                    ruleflag=Boolean.TRUE;
                    
                }
            
            if(ruleflag){
                    ruleAttributeMap.put(dpRuleFieldName, rule)  ;
                }
            
        }

        return ruleAttributeMap;

    }
    
    public static ArrayList<String[]> getDocProfiles() throws IdcClientException, NamingException {
        
        ArrayList<String[]> profileList = new ArrayList<String[]>();
        
        IdcClient  idcClient = RIDCUtil.getUCMConnection();            

        DataBinder binder = idcClient.createBinder();
        binder.putLocal("IdcService", "GET_DOCPROFILES"); 
            
        ServiceResponse  response = idcClient.sendRequest(RIDCUtil.getUserContext(), binder);
        DataBinder  binderResult = response.getResponseAsBinder();
              
        DataResultSet resultSet = binderResult.getResultSet("DocumentProfiles");            

        if( (resultSet != null)){
           List<DataObject> rows =     resultSet.getRows();
           if( rows!= null){
                   for (DataObject dataObject : rows) {
                          
                       String dpName =  dataObject.get("dpName");           
                       String dpDisplayLabel =  dataObject.get("dpDisplayLabel");
                       
                       String[] profile = new String[]{dpName,dpDisplayLabel};                     
                           
                       profileList.add(profile);
                   }
               }   
            }
        
        return profileList;
    }    
    
}
