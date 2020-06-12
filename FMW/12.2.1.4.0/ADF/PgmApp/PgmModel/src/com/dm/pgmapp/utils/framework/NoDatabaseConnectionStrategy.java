package com.dm.pgmapp.utils.framework;
import java.util.Hashtable;

import oracle.jbo.ApplicationModule;
import oracle.jbo.client.Configuration;
import oracle.jbo.common.PropertyConstants;
import oracle.jbo.common.PropertyMetadata;
import oracle.jbo.common.ampool.DefaultConnectionStrategy;


public class NoDatabaseConnectionStrategy extends DefaultConnectionStrategy {
    @Override
    public ApplicationModule createApplicationModule(Hashtable env) {
        env.put(Configuration.DB_REQUIRES_CONNECTION,Boolean.FALSE);
        env.put(PropertyMetadata.ENV_DO_FAILOVER.pName, PropertyConstants.FALSE);
        return super.createApplicationModule(env);
    }
}