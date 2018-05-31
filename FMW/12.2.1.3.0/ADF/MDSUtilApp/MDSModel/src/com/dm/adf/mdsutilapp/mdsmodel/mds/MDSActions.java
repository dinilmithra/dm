package com.dm.adf.mdsutilapp.mdsmodel.mds;


import com.dm.adf.mdsutilapp.mdsmodel.types.MDSElement;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import java.util.ArrayList;
import java.util.Iterator;

import oracle.adf.share.ADFContext;

import oracle.jbo.JboException;

import oracle.mds.core.ConcurrentMOChangeException;
import oracle.mds.core.IsolationLevel;
import oracle.mds.core.MDSSession;
import oracle.mds.core.SessionOptions;
import oracle.mds.core.ValidationException;
import oracle.mds.exception.MDSException;
import oracle.mds.exception.UnsupportedUpdateException;
import oracle.mds.internal.persistence.InternalPManager;
import oracle.mds.internal.query.NameQueryImpl;
import oracle.mds.naming.DocumentName;
import oracle.mds.naming.InvalidReferenceException;
import oracle.mds.naming.InvalidReferenceTypeException;
import oracle.mds.persistence.ConcurrentDocChangeException;
import oracle.mds.persistence.MDSIOException;
import oracle.mds.persistence.PContext;
import oracle.mds.persistence.PDocument;
import oracle.mds.persistence.PManager;
import oracle.mds.persistence.PTransaction;
import oracle.mds.query.ConditionFactory;
import oracle.mds.query.DocumentResult;
import oracle.mds.transfer.MDSTransfer;
import oracle.mds.transfer.TransferUnitList;

import org.apache.commons.io.IOUtils;

import org.xml.sax.InputSource;


public class MDSActions {
    private ArrayList<MDSElement> mdsElements = new ArrayList();
    private String mdsPath;
    private String mdsFileName;

    public void setMdsElements(ArrayList<MDSElement> mdsElements) {
        this.mdsElements = mdsElements;
    }

    public ArrayList<MDSElement> getMdsElements() {
        return this.mdsElements;
    }

    public void constructMDSElements(String mdsPath, String mdsFileName) throws MDSException {
        if (this.mdsElements == null) {
            this.mdsElements = new ArrayList();
        }

        this.mdsElements.clear();

        if (mdsPath == null) {
            mdsPath = "/";
        }
        this.mdsPath = mdsPath;
        if (mdsFileName == null) {
            mdsFileName = "%";
        }
        this.mdsFileName = mdsFileName;

        MDSSession mdss = (MDSSession) ADFContext.getCurrent().getMDSSessionAsObject();
        String versionNumber = mdss.getSessionOptions().getVersionCreatorName();

        NameQueryImpl query = new NameQueryImpl(mdss, ConditionFactory.createNameCondition(mdsPath, mdsFileName, true));
        Iterator<DocumentResult> result = query.execute();
        int i = 0;
        while (result.hasNext()) {

            DocumentResult qr = result.next();
            DocumentName res = (DocumentName) qr.getResourceName();

            MDSElement mdsElement = new MDSElement();
            mdsElement.setId(i);
            mdsElement.setDocumentName(res.getLocalName());
            mdsElement.setDocumentPath(res.getPackageName());
            mdsElement.setAbsolutePath(res.getAbsoluteName());

            this.mdsElements.add(mdsElement);

            i++;
        }


    }

    public void deleteMDSEntries(ArrayList<String> mdsEntries) throws MDSException {
        MDSSession mdss = (MDSSession) ADFContext.getCurrent().getMDSSessionAsObject();
        MDSTransfer transferInstance = MDSTransfer.getInstance(mdss.getMDSInstance());

        TransferUnitList transferUnitList =
            TransferUnitList.create(mdss.getMDSInstance(), mdsEntries, null, true, true);
        transferInstance.delete(transferUnitList, false);
        MDSTransfer.release(mdss.getMDSInstance());

        constructMDSElements(this.mdsPath, this.mdsFileName);
    }

    public String loadMDSXml(String documentPath) throws InvalidReferenceException, InvalidReferenceTypeException,
                                                         MDSIOException {
        MDSSession mdss = (MDSSession) ADFContext.getCurrent().getMDSSessionAsObject();

        DocumentName docName = DocumentName.create(documentPath);
        MDSSession mdsSession =
            mdss.getMDSInstance().createSession(new SessionOptions(IsolationLevel.READ_COMMITTED, null, null), null);
        PManager mdsPManager = mdsSession.getPersistenceManager();
        PContext mdsContext = mdsSession.getPContext();
        PDocument mdsDocument = mdsPManager.getDocument(mdsContext, docName);

        if (mdsDocument != null) {
            InputStream input = mdsDocument.read().getByteStream();
            Reader reader = mdsDocument.read().getCharacterStream();
            try {
                if (input != null) {
                    return IOUtils.toString(input);
                }
                if (reader != null) {
                    return IOUtils.toString(reader);
                }
            } catch (IOException e) {
                throw new JboException(e);
            }
        }

        return null;
    }

    public void updateMDSXml(String documentPath, String content) throws InvalidReferenceException,
                                                                         InvalidReferenceTypeException, MDSIOException,
                                                                         ConcurrentDocChangeException,
                                                                         UnsupportedUpdateException,
                                                                         ConcurrentMOChangeException,
                                                                         ValidationException {
        MDSSession mdss = (MDSSession) ADFContext.getCurrent().getMDSSessionAsObject();

        DocumentName docName = DocumentName.create(documentPath);
        MDSSession mdsSession =
            mdss.getMDSInstance().createSession(new SessionOptions(IsolationLevel.READ_COMMITTED, null, null), null);
        PManager mdsPManager = mdsSession.getPersistenceManager();

        InputSource docContents = new InputSource(IOUtils.toInputStream(content));

        PContext mdsPContext = mdsSession.getPContext();
        InternalPManager iPManager = (InternalPManager) mdsPManager;
        PDocument existingDocument = iPManager.getDocument(mdsPContext, docName);
        PTransaction mdsTran = mdsSession.getPTransaction();
        mdsTran.saveDocument(existingDocument, true, docContents);
        mdsSession.flushChanges();


    }

    public void setMdsPath(String mdsPath) {
        this.mdsPath = mdsPath;
    }

    public String getMdsPath() {
        return this.mdsPath;
    }

    public void setMdsFileName(String mdsFileName) {
        this.mdsFileName = mdsFileName;
    }

    public String getMdsFileName() {
        return this.mdsFileName;
    }
}
