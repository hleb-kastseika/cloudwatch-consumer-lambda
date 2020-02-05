package gk.logconsumer;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.Context;
import gk.logconsumer.model.CloudWatchPutRequest;
import gk.logconsumer.model.LambdaResponse;
import gk.logconsumer.model.CloudWatchLogEvent;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;
import org.pmw.tinylog.writers.ConsoleWriter;

public class LogHandler implements RequestHandler<CloudWatchPutRequest, LambdaResponse> {
    private static final String LOG_MESSAGE_TEMPLATE = "{date:yyyy-MM-dd HH:mm:ss} {level} {class}.{method}():{line} - {message}";
    private PayloadDecoder decoder = new PayloadDecoder();

    static {
        Configurator.defaultConfig()
                .writer(new ConsoleWriter())
                .level(Level.INFO)
                .formatPattern(LOG_MESSAGE_TEMPLATE)
                .activate();
    }

    @Override
    public LambdaResponse handleRequest(CloudWatchPutRequest request, Context context) {
        var logEvents = decoder.decode(request.getAwslogs().getData());
        var logs = logEvents.getLogEvents();

        for (CloudWatchLogEvent log : logs) {
            Logger.info("~~~~~~ Transmitted log = " + log);
        }
        return new LambdaResponse(); //TODO possibly remove
    }
}