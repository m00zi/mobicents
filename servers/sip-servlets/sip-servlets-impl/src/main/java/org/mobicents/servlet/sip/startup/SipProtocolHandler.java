/*
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.mobicents.servlet.sip.startup;

import gov.nist.javax.sip.SipStackImpl;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.sip.ListeningPoint;
import javax.sip.SipProvider;
import javax.sip.SipStack;

import net.java.stun4j.StunAddress;
import net.java.stun4j.client.NetworkConfigurationDiscoveryProcess;
import net.java.stun4j.client.StunDiscoveryReport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.coyote.Adapter;
import org.apache.coyote.ProtocolHandler;
import org.mobicents.servlet.sip.SipFactories;
import org.mobicents.servlet.sip.core.DNSAddressResolver;
import org.mobicents.servlet.sip.core.ExtendedListeningPoint;
import org.mobicents.servlet.sip.core.SipApplicationDispatcher;

/**
 * This is the sip protocol handler that will get called upon creation of the
 * tomcat connector defined in the server.xml.<br/> To use a sip connector, one
 * need to specify a new connector in server.xml with
 * org.mobicents.servlet.sip.startup.SipProtocolHandler as the value for the
 * protocol attribute.<br/>
 * 
 * Some of the fields (representing the sip stack propeties) get populated
 * automatically by the container.<br/>
 * 
 * @author Jean Deruelle
 * 
 */
public class SipProtocolHandler implements ProtocolHandler {
	// the logger
	private static transient Log logger = LogFactory.getLog(SipProtocolHandler.class.getName());

	private Adapter adapter = null;

	private Map<String, Object> attributes = new HashMap<String, Object>();

	private SipStack sipStack;

	/*
	 * the extended listening point with global ip address and port for it
	 */
	public ExtendedListeningPoint extendedListeningPoint;

	// sip stack attributes defined by the server.xml
	/**
	 * the sip stack signaling transport
	 */
	private String signalingTransport;

	/**
	 * the sip stack name
	 */
	private String sipStackName;

	/**
	 * the sip Path Name
	 */
	private String sipPathName;

	/**
	 * whether or not to enable the retransmission filter
	 */
	private String retransmissionFilter;

	/**
	 * the sip stack listening port
	 */
	private int port;

	/*
	 * The log level
	 */
	private String logLevel;

	/*
	 * The debug log file. TODO -- integrate this with log4j.
	 */
	private String debugLog;

	/*
	 * The server log file.
	 */
	private String serverLog;

	/*
	 * IP address for this protocol handler.
	 */
	private String ipAddress;	
	/*
	 * use Stun
	 */
	private boolean useStun;
	/*
	 * Stun Server Address
	 */
	private String stunServerAddress;
	/*
	 * Stun Server Port
	 */
	private int stunServerPort;
	

	/**
	 * {@inheritDoc}
	 */
	public void destroy() throws Exception {
		logger.info("Stopping the sip stack");
		//Jboss specific unloading case
		SipApplicationDispatcher sipApplicationDispatcher = (SipApplicationDispatcher)
			getAttribute(SipApplicationDispatcher.class.getSimpleName());
		if(sipApplicationDispatcher != null) {
			logger.info("Removing the Sip Application Dispatcher as a sip listener for connector listening on port " + port);
			extendedListeningPoint.getSipProvider().removeSipListener(sipApplicationDispatcher);
			sipApplicationDispatcher.getSipNetworkInterfaceManager().removeExtendedListeningPoint(extendedListeningPoint);
		}
		//stopping the sip stack
		sipStack.deleteSipProvider(extendedListeningPoint.getSipProvider());
		sipStack.deleteListeningPoint(extendedListeningPoint.getListeningPoint());
		sipStack.stop();
		extendedListeningPoint = null;
		logger.info("Sip stack stopped");
	}

