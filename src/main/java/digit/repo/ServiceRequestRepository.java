package digit.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static digit.config.ServiceConstants.*;

@Repository
@Slf4j
public class ServiceRequestRepository {

    // Core components for external communication
    private final ObjectMapper jsonMapper;
    private final RestTemplate apiClient;

    @Autowired
    public ServiceRequestRepository(ObjectMapper jsonMapper, RestTemplate apiClient) {
        this.jsonMapper = jsonMapper;
        this.apiClient = apiClient;
    }

    /**
     * Executes a remote service call and retrieves the result
     * @param endpointUrl The target endpoint
     * @param payload Request data to be sent
     * @return Response from external service
     */
    public Object fetchResult(StringBuilder endpointUrl, Object payload) {
        // Configure object serialization strategy
        jsonMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        
        Object serviceResponse = null;
        try {
            // Execute remote call
            serviceResponse = apiClient.postForObject(
                endpointUrl.toString(), 
                payload, 
                Map.class
            );
        } catch(HttpClientErrorException clientEx) {
            // Handle client-side communication errors
            log.error(EXTERNAL_SERVICE_EXCEPTION, clientEx);
            throw new ServiceCallException(clientEx.getResponseBodyAsString());
        } catch(Exception generalEx) {
            // Handle all other exceptions
            log.error(SEARCHER_SERVICE_EXCEPTION, generalEx);
        }

        return serviceResponse;
    }
}