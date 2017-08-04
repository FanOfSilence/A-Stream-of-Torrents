package tracker.response;

import java.util.List;

/**
 * Created by Jesper on 2017-08-02.
 */
public interface AnnounceResponse {
    //Includes both ip and port
    List<String> getPeers();
    boolean hasFailed();
    int getTimeout();
}
