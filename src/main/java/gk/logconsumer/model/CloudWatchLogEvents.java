package gk.logconsumer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CloudWatchLogEvents {
    private String owner;
    private String logGroup;
    private String logStream;
    private String[] subscriptionFilters;
    private List<CloudWatchLogEvent> logEvents;

    public CloudWatchLogEvents() {
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLogGroup() {
        return logGroup;
    }

    public void setLogGroup(String logGroup) {
        this.logGroup = logGroup;
    }

    public String getLogStream() {
        return logStream;
    }

    public void setLogStream(String logStream) {
        this.logStream = logStream;
    }

    public String[] getSubscriptionFilters() {
        return subscriptionFilters;
    }

    public void setSubscriptionFilters(String[] subscriptionFilters) {
        this.subscriptionFilters = subscriptionFilters;
    }

    public List<CloudWatchLogEvent> getLogEvents() {
        return logEvents;
    }

    public void setLogEvents(List<CloudWatchLogEvent> logEvents) {
        this.logEvents = logEvents;
    }
}
