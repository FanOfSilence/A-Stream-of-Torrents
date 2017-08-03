package torrent.file;

import torrent.piece.Piece;

import java.util.List;
import java.util.Map;

/**
 * Created by Jesper on 2017-08-02.
 */
public interface ReadableTorrentFile {
    List<Piece> getPieceList();
    byte[] getByteHash();
    String getHexHash();
}
