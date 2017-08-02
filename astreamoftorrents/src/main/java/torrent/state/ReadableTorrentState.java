package torrent.state;

/**
 * Created by Jesper on 2017-07-24.
 */
public interface ReadableTorrentState {
    int getUploaded();
    int getDownloaded();
    int getLeft();
    String getEvent();
}
