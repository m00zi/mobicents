package org.openxdm.xcap.server.slee;

import java.io.ByteArrayInputStream;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.ActivityContextInterface;
import javax.slee.ActivityEndEvent;
import javax.slee.ChildRelation;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.serviceactivity.ServiceActivity;
import javax.slee.serviceactivity.ServiceActivityFactory;

import org.apache.log4j.Logger;
import org.openxdm.xcap.common.appusage.AppUsage;
import org.openxdm.xcap.common.appusage.AppUsageFactory;
import org.openxdm.xcap.common.error.InternalServerErrorException;
import org.openxdm.xcap.common.uri.ResourceSelector;
import org.openxdm.xcap.server.slee.resource.appusagecache.AppUsageCacheResourceAdaptorSbbInterface;
import org.openxdm.xcap.server.slee.resource.datasource.DataSourceSbbInterface;

public abstract class AbstractAppUsageSbb implements javax.slee.Sbb {

	private SbbContext sbbContext = null; // This SBB's context

	private Context myEnv = null; // This SBB's environment

	private AppUsageCacheResourceAdaptorSbbInterface appUsageCache = null;

	private Logger logger = getLogger();

	protected DataSourceSbbInterface dataSource = null;
	
	protected DataSourceSbbInterface getDataSource() {
		return dataSource;
	}
	private void logAsInfo(String msg) {
		logger.info(msg);
	}

	private void logAsDebug(String msg) {		
		logger.debug(msg);	
	}

