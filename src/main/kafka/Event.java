import java.sql.Timestamp;

public class Event {
    public String id;
    public Long timestamp;
    public Double temp;

    public Event() {
    }

    public Event(String id, Long timestamp, Double temp) {
        this.id = id;
        this.timestamp = timestamp;
        this.temp = temp;
    }

    @Override
    public String toString() {
        return "Event{" +
                "user='" + id + '\'' +
                ", url='" + temp + '\'' +
                ", timestamp=" + new Timestamp(timestamp) +
                '}';
    }
}