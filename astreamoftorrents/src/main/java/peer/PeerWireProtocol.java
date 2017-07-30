package peer;

import org.apache.http.client.utils.URIBuilder;

import java.net.URISyntaxException;
import java.nio.charset.Charset;

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

    public PeerWireProtocol(String peer_id) {
        this.am_choking = 1;
        this.am_interested = 0;
        this.peer_choking = 1;
        this.peer_interested = 0;
        this.peer_id = peer_id;
    }

    public void handshake(byte[] info_hash) throws URISyntaxException {
//        URIBuilder uriBuilder = new URIBuilder(peerIp).setCharset(Charset.forName("ISO-8859-1"));
//        uriBuilder.setParameter(INFO_HASH, new String(hash, "ISO-8859-1"));
//        uriBuilder.setParameter(PEER_ID, id);
//        uriBuilder.setParameter(, "6881");

    }
}
