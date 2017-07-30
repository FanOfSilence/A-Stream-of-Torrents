package bencode;

import bencode.type.BType;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import static bencode.Constants.*;

/**
 * Created by Jesper on 2017-07-18.
 */
public class BEncoder {
    private OutputStream outputStream;
    private Charset charset;

    public BEncoder(OutputStream outputStream, String charset) {
        this.outputStream = outputStream;
        this.charset = Charset.forName(charset);
    }

    public void encodeString(String decodedString) throws IOException {
        String length = String.valueOf(decodedString.length());
        outputStream.write(length.getBytes(charset));
        outputStream.write(STRING_SEPARATOR);
        outputStream.write(decodedString.getBytes(charset));
    }

    public void encodeByteString(byte[] decodedBytes) throws IOException {
        String length = String.valueOf(decodedBytes.length);
        outputStream.write(length.getBytes(charset));
        outputStream.write(STRING_SEPARATOR);
        outputStream.write(decodedBytes);
    }

    public void encodeInteger(Long decodedInteger) throws IOException {
        outputStream.write(INTEGER_PREFIX);
        outputStream.write(decodedInteger.toString().getBytes(charset));
        outputStream.write(INTEGER_POSTFIX);
    }

    public void encodeList(List decodedList) throws IOException, Exception {
        outputStream.write(LIST_PREFIX);
        for (Object obj : decodedList) {
            encodeObject(obj);
        }
        outputStream.write(LIST_POSTFIX);
    }

    public void encodeDict(Map<String, Object> decodedDict) throws IOException, Exception {
        outputStream.write(DICT_PREFIX);
        for (Map.Entry<String, Object> entry : decodedDict.entrySet()) {
            encodeString(entry.getKey());
            encodeObject(entry.getValue());
        }
        outputStream.write(DICT_POSTFIX);
    }

    public void encodeObject(Object decodedObject) throws IOException, Exception {
        if (decodedObject instanceof String) {
            encodeString((String) decodedObject);
        } else if (decodedObject instanceof byte[]) {
            encodeByteString((byte[]) decodedObject);
        } else if (decodedObject instanceof Long) {
            encodeInteger((Long) decodedObject);
        } else if (decodedObject instanceof List) {
            encodeList((List) decodedObject);
        } else if (decodedObject instanceof Map) {
            encodeDict((Map<String, Object>) decodedObject);
        }
    }

    public void encodeBType(BType type) {
        type.encode(outputStream);
    }
}
