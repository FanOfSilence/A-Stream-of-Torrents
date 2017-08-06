package client.process;

import peer.PeerStrategy;
import peer.PeerWireProtocol;
import peer.PieceStrategy;
import torrent.state.ReadableTorrentState;

import java.io.IOException;
import java.util.*;

/**
 * Created by Jesper on 2017-08-05.
 */
public class LeechStrategy implements PeerStrategy, Observer {
    private boolean sentHandshake;
    private ReadableTorrentState torrentState;
    private List<Integer> newPieces;
    private PieceStrategy pieceStrategy;

    public LeechStrategy(PieceStrategy pieceStrategy) {
        sentHandshake = false;
        this.pieceStrategy = pieceStrategy;
    }


    @Override
    public void execute(PeerWireProtocol peerWireProtocol) throws Exception {
        if (!sentHandshake) {
            peerWireProtocol.handshake();
            try {
                peerWireProtocol.parseHandshakeResponse();
            } catch (IOException io) {
                Thread.sleep(1000);
                try {
                    peerWireProtocol.parseHandshakeResponse();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    throw new IOException("No timely response after handshake");
                }
            }
        }
        if (true /* has interesting bits */) {
            peerWireProtocol.interested();
        }
        while (!torrentState.finished()) {
            for (int pieceIndex : newPieces) {
                if (!peerHas(peerWireProtocol, pieceIndex)) {
                    peerWireProtocol.have(pieceIndex);
                }
            }
            BitSet availablePieces = getInterestingPieces(peerWireProtocol);
            if (!availablePieces.isEmpty() && !peerWireProtocol.peerChoking() && peerWireProtocol.amInterested()) {
                Map<String, Integer> piece = pieceStrategy.getNextRequest(availablePieces);
                peerWireProtocol.request(piece.get("index"), piece.get("begin"), piece.get("length"));
            }

        }


    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ReadableTorrentState) {
            if (arg instanceof Integer) {
                newPieces.add((Integer) arg);
            }
        }
    }

    private boolean peerHas(PeerWireProtocol peerWireProtocol,int index) {
        return peerWireProtocol.getPeerHas().get(index);
    }

    private BitSet getInterestingPieces(PeerWireProtocol peerWireProtocol) {
        BitSet clone = (BitSet) torrentState.getFinishedPieces().clone();
        clone.flip(0, clone.length());
        clone.and(peerWireProtocol.getPeerHas());
        return clone;
    }
}
