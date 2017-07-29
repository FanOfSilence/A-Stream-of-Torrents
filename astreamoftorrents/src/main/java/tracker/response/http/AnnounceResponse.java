package tracker.response.http;

import bencode.BDecoder;
import bencode.type.BDict;

import java.io.InputStream;
import java.util.Map;

/**
 * Created by Jesper on 2017-07-29.
 */
public class AnnounceResponse {
    Map<String, Object> announceMap;
    private static final int IP_AND_PORT_OFFSET = 5;

    public AnnounceResponse(InputStream inputStream) throws Exception {
        BDecoder decoder = new BDecoder(inputStream, "UTF-8");
        BDict dict = decoder.decodeDict();
        announceMap = dict.stringify();
        parsePeers(dict);
    }

    private void parsePeers(BDict dict) {
        byte[] peerBytes = (byte[]) dict.getValue().get("peers");
        for (int i = 0; i < peerBytes.length; i += IP_AND_PORT_OFFSET) {
            //TODO: parse out the ip from first 4 bytes and the port from last 2
        }
    }

    public boolean hasFailed() {
        return announceMap.containsKey("failure reason");
    }

    public Map<String, Object> getAnnounceMap() {
        return announceMap;
    }
}
