package dns_forwarder.server;

import dns_forwarder.client.ClientApplication;
import dns_forwarder.datagram.DatagramDeserializer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpServer {

    private final int DEFAULT_PORT = 1056;
    private int port;
    private DatagramSocket socket;
    private DatagramSocket forwardingSocket;

    private String GOOGLE_DNS_ADDRESS = "8.8.8.8";
    private int GOOGLE_DNS_PORT = 53;

    private ClientApplication clientHost;

    public UdpServer() {
        port = DEFAULT_PORT;
    }

    public UdpServer(int port) {
        this.port = port;
    }

    public void createDefaultSocket() {
        try {
            socket = new DatagramSocket(DEFAULT_PORT);
            System.out.printf("Server listening to port %d.%n", DEFAULT_PORT);
        } catch (Exception e) {
            System.out.println("Could not create socket: " + e.getMessage());
        }
    }

    public void createCustomSocket(int port) throws DatagramSocketException {
        try {
            socket = new DatagramSocket(port);
            System.out.printf("Server listening to port %d.%n", port);
        } catch (Exception e) {
            throw new DatagramSocketException(e.getMessage());
        }
    }

    public void createServerSocket() {
        if(port == DEFAULT_PORT)
            createDefaultSocket();
        else {
            try {
                createCustomSocket(this.port);
            } catch(DatagramSocketException e) {
                System.out.println(e);
                createDefaultSocket();
            }
        }
    }

    public void closeServerSocket() {
        if(socket != null && !socket.isClosed()) {
            socket.close();
            System.out.println("Server has stopped!");
        }
    }

    public void run() {
        createServerSocket();
        createForwardingSocket();

        if(socket != null & forwardingSocket != null) {
            while(true) {
                DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
                try {
                    socket.receive(packet);
                    forwardPacket(packet);
                } catch(Exception e) {
                    System.out.println("Could not receive packet: " + e.getMessage());
                }
            }
        }
    }

    public void createForwardingSocket() {
        try {
            forwardingSocket = new DatagramSocket();
        } catch (Exception e) {
            System.out.println("Could not create forwarding socket: " + e.getMessage());
        }
    }

    public void closeForwardingSocket() {
        if(forwardingSocket != null && !forwardingSocket.isClosed())
            forwardingSocket.close();
    }

    public void forwardPacket(DatagramPacket receivedPacket) {
        DatagramPacket packet = null;

        clientHost = new ClientApplication(receivedPacket.getPort(), receivedPacket.getAddress());
        try {
            packet = new DatagramPacket(receivedPacket.getData(),
                    receivedPacket.getLength(),
                    InetAddress.getByName(GOOGLE_DNS_ADDRESS), GOOGLE_DNS_PORT);
        } catch (Exception e) {
            System.out.println("Packet exception: " + e.getMessage());
        }

        try {
            if(packet != null) {
                forwardingSocket.send(packet);
                System.out.println("Forwarded packet to " + GOOGLE_DNS_ADDRESS + ":" + GOOGLE_DNS_PORT);

                DatagramPacket responsePacket = new DatagramPacket(new byte[1024], 1024);
                forwardingSocket.receive(responsePacket);
                System.out.println(new DatagramDeserializer(responsePacket));
                sendResponseClient(responsePacket);
            }
        } catch(Exception e) {
            System.out.println("Could not forward packet: " + e.getMessage());
        }
    }

    public void sendResponseClient(DatagramPacket responseForwarderPacket) throws IOException {
        DatagramPacket responseClientPacket = new DatagramPacket(responseForwarderPacket.getData(),
                                        responseForwarderPacket.getLength(),
                                        clientHost.getIp(), clientHost.getPort());
        socket.send(responseClientPacket);
    }
}