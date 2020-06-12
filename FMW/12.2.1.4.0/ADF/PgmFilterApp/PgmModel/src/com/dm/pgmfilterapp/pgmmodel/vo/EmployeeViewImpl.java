package com.dm.pgmfilterapp.pgmmodel.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import oracle.jbo.Key;
import oracle.jbo.RangePagingDataFilter;
import oracle.jbo.RowContext;
import oracle.jbo.ScrollableDataFilter;
import oracle.jbo.server.ProgrammaticViewObjectImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Tue Apr 14 22:12:01 EDT 2020
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class EmployeeViewImpl extends ProgrammaticViewObjectImpl {
    /**
     * This is the default constructor (do not remove).
     */
    public EmployeeViewImpl() {
    }

    private Collection<Object> populateData() {

        Collection<Object> rows = new ArrayList<Object>();

        HashMap<String, Object> row1 = new HashMap<String, Object>();
        row1.put("Id", 1);
        row1.put("FirstName", "Dinil");
        row1.put("LastName", "Mithra");
        row1.put("DeptId", 3);
        rows.add(row1);

        HashMap<String, Object> row2 = new HashMap<String, Object>();
        row2.put("Id", 2);
        row2.put("FirstName", "Krish");
        row2.put("LastName", "Menon");
        row2.put("DeptId", 2);
        rows.add(row2);

        HashMap<String, Object> row3 = new HashMap<String, Object>();
        row3.put("Id", 3);
        row3.put("FirstName", "Mathew");
        row3.put("LastName", "Jordan");
        row3.put("DeptId", 3);
        rows.add(row3);

        HashMap<String, Object> row4 = new HashMap<String, Object>();
        row4.put("Id", 4);
        row4.put("FirstName", "Shawn");
        row4.put("LastName", "Mendes");
        row4.put("DeptId", 2);
        rows.add(row4);

        HashMap<String, Object> row5 = new HashMap<String, Object>();
        row5.put("Id", 5);
        row5.put("FirstName", "Jorden");
        row5.put("LastName", "Hopkin");
        row5.put("DeptId", 3);
        rows.add(row5);

        HashMap<String, Object> row6 = new HashMap<String, Object>();
        row6.put("Id", 6);
        row6.put("FirstName", "Ryan");
        row6.put("LastName", "Muller");
        row6.put("DeptId", 1);
        rows.add(row6);

        return rows;

    }

    /**
     * getRangePagingData - for custom java data source support.
     */
    public Collection<Object> getRangePagingData(RangePagingDataFilter filter) {
        Collection<Object> value = super.getRangePagingData(filter);
        return value;
    }

    /**
     * retrieveDataByKey - for custom java data source support.
     */
    public Collection<Object> retrieveDataByKey(Key key, int size) {
        Collection<Object> value = super.retrieveDataByKey(key, size);
        return value;
    }

    /**
     * getScrollableData - for custom java data source support.
     */
    public Collection<Object> getScrollableData(ScrollableDataFilter filter) {
        //        Collection<Object> value = super.getScrollableData(filter);

        RowContext ctx = filter.getRowContext();
        String masterAccessorName = ctx.getMasterAccessorName();
        Map masterRowData = (Map) ctx.getMasterRowDataProvider();

        Collection<Object> value = populateData();
        return value;
    }
}

