package org.mobicents.servlet.sip.testsuite;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.sip.Proxy;
import javax.servlet.sip.SipErrorEvent;
import javax.servlet.sip.SipErrorListener;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipURI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class SpeedDialSipServlet extends SipServlet implements SipErrorListener,
		Servlet {

	private static Log logger = LogFactory.getLog(SpeedDialSipServlet.class);
	Map<String, String> dialNumberToSipUriMapping = null;
	
	/** Creates a new instance of SpeedDialSipServlet */
	public SpeedDialSipServlet() {}

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		logger.info("the speed dial sip servlet has been started");
		super.init(servletConfig);
		dialNumberToSipUriMapping = new HashMap<String, String>();
		dialNumberToSipUriMapping.put("1", "sip:receiver@sip-servlets.com");
		dialNumberToSipUriMapping.put("2", "sip:mranga@sip-servlets.com");
		dialNumberToSipUriMapping.put("3", "sip:vlad@sip-servlets.com");
		dialNumberToSipUriMapping.put("4", "sip:bartek@sip-servlets.com");
		dialNumberToSipUriMapping.put("5", "sip:jeand@sip-servlets.com");
	}

	@Override
	protected void doInvite(SipServletRequest request) throws ServletException,
			IOException {

		logger.info("Got request:\n"
				+ request.getMethod());
		logger.info(request.getRequestURI().toString());
		
		String dialNumber = ((SipURI)request.getRequestURI()).getUser();
		String mappedUri = dialNumberToSipUriMapping.get(dialNumber);	
		if(mappedUri != null) {
			SipFactory sipFactory = (SipFactory) getServletContext().getAttribute(SIP_FACTORY);
			Proxy proxy = request.getProxy();
			proxy.setRecordRoute(false);
			proxy.setParallel(false);
			proxy.setSupervised(false);
			proxy.proxyTo(sipFactory.createURI(mappedUri));				
		} else {
			SipServletResponse sipServletResponse = 
				request.createResponse(SipServletResponse.SC_NOT_ACCEPTABLE_HERE, "No mapping for " + dialNumber);
			sipServletResponse.send();			
		}		
	}

	// SipErrorListener methods
	/**
	 * {@inheritDoc}
	 */
	public void noAckReceived(SipErrorEvent ee) {
		logger.error("noAckReceived.");
	}

	/**
	 * {@inheritDoc}
	 */
	public void noPrackReceived(SipErrorEvent ee) {
		logger.error("noPrackReceived.");
	}

}
