import bencode.BEncoder;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by Jesper on 2017-07-18.
 */
public class TestBEncoder {

    @Test
    public void testString() throws IOException {
        String testString = "lengthLength";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BEncoder encoder = new BEncoder(baos, "UTF-8");
        encoder.encodeString(testString);
        assert (baos.toString().equals(String.format("12:%s", testString)));
    }

    @Test
    public void testByteString() throws IOException {
        byte[] string = {-84, 21, -9, 49, 44, -38, -122, 11, 91, 104, 17, 15, -28, 119, 107, 76, -28, -90, 19, 80,
                47, 69, -16, 85, 100, 36, 1, 112, -123, 40, -71, 94, 13, -81, -33, 103, -59, -52, -31, -98, 127, -111,
                75, -21, 31, 8, 91, 24, -119, 105, -93, -40, 63, 113, 41, 40, -83, 64, 18, -45, -58, -66, 82, 60, 7,
                92, -14, -11, -87, -75, -124, -4, -15, 10, 125, 62, 70, 106, 109, 59, 59, 32, 47, 77, 123, -45, 67,
                -25, -19, -32, -88, 96, 40, -74, -67, 0, -38, 26, -58, -27, 118, 15, 49, 58, 100, 9, 117, -94, 36, 8,
                81, 84, -44, 29, 87, 116, 126, -43, -117, 35, 95, 15, -76, 79, 65, 94,
                125, -52, 73, 81, 29, -70, 66, -20, -63, -12, -70, 88, 54, 35};
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BEncoder encoder = new BEncoder(baos, "UTF-8");
        encoder.encodeByteString(string);
        System.out.println(Arrays.toString(baos.toByteArray()));
    }

    @Test
    public void testInteger() throws IOException {
        Integer number = 3434343;
        String numberString = "3434343";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BEncoder encoder = new BEncoder(baos, "UTF-8");
        encoder.encodeInteger(number);
        System.out.println(baos.toString());
        assert (baos.toString().equals(String.format("i%se", numberString)));
    }

    @Test
    public void testList() throws Exception {
        List<Object> decodedList = new ArrayList<>();
        decodedList.add("Spam");
        decodedList.add(454645);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BEncoder encoder = new BEncoder(baos, "UTF-8");
        encoder.encodeList(decodedList);
        assert baos.toString("UTF-8").equals("l4:Spami454645ee");
    }

    @Test
    public void testDict() throws Exception {
        Map<String, Object> decodedMap = new LinkedHashMap<>();
        Map<String, Object> decodedInfo = new LinkedHashMap<>();
        decodedInfo.put("length", 1);
        decodedInfo.put("name", "a.txt");
        decodedInfo.put("piece length", 32768);
        decodedInfo.put("pieces", "1234567890abcdefghij");
        decodedMap.put("info", decodedInfo);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BEncoder encoder = new BEncoder(baos, "UTF-8");
        encoder.encodeDict(decodedMap);
        System.out.println(baos.toString());
        assert baos.toString().equals("d4:infod6:lengthi1e4:name5:a.txt12:piece lengthi32768e6:pieces20:1234567890abcdefghijee");
    }
}
