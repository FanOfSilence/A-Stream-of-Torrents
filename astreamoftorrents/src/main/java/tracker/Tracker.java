package tracker;

import tracker.response.AnnounceResponse;

/**
 * Created by Jesper on 2017-08-02.
 */
public interface Tracker {
    AnnounceResponse announce(byte[] hash);
}
