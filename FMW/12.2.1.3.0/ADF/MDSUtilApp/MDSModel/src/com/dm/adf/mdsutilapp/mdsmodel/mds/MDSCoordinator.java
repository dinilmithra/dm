package com.dm.adf.mdsutilapp.mdsmodel.mds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import oracle.adf.share.ADFContext;
import oracle.adf.share.security.SecurityContext;

import oracle.mds.core.IsolationLevel;
import oracle.mds.core.MDSInstance;
import oracle.mds.core.MDSSession;
import oracle.mds.core.SessionOptions;
import oracle.mds.exception.MDSException;
import oracle.mds.internal.persistence.InternalPManager;
import oracle.mds.internal.query.NameQueryImpl;
import oracle.mds.naming.DocumentName;
import oracle.mds.persistence.PContext;
import oracle.mds.persistence.PDocument;
import oracle.mds.persistence.PManager;
import oracle.mds.persistence.PTransaction;
import oracle.mds.query.ConditionFactory;
import oracle.mds.query.DocumentResult;
import oracle.mds.query.QueryResult;
import oracle.mds.transfer.MDSTransfer;
import oracle.mds.transfer.TransferUnitList;

import org.xml.sax.InputSource;


public class MDSCoordinator {
    private String fragmentName;
    private String sourceUser;

    public void clonePersonalizations() {
        ADFContext adfCtx = ADFContext.getCurrent();
        SecurityContext secCntx = adfCtx.getSecurityContext();
        String username = secCntx.getUserName();
        try {
            // retrieve the document with personalizations for the sourceUser on the current fragment
            List<String> fileToClone = queryFiles("/pages/mdssys/cust/user/" + sourceUser, getFragmentName() + ".xml");
            if (fileToClone.size() == 1) {
                try {
                    // clone the document and save it as /pages/mdssys/cust/user/<username>/<getFragmentName()>.xml");
                    String targetdocumentName =
                        "/pages/mdssys/cust/user/" + username + "/" + getFragmentName() + ".xml";
                    DocumentName docName = DocumentName.create(targetdocumentName);
                    DocumentName sourcedocName = DocumentName.create(fileToClone.get(0));
                    MDSSession mdsSession =
                        getCurrentMDSInstance()
                        .createSession(new SessionOptions(IsolationLevel.READ_COMMITTED, null, null), null);
                    PManager mdsPManager = mdsSession.getPersistenceManager();
                    PContext mdsPContext = mdsSession.getPContext();
                    InternalPManager iPManager = (InternalPManager) mdsPManager;
                    PDocument sourceDocument = iPManager.getDocument(mdsPContext, sourcedocName);
                    InputSource docContents = sourceDocument.read();
                    PDocument existingDocument = iPManager.getDocument(mdsPContext, docName);

                    PTransaction mdsTran = mdsSession.getPTransaction();
                    PDocument document =
                        existingDocument == null ? mdsTran.createDocument(docName, docContents) :
                        mdsTran.saveDocument(existingDocument, true, docContents);
                    mdsSession.flushChanges();
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                FacesContext.getCurrentInstance()
//                    .addMessage(null,
//                                new FacesMessage(FacesMessage.SEVERITY_INFO, null,
//                                                 "Copied personalizations from user " + sourceUser +
//                                                 " to the current user " + username + ". "));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void removePersonalizations() {
        ADFContext adfCtx = ADFContext.getCurrent();
        SecurityContext secCntx = adfCtx.getSecurityContext();
        String username = secCntx.getUserName();
        List<String> listOfFilesToDelete = queryFiles("/pages/mdssys/cust/user/" + username, getFragmentName() + "%");
        try {
            deleteDocuments(listOfFilesToDelete);
//            FacesContext.getCurrentInstance()
//                .addMessage(null,
//                            new FacesMessage(FacesMessage.SEVERITY_INFO, null,
//                                             "Deleted the following MDS-Documents for user " + username + ": " +
//                                             listOfFilesToDelete));
        } catch (MDSException e) {
            e.printStackTrace();
        }
    }


    /**
     * Query Files from MDS matching the given substr.
     * @return list of document file paths
     */
    public static List<String> queryFiles(final String path, final String name) {
        final MDSSession mdsSession = getCurrentMDSSession();
        final NameQueryImpl nameQuery =
            new NameQueryImpl(mdsSession, ConditionFactory.createNameCondition(path, name, true));
        final Iterator<DocumentResult> result = nameQuery.execute();
        final ArrayList<String> listOfFiles = new ArrayList<String>();
        while (result.hasNext()) {
            final QueryResult qr = result.next();
            final String absoluteName = qr.getAbsoluteName();
            listOfFiles.add(absoluteName);
        }
        return listOfFiles;
    }


    /**
     * Deletes the given file paths from MDS using the current MDS Instance
     * @param listOfFiles
     * @throws MDSException
     */
    public static void deleteDocuments(final List<String> listOfFiles) throws MDSException {
        if (listOfFiles == null || listOfFiles.isEmpty()) {
            System.out.println("listOfFiles is empty");
            return;
        }
        deleteDocuments(getCurrentMDSInstance(), listOfFiles);
    }

    public static void deleteDocuments(final MDSInstance mdsInstance,
                                       final List<String> listOfFiles) throws MDSException {

        MDSTransfer transferInstance = MDSTransfer.getInstance(mdsInstance);

        TransferUnitList transferUnitList = TransferUnitList.create(mdsInstance, listOfFiles, null, true, true);
        transferInstance.delete(transferUnitList, false); /* cancelOnException */
        transferInstance.release(mdsInstance);
    }


    public static MDSInstance getCurrentMDSInstance() {
        final MDSInstance mdsInstance = (MDSInstance) ADFContext.getCurrent().getMDSInstanceAsObject();

        return mdsInstance;
    }

    public static MDSSession getCurrentMDSSession() {
        final MDSSession mdsSession = (MDSSession) ADFContext.getCurrent().getMDSSessionAsObject();
        return mdsSession;
    }


    public void setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
    }

    public String getFragmentName() {
        return fragmentName;
    }

    public void setSourceUser(String sourceUser) {
        this.sourceUser = sourceUser;
    }

    public String getSourceUser() {
        return sourceUser;
    }
}
