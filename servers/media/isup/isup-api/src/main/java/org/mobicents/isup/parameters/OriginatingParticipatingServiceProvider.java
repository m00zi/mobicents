/**
 * Start time:13:58:48 2009-04-04<br>
 * Project: mobicents-jain-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.isup.parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Start time:13:58:48 2009-04-04<br>
 * Project: mobicents-jain-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 */
public class OriginatingParticipatingServiceProvider extends AbstractNumber {

	// FIXME: shoudl we add max octets ?
	private int opspLengthIndicator = 0;

	
	public OriginatingParticipatingServiceProvider() {
		
	}
	
	public OriginatingParticipatingServiceProvider(byte[] representation) {
		super(representation);
		// TODO Auto-generated constructor stub
	}

	public OriginatingParticipatingServiceProvider(ByteArrayInputStream bis) {
		super(bis);
		// TODO Auto-generated constructor stub
	}

	public OriginatingParticipatingServiceProvider(String address) {
		super(address);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws IllegalArgumentException {
		return super.decodeElement(b);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
		return super.encodeElement();
	}

	@Override
	public int decodeHeader(ByteArrayInputStream bis) throws IllegalArgumentException {
		int b = bis.read() & 0xff;

		this.oddFlag = (b & 0x80) >> 7;
		this.opspLengthIndicator = b & 0x0F;
		return 1;
	}

	@Override
	public int encodeHeader(ByteArrayOutputStream bos) {
		int b = 0;
		// Even is 000000000 == 0
		boolean isOdd = this.oddFlag == _FLAG_ODD;
		if (isOdd)
			b |= 0x80;
		b |= this.opspLengthIndicator & 0x0F;
		bos.write(b);
		return 1;
	}

	@Override
	public int decodeBody(ByteArrayInputStream bis) throws IllegalArgumentException {

		return 0;
	}

	@Override
	public int encodeBody(ByteArrayOutputStream bos) {

		return 0;
	}

	
	
	@Override
	public int decodeDigits(ByteArrayInputStream bis) throws IllegalArgumentException {
		return super.decodeDigits(bis, this.opspLengthIndicator);
	}

	public int getOpspLengthIndicator() {
		return opspLengthIndicator;
	}

	@Override
	public void setAddress(String address) {
		// TODO Auto-generated method stub
		super.setAddress(address);
		int l = super.address.length();
		this.opspLengthIndicator = l / 2 + l % 2;
		if (opspLengthIndicator > 4) {
			throw new IllegalArgumentException("Maximum octets for this parameter in digits part is 4.");
			// FIXME: add check for digit (max 7 ?)
		}
	}

}