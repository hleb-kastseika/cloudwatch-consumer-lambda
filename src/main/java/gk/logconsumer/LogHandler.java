package gk.logconsumer;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import gk.logconsumer.model.CloudWatchPutRequest;
import gk.logconsumer.model.LambdaResponse;

public class LogHandler implements RequestHandler<CloudWatchPutRequest, LambdaResponse> {
    private PayloadDecoder decoder;
    private ElasticSearchService esService;

    public LogHandler() {
        this.decoder = new PayloadDecoder();
        this.esService = new ElasticSearchService();
    }

    public LogHandler(PayloadDecoder decoder, ElasticSearchService esService) {
        this.decoder = decoder;
        this.esService = esService;
    }

    @Override
    public LambdaResponse handleRequest(CloudWatchPutRequest request, Context context) {
        var logEvents = decoder.decode(request.getAwslogs().getData());

        //TODO add retrying for failed uploads
        boolean isSuccess = esService.uploadLogs(logEvents);
        return new LambdaResponse(); //TODO possibly remove it at all
    }
}