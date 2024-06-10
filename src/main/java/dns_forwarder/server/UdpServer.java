package dns_forwarder.server;
import dns_forwarder.dns_message.DnsMessage;
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
        DnsMessage dnsMessage = new DnsMessage(packet);
        dnsMessage.extractQuestionSection();
        // System.out.println(packet);
    }
}