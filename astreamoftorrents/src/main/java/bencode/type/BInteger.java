package bencode.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Created by Jesper on 2017-07-19.
 */
public class BInteger<T extends Integer> implements BType {
    static final char INTEGER_PREFIX = 'i';
    static final char INTEGER_POSTFIX = 'e';
    private Integer number;
    private static final int UTF_OFFSET = 48;

    public BInteger(Integer number) {
        this.number = number;
    }

    @Override
    public Integer stringify() {
        return number;
    }

    @Override
    public Object getValue() {
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
