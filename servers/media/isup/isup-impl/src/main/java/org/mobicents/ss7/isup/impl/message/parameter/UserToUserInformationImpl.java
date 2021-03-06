/**
 * Start time:13:13:44 2009-04-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.ss7.isup.message.parameter.UserToUserInformation;

/**
 * Start time:13:13:44 2009-04-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 */
public class UserToUserInformationImpl extends AbstractParameter implements UserToUserInformation{

	
	//FIXME: add Q.931
	
	/* (non-Javadoc)
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	
	public UserToUserInformationImpl() {
		super();
		
	}
	public UserToUserInformationImpl(byte[] b) throws ParameterRangeInvalidException {
		super();
		decodeElement(b);
	}


	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	public int getCode() {

		return _PARAMETER_CODE;
	}
}
