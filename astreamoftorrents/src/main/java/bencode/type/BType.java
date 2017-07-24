package bencode.type;

import java.io.OutputStream;

/**
 * Created by Jesper on 2017-07-19.
 */
public interface BType<T> {

    T stringify();
    T getValue();
    void encode(OutputStream os);
}
