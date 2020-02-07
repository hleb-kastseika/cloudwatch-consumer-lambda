package gk.logconsumer.service;

import gk.logconsumer.model.CloudWatchLogEvent;
import gk.logconsumer.model.CloudWatchLogEvents;
import gk.logconsumer.service.DecoderService;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class DecoderServiceTest {
    private static final String ENCODED_CW_EVENT = "H4sIAAAAAAAAADWQzW6DMBCEXwXtGSRj72LMDak0p5zIrYoqp7jUEn/CTtoqyrt3Q5rjfjOa2d0rjC4E27vD7+Kggpf6UL/vm7atdw2kMH9PbmWsjVGkkNCUivEw97t1Pi+sDHY8dTb7slM3+KnPWAoPRxtXZ0e2BDsug7srWXiwFML5FD5Wv0Q/T69+iG4NUL09rZ8bgeOW01zcFO/qFXzHcYokKa2FkoS6wDJHg4XkQaJATaIQRpDJUcuiREOUixyl0QW3Rs/XRu6AKqdSlEppRUKI9PkFjm9+th0Sbk7+aaIk3I63P0GSy08sAQAA";
    private static final String DECODED_LOG_MESSAGE = "Example log message 32";
    private DecoderService decoder = new DecoderService();

    @Test
    public void testDecode() {
        CloudWatchLogEvents logEvents = decoder.decode(ENCODED_CW_EVENT);

        assertNotNull(logEvents);
        List<CloudWatchLogEvent> logs = logEvents.getLogEvents();
        assertEquals(1, logs.size());
        assertEquals(DECODED_LOG_MESSAGE, logs.get(0).getMessage());
    }

    @Test(expectedExceptions = {RuntimeException.class})
    public void testFailDecode() {
        CloudWatchLogEvents logEvents = decoder.decode("wrong encoded data");
    }
}
