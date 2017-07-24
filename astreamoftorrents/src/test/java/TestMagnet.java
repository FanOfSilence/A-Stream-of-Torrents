import magnet.Magnet;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;

/**
 * Created by Jesper on 2017-06-03.
 */
public class TestMagnet {
    private Magnet magnet;

    @Before
    public void setUp() {
        URI uri = URI.create(MockMagnetString.magnetString);
        String testString = uri.getSchemeSpecificPart();
        magnet = new Magnet(testString);
    }
    @Test
    public void testMagnetProperties() {
        assert magnet.xt().equals("urn:btih:5c29c2615e13815c0466726c8ea76d77a32e6c42");
    }
}
