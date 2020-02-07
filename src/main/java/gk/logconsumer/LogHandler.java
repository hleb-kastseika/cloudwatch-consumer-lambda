package gk.logconsumer;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import gk.logconsumer.model.CloudWatchPutRequest;
import gk.logconsumer.service.DecoderService;
import gk.logconsumer.service.ElasticSearchService;

public class LogHandler implements RequestHandler<CloudWatchPutRequest, Void> {
    private DecoderService decoder;
    private ElasticSearchService esService;

    public LogHandler() {
        this.decoder = new DecoderService();
        this.esService = new ElasticSearchService();
    }

    public LogHandler(DecoderService decoder, ElasticSearchService esService) {
        this.decoder = decoder;
        this.esService = esService;
    }

    @Override
    public Void handleRequest(CloudWatchPutRequest request, Context context) {
        var logEvents = decoder.decode(request.getAwslogs().getData());

        //TODO add retrying for failed uploads
        boolean isSuccess = esService.uploadLogs(logEvents);
        return null;
    }
}