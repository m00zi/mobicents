/*
 * Mobicents Media Gateway
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
package org.mobicents.media.server.impl.jmf.mixer;

import java.io.IOException;
import java.io.Serializable;
import javax.media.Buffer;
import javax.media.Format;
import javax.media.format.AudioFormat;
import javax.media.format.UnsupportedFormatException;
import javax.media.protocol.BufferTransferHandler;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PushBufferStream;
import org.mobicents.media.server.impl.jmf.dsp.Codec;
import org.mobicents.media.server.impl.jmf.dsp.CodecLocator;

/**
 *
 * @author Oleg Kulikov
 */
public class MixerOutputStream implements Serializable, PushBufferStream {

    private AudioFormat fmt;
    private final static AudioFormat linear = new AudioFormat(
            AudioFormat.LINEAR, 8000, 16, 1,
            AudioFormat.LITTLE_ENDIAN,
            AudioFormat.SIGNED);
    private byte[] frame;
    private BufferTransferHandler transferHandler;
    private int seq = 0;
    private long duration;
    private Codec codec;
    
    private long id = System.currentTimeMillis();
    
    /** Creates a new instance of PcmMuxStream */
    public MixerOutputStream(AudioFormat fmt, long duration)
            throws UnsupportedFormatException {
        this.fmt = fmt;
        this.duration = duration;

        if (!fmt.matches(linear)) {
            codec = CodecLocator.getCodec(Codec.LINEAR_AUDIO, fmt);
            if (codec == null) {
                throw new UnsupportedFormatException(fmt);
            }
        }
    }

    public Format getFormat() {
        return fmt;
    }

    void print (byte[] data) {
        for (int i = 0; i < data.length; i++) {
            System.out.print(data[i] + " ");
        }
        System.out.println();
        System.out.println("-----------------------------");
    }
    
    public void read(Buffer buffer) throws IOException {
        byte[] cframe = codec != null ? codec.process(frame) : frame;        
        buffer.setData(cframe);
        buffer.setOffset(0);
        buffer.setLength(cframe.length);
        buffer.setDuration(duration);
        buffer.setTimeStamp(duration * seq);
        buffer.setSequenceNumber(seq++);

        buffer.setFormat(fmt);
    }

    public void setTransferHandler(BufferTransferHandler transferHandler) {
        this.transferHandler = transferHandler;
    }

    public ContentDescriptor getContentDescriptor() {
        return new ContentDescriptor(ContentDescriptor.RAW);
    }

    public long getContentLength() {
        return MixerOutputStream.LENGTH_UNKNOWN;
    }

    public boolean endOfStream() {
        return false;
    }

    public Object[] getControls() {
        return new Object[0];
    }

    public Object getControl(String string) {
        return null;
    }

    protected void push(byte[] data) {
        if (transferHandler != null) {
            frame = data;
            transferHandler.transferData(this);
        }
    }
}
