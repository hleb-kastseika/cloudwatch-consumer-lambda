package gk.logconsumer;

import gk.logconsumer.model.AWSLogs;
import gk.logconsumer.model.CloudWatchLogEvent;
import gk.logconsumer.model.CloudWatchLogEvents;
import gk.logconsumer.model.CloudWatchPutRequest;
import gk.logconsumer.service.DecoderService;
import gk.logconsumer.service.ElasticSearchService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertNull;

public class LogHandlerTest {
    private static final String ENCODED_CW_EVENT = "H4sIAAAAAAAAADWQzW6DMBCEXwXtGSTjtbHNDak0p57IrYoqp7jUEhiEnf4oyrt3Ic1ptd+MZta+wuRitIM7/i4Oanhqjs3bS9t1zaGFHObv4FbCyhiUKKQwGgmP83BY58tCyminc2+LTxv60YehICneHV1anZ3IEu20jG5TinhnOcTLOb6vfkl+Ds9+TG6NUL8+rB87gdOe0365kDb1Cr6nOJRcolIMuRSqEroURlScFqQbBasMrzjbhlQl00ZzKStCglqTp9cm6oC6lJppRIWSMZY/foHi25/9hoyas3+aIcLtdPsDq47OrCwBAAA=";
    private CloudWatchLogEvents decodedLogs;
    private CloudWatchPutRequest request;
    @Mock
    private DecoderService decoder;
    @Mock
    private ElasticSearchService esService;
    @InjectMocks
    private LogHandler handler = new LogHandler(decoder, esService);

    @BeforeMethod
    public void setUp() {
        initMocks(this);

        CloudWatchLogEvent log = new CloudWatchLogEvent();
        log.setId("35253770325476481494622529663728138828543250848121946112");
        log.setMessage("Test log message");
        log.setTimestamp(1580833735000L);
        decodedLogs = new CloudWatchLogEvents();
        decodedLogs.setLogEvents(List.of(log));

        AWSLogs awsLogs = new AWSLogs();
        awsLogs.setData(ENCODED_CW_EVENT);
        request = new CloudWatchPutRequest();
        request.setAwslogs(awsLogs);
    }

    @Test
    public void testHandleRequest() {
        when(decoder.decode(ENCODED_CW_EVENT)).thenReturn(decodedLogs);
        when(esService.uploadLogs(decodedLogs)).thenReturn(true);

        var response = handler.handleRequest(request, null);

        assertNull(response);
        verify(decoder).decode(eq(ENCODED_CW_EVENT));
        verify(esService).uploadLogs(eq(decodedLogs));
        verifyNoMoreInteractions(decoder, esService);
    }

    @Test(expectedExceptions = {RuntimeException.class})
    public void testUploadDataError() {
        when(decoder.decode(ENCODED_CW_EVENT)).thenReturn(decodedLogs);
        when(esService.uploadLogs(decodedLogs)).thenReturn(false);

        handler.handleRequest(request, null);
    }
}
