package dns_forwarder.datagram;

import java.net.DatagramPacket;

public class DatagramHeaderDeserializer {

    private final int ID;
    private final int QR;

    public int getID() {
        return ID;
    }

    public int getQR() {
        return QR;
    }

    public int getOPCODE() {
        return OPCODE;
    }

    public int getAA() {
        return AA;
    }

    public int getTC() {
        return TC;
    }

    public int getRD() {
        return RD;
    }

    public int getRA() {
        return RA;
    }

    public int getZ() {
        return Z;
    }

    public int getRCODE() {
        return RCODE;
    }

    public int getQDCOUNT() {
        return QDCOUNT;
    }

    public int getANCOUNT() {
        return ANCOUNT;
    }

    public int getNSCOUNT() {
        return NSCOUNT;
    }

    public int getARCOUNT() {
        return ARCOUNT;
    }

    public int getFlags() {
        return flags;
    }

    private final int OPCODE;
    private final int AA;
    private final int TC;
    private final int RD;
    private final int RA;
    private final int Z;
    private final int RCODE;
    private final int QDCOUNT;
    private final int ANCOUNT;
    private final int NSCOUNT;
    private final int ARCOUNT;

    private final int flags;

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
    public DatagramHeaderDeserializer(DatagramPacket p) {

        if (p.getLength() < 12) {
            throw new IllegalArgumentException("Packet too short for DNS header");
        }

        this.ID = ((p.getData()[0] & 255) << 8) | (p.getData()[1] & 255);

        this.QR = (p.getData()[2] >> 7) & 1;
        this.OPCODE = (p.getData()[2] >> 3) & 15;
        this.AA = (p.getData()[2] >> 2) & 1;
        this.TC = (p.getData()[2] >> 1) & 1;
        this.RD = p.getData()[2] & 1;

        this.RA = (p.getData()[3] >> 7) & 1;
        this.Z = (p.getData()[3] >> 4) & 7;
        this.RCODE = p.getData()[3] & 15;;

        this.QDCOUNT = ((p.getData()[4] & 255) << 8) | (p.getData()[5] & 255);
        this.ANCOUNT = ((p.getData()[6] & 255) << 8) | (p.getData()[7] & 255);
        this.NSCOUNT = ((p.getData()[8] & 255) << 8) | (p.getData()[9] & 255);
        this.ARCOUNT = ((p.getData()[10] & 255) << 8) | (p.getData()[11] & 255);

        this.flags = (QR << 15) | (OPCODE << 11) | (AA << 10) | (TC << 9) | (RD << 8) | (RA << 7) | (Z << 4) | RCODE;
    }

    @Override
    public String toString() {
        return String.format("Header - id:%d flags:%d questions:%d answers:%d authorities:%d additionals:%d",
                ID, flags, QDCOUNT, ANCOUNT, NSCOUNT, ARCOUNT);
    }

}
