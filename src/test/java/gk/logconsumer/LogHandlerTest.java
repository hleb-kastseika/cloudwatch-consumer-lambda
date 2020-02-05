package gk.logconsumer;

import gk.logconsumer.model.AWSLogs;
import gk.logconsumer.model.CloudWatchPutRequest;
import gk.logconsumer.model.LambdaResponse;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class LogHandlerTest {
    private static final String ENCODED_CW_EVENT = "H4sIAAAAAAAAADWQzW6DMBCEXwXtGSTjtbHNDak0p57IrYoqp7jUEhiEnf4oyrt3Ic1ptd+MZta+wuRitIM7/i4Oanhqjs3bS9t1zaGFHObv4FbCyhiUKKQwGgmP83BY58tCyminc2+LTxv60YehICneHV1anZ3IEu20jG5TinhnOcTLOb6vfkl+Ds9+TG6NUL8+rB87gdOe0365kDb1Cr6nOJRcolIMuRSqEroURlScFqQbBasMrzjbhlQl00ZzKStCglqTp9cm6oC6lJppRIWSMZY/foHi25/9hoyas3+aIcLtdPsDq47OrCwBAAA=";
    private LogHandler handler = new LogHandler();

    @Test
    public void testHandleRequest() {
        AWSLogs awsLogs = new AWSLogs();
        awsLogs.setData(ENCODED_CW_EVENT);
        CloudWatchPutRequest request = new CloudWatchPutRequest();
        request.setAwslogs(awsLogs);

        LambdaResponse response = handler.handleRequest(request, null);
        assertNotNull(response);
    }
}
