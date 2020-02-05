package gk.logconsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import gk.logconsumer.model.CloudWatchLogEvents;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Base64;
import java.util.zip.GZIPInputStream;

public class PayloadDecoder {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public PayloadDecoder() {
    }

    public CloudWatchLogEvents decode(String base64Encoded) {
        var decoded = Base64.getMimeDecoder().decode(base64Encoded);
        final Reader reader;
        try {
            var in = new GZIPInputStream(new ByteArrayInputStream(decoded));
            reader = new InputStreamReader(in);
            return MAPPER.readValue(reader, CloudWatchLogEvents.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
