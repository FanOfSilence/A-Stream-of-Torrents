package bencode.type;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Jesper on 2017-07-19.
 */
public class BDict<T extends Map> implements BType {
    private Map<String, BType> dict;
    private Charset charset;

    public BDict(Map<String, BType> dict, Charset charset) {
        this.dict = dict;
        this.charset = charset;
    }


    @Override
    public Map<String, Object> stringify() {
        Map<String, Object> newDict = new LinkedHashMap<>();
        for (Map.Entry<String, BType> entry : dict.entrySet()) {
//            dict.put( entry.getKey(), entry.getValue().stringify());
            newDict.put(entry.getKey(), entry.getValue().stringify());
        }
        return newDict;
    }

    @Override
    public Map<String, Object> getValue() {
        Map<String, Object> newDict = new LinkedHashMap<>();
        for (Map.Entry<String, BType> entry : dict.entrySet()) {
            newDict.put(entry.getKey(), entry.getValue().getValue());
        }
        return newDict;
    }

    @Override
    public void encode(OutputStream os) {

    }
}
