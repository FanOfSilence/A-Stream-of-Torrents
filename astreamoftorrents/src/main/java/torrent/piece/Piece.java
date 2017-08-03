package torrent.piece;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jesper on 2017-08-02.
 */
public class Piece {
    private List<Byte> bytes;
    private byte[] hash;
    private boolean isLastPiece;

    public Piece(int size, byte[] hash, boolean isLastPiece) {
        //If size is too big it will overflow but it shouldn't happen with current implementations
        try {
            this.bytes = new ArrayList<>(size);
        } catch (IllegalArgumentException e) {
            this.bytes = new LinkedList<>();
        }
        this.hash = hash;
        this.isLastPiece = isLastPiece;
    }

    public boolean ok() {
        Byte[] copyBytes = new Byte[bytes.size()];
        byte[] compareHash = DigestUtils.sha1(ArrayUtils.toPrimitive(bytes.toArray(copyBytes)));
        return Arrays.equals(hash, compareHash);
    }
}
