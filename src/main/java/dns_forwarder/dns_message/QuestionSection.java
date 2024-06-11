package dns_forwarder.dns_message;
import java.net.DatagramPacket;

public class QuestionSection {
    private byte[] data;
    private int lengthData;
    private String QNAME;
    private int QTYPE;
    private int QCLASS;

    public QuestionSection(DatagramPacket packet){
        this.data = packet.getData();
        this.lengthData = packet.getLength();
    }

    public void buildQuestionSection(){
        setQName();
        setQType();
        setQClass();
    }

    public void setQName() {
        int position = 12;

        StringBuilder qnameBuilder = new StringBuilder();
        while (position < this.lengthData) {
            int labelLength = this.data[position] & 0xFF;

            if (position + labelLength >= this.lengthData) {
                throw new IllegalArgumentException("Pacote DNS inválido");
            }else{
                if (labelLength == 0) {
                    break;
                }

                if (qnameBuilder.length() > 0) {
                    qnameBuilder.append('.');
                }

                qnameBuilder.append(new String(this.data, position + 1, labelLength));
                position += labelLength + 1;
            }

        }
        this.QNAME = qnameBuilder.toString();
    }

    public void setQType(){
        int questionStart = 12;
        int index = questionStart;

        while (this.data[index] != 0) {
            index += data[index] + 1;
        }
        index++;

        int qtype = ((this.data[index] & 0xFF) << 8) | (this.data[index + 1] & 0xFF);
        this.QTYPE = qtype;
    }

    public void setQClass(){
        int questionStart = 12;
        int index = questionStart;

        while (this.data[index] != 0) {
            index += this.data[index] + 1;
        }
        index++;

        int qClass = ((this.data[index + 2] & 0xFF) << 8) | (this.data[index + 3] & 0xFF);
        this.QCLASS = qClass;
    }

    public String getQName(){
        return this.QNAME;
    }

    public int getQType(){
        return this.QTYPE;
    }

    public int getQClass(){
        return this.QCLASS;
    }
}