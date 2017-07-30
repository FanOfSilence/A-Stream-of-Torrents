import bencode.BDecoder;
import bencode.type.BDict;
import bencode.type.BString;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import torrent.TorrentFile;
import torrent.TorrentState;
import tracker.HttpTracker;
import tracker.response.http.AnnounceResponse;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
            TorrentFile torrentFile = new TorrentFile("src/main/resources/mma_the_rest_on_the_flight_into_egypt_438025_archive.torrent");
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
