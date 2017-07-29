package tracker.response.udp;


/**
 * Created by Jesper on 2017-07-23.
 */
public class ResponseCreator {
    private ResponseCreator() {}

    public static Response createResponse(byte[] responseData) {
        int action = responseData[0];
        switch (action) {
            case 0:
                return new ConnectResponse(responseData);
            case 1:
                return new AnnounceResponse(responseData);
            case 2:
                return new ScrapeResponse(responseData);
            default:
                return new ErrorResponse(responseData);
        }
    }
}
