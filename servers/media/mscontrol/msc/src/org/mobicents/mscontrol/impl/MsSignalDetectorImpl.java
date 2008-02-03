/*
 * MsSignalDetectorImpl.java
 *
 * The Simple Media API RA
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */

package org.mobicents.mscontrol.impl;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.mobicents.media.server.spi.Endpoint;
import org.mobicents.media.server.spi.EndpointQuery;
import org.mobicents.media.server.spi.NotificationListener;
import org.mobicents.media.server.spi.events.NotifyEvent;
import org.mobicents.mscontrol.MsConnection;
import org.mobicents.mscontrol.MsResourceListener;
import org.mobicents.mscontrol.MsSignalDetector;

/**
 *
 * @author Oleg Kulikov
 */
public class MsSignalDetectorImpl implements MsSignalDetector, NotificationListener {
    
    private Endpoint endpoint;
    private String endpointName;
    private MsProviderImpl provider;
    
    private String id = Long.toHexString(System.currentTimeMillis());
    private ArrayList <MsResourceListener> listeners = new ArrayList();
    
    private Logger logger = Logger.getLogger(MsSignalDetectorImpl.class);
    
    /** Creates a new instance of MsSignalDetectorImpl */
    public MsSignalDetectorImpl(MsProviderImpl provider, String endpointName) {
        this.provider = provider;
        this.endpointName = endpointName;
        listeners.addAll(provider.resourceListeners);
    }

    public String getID() {
        return id;
    }
    
    public void release() {
        //released = true;
    }
    
    public void receive(int signalID, boolean persistent) {
        new Thread(new SubscribeTx(this, signalID, persistent)).start();
    }
    
    public void receive(int signalID, MsConnection connection, String[] params) {
        if (logger.isDebugEnabled()) {
            logger.debug("Subscribe eventID=" + signalID + ", connection=" + connection);
        }
        new Thread(new SubscribeTx1(this, signalID, connection, params)).start();
    }
    
    public void update(NotifyEvent event) {
        MsNotifyEventImpl evt = 
                new MsNotifyEventImpl(this, event.getID(), event.getCause(), event.getMessage());
        for (MsResourceListener listener: listeners) {
            listener.update(evt);
        }
    }

    public void addResourceListener(MsResourceListener listener) {
        listeners.add(listener);
    }
    
    private class SubscribeTx implements Runnable {
        private int signalID;
        private boolean persistent;
        private NotificationListener listener;
        
        public SubscribeTx(NotificationListener listener, int signalID, boolean persistent)  {
            this.listener = listener;
            this.signalID = signalID;
            this.persistent = persistent;
        }
        
        public void run() {
            try {
                endpoint.subscribe(signalID, listener, persistent);
            } catch (Exception e) {
            }
        }
    }
    
    private class SubscribeTx1 implements Runnable {
        private int signalID;
        private boolean persistent;
        private NotificationListener listener;
        private String[] params;
        private MsConnection connection;
        
        public SubscribeTx1(NotificationListener listener, int signalID, 
                MsConnection connection, String params[])  {
            this.listener = listener;
            this.signalID = signalID;
            this.connection = connection;
            this.params = params;
        }
        
        public void run() {
            try {
                endpoint = EndpointQuery.find(endpointName);
                MsConnectionImpl con = (MsConnectionImpl) connection;
                String connectionID = con.connection.getId();
                if (logger.isDebugEnabled()) {
                    logger.debug("Subscribe signalID =" + signalID + ", endpoint=" + endpoint);
                }
                endpoint.subscribe(signalID, connectionID, params, listener);
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }
    
}
