package tracker.response.udp;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Jesper on 2017-07-23.
 */
public class AnnounceResponse implements Response {
    private byte[] data;

    AnnounceResponse(byte[] data) {
        this.data = data;
    }

    @Override
    public Map<String, Integer> getResponse() throws ResponseException {
        ByteBuffer response = ByteBuffer.wrap(data);
        Map<String, Integer> announceResponse = new LinkedHashMap<>(7);
        announceResponse.put("action", response.getInt());
        announceResponse.put("transaction_id", response.getInt());
        announceResponse.put("interval", response.getInt());
        announceResponse.put("leechers", response.getInt());
        announceResponse.put("seeders", response.getInt());
        announceResponse.put("IP address", response.getInt());
        announceResponse.put("TCP port", (int) response.getShort());
        return announceResponse;
    }
}
