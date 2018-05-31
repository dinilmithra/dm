package com.dm.wcextapp.customutils.mds.admin.beans;


import com.dm.wcextapp.customutils.MDSUtils;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import oracle.adf.share.ADFContext;
import oracle.adf.share.logging.ADFLogger;

import oracle.mds.core.MDSInstance;
import oracle.mds.exception.MDSException;


/**
 * Backing Bean for MDS admin purpose stuff.
 *
 * @author Andreas Koop
 * @date 29.12.2011
 */
public class MDSAdminBean {
    
    private static ADFLogger _logger = ADFLogger.createADFLogger(MDSAdminBean.class);

    private String username = "";


    public MDSAdminBean() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }


    /**
     * Delete all available MDS customization documents.
     * (aka "reset to factory defaults")
     * @param actionEvent
     */
    public void onResetToDefault(ActionEvent actionEvent) {
         _logger.severe("MDS Admin: onResetToDefault");

        List<String> listOfFiles = MDSUtils.queryAllFiles();
        try {
             _logger.severe("MDS Admin: Deleting " + listOfFiles);
            MDSUtils.deleteDocuments(listOfFiles);
            FacesContext.getCurrentInstance()
                .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, null,
                                             "Deleted the following MDS-Documents: " + listOfFiles));
        } catch (MDSException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resets the customizations of ALL users (<> layout user) to the layout of the
     * layout user
     * @param actionEvent
     */
    public void onResetAllToPoweruser(ActionEvent actionEvent) {
         _logger.severe("MDS Admin: onResetToPowerUser");
        final String mdsPowerUsername = MDSUtils.getMDSLayoutUsername();

        List<String> listOfFiles = MDSUtils.queryFilesExcept("/mdssys/cust/user/" + mdsPowerUsername);
        try {
             _logger.severe("MDS Admin: Deleting " + listOfFiles);
            MDSUtils.deleteDocuments(listOfFiles);
            FacesContext.getCurrentInstance()
                .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, null,
                                             "Deleted the following MDS-Documents: " + listOfFiles));
        } catch (MDSException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resets the customizazions of the layout user to the default layout
     * (with no customization at all)
     * @param actionEvent
     */
    public void onResetPoweruserToDefault(ActionEvent actionEvent) {
         _logger.severe("MDS Admin: onResePoweruserToDefault");

        final String mdsLayoutUsername = MDSUtils.getMDSLayoutUsername();

        deleteMDSDocumentsForUser(mdsLayoutUsername);
    }

    /**
     * Resets the customizations of specific user to the layout of the
     * layout user
     * @param actionEvent
     */
    public void onResetSpecificUserToPoweruser(ActionEvent actionEvent) {
         _logger.severe("MDS Admin: onResetToPowerUser for " + getUsername());

        final String username = getUsername();
        if (username == null || username.trim().isEmpty()) {
            FacesContext.getCurrentInstance()
                .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN, null,
                                             "Username must not be empty. Please provide a valid name."));
            return;
        }

        deleteMDSDocumentsForUser(username);
    }

    /**
     * Deletes all customization Documents for a specific user
     * @param username
     */
    private void deleteMDSDocumentsForUser(final String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("deleteMDSDocumentsForUser parameter error: username must not be empty!");

        }
        List<String> listOfFiles = MDSUtils.queryFilesMatch("/mdssys/cust/user/" + username);
        try {
             _logger.severe("MDS Admin: Deleting " + listOfFiles);
            MDSUtils.deleteDocuments(listOfFiles);
            FacesContext.getCurrentInstance()
                .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, null,
                                             "Deleted the following MDS-Documents for user " + getUsername() + ": " +
                                             listOfFiles));
        } catch (MDSException e) {
            e.printStackTrace();
        }
    }


    /**
     * Clears the MDS Cache. Currently I actually do not know what
     * happens under the covers. But maybe it becomes useful someday.
     * @param actionEvent
     */
    @Deprecated
    public void onClearMDSCache(ActionEvent actionEvent) {

        final MDSInstance mdsInstance = (MDSInstance) ADFContext.getCurrent().getMDSInstanceAsObject();

        // Clean Cache
        mdsInstance.clearCache();

    }

    /**
     * For Debug-Purposes: Traces some MDS Infos to stdout
     * @param actionEvent
     */
    public void onDebugMDSInfo(ActionEvent actionEvent) {
         _logger.severe("onDebugMDSInfo");
        MDSUtils.outputDebugInfo();
    }

    /**
     *
     * @return
     */
    public String getMDSLayoutUsername() {
        return MDSUtils.getMDSLayoutUsername();
    }

    /**
     *
     * @return
     */
    public boolean isMDSAdmin() {
        final String currentLoggedInUser = ADFContext.getCurrent()
                                                     .getSecurityContext()
                                                     .getUserName();
        //        return MDSUtils.getMDSLayoutUsername().equals(currentLoggedInUser);

        return true;
    }
}

