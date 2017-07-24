import java.io.*;
import java.net.*;
import java.util.HashMap;
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
            TorrentFile torrentFile = new TorrentFile("src/main/resources/unsort_dict.torrent");
            System.out.println(torrentFile.hash());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
