/*
 * Copyright (c) 2011 TEAM GmbH and/or its affiliates.
 * All rights reserved.
 *
 */
package de.team.faces.mds;

import java.io.File;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;

import oracle.adf.share.ADFConfigEx;
import oracle.adf.share.ADFContext;

import oracle.mds.config.CacheConfig;
import oracle.mds.config.CustConfig;
import oracle.mds.config.FileTypeConfig;
import oracle.mds.config.MDSConfig;
import oracle.mds.config.NamespaceConfig;
import oracle.mds.config.PConfig;
import oracle.mds.config.TypeConfig;
import oracle.mds.core.MDSInstance;
import oracle.mds.core.MDSSession;
import oracle.mds.exception.MDSException;
import oracle.mds.internal.persistence.file.DefaultMetadataStore;
import oracle.mds.internal.persistence.file.URLMetadataStore;
import oracle.mds.internal.query.NameQueryImpl;
import oracle.mds.naming.Namespace;
import oracle.mds.persistence.MDSIOException;
import oracle.mds.persistence.MetadataStore;
import oracle.mds.persistence.PManager;
import oracle.mds.persistence.stores.file.FileMetadataStore;
import oracle.mds.query.ConditionFactory;
import oracle.mds.query.DocumentResult;
import oracle.mds.query.QueryResult;
import oracle.mds.transfer.MDSTransfer;
import oracle.mds.transfer.TransferUnitList;


/**
 * Oracle MDS Utilities
 *
 * @author Andreas Koop
 * @date 29.12.2011
 */
public abstract class MDSUtils {
    public MDSUtils() {
        super();
    }

    /**
     * Returns the file path to the meta data store
     * @return
     */
    public static String tryMetadataPath() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     *
     * @return
     */
    public static MDSInstance getCurrentMDSInstance() {
        final MDSInstance mdsInstance = (MDSInstance) ADFContext.getCurrent().getMDSInstanceAsObject();

        return mdsInstance;
    }

    /**
     *
     * @return
     */
    public static MDSSession getCurrentMDSSession() {
        final MDSSession mdsSession = (MDSSession) ADFContext.getCurrent().getMDSSessionAsObject();
        return mdsSession;
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

        MDSUtils.deleteDocuments(MDSUtils.getCurrentMDSInstance(), listOfFiles);
    }

    /**
     * Deletes the given file paths from MDS
     *
     * @param mdsInstance
     * @param listOfFiles
     * @throws MDSException
     */
    public static void deleteDocuments(final MDSInstance mdsInstance,
                                       final List<String> listOfFiles) throws MDSException {

        MDSTransfer transferInstance = MDSTransfer.getInstance(mdsInstance);

        TransferUnitList transferUnitList = TransferUnitList.create(mdsInstance, listOfFiles, null, true, true);
        transferInstance.delete(transferUnitList, false /* cancelOnException */);
        transferInstance.release(mdsInstance);
    }

