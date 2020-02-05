package gk.logconsumer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CloudWatchLogEvents {
    private String owner;
    private String logGroup;
    private String logStream;
    private String[] subscriptionFilters;
    private List<CloudWatchLogEvent> logEvents;
}
