package tracker.response;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jesper on 2017-07-23.
 */
public class ConnectResponse implements Response {
    private byte[] data;

    ConnectResponse(byte[] data) {
        this.data = data;
    }

    @Override
    public Map<String, Integer> getResponse() throws ResponseException {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        Map<String, Integer> response = new HashMap<>(4);
        response.put("action", buffer.getInt());
        response.put("transactionId", buffer.getInt());
        response.put("connectionId0", buffer.getInt());
        response.put("connectionId1", buffer.getInt());
        return response;
    }
}
