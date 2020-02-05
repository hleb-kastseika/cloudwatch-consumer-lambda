package gk.logconsumer.model;

public class CloudWatchLogEvent {
    private String id;
    private String message;
    private long timestamp;

    public CloudWatchLogEvent() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "[timestamp=\"" + timestamp
                + "\", id=\"" + id
                + "\", message=\"" + message + "\"]";
    }
}
