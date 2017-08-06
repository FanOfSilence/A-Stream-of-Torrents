package peer;

import java.util.BitSet;
import java.util.Map;

/**
 * Created by Jesper on 2017-08-06.
 */
public interface PieceStrategy {
    Map<String, Integer> getNextRequest(BitSet bitSet);
}
