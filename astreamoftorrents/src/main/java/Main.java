import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import peer.PeerWireProtocol;
import torrent.file.TorrentFile;
import torrent.state.TorrentState;
import tracker.HttpTracker;
import tracker.response.http.AnnounceResponse;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Random;

/**
 * Created by Jesper on 2017-05-31.
 */
public class Main {
    private static DatagramSocket workingSocket;
    public static void main(String[] args) throws IOException {
        System.out.println(System.getProperty("user.dir"));
        Random random = new Random(100);
        int transId = random.nextInt(10000);
        try {
            TorrentFile torrentFile = new TorrentFile("src/main/resources/Jimmy.Kimmel.2017.07.31.Channing.Tatum.HDTV.x264-CROOKS[rartv]-[rarbg.to].torrent");
            PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setConnectionManager(manager)
                    .build();
            TorrentState state = new TorrentState();
            HttpTracker httpTracker = new HttpTracker(httpClient, (String) torrentFile.getStringMap().get("announce"),
                    "-JP0001-456726789357", 6969, state);
//            torrentFile.hash();
            System.out.println(torrentFile.hash());
            AnnounceResponse announceResponse = httpTracker.announce(torrentFile.getByteHash());
            if (!announceResponse.hasFailed()) {
                List<String> peers = (List<String>) announceResponse.getAnnounceMap().get("peers");
                for (String peer : peers) {
                    System.out.println(peer);
                }
            }
            PeerWireProtocol peerWireProtocol = new PeerWireProtocol("-JP0001-456726789357", "129.205.128.33", 52132);
            peerWireProtocol.handshake(torrentFile.getByteHash());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
