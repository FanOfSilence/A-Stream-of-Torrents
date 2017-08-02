package client.process;

import peer.PeerWireProtocol;
import torrent.file.ReadableTorrentFile;
import torrent.state.TorrentState;
import tracker.Tracker;

import java.util.List;

/**
 * Created by Jesper on 2017-08-02.
 */
public class LeechProcess {
    //TODO: Should write to torrent state, keep one or several connections to trackers, and keep connections to peers
    private TorrentState torrentState;
    private List<Tracker> trackers;
    private List<String> ips;
    private List<PeerWireProtocol> peers;
    private ReadableTorrentFile torrentFile;


    public LeechProcess(TorrentState torrentState) {
        this.torrentState = torrentState;
    }

    public LeechProcess() {
        this(new TorrentState());
    }
}
