package com.dm.pgmapp.utils.framework;
import oracle.jbo.common.ampool.ApplicationPoolImpl;

public class NoDatabaseApplicationPoolImpl extends ApplicationPoolImpl {
    public NoDatabaseApplicationPoolImpl() {
        super();
    }

    @Override
    public boolean isSupportsPassivation() {
        return false;
    }
}
