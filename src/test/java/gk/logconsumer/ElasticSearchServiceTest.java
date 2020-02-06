package gk.logconsumer;

import gk.logconsumer.model.CloudWatchLogEvent;
import gk.logconsumer.model.CloudWatchLogEvents;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.AssertJUnit.assertTrue;

public class ElasticSearchServiceTest {
    private static final String TEST_ES_URL = null;
    private CloudWatchLogEvents logEvents;
    @Mock
    private CloseableHttpResponse response;
    @Mock
    private StatusLine statusLine;
    @Mock
    private CloseableHttpClient client;
    @InjectMocks
    private ElasticSearchService esService = new ElasticSearchService(client, TEST_ES_URL);

    @BeforeMethod
    public void setUp() {
        initMocks(this);

        CloudWatchLogEvent log = new CloudWatchLogEvent();
        log.setId("35253770325476481494622529663728138828543250848121946112");
        log.setMessage("Test log message");
        log.setTimestamp(1580833735000L);
        logEvents = new CloudWatchLogEvents();
        logEvents.setLogEvents(List.of(log));
    }

    @Test
    public void testUploadLogs() throws IOException {
        when(client.execute(any(HttpPost.class))).thenReturn(response);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);

        boolean isSuccess = esService.uploadLogs(logEvents);

        assertTrue(isSuccess);
        verify(client).execute(any(HttpPost.class));
        verifyNoMoreInteractions(client);
    }
}