	private boolean isLogDebugEnabled() {
		if (logger.isDebugEnabled()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private void logAsError(String msg, Exception e) {
		logger.error(msg, e);
	}

	/**
	 * Called when an sbb object is instantied and enters the pooled state.
	 */
	public void setSbbContext(SbbContext context) {
		if (isLogDebugEnabled()) logAsDebug("setSbbContext(context=" + context.toString() + ")");
		this.sbbContext = context;
		try {
			myEnv = (Context) new InitialContext().lookup("java:comp/env");
			appUsageCache = (AppUsageCacheResourceAdaptorSbbInterface) myEnv
					.lookup("slee/resources/openxdm/appusagecache/sbbrainterface");
			dataSource = (DataSourceSbbInterface) myEnv.lookup("slee/resources/openxdm/datasource/sbbrainterface");

		} catch (NamingException e) {
			logAsError("Can't set sbb context.", e);
		}
	}

	public void unsetSbbContext() {
		if (isLogDebugEnabled()) logAsDebug("unsetSbbContext()");
		this.sbbContext = null;
	}

	public void sbbCreate() throws javax.slee.CreateException {
		if (isLogDebugEnabled()) logAsDebug("sbbCreate()");
	}

	public void sbbPostCreate() throws javax.slee.CreateException {
		if (isLogDebugEnabled()) logAsDebug("sbbPostCreate()");
	}

	public void sbbActivate() {
		if (isLogDebugEnabled()) logAsDebug("sbbActivate()");
	}

	public void sbbPassivate() {
		if (isLogDebugEnabled()) logAsDebug("sbbPassivate()");
	}

	public void sbbRemove() {
		if (isLogDebugEnabled()) logAsDebug("sbbRemove()");
	}

	public void sbbLoad() {
		if (isLogDebugEnabled()) logAsDebug("sbbLoad()");
	}

	public void sbbStore() {
		if (isLogDebugEnabled()) logAsDebug("sbbStore()");
	}

	public void sbbExceptionThrown(Exception exception, Object event,
			ActivityContextInterface activity) {
		if (isLogDebugEnabled()) logAsDebug("sbbExceptionThrown(exception=" + exception.toString()
				+ ",event=" + event.toString() + ",activity="
				+ activity.toString() + ")");
	}

	public void sbbRolledBack(RolledBackContext sbbRolledBack) {
		if (isLogDebugEnabled()) logAsDebug("sbbRolledBack(sbbRolledBack=" + sbbRolledBack.toString()
				+ ")");
	}

	protected SbbContext getSbbContext() {
		return sbbContext;
	}

	// CHILD RELATIONS & RA ABSTRACTIONS
	// ################################################################

	public abstract ChildRelation getRequestProcessorChildRelation();

	protected RequestProcessorSbbLocalObject getRequestProcessor()
			throws InternalServerErrorException {
		// get the child relation
		ChildRelation childRelation = getRequestProcessorChildRelation();
		// creates the child sbb if does not exist
		if (childRelation.isEmpty()) {
			try {
				return (RequestProcessorSbbLocalObject) childRelation.create();
			} catch (Exception e) {
				logAsError("unable to create the child sbb.", e);
				throw new InternalServerErrorException("");
			}
		}
		else {
			// return the child sbb
			return (RequestProcessorSbbLocalObject) childRelation.iterator().next();
		}
	}

	private void putAppUsageInCache() {		
		try {
			appUsageCache.put(getAppUsageFactory());
		} catch (InternalServerErrorException e) {
			logAsError("Unable to put app usage in cache", e);
		}		
	}

	private AppUsage borrowAppUsageFromCache(String auid)
			throws InternalServerErrorException {		
		try {
			// borrow app usage from cache
			return appUsageCache.borrow(auid);						
		} catch (Exception e) {
			logAsError("Unable to borrow app usage instance from cache", e);
			throw new InternalServerErrorException(
					"Unable to borrow app usage instance from cache");
		}		
	}

	private void releaseAppUsageToCache(AppUsage appUsage) {
		appUsageCache.release(appUsage);
	}

	private void removeAppUsageFromCache() {
		appUsageCache.remove(getAUID());
	}

	// ABSTRACT METHODS TO BE IMPLEMENTED
	// #########################################################

	public abstract Logger getLogger();

	public abstract String getAUID();

	public abstract AppUsageFactory getAppUsageFactory()
			throws InternalServerErrorException;

    // XCAP CAPS UPDATE
	
	private void updateXCAPCapsGlobalDoc() throws InternalServerErrorException {
		
		// we can't use the xcap caps app usage class, may not be loaded
		final String xcapCapsAUID = "xcap-caps";
		final String xcapCapsMimetype = "application/xcap-caps+xml";

		if (dataSource.containsAppUsage(xcapCapsAUID)) {
			// create xcap-caps global/index doc
			StringBuilder sb1 = new StringBuilder("<?xml version='1.0' encoding='UTF-8'?><xcap-caps xmlns='urn:ietf:params:xml:ns:xcap-caps'><auids>");
			StringBuilder sb2 = new StringBuilder("</auids><extensions/><namespaces>");
			AppUsage xcapCapsAppUsage = null;
			for(String auid:dataSource.getAppUsages()) {
				// borrow one app usage object from cache 
				AppUsage appUsage = borrowAppUsageFromCache(auid);
				// add auid and namespace
				if(appUsage != null) {
					sb1.append("<auid>").append(appUsage.getAUID()).append("</auid>");
					sb2.append("<namespace>").append(appUsage.getDefaultDocumentNamespace()).append("</namespace>");
					if (auid.equals(xcapCapsAUID)) {
						xcapCapsAppUsage = appUsage;
					}
					else {
						// release app usage object
						releaseAppUsageToCache(appUsage);
					}
				}
			}
			sb1.append(sb2).append("</namespaces></xcap-caps>");

			if (xcapCapsAppUsage != null) {
				try {
					getRequestProcessor().put(new ResourceSelector("/"+xcapCapsAUID+"/global/index",null), xcapCapsMimetype, new ByteArrayInputStream(sb1.toString().getBytes("utf-8")), null, ServerConfiguration.XCAP_ROOT);
				}
				catch (Exception e) {
					e.printStackTrace();
					throw new InternalServerErrorException("Failed to put xcap-caps global document. Cause: "+e.getCause()+" Message:"+e.getMessage());
				}
				// release app usage object
				releaseAppUsageToCache(xcapCapsAppUsage);
			}	
		}
		
	}
	
	// EVENT HANDLERS

	public void onServiceStartedEvent(
			javax.slee.serviceactivity.ServiceStartedEvent event,
			ActivityContextInterface aci) {

		try {
			// check if it's my service that is starting
			ServiceActivity sa = ((ServiceActivityFactory) myEnv
					.lookup("slee/serviceactivity/factory")).getActivity();
			if (sa.equals(aci.getActivity())) {
				logAsInfo("Application usage activated");
				// put app usage in cache
				putAppUsageInCache();
				if (isLogDebugEnabled()) logAsDebug("AppUsage cached");
				try {
					dataSource.addAppUsage(getAUID());
				}
				catch (Exception e) {
					logAsInfo("Failed to create the app usage in datasource, it may already exist. Error: "+e.getMessage());
				}
				// update xcap caps global doc
				try {
					updateXCAPCapsGlobalDoc();
				} catch (InternalServerErrorException e) {
					logger.error("failed to update xcap caps global doc", e);
				}
			} else {
				if (isLogDebugEnabled()) logAsDebug("Another service activated...");
				// we don't want to receive further events on this activity
				aci.detach(getSbbContext().getSbbLocalObject());
			}
		} catch (NamingException e) {
			logAsError("Can't handle service started event.", e);
		}
	}

	public void onActivityEndEvent(ActivityEndEvent event,
			ActivityContextInterface aci) {

		if (aci.getActivity() instanceof ServiceActivity) {
			// service activity ending
			if (isLogDebugEnabled()) logAsDebug("Service being deactivated...\n");
			// remove app usage from cache
			removeAppUsageFromCache();
			if (isLogDebugEnabled()) logAsDebug("AppUsage removed from cache...");
			// update xcap caps global doc
				try {
					updateXCAPCapsGlobalDoc();
				} catch (InternalServerErrorException e) {
					logger.error("failed to update xcap caps global doc", e);
				}
		}

	}

}