package peer;

import org.apache.commons.codec.DecoderException;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by Jesper on 2017-07-29.
 */
//TODO: Have a selector (or something asynchronuous) so that responses can come in whenever
//TODO: Insert a strategy into constructor that picks next piece to download
public class PeerWireProtocol extends Observable implements Runnable {
    private boolean am_choking;
    private boolean am_interested;
    private boolean peer_choking;
    private boolean peer_interested;
    private String peer_id;
    private final byte PSTR_LEN = 19;
    private final String PSTR = "BitTorrent protocol";
    private final byte[] RESERVED_BYTES = new byte[8];
    private Socket socket;
    private Map<String, Object> handshakeResponse;
//    private List<Integer> peerHas;
    private final byte CHOKE_ID = 0;
    private final byte UNCHOKE_ID = 1;
    private final byte INTERESTED_ID = 2;
    private final byte NOT_INTERESTED_ID = 3;
    private final byte HAVE_ID = 4;
    private final byte BITFIELD_ID = 5;
    private final byte REQUEST_ID = 6;
    private final byte PIECE_ID = 7;
    private final byte CANCEL_ID = 8;
    private final byte PORT_ID = 9;
    private PeerStrategy peerStrategy;
    private byte[] info_hash;
    private Map<String, Integer> peerRequest;
    private Map<String, Integer> request;
    private BitSet peerHas;
    public PeerWireProtocol(String peer_id, String peer) throws IOException {
        this.am_choking = true;
        this.am_interested = false;
        this.peer_choking = true;
        this.peer_interested = false;
        this.peer_id = peer_id;
        String[] ipAndPort = peer.split(":");
        this.socket = new Socket(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
    }

    public PeerWireProtocol(String peer) throws IOException {
        this(null, peer);
    }
    public void handshake() throws URISyntaxException, IOException, InterruptedException, DecoderException {
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
    public void parseHandshakeResponse() throws IOException, FormatException {
        InputStream is = socket.getInputStream();
        int protocolNumber = is.read();
        if (protocolNumber == PSTR_LEN) {
            byte[] protocol = new byte[PSTR_LEN];
            is.read(protocol);
            if (!Arrays.equals(protocol, PSTR.getBytes("ISO-8859-1"))) {
                socket.close();
                throw new FormatException(String.format("Expected %s but got %s", PSTR,
                        new String(protocol, "ISO-8859-1")));
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
//        while (true) {
//            peerStrategy.execute();
//        }
        //TODO: Keep alive every 2 minutes
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (false /* no response from handshake yet */) {
            return;
        }

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
        if (!peerHas.get(index)) {
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
        if (am_interested && !peer_choking && request == null) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(5 + 3 * 4);
            byteBuffer.putInt(13);
            byteBuffer.put((byte) 6);
            byteBuffer.putInt(index);
            byteBuffer.putInt(begin);
            byteBuffer.putInt(length);
            socket.getOutputStream().write(byteBuffer.array());
            request = new HashMap<>(3);
            request.put("index", index);
            request.put("begin", begin);
            request.put("length", length);
        }
    }

    //TODO: piece function (will set peerRequest to null)

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

    private void outputResponse() {


    }

    public void parseResponse(InputStream is) throws IOException {
        if (isBitTorrent(is)) {
            parseBitTorrentResponse(is);
        }
    }

    private boolean isBitTorrent(InputStream is) throws IOException {
        try {
            is.mark(4);
            byte[] lengthBytes = new byte[4];
            is.read(lengthBytes);
            is.reset();
            ByteBuffer byteBuffer = ByteBuffer.wrap(lengthBytes);
            int length = byteBuffer.getInt();
            is.mark(length);
            is.read(new byte[4]);
            byte[] bytes = new byte[1 + length * 4];
            is.read(bytes);
            if (bytes[0] <= PORT_ID && bytes[0] >= CHOKE_ID) {
                if (is.read() == -1) {
                    is.reset();
                    return true;
                }
            }
        } catch (IOException io) {
            //will fall through to return false
        }
        is.reset();
        return false;
    }

    private void parseBitTorrentResponse(InputStream is) throws IOException {
        byte[] lengthBytes = new byte[4];
        is.read(lengthBytes);
        ByteBuffer byteBuffer = ByteBuffer.wrap(lengthBytes);
        int length = byteBuffer.getInt();
        byte id = (byte) is.read();
        switch (id) {
            case CHOKE_ID:
                peer_choking = true;
                break;
            case UNCHOKE_ID:
                peer_choking = false;
                break;
            case INTERESTED_ID:
                peer_interested = true;
                break;
            case NOT_INTERESTED_ID:
                peer_interested = false;
                break;
            case HAVE_ID:
                parseHaveResponse(is);
                break;
            case BITFIELD_ID:
                break;
            case REQUEST_ID:
                parseRequestResponse(is);
                break;



        }
    }

    private void parseHaveResponse(InputStream is) throws IOException {
        byte[] bytes = new byte[4];
        is.read(bytes);
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        int haveIndex = byteBuffer.getInt();
        peerHas.set(haveIndex);
    }

    private void parseBitfieldResponse(InputStream is, int length) {
        length--;

    }

    private void parseRequestResponse(InputStream is) throws IOException {
        byte[] bytes = new byte[12];
        is.read(bytes);
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        peerRequest = new HashMap<>(3);
        peerRequest.put("index", byteBuffer.getInt());
        peerRequest.put("begin", byteBuffer.getInt());
        peerRequest.put("length", byteBuffer.getInt());
    }

    public void setPeerStrategy(PeerStrategy peerStrategy) {
        this.peerStrategy = peerStrategy;
    }

//    private void writeToChannel(ByteBuffer buffer) throws IOException {

//    }
//        }
//            channel.write(buffer);
//        while (buffer.hasRemaining()) {
//        buffer.flip();
public BitSet getPeerHas() {
    return peerHas;
}

    public boolean amChoking() {
        return am_choking;
    }

    public boolean amInterested() {
        return am_interested;
    }

    public boolean peerChoking() {
        return peer_choking;
    }

    public boolean peerInterested() {
        return peer_interested;
    }

}
