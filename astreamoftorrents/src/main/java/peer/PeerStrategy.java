package peer;

/**
 * Created by Jesper on 2017-08-05.
 */
public interface PeerStrategy {
    void execute(PeerWireProtocol peerWireProtocol) throws Exception;
}
