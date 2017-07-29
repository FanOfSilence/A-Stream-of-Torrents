package torrent;

import bencode.BDecoder;
import bencode.BEncoder;
import bencode.type.BDict;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Map;

/**
 * Created by Jesper on 2017-07-09.
 */
public class TorrentFile {
    private byte[] byteHash;
    private String hash;

    private Map<String, Object> torrentMap;

    private Map<String, Object> infoMap;
    private Map<String, Object> stringMap;
    private File file;
    public byte[] hash1 = new byte[20];

    public TorrentFile(String path) throws Exception {
        file = new File(path);
        readManifest();

    }

    public String hash() throws Exception {
        if (hash == null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BEncoder encoder = new BEncoder(baos, "UTF-8");
            encoder.encodeDict(infoMap);
            byteHash = DigestUtils.sha1(baos.toByteArray());
            hash = DigestUtils.sha1Hex(baos.toByteArray());
            System.out.println(hash);
        }
        return hash;
    }
    //TODO:GEneralise to become recursive
    private void stringifyValues(BDecoder decoder, Object decodedObject) throws Exception {
        if (decodedObject instanceof Map) {
            for (Object entry : ((Map<String, Object>) decodedObject).entrySet()) {
//                ((Map) decodedObject).put(entry.)
            }
        }
        for (Map.Entry entry : torrentMap.entrySet()) {
//            if (entry.getValue() instanceof List) {
//                for (Object object : (List) entry.getValue()) {
//                    ((List) entry.getValue()).remove(object);
//                    ((List) entry.getValue()).add(decoder.getEncodedString((byte[]) object));
//
//                }
//            }
        }
//        torrentMap.put("announce", decoder.getEncodedString((byte[]) torrentMap.announce("announce")));
    }
    public void readManifest() throws IOException, Exception {
        byte[] fileBytes = FileUtils.readFileToByteArray(file);
        ByteArrayInputStream bis = new ByteArrayInputStream(fileBytes);
        BDecoder decoder = new BDecoder(bis, "UTF-8");
        BDict dict = decoder.decodeDict();
        torrentMap = dict.getValue();
        infoMap = (Map<String, Object>) torrentMap.get("info");
//        for (Map.Entry entry : valueMap.entrySet()) {
//            System.out.println(entry.getKey());
//            System.out.println(entry.getValue());
//        }
//        System.out.println(hash());

//        infoMap = (Map<String, Object>) torrentMap.announce("info");
        stringMap = dict.stringify();
//        stringifyValues(decoder);
//        for (Map.Entry entry : stringMap.entrySet()) {
//            System.out.println(entry.getKey());
//            System.out.println(entry.getValue());
//        }
    }

    public Map<String, Object> getTorrentMap() {
        return torrentMap;
    }

    public Map<String, Object> getInfoMap() {
        return infoMap;
    }

    public Map<String, Object> getStringMap() {
        return stringMap;
    }

    public byte[] getByteHash() {
        return byteHash;
    }
}
