package tracker.response;

import java.util.Map;

/**
 * Created by Jesper on 2017-07-23.
 */
interface Response {
    Map<String, Integer> getResponse() throws ResponseException;
}
