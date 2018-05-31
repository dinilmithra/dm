package com.dm.adfcommon.vo;

import java.util.logging.Level;

import oracle.adf.share.logging.ADFLogger;

import oracle.jbo.Variable;
import oracle.jbo.VariableValueManager;
import oracle.jbo.server.ViewObjectImpl;

public class ComViewObjectImpl extends ViewObjectImpl {

    private static ADFLogger _logger = ADFLogger.createADFLogger(ComViewObjectImpl.class);

    @Override
    protected void executeQueryForCollection(Object object, Object[] object2, int i) {

        if (_logger.isLoggable(Level.INFO)) {
            dumpQueryAndParameters();
        }


        super.executeQueryForCollection(object, object2, i);
    }

    public void dumpQueryAndParameters() {
        // get the query in it's current state

        String lQuery = getQuery();
        //get Valriables
        VariableValueManager lEnsureVariableManager = ensureVariableManager();
        Variable[] lVariables = lEnsureVariableManager.getVariables();
        int lCount = lEnsureVariableManager.getVariableCount();
        
        _logger.info("View Object: " + getName());
        _logger.info("---QUERY--- ");
        _logger.info(lQuery);
        // if variables found dump them
        if (lCount > 0) {
            _logger.info("---Variables:");
            for (int ii = 0; ii < lCount; ii++) {
                Object lObject = lEnsureVariableManager.getVariableValue(lVariables[ii]);
                _logger.info(lVariables[ii].getName() + " : " + (lObject != null ? lObject : "null"));
            }
        }
    }
}
