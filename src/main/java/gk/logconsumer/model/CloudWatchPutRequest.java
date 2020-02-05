package gk.logconsumer.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CloudWatchPutRequest {
    private AWSLogs awslogs;
}
