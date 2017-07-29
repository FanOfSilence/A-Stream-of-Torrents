package torrent;

/**
 * Created by Jesper on 2017-07-24.
 */

//TODO: Have one constructor for torrent piece length for a new torrent and one for the bytes already downloaded
public class TorrentState implements WritableTorrentState{
    private final String STARTED = "started";
    private final String STOPPED = "stopped";
    private final String COMPLETED = "completed";
    private int uploaded;
    private int downloaded;


    private byte[] pieces;

    //TODO: One bit for each byte? For seeing what bytes has been downloaded and counting how much is left

    @Override
    public int getUploaded() {
        return 0;
    }

    @Override
    public int getDownloaded() {
        return 0;
    }

    @Override
    public int getLeft() {
        return 3295839;
    }

    @Override
    public String getEvent() {
        return STARTED;
    }
}
