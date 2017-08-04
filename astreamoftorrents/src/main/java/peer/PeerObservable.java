package peer;

import torrent.file.ReadableTorrentFile;
import torrent.state.ReadableTorrentState;

import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Jesper on 2017-08-04.
 */
public class PeerObservable extends Observable {
    private List<String> allPeers;
    private List<PeerWireProtocol> activePeers;
    private ReadableTorrentFile torrentFile;
    private ReadableTorrentState torrentState;
    private ExecutorService executor;

    public PeerObservable(List<String> peers, ReadableTorrentFile torrentFile, ReadableTorrentState torrentState) throws IOException {
        this.allPeers = peers;
        this.torrentFile = torrentFile;
        this.torrentState = torrentState;
        executor = Executors.newFixedThreadPool(5);

    }

    public void start() {

    }
}
