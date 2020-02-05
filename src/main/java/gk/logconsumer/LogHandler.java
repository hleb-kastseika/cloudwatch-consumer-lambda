package gk.logconsumer;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import gk.logconsumer.model.CloudWatchLogEvent;
import gk.logconsumer.model.CloudWatchPutRequest;
import gk.logconsumer.model.LambdaResponse;

import java.util.logging.Logger;

public class LogHandler implements RequestHandler<CloudWatchPutRequest, LambdaResponse> {
    private static Logger LOGGER = Logger.getGlobal();
    private PayloadDecoder decoder = new PayloadDecoder();

    @Override
    public LambdaResponse handleRequest(CloudWatchPutRequest request, Context context) {
        var logEvents = decoder.decode(request.getAwslogs().getData());
        var logs = logEvents.getLogEvents();

        for (CloudWatchLogEvent log : logs) {
            LOGGER.info(log.toString());
        }
        return new LambdaResponse(); //TODO possibly remove
    }
}