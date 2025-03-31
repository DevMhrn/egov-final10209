package digit.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.repository.ServiceRequestRepository;
import digit.config.Configuration;
import org.egov.common.contract.idgen.IdGenerationRequest;
import org.egov.common.contract.idgen.IdGenerationResponse;
import org.egov.common.contract.idgen.IdRequest;
import org.egov.common.contract.idgen.IdResponse;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static digit.config.ServiceConstants.*;

@Component
public class IdgenUtil {

    @Autowired
    private ObjectMapper jsonConverter;

    @Autowired
    private ServiceRequestRepository apiClient;

    @Autowired
    private Configuration appConfig;

    /**
     * Generates a batch of unique identifiers using ID generation service
     * Handles formatting and validation
     * 
     * @param requestMetadata Client request information
     * @param jurisdictionId Tenant identifier
     * @param idName Name of the ID sequence
     * @param formatPattern Format pattern for generated IDs
     * @param batchSize Number of IDs to generate
     * @return List of generated unique identifiers
     */
    public List<String> getIdList(
            RequestInfo requestMetadata, 
            String jurisdictionId, 
            String idName, 
            String formatPattern, 
            Integer batchSize) {
        
        // Prepare batch of ID generation requests
        List<IdRequest> idRequests = new ArrayList<>();
        for (int i = 0; i < batchSize; i++) {
            idRequests.add(
                IdRequest.builder()
                    .idName(idName)
                    .format(formatPattern)
                    .tenantId(jurisdictionId)
                    .build()
            );
        }

        // Prepare complete ID generation request
        IdGenerationRequest generationRequest = IdGenerationRequest.builder()
                                               .idRequests(idRequests)
                                               .requestInfo(requestMetadata)
                                               .build();
        
        // Build ID generation service endpoint
        StringBuilder serviceEndpoint = new StringBuilder(appConfig.getIdGenHost())
                                      .append(appConfig.getIdGenPath());
        
        // Execute ID generation request
        Object rawResponse = apiClient.fetchResult(serviceEndpoint, generationRequest);
        
        // Convert response to structured format
        IdGenerationResponse idGenResponse = jsonConverter.convertValue(
            rawResponse, 
            IdGenerationResponse.class
        );

        // Extract generated IDs from response
        List<IdResponse> idResponseList = idGenResponse.getIdResponses();

        // Validate response
        if (CollectionUtils.isEmpty(idResponseList))
            throw new CustomException(IDGEN_ERROR, NO_IDS_FOUND_ERROR);

        // Extract and return IDs from response
        return idResponseList.stream()
                .map(IdResponse::getId)
                .collect(Collectors.toList());
    }
}