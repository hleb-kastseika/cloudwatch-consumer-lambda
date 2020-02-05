package gk.logconsumer.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class CloudWatchLogEvent {
    private String id;
    private String message;
    private long timestamp;
}
