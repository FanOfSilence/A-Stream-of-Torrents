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
import java.util.Map;

/**
 * Created by Jesper on 2017-07-29.
 */
public class PeerWireProtocol {
    private int am_choking;
    private int am_interested;
    private int peer_choking;
    private int peer_interested;
    private String peer_id;
    private String peerIp;
    private int peerPort;
    private final byte PSTR_LEN = 19;
    private final String PSTR = "BitTorrent protocol";
    private final byte[] RESERVED_BYTES = new byte[8];
    private Socket socket;
    private Map<String, Object> handshakeResponse;

    public PeerWireProtocol(String peer_id, String peerIp, int peerPort) throws IOException {
        this.am_choking = 1;
        this.am_interested = 0;
        this.peer_choking = 1;
        this.peer_interested = 0;
        this.peer_id = peer_id;
        this.peerIp = peerIp;
        this.peerPort = peerPort;
        this.socket = new Socket(peerIp, peerPort);
    }

    public void handshake(byte[] info_hash) throws URISyntaxException, IOException, InterruptedException, DecoderException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(49 + PSTR.length());
        byteBuffer.put(PSTR_LEN);
        byteBuffer.put(PSTR.getBytes(Charset.forName("ISO-8859-1")));
        byteBuffer.put(RESERVED_BYTES);
        byteBuffer.put(info_hash);
        byteBuffer.put(peer_id.getBytes(Charset.forName("ISO-8859-1")));
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
}
