package digit.enrichment;

import digit.util.IdgenUtil;
import digit.web.models.AuditDetails;
import digit.web.models.WaterConnection;
import digit.web.models.WaterConnectionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Component responsible for enriching water connection data
 * Handles identifier generation, audit trails and data validation
 */
@Component
@Slf4j
public class WaterConnectionEnrichment {

    private final IdgenUtil idgenUtil;
    
    @Autowired
    public WaterConnectionEnrichment(IdgenUtil idgenUtil) {
        this.idgenUtil = idgenUtil;
    }

    /**
     * Enriches a new water connection request with identifiers and audit information
     * 
     * @param waterConnectionRequest Request payload containing connection details
     */
    public void enrichWaterConnectionRequest(WaterConnectionRequest waterConnectionRequest) {
        // Generate a unique connection identifier from ID service
        final String tenantIdentifier = waterConnectionRequest.getWaterConnection().getTenantId();
        final String requestorUuid = waterConnectionRequest.getRequestInfo().getUserInfo().getUuid();
        final long currentTimestamp = System.currentTimeMillis();
        
        // Fetch connection number from ID generation service
        List<String> connectionIdentifiers = idgenUtil.getIdList(
                waterConnectionRequest.getRequestInfo(),
                tenantIdentifier,
                "wc.connection.id", 
                "", 
                1
        );
        
        WaterConnection connectionEntity = waterConnectionRequest.getWaterConnection();
        
        // Create comprehensive audit trail
        AuditDetails auditMetadata = createAuditMetadata(requestorUuid, currentTimestamp, requestorUuid, currentTimestamp);
        
        // Apply identifiers and metadata
        connectionEntity.setAuditDetails(auditMetadata);
        connectionEntity.setId(generateUniqueIdentifier());
        connectionEntity.setConnectionNo(connectionIdentifiers.isEmpty() ? null : connectionIdentifiers.get(0));
    }

    /**
     * Updates audit information during connection modification
     * 
     * @param waterConnectionRequest Request containing updated connection details
     */
    public void enrichWaterConnectionUponUpdate(WaterConnectionRequest waterConnectionRequest) {
        final WaterConnection connectionData = waterConnectionRequest.getWaterConnection();
        final String modifierUuid = waterConnectionRequest.getRequestInfo().getUserInfo().getUuid();
        final long modificationTimestamp = System.currentTimeMillis();
        
        // Update only modification-related audit fields
        AuditDetails existingAuditTrail = connectionData.getAuditDetails();
        if (existingAuditTrail != null) {
            existingAuditTrail.setLastModifiedTime(modificationTimestamp);
            existingAuditTrail.setLastModifiedBy(modifierUuid);
        } else {
            log.warn("Missing audit details during connection update: {}", connectionData.getConnectionNo());
            connectionData.setAuditDetails(createAuditMetadata(modifierUuid, modificationTimestamp, modifierUuid, modificationTimestamp));
        }
    }

    /**
     * Enriches connection information during search operations
     * 
     * @param waterConnection Connection entity to be enriched
     */
    public void enrichWaterConnectionOnSearch(WaterConnection waterConnection) {
        if (waterConnection == null) {
            log.warn("Cannot enrich null water connection during search operation");
            return;
        }
        
        // Extract existing audit information if available
        AuditDetails existingAuditData = waterConnection.getAuditDetails();
        String originalCreator = (existingAuditData != null) ? existingAuditData.getCreatedBy() : null;
        Long creationTimestamp = (existingAuditData != null) ? existingAuditData.getCreatedTime() : null;
        
        // Generate system identifier for query operations
        String systemGeneratedId = generateUniqueIdentifier();
        long currentTimestamp = System.currentTimeMillis();
        
        // Construct enriched audit trail preserving original creation data
        AuditDetails enhancedAuditData = AuditDetails.builder()
                .createdBy(originalCreator)
                .createdTime(creationTimestamp)
                .lastModifiedBy(systemGeneratedId)
                .lastModifiedTime(currentTimestamp)
                .build();
        
        // Apply enhanced data
        waterConnection.setAuditDetails(enhancedAuditData);
        
        log.info("Search enrichment completed for connection: {}", 
                 waterConnection.getConnectionNo() != null ? waterConnection.getConnectionNo() : waterConnection.getId());
    }
    
    /**
     * Creates a standardized audit metadata object
     */
    private AuditDetails createAuditMetadata(String creator, Long createdTime, 
                                            String modifier, Long modifiedTime) {
        return AuditDetails.builder()
                .createdBy(creator)
                .createdTime(createdTime)
                .lastModifiedBy(modifier)
                .lastModifiedTime(modifiedTime)
                .build();
    }
    
    /**
     * Generates a globally unique identifier for entities
     */
    private String generateUniqueIdentifier() {
        return UUID.randomUUID().toString();
    }
}
