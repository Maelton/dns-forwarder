package dns_forwarder.datagram;

import java.net.DatagramPacket;

public class DatagramQuestionSectionDeserializer {
    private final byte[] datagramData;
    private final int QUESTION_SECTION_STARTING_INDEX = 12;

    public String getQNAME() {
        return QNAME;
    }

    public int getQTYPE() {
        return QTYPE;
    }

    public int getQCLASS() {
        return QCLASS;
    }

    private final String QNAME;
    private final int QTYPE;
    private final int QCLASS;

    public DatagramQuestionSectionDeserializer(DatagramPacket packet) {
        this.datagramData = packet.getData();
        this.QNAME = deserializeQNAME();
        this.QTYPE = deserializeQTYPE();
        this.QCLASS = deserializeQCLASS();
    }

    public String deserializeQNAME() {
        int labelLengthIndicatorIndex = QUESTION_SECTION_STARTING_INDEX;
        StringBuilder qNameStringBuilder = new StringBuilder();

        while (labelLengthIndicatorIndex < datagramData.length) {
            int nextLabelLength = datagramData[labelLengthIndicatorIndex] & 255;
            if (labelLengthIndicatorIndex + nextLabelLength >= datagramData.length)
                throw new IllegalArgumentException("Question Section index out of range!");
            else {
                if (nextLabelLength == 0)
                    break;
                if (qNameStringBuilder.length() > 0)
                    qNameStringBuilder.append('.');
                qNameStringBuilder.append(new String(datagramData, labelLengthIndicatorIndex + 1, nextLabelLength));
                labelLengthIndicatorIndex += nextLabelLength + 1;
            }
        }
        return qNameStringBuilder.toString();
    }

    public int deserializeQTYPE() {
        int index = QUESTION_SECTION_STARTING_INDEX;
        while (datagramData[index] != 0)
            index += datagramData[index] + 1;
        index++;
        return ((datagramData[index] & 255) << 8) | (datagramData[index + 1] & 255);
    }

    public int deserializeQCLASS() {
        int index = QUESTION_SECTION_STARTING_INDEX;
        while (datagramData[index] != 0)
            index += datagramData[index] + 1;
        index++;
        return ((this.datagramData[index + 2] & 255) << 8) | (datagramData[index + 3] & 255);
    }

    @Override
    public String toString() {
        return "Questions - name:" + QNAME +
                " type:" + QTYPE +
                " class:" + QCLASS;
    }
}
