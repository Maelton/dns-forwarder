package dns_forwarder.dns_message;
import java.net.DatagramPacket;

public class QuestionSection {
    //private DatagramPacket packet;
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
        int position = 12; // Posição inicial do QNAME após o cabeçalho de 12 bytes

        StringBuilder qnameBuilder = new StringBuilder();
        while (position < this.lengthData) {
            int labelLength = this.data[position] & 0xFF; // Obtém o comprimento da etiqueta

            if (labelLength == 0) {
                break; // Fim do QNAME
            }

            if (position + labelLength >= this.lengthData) {
                throw new IllegalArgumentException("Pacote DNS inválido");
            }

            if (qnameBuilder.length() > 0) {
                qnameBuilder.append('.');
            }

            qnameBuilder.append(new String(this.data, position + 1, labelLength));
            position += labelLength + 1; // Avança para a próxima etiqueta
        }
        this.QNAME = qnameBuilder.toString();
    }

    public void setQType(){
        // DNS header is 12 bytes, question section starts after header
        int questionStart = 12;
        int index = questionStart;

        // Skip the query name
        while (this.data[index] != 0) {
            index += data[index] + 1;
        }
        index++;

        // QTYPE is 2 bytes after the query name (QNAME)
        int qtype = ((this.data[index] & 0xFF) << 8) | (this.data[index + 1] & 0xFF);
        this.QTYPE = qtype;
    }

    public void setQClass(){
        // DNS header is 12 bytes, question section starts after header
        int questionStart = 12;
        int index = questionStart;

        // Skip the query name
        while (this.data[index] != 0) {
            index += this.data[index] + 1;
        }
        index++;

        // QCLASS is 2 bytes after the QTYPE
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