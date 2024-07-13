package dns_forwarder.client;

import java.net.InetAddress;

public class ClientApplication {
    private int port;
    private InetAddress ip;

    public ClientApplication(int port, InetAddress ip){
        this.port = port;
        this.ip = ip;
    }

    public int getPort(){
        return this.port;
    }

    public InetAddress getIp(){
        return this.ip;
    }
}
