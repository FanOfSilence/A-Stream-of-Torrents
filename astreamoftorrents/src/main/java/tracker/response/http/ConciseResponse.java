package tracker.response.http;

import bencode.BDecoder;
import bencode.type.BDict;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jesper on 2017-07-29.
 */
public class ConciseResponse implements AnnounceResponse {
    Map<String, Object> announceMap;
    private static final int IP_AND_PORT_OFFSET = 6;

    public ConciseResponse(InputStream inputStream) throws Exception {
        BDecoder decoder = new BDecoder(inputStream, "UTF-8");
        BDict dict = decoder.decodeDict();
        announceMap = dict.stringify();
        if (!hasFailed()) {
            parsePeers(dict);
        }
    }

    private void parsePeers(BDict dict) throws Exception {
        byte[] peerBytes = (byte[]) dict.getValue().get("peers");
        if (peerBytes.length % IP_AND_PORT_OFFSET != 0) {
            throw new Exception("Peers in announce response are of illegal length");
        }
        List<String> peerList = new ArrayList<>();
        for (int i = 0; i < peerBytes.length; i += IP_AND_PORT_OFFSET) {
            int portHigh = Byte.toUnsignedInt(peerBytes[i + 4]);
            int portLow = Byte.toUnsignedInt(peerBytes[i + 5]);
            //Parses the peerBytes into a String of the form '127.0.0.1:8080'
            peerList.add(String.format("%d.%d.%d.%d:%d", Byte.toUnsignedInt(peerBytes[i]),
                    Byte.toUnsignedInt(peerBytes[i + 1]), Byte.toUnsignedInt(peerBytes[i + 2]),
                    Byte.toUnsignedInt(peerBytes[i + 3]), (portHigh << 8) + portLow));
        }
        announceMap.put("peers", peerList);
    }

    @Override
    public boolean hasFailed() {
        return announceMap.containsKey("failure reason");
    }

    @Override
    public Map<String, Object> getAnnounceMap() {
        return announceMap;
    }
}
