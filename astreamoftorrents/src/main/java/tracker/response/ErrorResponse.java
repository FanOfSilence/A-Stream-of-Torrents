package tracker.response;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Created by Jesper on 2017-07-23.
 */
public class ErrorResponse implements Response {
    private byte[] data;

    ErrorResponse(byte[] data) {
        this.data = data;
    }

    @Override
    public Map<String, Integer> getResponse() throws ResponseException {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int action = buffer.getInt();
        int trans_id = buffer.getInt();
        //TODO: Get the correct size and encoding for the string error message
        throw new ResponseException(String.format("Received action %d, transaction id %d with error message: %s",
                action, trans_id));

    }
}
