package dns_forwarder.dns_message;
import java.net.DatagramPacket;
import dns_forwarder.dns_message.QuestionSection;

public class DnsMessage {
    private DatagramPacket packet;
    private QuestionSection questionSection;

    public DnsMessage(DatagramPacket packet){
        this.packet = packet;
    }

    public void extractQuestionSection(DatagramPacket packet){
        QuestionSection qSection = new QuestionSection(packet);
        qSection.buildQuestionSection();
        System.out.println("Questions - name:" + qSection.getQName() +
                            " type:" + qSection.getQType() +
                            " class:" + qSection.getQClass());
    }

}
