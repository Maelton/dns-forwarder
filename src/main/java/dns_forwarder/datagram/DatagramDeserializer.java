package dns_forwarder.datagram;

import java.net.DatagramPacket;

public class DatagramDeserializer {
    public final DatagramHeaderDeserializer header;
    public final DatagramQuestionSectionDeserializer questionSection;

    public DatagramDeserializer(DatagramPacket packet) {
        this.header = new DatagramHeaderDeserializer(packet);
        this.questionSection = new DatagramQuestionSectionDeserializer(packet);
    }

    @Override
    public String toString() {
        StringBuilder datagramStringBuilder = new StringBuilder();
        datagramStringBuilder.append(header.toString() + "\n");
        datagramStringBuilder.append(questionSection.toString() + "\n");

        if (header.getANCOUNT() == 0)
            datagramStringBuilder.append("Answers: Empty" + "\n");
        else
            datagramStringBuilder.append("Answers: " + header.getANCOUNT() + "\n");

        if (header.getNSCOUNT() == 0)
            datagramStringBuilder.append("Authorities: Empty" + "\n");
        else
            datagramStringBuilder.append("Authorities: " + header.getNSCOUNT() + "\n");

        datagramStringBuilder.append("..." + "\n");
        return datagramStringBuilder.toString();
    }
}