	public Adapter getAdapter() {		
		return adapter;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getAttribute(String attribute) {
		return attributes.get(attribute);
	}

	/**
	 * {@inheritDoc}
	 */
	public Iterator getAttributeNames() {
		return attributes.keySet().iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	public void init() throws Exception {
		SipFactories.initialize(this.sipPathName);
		setAttribute("isSipConnector",Boolean.TRUE);
	}

	public void pause() throws Exception {
		//This is optionnal, no implementation there

	}

	public void resume() throws Exception {
		// This is optionnal, no implementation there

	}

	public void setAdapter(Adapter adapter) {
		this.adapter = adapter;

	}

	/**
	 * {@inheritDoc}
	 */
	public void setAttribute(String arg0, Object arg1) {
		attributes.put(arg0, arg1);
	}		
	
	public void start() throws Exception {
		try {
			logger.info("Starting the sip stack");

			
			String catalinaHome = System.getProperty("catalina.home");
	        if (catalinaHome == null) {
	        	catalinaHome = System.getProperty("catalina.base");
	        }
			// defining sip stack properties
			Properties properties = new Properties();
			
			
			logger.info("logLevel = " + this.logLevel);
			
		
			if (this.logLevel != null) {
				properties.setProperty("gov.nist.javax.sip.LOG_MESSAGE_CONTENT",
				"true");

				properties.setProperty("gov.nist.javax.sip.TRACE_LEVEL",
						this.logLevel);
				properties.setProperty("gov.nist.javax.sip.DEBUG_LOG",
						catalinaHome + "/" + this.debugLog);
				properties.setProperty("gov.nist.javax.sip.SERVER_LOG",
						catalinaHome + "/" + this.serverLog);
				
				logger.info(properties.toString());
				
			}
			properties.setProperty("javax.sip.STACK_NAME", sipStackName);
			properties.setProperty("javax.sip.AUTOMATIC_DIALOG_SUPPORT", "off");			
			
			//checking the external ip address if stun enabled			
			String globalIpAddress = null;			
			int globalPort = -1;
			if (useStun) {
				if(InetAddress.getByName(ipAddress).isLoopbackAddress()) {
					logger.warn("The Ip address provided is the loopback address, stun won't be enabled for it");
				} else {
					//TODO port should be chosen randomly
					StunAddress localStunAddress = new StunAddress(ipAddress,
							port);
	
					StunAddress serverStunAddress = new StunAddress(
							stunServerAddress, stunServerPort);
	
					NetworkConfigurationDiscoveryProcess addressDiscovery = new NetworkConfigurationDiscoveryProcess(
							localStunAddress, serverStunAddress);
					addressDiscovery.start();
					StunDiscoveryReport report = addressDiscovery
							.determineAddress();				
					globalIpAddress = report.getPublicAddress().getSocketAddress().getAddress().getHostAddress();
					globalPort = report.getPublicAddress().getPort();
					addressDiscovery.shutDown();
					logger.info("Stun report = " + report);
					//TODO set a timer to retry the binding and provide a callback to update the global ip address and port
				}
			}
			
			// Create SipStack object
			sipStack = SipFactories.sipFactory.createSipStack(properties);

			ListeningPoint listeningPoint = sipStack.createListeningPoint(ipAddress,
					port, signalingTransport);
			SipProvider sipProvider = sipStack.createSipProvider(listeningPoint);
			sipStack.start();
			
			//creating the extended listening point
			extendedListeningPoint = new ExtendedListeningPoint(sipProvider, listeningPoint);
			extendedListeningPoint.setGlobalIpAddress(globalIpAddress);
			extendedListeningPoint.setGlobalPort(globalPort);
			//TODO add it as a listener for global ip address changes if STUN rediscover a new addess at some point
			
			logger.info("useStun " + useStun + ", stunAddress " + stunServerAddress + ", stunPort : " + stunServerPort);						
			
			//made the sip stack and the extended listening Point available to the service implementation
			setAttribute(SipStack.class.getSimpleName(), sipStack);					
			setAttribute(ExtendedListeningPoint.class.getSimpleName(), extendedListeningPoint);
			
			//Jboss specific loading case
			SipApplicationDispatcher sipApplicationDispatcher = (SipApplicationDispatcher)
				getAttribute(SipApplicationDispatcher.class.getSimpleName());
			if(sipApplicationDispatcher != null) {
				logger.info("Adding the Sip Application Dispatcher as a sip listener for connector listening on port " + port);
				sipProvider.addSipListener(sipApplicationDispatcher);
				sipApplicationDispatcher.getSipNetworkInterfaceManager().addExtendedListeningPoint(extendedListeningPoint);
				// for nist sip stack set the DNS Address resolver allowing to make DNS SRV lookups
				if(sipStack instanceof SipStackImpl) {
					logger.info(sipStack.getStackName() +" will be using DNS SRV lookups as AddressResolver");
					((SipStackImpl) sipStack).setAddressResolver(new DNSAddressResolver(sipApplicationDispatcher));
				}
			}
			
			logger.info("Sip stack started on ip address : " + ipAddress
					+ " and port " + port);
		} catch (Exception ex) {
			logger.fatal(
					"Bad shit happened -- check server.xml for tomcat. ", ex);
			throw ex;
		}
	}	
	
	/**
	 * @return the retransmissionFilter
	 */
	public String getRetransmissionFilter() {
		return retransmissionFilter;
	}

	/**
	 * @param retransmissionFilter
	 *            the retransmissionFilter to set
	 */
	public void setRetransmissionFilter(String retransmissionFilter) {
		this.retransmissionFilter = retransmissionFilter;
	}

	/**
	 * @return the sipPathName
	 */
	public String getSipPathName() {
		return sipPathName;
	}

	/**
	 * @param sipPathName
	 *            the sipPathName to set
	 */
	public void setSipPathName(String sipPathName) {
		this.sipPathName = sipPathName;
	}

	/**
	 * @return the sipStackName
	 */
	public String getSipStackName() {
		return sipStackName;
	}

	/**
	 * @param sipStackName
	 *            the sipStackName to set
	 */
	public void setSipStackName(String sipStackName) {
		this.sipStackName = sipStackName;
	}

	/**
	 * @return the signalingTransport
	 */
	public String getSignalingTransport() {
		return signalingTransport;
	}

	/**
	 * @param signalingTransport
	 *            the signalingTransport to set
	 */
	public void setSignalingTransport(String transport) {
		this.signalingTransport = transport;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public String getLogLevel() {
		return logLevel;
	}

	public void setServerLog(String serverLog) {
		this.serverLog = serverLog;
	}

	public String getServerLog() {
		return serverLog;
	}

	public void setDebugLog(String debugLog) {
		this.debugLog = debugLog;
	}

	public String getDebugLog() {
		return debugLog;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @return the stunServerAddress
	 */
	public String getStunServerAddress() {
		return stunServerAddress;
	}

	/**
	 * @param stunServerAddress the stunServerAddress to set
	 */
	public void setStunServerAddress(String stunServerAddress) {
		this.stunServerAddress = stunServerAddress;
	}

	/**
	 * @return the stunServerPort
	 */
	public int getStunServerPort() {
		return stunServerPort;
	}

	/**
	 * @param stunServerPort the stunServerPort to set
	 */
	public void setStunServerPort(int stunServerPort) {
		this.stunServerPort = stunServerPort;
	}

	/**
	 * @return the useStun
	 */
	public boolean isUseStun() {
		return useStun;
	}

	/**
	 * @param useStun the useStun to set
	 */
	public void setUseStun(boolean useStun) {
		this.useStun = useStun;
	}

}
