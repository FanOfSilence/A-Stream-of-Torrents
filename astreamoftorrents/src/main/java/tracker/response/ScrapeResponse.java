package tracker.response;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jesper on 2017-07-23.
 */
public class ScrapeResponse implements Response {
    private byte[] data;

    ScrapeResponse(byte[] data) {
        this.data = data;
    }

    @Override
    public Map<String, Integer> getResponse() throws ResponseException {
        ByteBuffer response = ByteBuffer.wrap(data);
        Map<String, Integer> scrapeResponse = new HashMap<>();
        scrapeResponse.put("action", response.getInt());
        scrapeResponse.put("transaction_id", response.getInt());
        scrapeResponse.put("seeders", response.getInt());
        scrapeResponse.put("completed", response.getInt());
        scrapeResponse.put("leechers", response.getInt());
        return scrapeResponse;
    }
}
