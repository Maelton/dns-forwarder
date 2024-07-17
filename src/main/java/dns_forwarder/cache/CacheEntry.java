package dns_forwarder.cache;

public class CacheEntry {
    final private byte[] data;
    final private long expiryTime;

    public CacheEntry(byte[] data, long ttl) {
        this.data = data;
        this.expiryTime = System.currentTimeMillis() + ttl * 1000;
    }

    public byte[] getData() {
        return data;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}