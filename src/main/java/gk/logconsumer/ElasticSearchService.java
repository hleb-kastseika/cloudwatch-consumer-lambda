package gk.logconsumer;

import gk.logconsumer.model.CloudWatchLogEvents;
import gk.logconsumer.model.ESLogRecord;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;

public class ElasticSearchService {
    private String esBulkUrl;
    private CloseableHttpClient client;

    public ElasticSearchService() {
        this.client = HttpClients.createDefault();
        this.esBulkUrl = System.getenv("ES_INDEX_URL") + "_bulk";
    }

    public ElasticSearchService(CloseableHttpClient client, String esUrl) {
        this.client = client;
        this.esBulkUrl = esUrl + "_bulk";
    }

    public Boolean uploadLogs(CloudWatchLogEvents logEvents) {
        try {
            //TODO process each document in the response to make sure that uploaded successfully
            var response = client.execute(prepareHttpPost(logEvents));

            return response.getStatusLine().getStatusCode() == 200;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpPost prepareHttpPost(CloudWatchLogEvents logEvents) throws UnsupportedEncodingException {
        var httpPost = new HttpPost(esBulkUrl);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(prepareRequestBody(logEvents)));
        return httpPost;
    }

    private String prepareRequestBody(CloudWatchLogEvents logEvents) {
        return logEvents.getLogEvents()
                .stream()
                .map(log -> ESLogRecord.builder()
                        .id(log.getId())
                        .message(log.getMessage())
                        .timestamp(log.getTimestamp())
                        .logGroup(logEvents.getLogGroup())
                        .logStream(logEvents.getLogStream())
                        .owner(logEvents.getOwner())
                        .build())
                .map(this::getJsonPayload)
                .collect(Collectors.joining());
    }

    private String getJsonPayload(ESLogRecord esRecord) {
        return new StringBuilder()
                .append("{\"index\":{\"_id\":\"")
                .append(esRecord.getId())
                .append("\",\"_type\":\"_doc\"}}\n")
                .append("{\"id\":\"")
                .append(esRecord.getId())
                .append("\",\"message\":\"")
                .append(esRecord.getMessage())
                .append("\",\"timestamp\":")
                .append(esRecord.getTimestamp())
                .append(",\"owner\":\"")
                .append(esRecord.getOwner())
                .append("\",\"logGroup\":\"")
                .append(esRecord.getLogGroup())
                .append("\",\"logStream\":\"")
                .append(esRecord.getLogStream())
                .append("\"}\n")
                .toString();
    }
}
