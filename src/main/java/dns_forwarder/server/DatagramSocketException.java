package dns_forwarder.server;

public class DatagramSocketException extends Exception {
    private final String message;

    public DatagramSocketException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "DatagramSocketException: " + message;
    }
}
