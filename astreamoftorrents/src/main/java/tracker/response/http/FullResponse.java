package tracker.response.http;

import java.util.Map;

/**
 * Created by Jesper on 2017-08-01.
 */
//TODO: Implement this class in case Concise response is not possible
public class FullResponse implements AnnounceResponse {
    @Override
    public boolean hasFailed() {
        return false;
    }

    @Override
    public Map<String, Object> getAnnounceMap() {
        return null;
    }
}
