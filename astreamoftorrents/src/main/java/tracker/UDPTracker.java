package tracker;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import  org.apache.commons.codec.digest.DigestUtils;



/**
 * Created by Jesper on 2017-06-28.
 */
public class UDPTracker {
    private int transId;
    private static final int UDP_SCRAPE = 2;
    private static final int UDP_ANNOUNCE = 1;
    private static final int UDP_CONNECT = 0;
    private static final int EVENT_NONE = 0;
    private static final int EVENT_COMPLETED = 1;
    private static final int EVENT_STARTED = 2;
    private static final int EVENT_STOPPED = 3;
    private final long protocolId = 0x41727101980L;
    private int hostPort;
    private InetAddress hostAddress;
    private InetSocketAddress socketAddress;
    //TODO: Tracker should return a Response
    Map<String, Integer> connectResponse;
    Map<String, Integer> scrapeResponse;
    Map<String, Integer> announceResponse;
    private DatagramSocket clientSocket;
    //TODO: Random number for peer_id, insert it from client
    private String peer_id = "-JP0001-456726789357";

    public UDPTracker(int port, String dns, int hostPort) throws UnknownHostException, SocketException {
        this.socketAddress = new InetSocketAddress(port);
        try {
            this.hostAddress = InetAddress.getByName(dns);
        } catch (UnknownHostException e) {
            throw new UnknownHostException("Could not resolve dns: " + dns + e);
        }
        this.hostPort = hostPort;
        //TODO: random port number
        openSocket();
    }

    public Map connect() throws Exception {
        final ByteArrayOutputStream baos=new ByteArrayOutputStream();
        try (DataOutputStream daos = new DataOutputStream(baos)) {
            int protocolId0 = 0x417;
            daos.writeInt(protocolId0);
            int protocolId1 = 0x27101980;
            daos.writeInt(protocolId1);
            daos.writeInt(0);
            daos.writeInt(54);
            daos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final byte[] bytes = baos.toByteArray();
        baos.close();
        try {
            clientSocket.connect(hostAddress, hostPort);
            send(bytes);
            DatagramPacket receivePacket = receive();
            return parseConnectResponse(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Couldn't connect to tracker because: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    private Map parseConnectResponse(DatagramPacket receivePacket) throws Exception {
        if (receivePacket.getLength() == 16) {
            ByteBuffer buffer = ByteBuffer.wrap(receivePacket.getData());
            Map<String, Integer> response = new HashMap<>(4);
            response.put("action", buffer.getInt());
            response.put("transactionId", buffer.getInt());
            response.put("connectionId0", buffer.getInt());
            response.put("connectionId1", buffer.getInt());
            this.connectResponse = response;
            return response;
        } else {
            throw new Exception("Connection response was not 16 bytes long");
        }
    }
    private void send(byte[] sendData) throws IOException {
        if (!socketIsOpen()) {
            throw new SocketException("Socket needs to be open to be able to send");
        }
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length);
        try {
            clientSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Couldn't send message to tracker");
        }
    }

    public DatagramPacket receive() throws IOException {
        if (!socketIsOpen()) {
            throw new SocketException("Socket needs to be open to be able to receive");
        }
        byte[] received = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(received, received.length);
        try {
            clientSocket.receive(receivePacket);
            System.out.println("RECEIVING!!!!!!!");
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Didn't receive anything from tracker");
        }
        return receivePacket;
    }

    public Map scrape(String hash) throws Exception {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);
        dataStream.writeInt(connectResponse.get("connectionId0"));
        dataStream.writeInt(connectResponse.get("connectionId1"));
        dataStream.writeInt(UDP_SCRAPE);
        dataStream.writeInt(connectResponse.get("transactionId"));
        dataStream.write(Hex.decodeHex(hash.toCharArray()));
        dataStream.close();
        final byte[] scrapeBytes= byteStream.toByteArray();
        send(scrapeBytes);
        return parseScrapeResponse(receive());
    }

    private Map parseScrapeResponse(DatagramPacket receivePacket) throws Exception {
        if (receivePacket.getLength() < 8) {
            throw new Exception("Scrape response is too short");
        }
        ByteBuffer response = ByteBuffer.wrap(receivePacket.getData());
        scrapeResponse = new HashMap<>();
        scrapeResponse.put("action", response.getInt());
        scrapeResponse.put("transaction_id", response.getInt());
        scrapeResponse.put("seeders", response.getInt());
        scrapeResponse.put("completed", response.getInt());
        scrapeResponse.put("leechers", response.getInt());
        return scrapeResponse;
    }

    public Map<String, Integer> announce(String hash) throws Exception {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);
        dataStream.writeInt(connectResponse.get("connectionId0"));
        dataStream.writeInt(connectResponse.get("connectionId1"));
        dataStream.writeInt(UDP_ANNOUNCE);
        dataStream.writeInt(connectResponse.get("transactionId"));
        dataStream.write(Hex.decodeHex(hash.toCharArray()));
        //TODO: A random string of length 20 generated by peer 'peer_id'
//        dataStream.write(Hex.decodeHex("23456DFFFEFFFABCDEAE".toCharArray()));
        String hex = Hex.encodeHexString(peer_id.getBytes("UTF-8"));
        dataStream.write(Hex.decodeHex(hex.toCharArray()));
//        dataStream.writeChars(peer_id);
        //TODO: Total amount downloaded in base 10 64-bit 'downloaded'
        dataStream.writeInt(0);
        dataStream.writeInt(0);
        //TODO: Amount left in base 10 64-bit NOT SURE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        dataStream.writeInt(0);
        dataStream.writeInt(865168821);
        //TODO: Uploaded amount same as downloaded
        dataStream.writeInt(0);
        dataStream.writeInt(0);
        //TODO: 'event'
        dataStream.writeInt(2);
        //TODO: 'IP address'
        dataStream.writeInt(0);
        //TODO: 'key'? NOT SURE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        dataStream.writeInt(234);
        //TODO: 'num_want'?
        dataStream.writeInt(200);
        dataStream.writeShort(8999);

        dataStream.close();
        byte[] announceBytes = byteStream.toByteArray();
        send(announceBytes);
        return parseAnnounceResponse(receive());
    }

    private Map parseAnnounceResponse(DatagramPacket receivePacket) throws Exception {
        if (receivePacket.getLength() < 20) {
            throw new Exception("Announce response is too short");
        }
        ByteBuffer response = ByteBuffer.wrap(receivePacket.getData());
        announceResponse = new LinkedHashMap<>();
        announceResponse.put("action", response.getInt());
        announceResponse.put("transaction_id", response.getInt());
        announceResponse.put("interval", response.getInt());
        announceResponse.put("leechers", response.getInt());
        announceResponse.put("seeders", response.getInt());
        announceResponse.put("IP address", response.getInt());
        announceResponse.put("TCP port", (int) response.getShort());
        return announceResponse;


    }

    public void closeSocket() {
        clientSocket.close();
    }

    public void openSocket() throws SocketException {
        clientSocket = new DatagramSocket(8999);
    }

    public boolean socketIsOpen() {
        return clientSocket != null;
    }
}
