package com.dm.wcextapp.customutils.user;

import javax.faces.context.FacesContext;

import oracle.adf.share.config.UserCC;
import oracle.adf.share.logging.ADFLogger;

import oracle.mds.core.MetadataObject;
import oracle.mds.core.RestrictedSession;

public class DefaultUserCC extends UserCC {


    private static ADFLogger _logger = ADFLogger.createADFLogger(DefaultUserCC.class);

    public static final String PARAM_KEY_USERNAME = "com.dm.mds.DEFAULT_LAYOUT_USERNAME";
    public static final String DEFAULT_USERNAME = "mdsadmin";


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

            _logger.severe("[HINT] If you use " + this.getClass().getName() + " as MDS-CustomaziationClass, ");
            _logger.severe("[HINT] you can specify '" + PARAM_KEY_USERNAME +
                           "' init-parameter in web.xml to be used as the layout username, i.e. \n" +
                           "  <context-param>\n" + "    <param-name>" + PARAM_KEY_USERNAME + "</param-name>\n" +
                           "    <param-value>layout</param-value>\n" + "  </context-param>");
            _logger.severe("[INFO] The user '" + DEFAULT_USERNAME + "' is currently used by convention.");
            valueUsername = DefaultUserCC.DEFAULT_USERNAME;
        }

        return new String[] { valueUsername };

    }
}
