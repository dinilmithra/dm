package com.dm.menudemoapp.mdmodel.demo.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import oracle.jbo.DataFilter;
import oracle.jbo.RangePagingDataFilter;
import oracle.jbo.ScrollableDataFilter;
import oracle.jbo.server.ProgrammaticViewObjectImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Tue Feb 26 10:50:05 EST 2019
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class ClaimsMasterViewImpl extends ProgrammaticViewObjectImpl {
    /**
     * This is the default constructor (do not remove).
     */
    public ClaimsMasterViewImpl() {
    }

    private Map dataRow(Integer id, String type) {

        Map ret = new HashMap();
        ret.put("Id", id);
        ret.put("CcType", type);
        return ret;

    }

    private Collection<Object> getData(DataFilter filter) {

        Collection<Object> rows = new ArrayList<Object>();
        rows.add(dataRow(0, null));

        return rows;

    }


    @Override
    protected Collection<Object> getScrollableData(ScrollableDataFilter scrollableDataFilter) {
        //        return super.getScrollableData(scrollableDataFilter);
        return getData(scrollableDataFilter);
    }

    @Override
    protected Collection<Object> getRangePagingData(RangePagingDataFilter rangePagingDataFilter) {
        //        return super.getRangePagingData(rangePagingDataFilter);
        return getData(rangePagingDataFilter);
    }
}
