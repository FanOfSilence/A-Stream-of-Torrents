package client.process;

import peer.PeerWireProtocol;
import torrent.file.ReadableTorrentFile;
import torrent.state.WritableTorrentState;
import tracker.Tracker;
import tracker.response.AnnounceResponse;

import java.util.List;

/**
 * Created by Jesper on 2017-08-02.
 */
public class LeechProcess implements Runnable {
    //TODO: Should write to torrent state, keep one or several connections to trackers, and keep connections to peers
    private WritableTorrentState torrentState;
    private ReadableTorrentFile torrentFile;
    private List<Tracker> trackers;
    private List<String> ips;
    private List<PeerWireProtocol> peers;
    private List<Tracker> goToTrackers;
    private int timeoutInterval;


    public LeechProcess(WritableTorrentState torrentState) {
        this.torrentState = torrentState;
    }

    @Override
    public void run() {
        for (Tracker tracker : trackers) {
            AnnounceResponse response = tracker.announce(torrentFile.getByteHash());
            if (!response.hasFailed()) {
                goToTrackers.add(tracker);
                ips.addAll(response.getPeers());
                timeoutInterval = response.getTimeout();
                if (goToTrackers.size() == 2) {
                    break;
                }
            }
        }

        if (goToTrackers.size() == 0) {
            return;
        }

        //TODO: Set up connections to x amount of peers
        //TODO: Update torrent state
        //TODO: Once in a while contact trackers
    }
}
