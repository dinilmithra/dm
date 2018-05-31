package com.dm.customizationapp.customizationview.custlayers;

import oracle.mds.core.MetadataObject;
import oracle.mds.core.RestrictedSession;
import oracle.mds.cust.CacheHint;
import oracle.mds.cust.CustomizationClass;

public class WCSiteCC extends CustomizationClass {
    public WCSiteCC() {
        super();
    }

    @Override
    public CacheHint getCacheHint() {
        // TODO Implement this method
        return null;
    }

    @Override
    public String getName() {
        // TODO Implement this method
        return "site";
    }

    @Override
    public String[] getValue(RestrictedSession mdsSession, MetadataObject mo) {
        // TODO Implement this method
        return new String[] { "webcenter" };
    }
}
