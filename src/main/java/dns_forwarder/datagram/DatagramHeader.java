package dns_forwarder.datagram;

import java.net.DatagramPacket;

public class DatagramHeader {

    private final int id, qr, opcode, tc,
            rd, ra, z, rcode, qdcount,
            ancount, nscount, arcount;

    public int getId() {
        return id;
    }

    public int getQr() {
        return qr;
    }

    public int getOpcode() {
        return opcode;
    }

    public int getTc() {
        return tc;
    }

    public int getRd() {
        return rd;
    }

    public int getRa() {
        return ra;
    }

    public int getZ() {
        return z;
    }

    public int getRcode() {
        return rcode;
    }

    public int getQdcount() {
        return qdcount;
    }

    public int getAncount() {
        return ancount;
    }

    public int getNscount() {
        return nscount;
    }

    public int getArcount() {
        return arcount;
    }

    public DatagramHeader(DatagramPacket packet) {
        this.arcount = 0;
        this.nscount = 0;
        this.ancount = 0;
        this.qdcount = 0;
        this.rcode = 0;
        this.z = 0;
        this.ra = 0;
        this.rd = 0;
        this.tc = 0;
        this.opcode = 0;
        this.qr = 0;
        this.id = 0;
    }
}
