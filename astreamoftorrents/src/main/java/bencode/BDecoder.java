package bencode;

import bencode.type.*;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static bencode.Constants.*;

/**
 * Created by Jesper on 2017-07-16.
 */
public class BDecoder {
    private static final int COLON_SEPARATOR = 0x3b;
    private static final int UTF_OFFSET = 48;

    private InputStream inputStream;
    private Charset charset;
    public BDecoder(InputStream inputStream, String charset) throws IOException {
        if (inputStream.markSupported()) {
            this.inputStream = inputStream;
        } else {
            byte[] b = IOUtils.toByteArray(inputStream);
//            inputStream.read(b);
            this.inputStream = new ByteArrayInputStream(b);
        }
        this.charset = Charset.forName(charset);
    }

    public BDecoder(InputStream inputStream) {
        this.inputStream = inputStream;
    }

//    public BDecoder(byte[] b, String charset) {
//        this.inputStream = new ByteArrayInputStream(b);
//        this.charset = Charset.forName(charset);
//    }

    public BInteger decodeInteger() throws IOException, Exception {
        int prefix = inputStream.read();
        if (prefix != INTEGER_PREFIX) {
            throw new Exception(String.format("Integer has to be prefixed by '%c'", INTEGER_PREFIX));
        }
        int result = 0;
        int next = inputStream.read();
        while (next != INTEGER_POSTFIX) {
            result *= 10;
            if (!isNumber(next)) {
                throw new Exception("Integer can only contain numbers");
            }
            result += next - UTF_OFFSET;
            next = inputStream.read();
        }
        return new BInteger(result);
    }

    public BDict decodeDict() throws IOException, Exception {
        int prefix = inputStream.read();
        if (prefix != DICT_PREFIX) {
            throw new Exception("Not a dictionary");
        }
        Map<String, BType> dict = new LinkedHashMap<>();

        int next = peek();
        while (next != DICT_POSTFIX) {
//            String key = getEncodedString(decodeByteString());
            String key = decodeByteString().stringify();
            BType value = decodeObject();
            dict.put(key, value);
            next = peek();
        }
        inputStream.read();
        return new BDict(dict, charset);
    }

    public BString decodeByteString() throws IOException, Exception {
        int length = getStringLength();

        byte[] b = new byte[length];

        int read = 0;
        while (read < length)
        {
            int i = inputStream.read(b, read, length - read);
            read += i;
        }
//        for (int i = 0; i < length; i++) {
//            b[i] = (byte) inputStream.read();
//        }
//        inputStream.read(b, 0, length);
        return new BString(b, charset);
    }

//    public String getEncodedString(byte[] bytes) throws Exception {
//        if (charset == null) {
//            throw new Exception("Need to specfiy a charset to announce a string");
//        }
//        return new String(bytes, charset);
//    }

//    public String getEncodedString(byte[] bytes, String charset) throws Exception {
//        return new String(bytes, charset);
//    }

    private int getStringLength() throws IOException, Exception {
        int length = 0;
        int next = inputStream.read();
        while (next != STRING_SEPARATOR) {
            length *= 10;
            if (!isNumber(next)) {
                throw new Exception(String.format("Invalid character %d in String length", next));
            }
            length += next - UTF_OFFSET;
            next = inputStream.read();
        }
        return length;
    }
    public BList decodeList() throws IOException, Exception {
        List result = new ArrayList();
        if (inputStream.read() != LIST_PREFIX) {
            throw new Exception("List neeeds to start with l");
        }
        while (peek() != LIST_POSTFIX) {
            result.add(decodeObject());
        }
        inputStream.read();
        return new BList(result);
    }

//    }

    public BType decodeObject() throws IOException, Exception {
        int next = peek();
        if (next == INTEGER_PREFIX) {
            return decodeInteger();
        } else if (next == LIST_PREFIX) {
            return decodeList();
        } else if (isNumber(next)) {
            return decodeByteString();
        } else if (next == DICT_PREFIX){
            return decodeDict();
        }
        throw new Exception("Couldn't decode object");
    }

    private int peek() throws IOException {
        inputStream.mark(1);
        int result = inputStream.read();
        inputStream.reset();
        return result;
    }

    private boolean isNumber(int number) {
        return number >= '0' && number <= '9';
    }
}
