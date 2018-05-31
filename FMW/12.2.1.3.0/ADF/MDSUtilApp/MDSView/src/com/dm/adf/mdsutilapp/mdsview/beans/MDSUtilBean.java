package com.dm.adf.mdsutilapp.mdsview.beans;

import com.dm.adf.mdsutilapp.mdsview.utils.ADFUtils;
import com.dm.adf.mdsutilapp.mdsview.utils.JSFUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.event.DialogEvent;
import oracle.adf.view.rich.event.PopupFetchEvent;

import oracle.binding.OperationBinding;

import oracle.jbo.uicli.binding.JUCtrlHierBinding;
import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;

import org.apache.myfaces.trinidad.model.CollectionModel;
import org.apache.myfaces.trinidad.model.RowKeySet;

public class MDSUtilBean {


    private RichTable mdsElements;
    private RichInputText sourceXml;

    public void mdsSearchActionListener(ActionEvent actionEvent) {
        OperationBinding ob = ADFUtils.findOperation("constructMDSElements");
        ob.execute();
        if (ob.getErrors().size() == 0) {
            ADFUtils.findIterator("mdsElementsIterator").executeQuery();
        }
    }

    public void mdsDeleteDocDialogListener(DialogEvent dialogEvent) {
        JUCtrlHierBinding mdsElementsTable =
            (JUCtrlHierBinding) ((CollectionModel) getMdsElements().getValue()).getWrappedData();
        RowKeySet rks = getMdsElements().getSelectedRowKeys();
        Iterator itr = rks.iterator();

        if (rks.size() > 0) {

            ArrayList<String> mdsEntries = new ArrayList(rks.size());
            while (itr.hasNext()) {
                List key = (List) itr.next();
                JUCtrlHierNodeBinding row = mdsElementsTable.findNodeByKeyPath(key);
                mdsEntries.add((String) row.getAttribute("absolutePath"));
            }

            OperationBinding ob = ADFUtils.findOperation("deleteMDSEntries");
            ob.getParamsMap().put("mdsEntries", mdsEntries);
            ob.execute();

            if (ob.getErrors().size() == 0) {
                FacesContext fctx = FacesContext.getCurrentInstance();
                fctx.addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_INFO, null,
                                                 "Deleted MDS documents count: " + mdsEntries.size()));

                ADFUtils.findIterator("mdsElementsIterator").executeQuery();
            }
        }
    }

    public void loadXmlPopUpFetchListener(PopupFetchEvent popupFetchEvent) {
        JUCtrlHierBinding mdsElementsTable =
            (JUCtrlHierBinding) ((CollectionModel) getMdsElements().getValue()).getWrappedData();
        RowKeySet rks = getMdsElements().getSelectedRowKeys();
        Iterator itr = rks.iterator();

        if (rks.size() > 0) {
            List key = (List) itr.next();
            JUCtrlHierNodeBinding row = mdsElementsTable.findNodeByKeyPath(key);
            String documentPath = (String) row.getAttribute("absolutePath");

            OperationBinding ob = ADFUtils.findOperation("loadMDSXml");
            ob.getParamsMap().put("documentPath", documentPath);
            String mdsXml = (String) ob.execute();

            getSourceXml().setValue(mdsXml);
            JSFUtils.setManagedBeanValue("requestScope.sourceXMLTitle", documentPath);
        } else {
            getSourceXml().setValue(null);
        }
    }

    public void updateXmlDialogListener(DialogEvent dialogEvent) {

        JUCtrlHierBinding mdsElementsTable =
            (JUCtrlHierBinding) ((CollectionModel) getMdsElements().getValue()).getWrappedData();
        RowKeySet rks = getMdsElements().getSelectedRowKeys();
        Iterator itr = rks.iterator();

        if (rks.size() > 0) {
            List key = (List) itr.next();
            JUCtrlHierNodeBinding row = mdsElementsTable.findNodeByKeyPath(key);
            String documentPath = (String) row.getAttribute("documentPath");

            OperationBinding ob = ADFUtils.findOperation("updateMDSXml");
            ob.getParamsMap().put("documentPath", documentPath);
            ob.getParamsMap().put("content", getSourceXml().getValue());
            ob.execute();

            if (ob.getErrors().size() == 0) {
                FacesContext fctx = FacesContext.getCurrentInstance();
                fctx.addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_INFO, null,
                                                 "Updated MDS document: " + documentPath));
            }
        }
    }

    public void setMdsElements(RichTable mdsElements) {
        this.mdsElements = mdsElements;
    }

    public RichTable getMdsElements() {
        return this.mdsElements;
    }

    public void setSourceXml(RichInputText sourceXml) {
        this.sourceXml = sourceXml;
    }

    public RichInputText getSourceXml() {
        return this.sourceXml;
    }
}
