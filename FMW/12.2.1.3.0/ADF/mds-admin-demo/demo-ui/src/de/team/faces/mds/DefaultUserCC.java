/*
 * Copyright (c) 2011 TEAM GmbH and/or its affiliates.
 * All rights reserved.
 *
 */
package de.team.faces.mds;

import javax.faces.context.FacesContext;

import oracle.adf.share.config.UserCC;

import oracle.mds.core.MetadataObject;
import oracle.mds.core.RestrictedSession;

/**
 * Implementation of an additional customization layer.
 * With this Layer it is possible to define a customization
 * as default to users which themself can refine the customization further.
 * <p>
 * Make sure to include the following CC in adf-config.xml
 * <cust-config>
 * <match path="/">
 * <customization-class name="de.team.faces.mds.DefaultUserCC"/>
 * <customization-class name="oracle.adf.share.config.UserCC"/>
 * </match>
 * </cust-config>
 * and in web.xml
 * <context-param>
 * <param-name>de.team.faces.mds.PARAM_KEY_USERNAME</param-name>
 * <param-value>layout</param-value>
 * </context-param>
 *
 * </p>
 * @author Andreas Koop
 */
public class DefaultUserCC extends UserCC {

    public static final String PARAM_KEY_USERNAME = "com.dm.mds.DEFAULT_LAYOUT_USERNAME";
    public static final String DEFAULT_USERNAME = "mdsadmin";


    public DefaultUserCC() {
        super();
    }

    /**
     * Overrides the UserCC
     * @param sess
     * @param mo
     * @return
     */
    @Override
    public String[] getValue(RestrictedSession sess, MetadataObject mo) {

        String[] userCCValue = super.getValue(sess, mo);
        if (userCCValue == null) {
            return userCCValue;
        }

        FacesContext context = FacesContext.getCurrentInstance();
        if (context == null) {
            return userCCValue;
        }

        if (context.getExternalContext() == null) {
            return userCCValue;
        }

        String valueUsername = context.getExternalContext().getInitParameter(PARAM_KEY_USERNAME);
        if (valueUsername == null) {

            System.out.println("[HINT] If you use " + this.getClass().getName() + " as MDS-CustomaziationClass, ");
            System.out.println("[HINT] you can specify '" + PARAM_KEY_USERNAME +
                               "' init-parameter in web.xml to be used as the layout username, i.e. \n" +
                               "  <context-param>\n" + "    <param-name>" + PARAM_KEY_USERNAME + "</param-name>\n" +
                               "    <param-value>layout</param-value>\n" + "  </context-param>");
            System.out.println("[INFO] The user '" + DEFAULT_USERNAME + "' is currently used by convention.");
            valueUsername = DefaultUserCC.DEFAULT_USERNAME;
        }

        return new String[] { valueUsername };

    }

    /* @Override
    public String getName() {
        return "poweruser";
    }*/
}
