package peer;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.client.utils.URIBuilder;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jesper on 2017-07-29.
 */
public class PeerWireProtocol implements Runnable {
    private boolean am_choking;
    private boolean am_interested;
    private boolean peer_choking;
    private boolean peer_interested;
    private String peer_id;
    private String peerIp;
    private int peerPort;
    private final byte PSTR_LEN = 19;
    private final String PSTR = "BitTorrent protocol";
    private final byte[] RESERVED_BYTES = new byte[8];
    private Socket socket;
    private Map<String, Object> handshakeResponse;
    private List<Integer> peerHas;

    public PeerWireProtocol(String peer_id, String peer) throws IOException {
        this.am_choking = true;
        this.am_interested = false;
        this.peer_choking = true;
        this.peer_interested = false;
        this.peer_id = peer_id;
        String[] ipAndPort = peer.split(":");
        this.peerIp = ipAndPort[0];
        this.peerPort = Integer.parseInt(ipAndPort[1]);
        this.socket = new Socket(peerIp, peerPort);
    }

    public PeerWireProtocol(String peer) throws IOException {
        this(null, peer);
    }

    public void handshake(byte[] info_hash) throws URISyntaxException, IOException, InterruptedException, DecoderException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(49 + PSTR.length());
        byteBuffer.put(PSTR_LEN);
        byteBuffer.put(PSTR.getBytes(Charset.forName("ISO-8859-1")));
        byteBuffer.put(RESERVED_BYTES);
        byteBuffer.put(info_hash);
        if (peer_id != null) {
            byteBuffer.put(peer_id.getBytes(Charset.forName("ISO-8859-1")));
        }
        DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        if (!socket.isConnected()) {
            Thread.sleep(3000);
        }
        outToServer.write(byteBuffer.array());
    }

    private void parseHandshakeResponse() throws IOException, Exception {
        InputStream is = socket.getInputStream();
        int protocolNumber = is.read();
        if (protocolNumber == PSTR_LEN) {
            byte[] protocol = new byte[PSTR_LEN];
            is.read(protocol);
            if (!Arrays.equals(protocol, PSTR.getBytes("ISO-8859-1"))) {
                socket.close();
                throw new Exception(String.format("Expected %s but got %s", PSTR, new String(protocol, "ISO-8859-1")));
            }
            this.handshakeResponse = new HashMap<>();
            byte[] reserved_bytes = new byte[8];
            is.read(reserved_bytes);
            if (reserved_bytes[5] == 0x10) {
                //TODO: LTEP extension
            } else {
                byte[] info_hash = new byte[20];
                is.read(info_hash);
                this.handshakeResponse.put("info_hash", info_hash);
                byte[] peer_id = new byte[20];
                is.read(peer_id);
                this.handshakeResponse.put("peer_id", peer_id);
            }

        }
    }

    public void close() throws IOException {
        socket.close();
    }

    @Override
    public void run() {
        //TODO: Keep alive every 2 minutes

    }

    public void keepAlive() throws IOException {
        socket.getOutputStream().write(0);
    }

    public void choke() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(5);
        byteBuffer.putInt(1);
        byteBuffer.put((byte) 0);
        socket.getOutputStream().write(byteBuffer.array());
        am_choking = true;
    }

    public void unchoke() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(5);
        byteBuffer.putInt(1);
        byteBuffer.put((byte) 1);
        socket.getOutputStream().write(byteBuffer.array());
        am_choking = false;
    }

    public void interested() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(5);
        byteBuffer.putInt(1);
        byteBuffer.put((byte) 2);
        socket.getOutputStream().write(byteBuffer.array());
        am_interested = true;
    }

    public void uninterested() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(5);
        byteBuffer.putInt(1);
        byteBuffer.put((byte) 3);
        socket.getOutputStream().write(byteBuffer.array());
        am_interested = false;
    }

    public void have(int index) throws IOException {
        if (!peerHas.contains(index)) {
            //TODO: Make sure the index should be an int (says 'number')
            ByteBuffer byteBuffer = ByteBuffer.allocate(5 + 4);
            byteBuffer.putInt(5);
            byteBuffer.put((byte) 4);
            byteBuffer.putInt(index);
            socket.getOutputStream().write(byteBuffer.array());
        }

    }

    //TODO: bitfield function

    public void request(int index, int begin, int length) throws IOException {
        if (am_interested && !peer_choking) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(5 + 3 * 4);
            byteBuffer.putInt(13);
            byteBuffer.put((byte) 6);
            byteBuffer.putInt(index);
            byteBuffer.putInt(begin);
            byteBuffer.putInt(length);
            socket.getOutputStream().write(byteBuffer.array());
        }
    }

    //TODO: piece function

    public void cancel(int index, int begin, int length) throws IOException {
        if (am_interested && !peer_choking) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(5 + 3 * 4);
            byteBuffer.putInt(13);
            byteBuffer.put((byte) 8);
            byteBuffer.putInt(index);
            byteBuffer.putInt(begin);
            byteBuffer.putInt(length);
            socket.getOutputStream().write(byteBuffer.array());
        }
    }

    //TODO: port function

}
