package com.dm.pgmapp.pgmmodel.views;

import oracle.jbo.server.ProgrammaticViewRowImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Sun Apr 19 23:28:59 EDT 2020
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class EmployeesViewRowImpl extends ProgrammaticViewRowImpl {


    public static final int ENTITY_EMPLOYEE = 0;

    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. DO NOT MODIFY.
     */
    protected enum AttributesEnum {
        Id {
            protected Object get(EmployeesViewRowImpl obj) {
                return obj.getAttributeInternal(index());
            }

            protected void put(EmployeesViewRowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        FirstName {
            protected Object get(EmployeesViewRowImpl obj) {
                return obj.getAttributeInternal(index());
            }

            protected void put(EmployeesViewRowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        DepartmentId {
            protected Object get(EmployeesViewRowImpl obj) {
                return obj.getAttributeInternal(index());
            }

            protected void put(EmployeesViewRowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        LastName {
            protected Object get(EmployeesViewRowImpl obj) {
                return obj.getAttributeInternal(index());
            }

            protected void put(EmployeesViewRowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        DepartmentsView {
            protected Object get(EmployeesViewRowImpl obj) {
                return obj.getAttributeInternal(index());
            }

            protected void put(EmployeesViewRowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ;
        private static AttributesEnum[] vals = null;
        ;
        private static final int firstIndex = 0;


        protected int index() {
            return AttributesEnum.firstIndex() + ordinal();
        }

        protected static final int firstIndex() {
            return firstIndex;
        }

        protected static int count() {
            return AttributesEnum.firstIndex() + AttributesEnum.staticValues().length;
        }

        protected static final AttributesEnum[] staticValues() {
            if (vals == null) {
                vals = AttributesEnum.values();
            }
            return vals;
        }


        protected abstract Object get(EmployeesViewRowImpl object);

        protected abstract void put(EmployeesViewRowImpl object, Object value);
    }


    public static final int ID = AttributesEnum.Id.index();
    public static final int FIRSTNAME = AttributesEnum.FirstName.index();
    public static final int DEPARTMENTID = AttributesEnum.DepartmentId.index();
    public static final int LASTNAME = AttributesEnum.LastName.index();
    public static final int DEPARTMENTSVIEW = AttributesEnum.DepartmentsView.index();

    /**
     * This is the default constructor (do not remove).
     */
    public EmployeesViewRowImpl() {
    }
}

