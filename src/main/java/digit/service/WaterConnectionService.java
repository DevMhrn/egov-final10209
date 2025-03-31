package digit.service;

import digit.kafka.Producer;
import digit.repository.WaterConnectionRepository;
import digit.repository.querybuilder.WaterConnectionSearchCriteria;
import digit.web.models.WaterConnection;
import digit.web.models.WaterConnectionRequest;
import digit.enrichment.WaterConnectionEnrichment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import digit.validators.WaterConnectionValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class WaterConnectionService {
    
    // Core dependencies for water connection processing
    @Autowired
    private WaterConnectionValidator validationHandler;

    @Autowired
    private WaterConnectionEnrichment dataEnricher;

    @Autowired
    private WaterConnectionRepository connectionDataStore;

    @Autowired
    private Producer messageBroker;

    /**
     * Processes new water connection applications
     * Performs validation, enrichment, and persistence
     */
    public WaterConnection createWaterConnection(WaterConnectionRequest applicationData) {
        // Step 1: Validate all incoming data
        validationHandler.validateWaterConnection(applicationData);
        
        // Step 2: Enrich application with system-generated data
        dataEnricher.enrichWaterConnectionRequest(applicationData);
        
        // Step 3: Update application processing status
        applyWorkflowTransition(applicationData);
        
        // Step 4: Queue for asynchronous persistence
        messageBroker.push("save-wc-application", applicationData);
        
        // Return processed application
        return applicationData.getWaterConnection();
    }

    /**
     * Manages state transitions for water connection applications
     */
    private void applyWorkflowTransition(WaterConnectionRequest applicationData) {
        WaterConnection connection = applicationData.getWaterConnection();

        // Apply business rules for status transition

        if (Objects.equals(connection.getStatus(), WaterConnection.StatusEnum.IN_PROGRESS)) {
            // Transition to next logical state per workflow rules
            connection.setStatus(WaterConnection.StatusEnum.ACTIVE);
        }
    }

    /**
     * Retrieves water connections matching specified criteria
     * Returns enriched connection data
     */
    public List<WaterConnection> searchWaterConnections(WaterConnectionSearchCriteria filterCriteria) {
        // Step 1: Query repository for matching records
        List<WaterConnection> resultRecords = connectionDataStore.getConnections(filterCriteria);

        // Step 2: Handle empty result case

        if (CollectionUtils.isEmpty(resultRecords)) {
            return new ArrayList<>(); // Return empty collection instead of null
        }

        // Step 3: Post-process and enrich each result

        for (WaterConnection record : resultRecords) {
            dataEnricher.enrichWaterConnectionOnSearch(record);
        }

        // Return fully processed results

        return resultRecords;
    }

    /**
     * Processes changes to existing water connection records
     * Validates, enriches, and persists modifications
     */
    public WaterConnection updateWaterConnection(WaterConnectionRequest modificationData) {
        // Step 1: Verify existence and retrieve current state
        WaterConnection currentRecord = validationHandler.validateApplicationExistence(
                modificationData.getWaterConnection());

        // Step 2: Apply modifications to validated record

        modificationData.setWaterConnection(currentRecord);

        // Step 3: Supplement with system-generated data
        dataEnricher.enrichWaterConnectionUponUpdate(modificationData);

        // Step 4: Apply relevant workflow transitions
        
        applyWorkflowTransition(modificationData);

        // Step 5: Queue for asynchronous persistence
        messageBroker.push("update-wc-application", modificationData);

        // Return processed modification
        return modificationData.getWaterConnection();
    }
}
