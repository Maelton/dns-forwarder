package src;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpServer {
    private DatagramSocket socket;
    private int port;

    public UdpServer(int port) {
        this.port = port;
    }

    public void start() {
        try {
            // Inicializa o socket UDP
            socket = new DatagramSocket(port);
            
            System.out.println("Servidor UDP estah ouvindo na porta " + port);

            // Loop principal para ouvir por requisições
            while (true) {
                byte[] buffer = new byte[1024]; // Tamanho do buffer para receber os dados
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                // Aguarda pela recepção de um pacote
                socket.receive(packet);

                // Processa a requisição recebida
                processRequest(packet);
            }
        } catch (Exception e) {
            System.err.println("Erro ao iniciar o servidor: " + e.getMessage());
        }
    }

    private void processRequest(DatagramPacket packet) {
        System.out.println("Recebi o pacote:");
        System.out.println(socket);
    }

    public static void main(String[] args) {
        int port = 53; // Porta padrão
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Número de porta inválido. Usando a porta padrão 53.");
            }
        }

        UdpServer server = new UdpServer(port);
        server.start();
    }
}
