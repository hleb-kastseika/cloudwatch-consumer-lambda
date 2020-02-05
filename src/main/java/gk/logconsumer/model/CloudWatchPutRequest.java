package gk.logconsumer.model;

public class CloudWatchPutRequest {
    private AWSLogs awslogs;

    public CloudWatchPutRequest() {
    }

    public AWSLogs getAwslogs() {
        return awslogs;
    }

    public void setAwslogs(AWSLogs awslogs) {
        this.awslogs = awslogs;
    }
}
