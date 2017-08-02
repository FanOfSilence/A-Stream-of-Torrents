package bencode.type;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Created by Jesper on 2017-07-19.
 */
public class BString<T extends String> implements BType {
    private byte[] byteString;
    private Charset charset;

    public BString(byte[] byteString, Charset charset) {
        this.byteString = byteString;
        this.charset = charset;
    }



    @Override
    public String stringify() {
//        encodedString = new String(byteString, charset);
        return new String(byteString, charset);
    }

    @Override
    public byte[] getValue() {
        return byteString;
    }

    @Override
    public void encode(OutputStream os) {

    }

    @Override
    public String toString() {
        return "BString";
    }
}
