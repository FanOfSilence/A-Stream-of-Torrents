package tracker.response.http;

import java.util.List;
import java.util.Map;

/**
 * Created by Jesper on 2017-08-01.
 */
//TODO: Implement this class in case Concise response is not possible
public class FullResponse implements AnnounceResponse {
    @Override
    public List<String> getPeers() {
        return null;
    }

    @Override
    public boolean hasFailed() {
        return false;
    }

    @Override
    public int getTimeout() {
        return 0;
    }

    @Override
    public Map<String, Object> getAnnounceMap() {
        return null;
    }
}
