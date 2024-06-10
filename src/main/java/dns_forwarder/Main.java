package dns_forwarder;

import dns_forwarder.server.UdpServer;

public class Main {
    static UdpServer server;

    public static void main(String[] args) {

        try {
            if (args.length > 0)
                server = new UdpServer(Integer.parseInt(args[0]));
        } catch (NumberFormatException e) {
            System.out.println("Specified port is not valid.");
        }

        if(server == null)
            server = new UdpServer();
        server.run();
        server.closeServerSocket();
    }
}