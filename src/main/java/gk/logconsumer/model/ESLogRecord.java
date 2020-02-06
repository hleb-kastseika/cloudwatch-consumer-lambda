package gk.logconsumer.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ESLogRecord {
    private String id;
    private String message;
    private long timestamp;
    private String owner;
    private String logGroup;
    private String logStream;
}
