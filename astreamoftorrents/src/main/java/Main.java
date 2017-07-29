import bencode.BDecoder;
import bencode.type.BDict;
import bencode.type.BString;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import torrent.TorrentFile;
import torrent.TorrentState;
import tracker.HttpTracker;

import java.io.*;
import java.net.*;
import java.util.Arrays;
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
            System.out.println(torrentFile.getStringMap().get("announce"));
            TorrentState state = new TorrentState();
            HttpTracker httpTracker = new HttpTracker(httpClient, (String) torrentFile.getStringMap().get("announce"),
                    "-JP0001-456726789357", 6969, state);
            torrentFile.hash();
            InputStream is = httpTracker.announce(torrentFile.getByteHash());

//            BDecoder decoder = new BDecoder()
            BDecoder decoder = new BDecoder(is, "UTF-8");
            BDict dict = decoder.decodeDict();
            Map<String, Object> dictMap = dict.getValue();
//            BS
//            System.out.println(dict.stringify());
            BString peers = (BString) dictMap.get("peers");
            System.out.println(Arrays.toString(peers.getValue()));
//            byte[] readBytes = new byte[1000];
//            is.read(readBytes);
//
//            System.out.println(Arrays.toString(readBytes));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
