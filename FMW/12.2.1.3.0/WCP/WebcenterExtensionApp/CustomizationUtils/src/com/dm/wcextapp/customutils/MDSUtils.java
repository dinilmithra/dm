package com.dm.wcextapp.customutils;


import com.dm.wcextapp.customutils.user.DefaultUserCC;

import java.io.File;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;

import oracle.adf.share.ADFConfigEx;
import oracle.adf.share.ADFContext;
import oracle.adf.share.logging.ADFLogger;

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
import oracle.mds.transfer.TransferException;
import oracle.mds.transfer.TransferUnitList;


/**
 * Oracle MDS Utilities
 *
 * @author Andreas Koop
 * @date 29.12.2011
 */
public abstract class MDSUtils {
    private static ADFLogger _logger = ADFLogger.createADFLogger(MDSUtils.class);

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
            _logger.severe("listOfFiles is empty");
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

        try {
            MDSTransfer transferInstance = MDSTransfer.getInstance(mdsInstance);

            TransferUnitList transferUnitList = TransferUnitList.create(mdsInstance, listOfFiles, null, true, true);
            transferInstance.delete(transferUnitList, false /* cancelOnException */);
            transferInstance.release(mdsInstance);
            
        } catch (MDSIOException mdsioe) {
            // TODO: Add catch code
            mdsioe.printStackTrace();
        } catch (TransferException te) {
            // TODO: Add catch code
            te.printStackTrace();
        } catch (MDSException mdse) {
            // TODO: Add catch code
            mdse.printStackTrace();
        }
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
        
        _logger.severe("Inside outputDebugInfo");
        
        final ADFConfigEx adfConfig = (ADFConfigEx) ADFContext.getCurrent().getADFConfig();
        final MDSInstance mdsInstance = (MDSInstance) ADFContext.getCurrent().getMDSInstanceAsObject();
        final MDSSession mdsSession = (MDSSession) ADFContext.getCurrent().getMDSSessionAsObject();

        final MDSConfig mdsConfig = mdsInstance.getMDSConfig();

        _logger.severe("MDS Instance: " + mdsInstance + " of type " + mdsInstance.getClass().getName());
        _logger.severe("MDS Session:  " + mdsSession + " of type " + mdsSession.getClass().getName());

        final Object mdsInstanceFromConfig = adfConfig.getMDSInstance();
        _logger.severe("MDS Instance == adfconfig.getMDSInstance => " + (mdsInstanceFromConfig == mdsInstance));

        final CustConfig mdsCustConfig = mdsConfig.getCustConfig();

        final TypeConfig mdsTypeConfig = mdsConfig.getTypeConfig();
        final CacheConfig mdsCacheConfig = mdsConfig.getCacheConfig();
        final FileTypeConfig mdsFileTypeConfig = mdsConfig.getFileTypeConfig();
        final PConfig mdsPConfig = mdsConfig.getPConfig();

        final List<NamespaceConfig> mdsNsConfigList = (List<NamespaceConfig>) mdsPConfig.getDefaultNamespaceConfig();
        _logger.severe("MDS Namespace Config size: " + mdsNsConfigList.size());

        for (int i = 0; i < mdsNsConfigList.size(); i++) {
            _logger.severe("---------------------------------------");
            final NamespaceConfig mdsNsConfig = mdsNsConfigList.get(i);
            final Namespace mdsNamespace = mdsNsConfig.getNamespace();


            // Check Metadatastore
            final MetadataStore mdsStoreObject = mdsNsConfig.getMetadataStore();
            _logger.severe("MDS Metadata Store : " + mdsStoreObject + " typeof " + mdsStoreObject.getClass().getName());

            // MDS FileMetadataStore
            if (mdsStoreObject instanceof FileMetadataStore) {
                final FileMetadataStore mdsStore = (FileMetadataStore) mdsNsConfig.getMetadataStore();
                final File[] mdsStorePaths = mdsStore.getMetadataPath();

                _logger.severe("MDS Store: appname                 " + mdsStore.getApplicationName());
                _logger.severe("MDS Store repository name:         " + mdsStore.getRepositoryName());
                _logger.severe("MDS Store partition name:          " + mdsStore.getPartitionName());
                _logger.severe("MDS Store shared app name:         " + mdsStore.getSharedAppName());
                _logger.severe("MDS Store last modified precision: " + mdsStore.getLastModifiedPrecision());

                for (File mdsPath : mdsStorePaths) {
                    _logger.severe("MDS Store path:                    " + mdsPath.getAbsolutePath());
                }
                _logger.severe("MDS Store PManager:                " + mdsStore.getPManager());

                try {
                    _logger.severe("MDS Store FileConnection:          " + mdsStore.getConnection());
                } catch (MDSIOException e) {
                    _logger.severe(e);
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
                _logger.severe("MDS Store Metadatapath: " + metadataPath);
            }
        }
        _logger.severe("---------------------------------------");
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
