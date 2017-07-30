package bencode.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Created by Jesper on 2017-07-19.
 */
public class BInteger<T extends Long> implements BType {
    static final char INTEGER_PREFIX = 'i';
    static final char INTEGER_POSTFIX = 'e';
    private Long number;
    private static final int UTF_OFFSET = 48;

    public BInteger(Long number) {
        this.number = number;
    }

    @Override
    public Long stringify() {
        return number;
    }

    @Override
    public Long getValue() {
        return number;
    }

    @Override
    public void encode(OutputStream os) {

    }

    @Override
    public String toString() {
        return "BInteger";
    }

    private boolean isNumber(int number) {
        return number >= '0' && number <= '9';
    }
}
