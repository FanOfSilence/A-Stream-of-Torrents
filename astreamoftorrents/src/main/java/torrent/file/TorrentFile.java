package torrent.file;

import bencode.BDecoder;
import bencode.BEncoder;
import bencode.type.BDict;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;

/**
 * Created by Jesper on 2017-07-09.
 */
public class TorrentFile implements ReadableTorrentFile {
    private byte[] byteHash;
    private String hash;

    private Map<String, Object> torrentMap;

    private Map<String, Object> infoMap;
    private Map<String, Object> stringMap;
    private File file;
    private List<byte[]> pieceList;

    public TorrentFile(String path) throws Exception {
        file = new File(path);
        readManifest();
        hash();

    }

    public String hash() throws Exception {
        if (hash == null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BEncoder encoder = new BEncoder(baos, "UTF-8");
            encoder.encodeDict(infoMap);
            byteHash = DigestUtils.sha1(baos.toByteArray());
            hash = DigestUtils.sha1Hex(baos.toByteArray());
            System.out.println(baos.toByteArray()[baos.toByteArray().length - 2]);
            System.out.println(hash);
        }
        return hash;
    }

    public void readManifest() throws IOException, Exception {
        byte[] fileBytes = FileUtils.readFileToByteArray(file);
        ByteArrayInputStream bis = new ByteArrayInputStream(fileBytes);
        BDecoder decoder = new BDecoder(bis, "UTF-8");
        BDict dict = decoder.decodeDict();
        torrentMap = dict.getValue();
        infoMap = (Map<String, Object>) torrentMap.get("info");
        stringMap = dict.stringify();
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

    @Override
    public List<byte[]> getPieceList() {
        if (pieceList == null) {
            byte[] pieces = (byte[]) infoMap.get("pieces");
            pieceList = new ArrayList<>(pieces.length / 20);
            for (int i = 0; i < pieces.length; i += 20) {
                byte[] addBytes = new byte[20];
                for (int j = 0; j < addBytes.length; j++) {
                    addBytes[j] = pieces[i];
                }
                pieceList.add(addBytes);
            }
        }
        return pieceList;
    }

    @Override
    public byte[] getByteHash() {
        return byteHash;
    }

    @Override
    public String getHexHash() {
        return hash;
    }
}
