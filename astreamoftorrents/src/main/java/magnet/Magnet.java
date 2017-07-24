package magnet;

/**
 * Created by Jesper on 2017-06-03.
 */

//TODO: Completely rewrite this, using a better way to parse
public class Magnet {
    private String dn;
    private String xl;
    private String xt;
    private String as;
    private String xs;
    private String kt;
    private String mt;
    private String tr;

    private String uri;

    public Magnet(String uri) {
        this.uri = uri;
        parseMagnet();
    }

    private void parseMagnet() {
        this.xt = parse('x', 't');
    }

    private String parse(char first, char second) {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < uri.length(); i++) {
            if (uri.charAt(i) == first) {
                if (uri.charAt(i + 1) == second && uri.charAt(i + 2) == '=') {
                    int j = i + 3;
                    while (uri.charAt(j) != '&') {
                        temp.append(uri.charAt(j));
                        j++;
                    }
                }
            }
        }
        return temp.toString();
    }

    @Override
    public String toString() {
        String toString = "dn: " + dn + "\nxl: " + xl + "\nxt: " +xt;
        return toString;
    }

    public static boolean isMagnet(String checkString) {
        return false;
    }

    public String dn() {
        return dn;
    }

    public String xl() {
        return xl;
    }

    public String xt() {
        return xt;
    }

    public String as() {
        return as;
    }

    public String xs() {
        return xs;
    }

    public String kt() {
        return kt;
    }

    public String mt() {
        return mt;
    }

    public String tr() {
        return tr;
    }


}
