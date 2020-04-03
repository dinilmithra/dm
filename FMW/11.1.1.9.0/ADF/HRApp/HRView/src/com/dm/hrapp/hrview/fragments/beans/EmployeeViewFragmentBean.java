package com.dm.hrapp.hrview.fragments.beans;


import oracle.adf.share.ADFContext;
import oracle.adf.share.logging.ADFLogger;

public class EmployeeViewFragmentBean {

    public EmployeeViewFragmentBean() {
    }

    ADFLogger logger =
        ADFLogger.createADFLogger(EmployeeViewFragmentBean.class);

    public String cb5_action() {


        String username =
            ADFContext.getCurrent().getSecurityContext().getUserName();
        logger.info("username : " + username);
        System.out.println("username : " + username);

        String[] roles =
            ADFContext.getCurrent().getSecurityContext().getUserRoles();
        for (String role : roles) {

            logger.info("role : " + role);
            System.out.println("role : " + role);

        }
        return null;
    }
}
