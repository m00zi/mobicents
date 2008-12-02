package org.mobicents.media.container.management.console.client.endpoint;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ConnectionInfo implements IsSerializable{

	
	protected String endpointName=null;
	protected String connecitonId=null;
	protected long creationTime=0;
	protected String localSdp=null;
	protected String remoteSdp=null;
	protected String otherEnd=null;
	protected String state=null;
	protected String mode=null;
	protected int packetsSent=0;
	protected int packetsReceived=0;
	protected int octetsSent=0;
	protected int octetsReceived=0;
	protected int interArrivalJitter=0;
	protected long packetsLost=0;


	private ConnectionInfo(String endpointName, String connecitonId,
			long creationTime, String localSdp, String remoteSdp,
			String otherEnd, String state, String mode, int packetsSent,
			int packetsReceived, int octetsSent, int octetsReceived,
			int interArrivalJitter, long packetsLost) {
		super();
		this.endpointName = endpointName;
		this.connecitonId = connecitonId;
		this.creationTime = creationTime;
		this.localSdp = localSdp;
		this.remoteSdp = remoteSdp;
		this.otherEnd = otherEnd;
		this.state = state;
		this.mode = mode;
		this.packetsSent = packetsSent;
		this.packetsReceived = packetsReceived;
		this.octetsSent = octetsSent;
		this.octetsReceived = octetsReceived;
		this.interArrivalJitter = interArrivalJitter;
		this.packetsLost = packetsLost;
	}
	
	public ConnectionInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getPacketsLost() {
		return packetsLost;
	}

	public void setPacketsLost(long packetsLost) {
		this.packetsLost = packetsLost;
	}
	
	public int getPacketsSent() {
		return packetsSent;
	}

	public void setPacketsSent(int packetsSent) {
		this.packetsSent = packetsSent;
	}

	public int getPacketsReceived() {
		return packetsReceived;
	}

	public void setPacketsReceived(int packetsReceived) {
		this.packetsReceived = packetsReceived;
	}

	public int getOctetsSent() {
		return octetsSent;
	}

	public void setOctetsSent(int octetsSent) {
		this.octetsSent = octetsSent;
	}

	public int getOctetsReceived() {
		return octetsReceived;
	}

	public void setOctetsReceived(int octetsReceived) {
		this.octetsReceived = octetsReceived;
	}

	public int getInterArrivalJitter() {
		return interArrivalJitter;
	}

	public void setInterArrivalJitter(int interArrivalJitter) {
		this.interArrivalJitter = interArrivalJitter;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
	public String getEndpointName() {
		return endpointName;
	}

	public void setEndpointName(String endpointName) {
		this.endpointName = endpointName;
	}

	public String getConnecitonId() {
		return connecitonId;
	}

	public void setConnecitonId(String connecitonId) {
		this.connecitonId = connecitonId;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public String getLocalSdp() {
		return localSdp;
	}

	public void setLocalSdp(String localSdp) {
		this.localSdp = localSdp;
	}

	public String getRemoteSdp() {
		return remoteSdp;
	}

	public void setRemoteSdp(String remoteSdp) {
		this.remoteSdp = remoteSdp;
	}

	public String getOtherEnd() {
		return otherEnd;
	}

	public void setOtherEnd(String otherEnd) {
		this.otherEnd = otherEnd;
	}


	
}

