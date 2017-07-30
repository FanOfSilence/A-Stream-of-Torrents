package tracker;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import torrent.ReadableTorrentState;
import tracker.response.http.AnnounceResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

//TODO: Tracker should have a method to see if it was connectable for future keep-alive messages

/**
 * Created by Jesper on 2017-07-23.
 */
public class HttpTracker {

    private final String INFO_HASH = "info_hash";
    private final String PEER_ID = "peer_id";
    private final String PORT = "port";
    private final String UPLOADED = "uploaded";
    private final String DOWNLOADED = "downloaded";
    private final String LEFT = "left";
    private final String COMPACT = "compact";
    private final String EVENT = "event";
    private final String NUMWANT = "numwant";

    private final HttpClient client;
    private String ip;
    private String id;
    private int port;
    private ReadableTorrentState torrent;
    public HttpTracker(HttpClient client, String ip, String id, int port, ReadableTorrentState torrent) {
        this.client = client;
        this.ip = ip;
        this.id = id;
        this.port = port;
        this.torrent = torrent;
    }

    public AnnounceResponse announce(byte[] hash) throws URISyntaxException, IOException, Exception {
        URIBuilder uriBuilder = new URIBuilder(ip).setCharset(Charset.forName("ISO-8859-1"));
        uriBuilder.setParameter(INFO_HASH, new String(hash, "ISO-8859-1"));
        uriBuilder.setParameter(PEER_ID, id);
        uriBuilder.setParameter(PORT, "6881");
        uriBuilder.setParameter(UPLOADED, String.valueOf(torrent.getUploaded()));
        uriBuilder.setParameter(DOWNLOADED, String.valueOf(torrent.getDownloaded()));
        uriBuilder.setParameter(LEFT, String.valueOf(torrent.getLeft()));
        uriBuilder.setParameter(COMPACT, "1");
        uriBuilder.setParameter(EVENT, torrent.getEvent());
        uriBuilder.setParameter(NUMWANT, "200");

        URI uri = uriBuilder.build();
        System.out.println(uri.toString());

        HttpGet get = new HttpGet(uri);
        HttpResponse response = client.execute(get);
        if (response.getStatusLine().getStatusCode() == 200) {
            return new AnnounceResponse(response.getEntity().getContent());
        }
        throw new Exception(String.format("Got back a %d response when announcing", response.getStatusLine().getStatusCode()));
    }
}
