import bencode.BDecoder;
import bencode.type.BDict;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Created by Jesper on 2017-07-16.
 */
public class TestBDecoder {
    private BDecoder decoder;

    @Before
    public void setUp() {
//        decoder = new bencode.BDecoder("UTF-8");
    }

    @Test
    public void testString() throws Exception {
        String length = "lengthLength";
        InputStream stubInputStream =
                IOUtils.toInputStream(String.format("12:%s", length), "UTF-8");
        decoder = new BDecoder(stubInputStream, "UTF-8");
        String after = decoder.decodeByteString().stringify();
        assert (after.equals(length));
    }

    @Test
    public void testString2() {

    }

    @Test
    public void testInt() throws Exception{
        int testInt = 43535;
        InputStream intInputStream = IOUtils.toInputStream(String.format("i%de", testInt), "UTF-8");
        decoder = new BDecoder(intInputStream, "UTF-8");
        int after = (int) decoder.decodeInteger().getValue();
        assert (after == testInt);
    }

    @Test
    public void testList() throws Exception {
        String stringPart = "Spam";
        int intPart = 42435;
        InputStream listInputStream = IOUtils.toInputStream(String.format("l4:%si%dee", stringPart, intPart), "UTF-8");
        decoder = new BDecoder(listInputStream, "UTF-8");
        List after = decoder.decodeList().stringify();
        assert (after.size() == 2);
        assert after.get(0).equals(stringPart);
        assert (after.get(1).equals(intPart));
    }

    @Test
    public void testListWithinList() throws Exception {
        String listString = "ll36:http://bt1.archive.org:6969/announceel36:http://bt2.archive.org:6969/announceee";
        InputStream listInputStream = IOUtils.toInputStream(listString);
        decoder = new BDecoder(listInputStream, "UTF-8");
//        List after = decoder.decodeList();
//        System.out.println(decoder.getEncodedString((byte[]) ((List)after.announce(1)).announce(0)));
    }

    @Test
    public void testDict() throws Exception {
//        String key0 = "info";
        InputStream dictInputStream = IOUtils.toInputStream("d4:infod6:lengthi1e4:name5:a.txt12:piece lengthi32768e6:pieces20:1234567890abcdefghijee", "UTF-8");
        decoder = new BDecoder(dictInputStream, "UTF-8");
//        Map<String, Object> after = decoder.decodeDict();
//        for (Map.Entry entry : after.entrySet()) {
//            System.out.println("----------------------------");
//            System.out.println(entry.getKey());
//            System.out.println(entry.getValue());
//        }
    }
}
