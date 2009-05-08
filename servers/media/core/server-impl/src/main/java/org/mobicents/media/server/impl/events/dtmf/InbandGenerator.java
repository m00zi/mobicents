/*
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
package org.mobicents.media.server.impl.events.dtmf;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import org.apache.log4j.Logger;
import org.mobicents.media.Buffer;
import org.mobicents.media.BufferFactory;
import org.mobicents.media.Format;
import org.mobicents.media.server.impl.AbstractSource;
import org.mobicents.media.server.impl.rtp.RtpHeader;
import org.mobicents.media.server.spi.Timer;
import org.mobicents.media.server.spi.dsp.Codec;

/**
 * 
 * @author Oleg Kulikov
 * @author amit bhayani
 */
public class InbandGenerator extends AbstractSource {

	public static Logger logger = Logger.getLogger(InbandGenerator.class);
	public final static String[][] events = new String[][] { { "1", "2", "3", "A" }, { "4", "5", "6", "B" },
			{ "7", "8", "9", "C" }, { "*", "0", "#", "D" } };

	private int[] lowFreq = new int[] { 697, 770, 852, 941 };
	private int[] highFreq = new int[] { 1209, 1336, 1477, 1633 };

	private volatile boolean started = false;

	private static List<Buffer> listDTMF_0 = new ArrayList<Buffer>(50);
	private static List<Buffer> listDTMF_1 = new ArrayList<Buffer>(50);
	private static List<Buffer> listDTMF_2 = new ArrayList<Buffer>(50);
	private static List<Buffer> listDTMF_3 = new ArrayList<Buffer>(50);
	private static List<Buffer> listDTMF_4 = new ArrayList<Buffer>(50);
	private static List<Buffer> listDTMF_5 = new ArrayList<Buffer>(50);
	private static List<Buffer> listDTMF_6 = new ArrayList<Buffer>(50);
	private static List<Buffer> listDTMF_7 = new ArrayList<Buffer>(50);
	private static List<Buffer> listDTMF_8 = new ArrayList<Buffer>(50);
	private static List<Buffer> listDTMF_9 = new ArrayList<Buffer>(50);
	private static List<Buffer> listDTMF_STAR = new ArrayList<Buffer>(50);
	private static List<Buffer> listDTMF_HASH = new ArrayList<Buffer>(50);
	private static List<Buffer> listDTMF_A = new ArrayList<Buffer>(50);
	private static List<Buffer> listDTMF_B = new ArrayList<Buffer>(50);
	private static List<Buffer> listDTMF_C = new ArrayList<Buffer>(50);
	private static List<Buffer> listDTMF_D = new ArrayList<Buffer>(50);

	private long seqNumber = 0;
	private Timer timer = null;

	private transient ScheduledFuture worker;

	private int packetPeriod = 20;
	private int sizeInBytes = 320;
	private int dataLength = 16000;

	private BufferFactory bufferFactory = null;

	private String digit = null;

	// Min duration = 40ms and max = 500ms
	private int duration = 40;

	public InbandGenerator(String name) {
		super(name);
	}

	public void init() {
		timer = getEndpoint().getTimer();
		packetPeriod = timer.getHeartBeat();
		sizeInBytes = (int) (Codec.LINEAR_AUDIO.getSampleRate() * (Codec.LINEAR_AUDIO.getSampleSizeInBits() / 8) / 1000 * packetPeriod);
		dataLength = (int) Codec.LINEAR_AUDIO.getSampleRate() * Codec.LINEAR_AUDIO.getSampleSizeInBits() / 8;

		System.out.println(sizeInBytes);

		byte[] data = generateData("0");
		populateList(listDTMF_0, data);

		data = generateData("1");
		populateList(listDTMF_1, data);

		data = generateData("2");
		populateList(listDTMF_2, data);

		data = generateData("3");
		populateList(listDTMF_3, data);

		data = generateData("4");
		populateList(listDTMF_4, data);

		data = generateData("5");
		populateList(listDTMF_5, data);

		data = generateData("6");
		populateList(listDTMF_6, data);

		data = generateData("7");
		populateList(listDTMF_7, data);

		data = generateData("8");
		populateList(listDTMF_8, data);

		data = generateData("9");
		populateList(listDTMF_9, data);

		data = generateData("*");
		populateList(listDTMF_STAR, data);

		data = generateData("#");
		populateList(listDTMF_HASH, data);

		data = generateData("A");
		populateList(listDTMF_A, data);

		data = generateData("B");
		populateList(listDTMF_B, data);

		data = generateData("C");
		populateList(listDTMF_C, data);

		data = generateData("D");
		populateList(listDTMF_D, data);
	}

	private void populateList(List<Buffer> list, byte[] data) {
		int count = 0;
		boolean marker = true;

		for (int i = 0; i < 50; i++) {

			Buffer buffer = new Buffer();

			RtpHeader rtpHeader = new RtpHeader();
			rtpHeader.setMarker(marker);
			marker = false;

			byte[] bufferData = new byte[sizeInBytes];
			for (int j = 0; j < sizeInBytes; j++) {
				bufferData[j] = data[count];
				count++;
			}
			buffer.setData(bufferData);
			buffer.setSequenceNumber(i);
			buffer.setTimeStamp(i * this.packetPeriod);
			buffer.setOffset(0);
			buffer.setLength(320);
			buffer.setFormat(Codec.LINEAR_AUDIO);
			list.add(buffer);
		}
	}

