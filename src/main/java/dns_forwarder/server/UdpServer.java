package dns_forwarder.server;

import dns_forwarder.cache.CacheEntry;
import dns_forwarder.client.ClientApplication;
import dns_forwarder.datagram.DatagramDeserializer;
import dns_forwarder.datagram.DatagramQuestionSectionDeserializer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ConcurrentHashMap;

public class UdpServer {

    private static final int DEFAULT_PORT = 1056;
    private static final String GOOGLE_DNS_ADDRESS = "8.8.8.8";
    private static final int GOOGLE_DNS_PORT = 53;
    private static final int BUFFER_SIZE = 1024;

    private int port;
    private DatagramSocket socket;
    private DatagramSocket forwardingSocket;
    private int TTL = 10;
    private final ConcurrentHashMap<String, CacheEntry> cache;

    public UdpServer() {
        this(DEFAULT_PORT);
    }

    public UdpServer(int port) {
        this.port = port;
        this.cache = new ConcurrentHashMap<>();
    }

    public void createServerSocket() {
        try {
            socket = new DatagramSocket(port);
            System.out.printf("Server listening to port %d.%n", port);
        } catch (Exception e) {
            System.err.println("Could not create socket: " + e.getMessage());
        }
    }

    public void createForwardingSocket() {
        try {
            forwardingSocket = new DatagramSocket();
        } catch (Exception e) {
            System.err.println("Could not create forwarding socket: " + e.getMessage());
        }
    }

    public void closeSockets() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
            System.out.println("Server has stopped!");
        }
        if (forwardingSocket != null && !forwardingSocket.isClosed()) {
            forwardingSocket.close();
        }
    }

    public void run() {
        createServerSocket();
        createForwardingSocket();

        if (socket != null && forwardingSocket != null) {
            while (true) {
                DatagramPacket packet = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
                try {
                    socket.receive(packet);
                    forwardPacket(packet);
                } catch (Exception e) {
                    System.err.println("Could not receive packet: " + e.getMessage());
                }
            }
        }
    }

    public void forwardPacket(DatagramPacket receivedPacket) throws IOException {
        String query = new DatagramQuestionSectionDeserializer(receivedPacket).getQNAME();
        CacheEntry cachedResponse = cache.get(query);

        if (cachedResponse != null && !cachedResponse.isExpired()) {
            System.out.println("Returning cached response for: " + query);
            sendResponseClient(cachedResponse.getData(), receivedPacket);
            return;
        }

        System.out.println("Forwarding request to Google DNS for: " + query);
        DatagramPacket packet = createForwardingPacket(receivedPacket);
        if (packet != null) {
            forwardingSocket.send(packet);
            System.out.println("Forwarded packet to " + GOOGLE_DNS_ADDRESS + ":" + GOOGLE_DNS_PORT);
            handleResponse(receivedPacket, query);
        }
    }

    private DatagramPacket createForwardingPacket(DatagramPacket receivedPacket) {
        try {
            return new DatagramPacket(receivedPacket.getData(),
                    receivedPacket.getLength(),
                    InetAddress.getByName(GOOGLE_DNS_ADDRESS), GOOGLE_DNS_PORT);
        } catch (Exception e) {
            System.err.println("Packet exception: " + e.getMessage());
            return null;
        }
    }

    private void handleResponse(DatagramPacket receivedPacket, String query) throws IOException {
        DatagramPacket responsePacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
        forwardingSocket.receive(responsePacket);
        System.out.println(new DatagramDeserializer(responsePacket));

        cache.put(query, new CacheEntry(responsePacket.getData(), TTL));
        sendResponseClient(responsePacket.getData(), receivedPacket);
    }

    public void sendResponseClient(byte[] responseData, DatagramPacket receivedPacket) throws IOException {
        responseData[0] = receivedPacket.getData()[0];
        responseData[1] = receivedPacket.getData()[1];

        DatagramPacket responseClientPacket = new DatagramPacket(
                responseData,
                responseData.length,
                receivedPacket.getAddress(),
                receivedPacket.getPort()
        );

        socket.send(responseClientPacket);
    }
}