package dns_forwarder.datagram;

import java.net.DatagramPacket;

public class DatagramHeader {

    private final int id;
    private final int qr;
    private final int opcode;
    private final int aa;
    private final int tc;
    private final int rd;
    private final int ra;
    private final int z;
    private final int rcode;
    private final int qdcount;
    private final int ancount;
    private final int nscount;
    private final int arcount;

    public int getId() {
        return id;
    }

    public int getQr() {
        return qr;
    }

    public int getOpcode() {
        return opcode;
    }

    public int getAa() {
        return aa;
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

    /**
     *  Header section format (RFC 1035 Section 4.1.1)
     *
     *       0  1  2  3  4  5  6  7  8  9  0  1  2  3  4  5
     *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     *     |                      ID                       |
     *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     *     |QR|   Opcode  |AA|TC|RD|RA|   Z    |   RCODE   |
     *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     *     |                    QDCOUNT                    |
     *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     *     |                    ANCOUNT                    |
     *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     *     |                    NSCOUNT                    |
     *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     *     |                    ARCOUNT                    |
     *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     */
    public DatagramHeader(DatagramPacket p) {
        this.id = ((p.getData()[0] & 255) << 8) | (p.getData()[1] & 255);

        this.qr = (p.getData()[2] >> 7) & 1;
        this.opcode = (p.getData()[2] >> 3) & 15;
        this.aa = (p.getData()[2] >> 2) & 1;
        this.tc = (p.getData()[2] >> 1) & 1;
        this.rd = p.getData()[2] & 1;

        this.ra = (p.getData()[3] >> 7) & 1;
        this.z = (p.getData()[3] >> 4) & 7;
        this.rcode = p.getData()[3] & 15;;

        this.qdcount = ((p.getData()[4] & 255) << 8) | (p.getData()[5] & 255);
        this.ancount = ((p.getData()[6] & 255) << 8) | (p.getData()[7] & 255);
        this.nscount = ((p.getData()[8] & 255) << 8) | (p.getData()[9] & 255);
        this.arcount = ((p.getData()[10] & 255) << 8) | (p.getData()[11] & 255);
    }
}
