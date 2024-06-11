package dns_forwarder.server;

import dns_forwarder.dns_message.DnsMessage;
import dns_forwarder.datagram.DatagramHeader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpServer {

    private final int DEFAULT_PORT = 1056;
    private int port;
    private DatagramSocket socket;

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
        if(socket != null) {
            while(true) {
                DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
                try {
                    socket.receive(packet);
                    processPacket(packet);
                } catch(Exception e) {
                    System.out.println("Could not receive packet: " + e.getMessage());
                }
            }
        }
    }

    public void processPacket(DatagramPacket packet) {
        DatagramHeader packetHeader = new DatagramHeader(packet);
        System.out.println("ID: " + packetHeader.getId());

        System.out.println("QR: " + packetHeader.getQr());
        System.out.println("OPCODE: " + packetHeader.getOpcode());
        System.out.println("AA: " + packetHeader.getAa());
        System.out.println("TC: " + packetHeader.getTc());
        System.out.println("RD: " + packetHeader.getRd());

        System.out.println("RA: " + packetHeader.getRa());
        System.out.println("Z: " + packetHeader.getZ());
        System.out.println("RCODE: " + packetHeader.getRcode());

        System.out.println("QDCOUNT: " + packetHeader.getQdcount());
        System.out.println("ANCOUNT: " + packetHeader.getAncount());
        System.out.println("NSCOUNT: " + packetHeader.getNscount());
        System.out.println("ARCOUNT: " + packetHeader.getArcount());

        DnsMessage dnsMessage = new DnsMessage(packet);
        dnsMessage.extractQuestionSection();
    }
}