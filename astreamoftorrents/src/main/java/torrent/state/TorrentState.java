package torrent.state;

import java.util.Observable;

/**
 * Created by Jesper on 2017-07-24.
 */

//TODO: Have one constructor for torrent piece length for a new torrent and one for the bytes already downloaded
public class TorrentState extends Observable implements WritableTorrentState {
    private final String STARTED = "started";
    private final String STOPPED = "stopped";
    private final String COMPLETED = "completed";
    private int uploaded;
    private int downloaded;
    private final String property = "java.io.tmpdir";
    private String tempDir;

    //List<File> TODO: For writing out the actual pieces to the files
    //File List<BitMap> TODO: For keeping track of which pieces of which files have been downloaded
    //TODO: Piece is a class and bitmap goes from bit to Piece

    public TorrentState() {
        tempDir = System.getProperty(property) + "AStreamOfTorrents";
    }


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
