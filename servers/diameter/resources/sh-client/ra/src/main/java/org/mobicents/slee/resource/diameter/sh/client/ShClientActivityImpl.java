/*
 * Mobicents, Communications Middleware
 * 
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party
 * contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 *
 * Boston, MA  02110-1301  USA
 */
package org.mobicents.slee.resource.diameter.sh.client;

import java.io.IOException;
import java.util.ArrayList;

import javax.slee.resource.SleeEndpoint;

import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.AuthSessionStateType;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;
import net.java.slee.resource.diameter.sh.client.DiameterShAvpFactory;
import net.java.slee.resource.diameter.sh.client.ShClientActivity;
import net.java.slee.resource.diameter.sh.client.ShClientMessageFactory;
import net.java.slee.resource.diameter.sh.client.ShSessionState;
import net.java.slee.resource.diameter.sh.client.events.avp.DataReferenceType;
import net.java.slee.resource.diameter.sh.client.events.avp.UserIdentityAvp;
import net.java.slee.resource.diameter.sh.server.events.ProfileUpdateRequest;
import net.java.slee.resource.diameter.sh.server.events.PushNotificationAnswer;
import net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest;
import net.java.slee.resource.diameter.sh.server.events.UserDataRequest;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Request;
import org.jdiameter.api.app.StateChangeListener;
import org.jdiameter.api.sh.ClientShSession;
import org.jdiameter.common.impl.app.sh.ProfileUpdateRequestImpl;
import org.jdiameter.common.impl.app.sh.SubscribeNotificationsRequestImpl;
import org.jdiameter.common.impl.app.sh.UserDataRequestImpl;
import org.jdiameter.common.impl.validation.JAvpNotAllowedException;
import org.mobicents.slee.resource.diameter.base.DiameterActivityImpl;
import org.mobicents.slee.resource.diameter.base.DiameterAvpFactoryImpl;
import org.mobicents.slee.resource.diameter.base.DiameterMessageFactoryImpl;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.sh.client.handlers.ShClientSessionListener;

/**
 * 
 * <br><br>Super project:  mobicents-jainslee-server
 * <br>16:46:45 2008-09-10	
 * <br>
 * Sh Client activity created for request/response use casses
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a> 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @see  ShClientActivity
 */
public class ShClientActivityImpl extends DiameterActivityImpl implements ShClientActivity , StateChangeListener{

  protected ClientShSession clientSession = null;
  protected ShSessionState state = ShSessionState.NOTSUBSCRIBED;
  protected ShClientSessionListener listener = null;
  protected DiameterShAvpFactory shAvpFactory = null;
  protected ShClientMessageFactory messageFactory = null;


  //Is there any way to add
	
  
  public ShClientActivityImpl(DiameterMessageFactoryImpl messageFactory, ShClientMessageFactory shClientMessageFactory, DiameterAvpFactoryImpl avpFactory, DiameterShAvpFactory diameterShAvpFactory, ClientShSession session,
      long timeout, DiameterIdentity destinationHost, DiameterIdentity destinationRealm, SleeEndpoint endpoint)
  {
    super(messageFactory, avpFactory, null, (EventListener<Request, Answer>) session, timeout, destinationHost, destinationRealm, endpoint);

    this.clientSession = session;
    this.clientSession.addStateChangeNotification(this);
    super.setCurrentWorkingSession(this.clientSession.getSessions().get(0));
    this.shAvpFactory = diameterShAvpFactory;
    this.messageFactory = shClientMessageFactory;

  }

  
  
  public void sendProfileUpdateRequest(ProfileUpdateRequest message) throws IOException {
		try {
			DiameterMessageImpl msg = (DiameterMessageImpl) message;
			clientSession.sendProfileUpdateRequest(new ProfileUpdateRequestImpl((Request) msg.getGenericData()));
		} catch (JAvpNotAllowedException e) {
			AvpNotAllowedException anae = new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
			throw anae;
		} catch (Exception e) {
			e.printStackTrace();
			IOException ioe = new IOException("Failed to send message, due to: " + e);
			throw ioe;
		}
	}

	public void sendSubscribeNotificationsRequest(SubscribeNotificationsRequest message) throws IOException {
		//FIXME: IMHO this should not be here.
		try {
			DiameterMessageImpl msg = (DiameterMessageImpl) message;
			this.clientSession.sendSubscribeNotificationsRequest(new SubscribeNotificationsRequestImpl((Request) msg.getGenericData()));
		} catch (JAvpNotAllowedException e) {
			AvpNotAllowedException anae = new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
			throw anae;
		} catch (Exception e) {
			e.printStackTrace();
			IOException ioe = new IOException("Failed to send message, due to: " + e);
			throw ioe;
		}
	}

	public void sendUserDataRequest(UserDataRequest message) throws IOException {
		try {
			DiameterMessageImpl msg = (DiameterMessageImpl) message;
			this.clientSession.sendUserDataRequest(new UserDataRequestImpl((Request) msg.getGenericData()));
		} catch (JAvpNotAllowedException e) {
			AvpNotAllowedException anae = new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
			throw anae;
		} catch (Exception e) {
			e.printStackTrace();
			IOException ioe = new IOException("Failed to send message, due to: " + e);
			throw ioe;
		}
	}

  @Override
  public Object getSessionListener()
  {
    return this.listener;
  }

  @Override
  public void setSessionListener(Object ra)
  {
    this.listener = (ShClientSessionListener) ra;
  }

  public void endActivity()
  {
    this.clientSession.release();
  }

  public Object getDiameterAvpFactory()
  {
    return this.shAvpFactory;
  }

  public Object getDiameterMessageFactory()
  {
    return this.messageFactory;
  }

  public String getSessionId()
  {
    return super.getSessionId();
  }

  public void sendMessage(DiameterMessage message) throws IOException
  {
    super.sendMessage(message);
  }

  public void stateChanged(Enum oldState, Enum newState)
  {
    org.jdiameter.common.api.app.sh.ShSessionState _state = (org.jdiameter.common.api.app.sh.ShSessionState) newState;
    switch(_state)
    {
    case NOTSUBSCRIBED:
      break;
    case SUBSCRIBED:
      //FIXME: error?
      break;
    case TERMINATED:
      state=ShSessionState.TERMINATED;
      this.clientSession.removeStateChangeNotification(this);
      listener.sessionDestroyed(getSessionId(),clientSession);
      break;
    }
  }

  ClientShSession getClientSession()
  {
    return this.clientSession;
  }

}
