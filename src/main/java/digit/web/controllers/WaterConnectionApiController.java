package digit.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.service.WaterConnectionService;
import digit.util.ResponseInfoFactory;
import digit.web.models.WaterConnectionRequest;
import digit.web.models.WaterConnectionResponse;
import digit.web.models.WaterConnectionSearchRequest;
import digit.web.models.WaterConnection;
import digit.web.models.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Collections;

@Controller
@RequestMapping("/v1/water-connection")
public class WaterConnectionApiController {

    // Core dependencies
    private final ObjectMapper dataConverter;
    private final HttpServletRequest webRequest;
    private final WaterConnectionService applicationProcessor;
    
    @Autowired
    private ResponseInfoFactory metadataFactory;

    @Autowired
    public WaterConnectionApiController(
            ObjectMapper dataConverter, 
            HttpServletRequest webRequest, 
            WaterConnectionService applicationProcessor) {
        this.dataConverter = dataConverter;
        this.webRequest = webRequest;
        this.applicationProcessor = applicationProcessor;
    }

    /**
     * Handles registration of new water connection applications
     * Accepts application details and processes through service layer
     */
    @RequestMapping(value = "/_create", method = RequestMethod.POST)
    public ResponseEntity<WaterConnectionResponse> registerNewApplication(
            @Valid @RequestBody WaterConnectionRequest applicationDetails) {

        // Process application creation request
        WaterConnection registeredConnection = applicationProcessor.createWaterConnection(applicationDetails);
        
        // Generate response metadata
        ResponseInfo responseMetadata = metadataFactory.createResponseInfoFromRequestInfo(
                applicationDetails.getRequestInfo(), true);
                
        // Compile complete response
        WaterConnectionResponse applicationResponse = WaterConnectionResponse.builder()
                .waterConnection(Collections.singletonList(registeredConnection))
                .responseInfo(responseMetadata)
                .build();

        // Return response with success status
        return new ResponseEntity<>(applicationResponse, HttpStatus.OK);
    }

    /**
     * Allows searching for water connection applications
     * Supports various filter criteria for precise lookups
     */
    @RequestMapping(value = "/registration/_search", method = RequestMethod.POST)
    public ResponseEntity<WaterConnectionResponse> findApplications(
            @Valid @RequestBody WaterConnectionSearchRequest searchQuery) {

        // Execute application search
        List<WaterConnection> foundApplications = applicationProcessor.searchWaterConnections(
                searchQuery.getWaterConnectionSearchCriteria());
        
        // Generate response metadata
        ResponseInfo responseMetadata = metadataFactory.createResponseInfoFromRequestInfo(
                searchQuery.getRequestInfo(), true);
        
        // Compile search results
        WaterConnectionResponse searchResponse = WaterConnectionResponse.builder()
                .waterConnection(foundApplications)
                .responseInfo(responseMetadata)
                .build();

        // Always return 200 OK, even for empty results
        if (foundApplications.isEmpty()) {
            return new ResponseEntity<>(searchResponse, HttpStatus.OK);
        }
        
        // Return response with success status
        return new ResponseEntity<>(searchResponse, HttpStatus.OK);
    }

    /**
     * Processes updates to existing water connection applications
     * Allows modification of application details
     */
    @RequestMapping(value = "/registration/_update", method = RequestMethod.POST)
    public ResponseEntity<WaterConnectionResponse> amendExistingApplication(
            @Valid @RequestBody WaterConnectionRequest modificationRequest) {

        // Process update request
        WaterConnection updatedConnection = applicationProcessor.updateWaterConnection(modificationRequest);
        
        // Generate response metadata
        ResponseInfo responseMetadata = metadataFactory.createResponseInfoFromRequestInfo(
                modificationRequest.getRequestInfo(), true);
        
        // Compile update response
        WaterConnectionResponse updateResponse = WaterConnectionResponse.builder()
                .waterConnection(Collections.singletonList(updatedConnection))
                .responseInfo(responseMetadata)
                .build();

        // Return response with success status
        return new ResponseEntity<>(updateResponse, HttpStatus.OK);
    }
}
