package digit.validators;

import digit.repository.WaterConnectionRepository;
import digit.web.models.WaterConnection;
import digit.web.models.WaterConnectionRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import digit.repository.querybuilder.WaterConnectionSearchCriteria;

import java.util.List;

@Component
public class WaterConnectionValidator {

    @Autowired
    private WaterConnectionRepository connectionDataStore;

    /**
     * Ensures all water connection applications are properly formed
     * Validates core required fields for processing
     * 
     * @param applicationPackage The container with connection applications
     * @throws CustomException When validation fails with specific error code
     */
    public void validateWaterConnection(WaterConnectionRequest applicationPackage) {
        // Validate each connection in package
        applicationPackage.getConnections().forEach(connectionData -> {
            // Check for mandatory tenant information
            if (ObjectUtils.isEmpty(connectionData.getTenantId())) {
                throw new CustomException(
                    "WC_TENANTID_MISSING", 
                    "tenantId is mandatory for creating water connection applications"
                );
            }
            
            // Additional validations can be added here
        });
    }

    /**
     * Verifies existence of application before allowing updates
     * Retrieves current state for modification
     * 
     * @param connectionData The water connection to validate
     * @return The existing connection record if found
     * @throws CustomException When application does not exist
     */
    public WaterConnection validateApplicationExistence(WaterConnection connectionData) {
        // Build search criteria using connection number
        WaterConnectionSearchCriteria lookupCriteria = WaterConnectionSearchCriteria.builder()
                .connectionNumber(Long.valueOf(connectionData.getConnectionNo()))
                .build();
                
        // Search for existing connection
        List<WaterConnection> existingRecords = connectionDataStore.getConnections(lookupCriteria);

        // Validate connection exists
        if (existingRecords.isEmpty()) {
            throw new CustomException(
                "WC_APP_NOT_FOUND", 
                "No water connection application found with application number: " + 
                connectionData.getConnectionNo()
            );
        }

        // Return first matching record
        return existingRecords.get(0);
    }
}
