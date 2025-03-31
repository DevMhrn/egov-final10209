package digit.util;

import org.egov.common.contract.request.RequestInfo;
import digit.web.models.ResponseInfo;
import org.springframework.stereotype.Component;

import static digit.config.ServiceConstants.*;

@Component
public class ResponseInfoFactory {

    /**
     * Generates standardized response metadata from request metadata
     * Creates consistent response structure across the application
     * 
     * @param inputMetadata Original request metadata
     * @param operationResult Indicates if operation was successful
     * @return Structured response metadata
     */
    public ResponseInfo createResponseInfoFromRequestInfo(final RequestInfo inputMetadata, final Boolean operationResult) {
        // Extract API identifier from request or use default
        final String apiIdentifier = inputMetadata != null ? inputMetadata.getApiId() : "";
        
        // Extract API version from request or use default
        final String apiVersion = inputMetadata != null ? inputMetadata.getVer() : "";
        
        // Extract timestamp or use null
        Long timestamp = null;
        if(inputMetadata != null)
            timestamp = inputMetadata.getTs();
        
        // Set response message ID (hardcoded for now)
        final String responseMessageId = RES_MSG_ID;
        
        // Extract original message ID from request or use default
        final String originalMessageId = inputMetadata != null ? inputMetadata.getMsgId() : "";
        
        // Set status based on operation result
        final String operationStatus = operationResult ? SUCCESSFUL : FAILED;

        // Build and return complete response metadata
        return ResponseInfo.builder()
                .apiId(apiIdentifier)
                .ver(apiVersion)
                .ts(timestamp)
                .resMsgId(responseMessageId)
                .msgId(originalMessageId)
                .resMsgId(responseMessageId)
                .status(operationStatus)
                .build();
    }
}