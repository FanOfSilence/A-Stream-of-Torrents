package tracker;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//TODO: Tracker should have a method to see if it was connectable for future keep-alive messages

/**
 * Created by Jesper on 2017-07-23.
 */
public class HttpTracker {
    private final HttpClient client;
    private String ip;
    private String id;
    private int port;
    public HttpTracker(HttpClient client, String ip, String id, int port) {
        this.client = client;
        this.ip = ip;
        this.id = id;
        this.port = port;
    }

    public void get(String hash) throws URISyntaxException, IOException {
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setHost(ip).setPort(port);
        uriBuilder.setParameter("info_hash", hash);
        uriBuilder.setParameter("peer_id", id);
        uriBuilder.setParameter("port", "6881");
        uriBuilder.setParameter("uploaded", "0");
        

        URI uri = uriBuilder.build();
//        RequestBuilder requestBuilder = RequestBuilder.get(uri);
//        requestBuilder.

        HttpGet get = new HttpGet(uri);
        HttpResponse response = client.execute(get);
    }
}