	public static String getToneName(int row, int column) {
		try {
			return events[row][column];
		} catch (ArrayIndexOutOfBoundsException aiobe) {
			return null;
		}
	}

	private byte[] generateData(String tone) {
		byte[] data;
		data = new byte[dataLength];
		int len = dataLength / 2;
		int[] freq = getFreq(tone);

		int k = 0;
		for (int i = 0; i < len; i++) {
			short s = (short) ((short) (Short.MAX_VALUE / 2 * Math.sin(2 * Math.PI * freq[0] * i / len)) + (short) (Short.MAX_VALUE / 2 * Math
					.sin(2 * Math.PI * freq[1] * i / len)));
			data[k++] = (byte) (s);
			data[k++] = (byte) (s >> 8);
		}

		return data;
	}

	public void setDigit(String digit) {
		this.digit = digit;
	}

	public void setDuraion(int duration) {
		if (duration < 40) {
			throw new IllegalArgumentException("Duration cannot be less than 40ms");
		}
		this.duration = duration;
	}

	public static void print(byte[] data) {
		System.out.println("--------------------");
		System.out.println("Length = " + data.length);

		for (int i = 0; i < data.length; i++) {
			System.out.println("i=" + i + " data = " + data[i] + " ");
		}
		System.out.println();
		System.out.println("--------------------");
	}

	private int[] getFreq(String tone) {
		int freq[] = new int[2];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (events[i][j].equalsIgnoreCase(tone)) {
					freq[0] = lowFreq[i];
					freq[1] = highFreq[j];
				}
			}
		}
		return freq;
	}

	public Format getFormat() {
		return Codec.LINEAR_AUDIO;
	}

	/*
	 * public void setTransferHandler(BufferTransferHandler transferHandler) {
	 * this.transferHandler = transferHandler; if (transferHandler != null) {
	 * start(); } }
	 */

	public void start() {
		if (!started) {
			Transmitter transmitter = new Transmitter(this.digit, this.duration, this.packetPeriod);
			worker = timer.synchronize(transmitter);
			started = true;
		}
	}

	public void stop() {
		if (started) {
			if (worker != null && !worker.isCancelled()) {
				worker.cancel(true);
			}
			started = false;
		}
	}

	public Format[] getFormats() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private List<Buffer> getData(String digit) {
		byte[] data = null;
		if (digit.equals("1")) {
			return listDTMF_1;
		} else if (digit.equals("2")) {
			return listDTMF_2;
		} else if (digit.equals("3")) {
			return listDTMF_3;
		} else if (digit.equals("4")) {
			return listDTMF_4;
		} else if (digit.equals("5")) {
			return listDTMF_5;
		} else if (digit.equals("6")) {
			return listDTMF_6;
		} else if (digit.equals("7")) {
			return listDTMF_7;
		} else if (digit.equals("8")) {
			return listDTMF_8;
		} else if (digit.equals("9")) {
			return listDTMF_9;
		} else if (digit.equals("0")) {
			return listDTMF_0;
		} else if (digit.equals("*")) {
			return listDTMF_STAR;
		} else if (digit.equals("#")) {
			return listDTMF_HASH;
		} else if (digit.equals("A")) {
			return listDTMF_A;
		} else if (digit.equals("B")) {
			return listDTMF_B;
		} else if (digit.equals("C")) {
			return listDTMF_C;
		} else if (digit.equals("D")) {
			return listDTMF_D;
		} else {
			throw new IllegalArgumentException("Digit " + digit + " not supported");
		}

	}

	private class Transmitter implements Runnable {
		String digit = null;
		int duration = 0;
		int heartBeat = 20;
		int numberOfPackets;
		int offset = 0;
		int count = 0;
		List<Buffer> bufferList;

		public Transmitter(String digit, int duration, int heartBeat) {
			this.digit = digit;
			this.duration = duration;
			this.heartBeat = heartBeat;
			this.numberOfPackets = Math.round(this.duration / this.heartBeat); // TODO
			// TODO: Do we care for exact duration?
			this.bufferList = getData(digit);
		}

		public void run() {
			if (count < numberOfPackets) {
				Buffer buffer = bufferList.get(count);
				try {
					otherParty.receive(buffer);
				} catch (Exception e) {
					// TODO : Send failed event?
					worker.cancel(true);
				}
				count++;
			} else {
				stop();
			}
		}
	}

	public static void main(String args[]) {
		InbandGenerator gen = new InbandGenerator("test");
		int sizeInBytes = (int) (Codec.LINEAR_AUDIO.getSampleRate() * (Codec.LINEAR_AUDIO.getSampleSizeInBits() / 8)
				/ 1000 * 20);
		int dataLength = (int) Codec.LINEAR_AUDIO.getSampleRate() * Codec.LINEAR_AUDIO.getSampleSizeInBits() / 8;

		System.out.println(sizeInBytes);
		System.out.println(dataLength);
		// gen.print(gen.dataDTMF_0);

		// gen.print(gen.dataDTMF_6);

		// gen.print(gen.dataDTMF_3);
	}
}
