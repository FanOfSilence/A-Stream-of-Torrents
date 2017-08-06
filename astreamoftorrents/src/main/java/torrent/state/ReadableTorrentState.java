package torrent.state;

import java.util.BitSet;

/**
 * Created by Jesper on 2017-07-24.
 */
public interface ReadableTorrentState extends IObservable {
    int getUploaded();
    int getDownloaded();
    int getLeft();
    String getEvent();
    boolean finished();
    BitSet getFinishedPieces();
}
