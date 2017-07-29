package bencode.type;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jesper on 2017-07-19.
 */
public class BList<T extends List> implements BType {
    private List<BType> list;

    public BList(List<BType> list) {
        this.list = list;
    }

    @Override
    public List stringify() {
        List<Object> newList = new ArrayList<>();
        for (BType type : list) {
            newList.add(type.stringify());
        }
        return newList;
    }

    @Override
    public List<Object> getValue() {
        List<Object> newList = new ArrayList<>();
        for (BType type : list) {
            newList.add(type.getValue());
        }
        return newList;
    }

    @Override
    public void encode(OutputStream os) {

    }

    @Override
    public String toString() {
        return "BList";
    }
}
