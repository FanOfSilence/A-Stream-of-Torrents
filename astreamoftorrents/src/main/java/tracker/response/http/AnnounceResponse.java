package tracker.response.http;

import java.util.Map;

/**
 * Created by Jesper on 2017-08-01.
 */
public interface AnnounceResponse extends tracker.response.AnnounceResponse {
    boolean hasFailed();
    Map<String, Object> getAnnounceMap();
}