    /**
     * Query ALL Files from MDS
     * @return list of document file paths
     */
    public static List<String> queryAllFiles() {
        final MDSSession mdsSession = MDSUtils.getCurrentMDSSession();

        //String packagePath, String documentName, boolean recursive,
        final NameQueryImpl nameQuery =
            new NameQueryImpl(mdsSession, ConditionFactory.createNameCondition("/", "%", true));
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
     * Query Files from MDS matching all fiel paths except the given substr.
     * @return list of document file paths
     */
    public static List<String> queryFilesExcept(final String exceptSubstr) {
        final MDSSession mdsSession = MDSUtils.getCurrentMDSSession();

        //String packagePath, String documentName, boolean recursive,
        final NameQueryImpl nameQuery =
            new NameQueryImpl(mdsSession, ConditionFactory.createNameCondition("/", "%", true));
        final Iterator<DocumentResult> result = nameQuery.execute();
        final ArrayList<String> listOfFiles = new ArrayList<String>();
        while (result.hasNext()) {
            final QueryResult qr = result.next();
            final String absoluteName = qr.getAbsoluteName();

            if (absoluteName.indexOf(exceptSubstr) > -1) {
                continue;
            }
            listOfFiles.add(absoluteName);
        }

        return listOfFiles;
    }

    /**
     * Query Files from MDS matching the given substr.
     * @return list of document file paths
     */
    public static List<String> queryFilesMatch(final String matchSubstr) {
        final MDSSession mdsSession = MDSUtils.getCurrentMDSSession();

        //String packagePath, String documentName, boolean recursive,
        final NameQueryImpl nameQuery =
            new NameQueryImpl(mdsSession, ConditionFactory.createNameCondition("/", "%", true));
        final Iterator<DocumentResult> result = nameQuery.execute();
        final ArrayList<String> listOfFiles = new ArrayList<String>();
        while (result.hasNext()) {
            final QueryResult qr = result.next();
            final String absoluteName = qr.getAbsoluteName();

            if (absoluteName.indexOf(matchSubstr) < 0) {
                continue;
            }
            listOfFiles.add(absoluteName);
        }

        return listOfFiles;
    }

    /**
     *
     */
    public static void outputDebugInfo() {
        final ADFConfigEx adfConfig = (ADFConfigEx) ADFContext.getCurrent().getADFConfig();
        final MDSInstance mdsInstance = (MDSInstance) ADFContext.getCurrent().getMDSInstanceAsObject();
        final MDSSession mdsSession = (MDSSession) ADFContext.getCurrent().getMDSSessionAsObject();

        final MDSConfig mdsConfig = mdsInstance.getMDSConfig();

        System.out.println("MDS Instance: " + mdsInstance + " of type " + mdsInstance.getClass().getName());
        System.out.println("MDS Session:  " + mdsSession + " of type " + mdsSession.getClass().getName());

        final Object mdsInstanceFromConfig = adfConfig.getMDSInstance();
        System.out.println("MDS Instance == adfconfig.getMDSInstance => " + (mdsInstanceFromConfig == mdsInstance));

        final CustConfig mdsCustConfig = mdsConfig.getCustConfig();

        final TypeConfig mdsTypeConfig = mdsConfig.getTypeConfig();
        final CacheConfig mdsCacheConfig = mdsConfig.getCacheConfig();
        final FileTypeConfig mdsFileTypeConfig = mdsConfig.getFileTypeConfig();
        final PConfig mdsPConfig = mdsConfig.getPConfig();

        final List<NamespaceConfig> mdsNsConfigList = (List<NamespaceConfig>) mdsPConfig.getDefaultNamespaceConfig();
        System.out.println("MDS Namespace Config size: " + mdsNsConfigList.size());

        for (int i = 0; i < mdsNsConfigList.size(); i++) {
            System.out.println("---------------------------------------");
            final NamespaceConfig mdsNsConfig = mdsNsConfigList.get(i);
            final Namespace mdsNamespace = mdsNsConfig.getNamespace();


            // Check Metadatastore
            final MetadataStore mdsStoreObject = mdsNsConfig.getMetadataStore();
            System.out.println("MDS Metadata Store : " + mdsStoreObject + " typeof " +
                               mdsStoreObject.getClass().getName());

            // MDS FileMetadataStore
            if (mdsStoreObject instanceof FileMetadataStore) {
                final FileMetadataStore mdsStore = (FileMetadataStore) mdsNsConfig.getMetadataStore();
                final File[] mdsStorePaths = mdsStore.getMetadataPath();

                System.out.println("MDS Store: appname                 " + mdsStore.getApplicationName());
                System.out.println("MDS Store repository name:         " + mdsStore.getRepositoryName());
                System.out.println("MDS Store partition name:          " + mdsStore.getPartitionName());
                System.out.println("MDS Store shared app name:         " + mdsStore.getSharedAppName());
                System.out.println("MDS Store last modified precision: " + mdsStore.getLastModifiedPrecision());

                for (File mdsPath : mdsStorePaths) {
                    System.out.println("MDS Store path:                    " + mdsPath.getAbsolutePath());
                }
                System.out.println("MDS Store PManager:                " + mdsStore.getPManager());

                try {
                    System.out.println("MDS Store FileConnection:          " + mdsStore.getConnection());
                } catch (MDSIOException e) {
                    System.out.println(e);
                }
            } else if (mdsStoreObject instanceof DefaultMetadataStore) {
                final DefaultMetadataStore mdsStore = (DefaultMetadataStore) mdsNsConfig.getMetadataStore();
                //MetadataStoreConnection mdsConnection = mdsStore.getConnection();
                PManager manager = mdsStore.getPManager();

                //PContext context = manager.createPContext();
                //PConfig pconfig = manager.getPConfig();

            } else if (mdsStoreObject instanceof URLMetadataStore) {

                final URLMetadataStore mdsStore = (URLMetadataStore) mdsNsConfig.getMetadataStore();
                final String metadataPath = (String) mdsStore.getProperty("write-dir");
                System.out.println("MDS Store Metadatapath: " + metadataPath);
            }
        }
        System.out.println("---------------------------------------");
    }

    /**
     * Returns the username used for definining a default user layout
     * in ADF Applications.
     * @return
     */
    public static String getMDSLayoutUsername() {
        String mdsDefaultLayoutUsername = FacesContext.getCurrentInstance()
                                                      .getExternalContext()
                                                      .getInitParameter(DefaultUserCC.PARAM_KEY_USERNAME);

        if (mdsDefaultLayoutUsername == null || mdsDefaultLayoutUsername.trim().isEmpty()) {
            mdsDefaultLayoutUsername = DefaultUserCC.DEFAULT_USERNAME;
        }

        return mdsDefaultLayoutUsername;
    }
}
