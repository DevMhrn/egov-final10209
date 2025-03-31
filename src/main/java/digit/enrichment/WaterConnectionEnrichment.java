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

@Component
@Slf4j
public class WaterConnectionEnrichment {

    @Autowired
    private IdgenUtil idgenUtil;

    /**
     * Enrich the WaterConnectionRequest with necessary details such as id, application number, and audit details.
     *
     * @param waterConnectionRequest WaterConnectionRequest object
     */
    public void enrichWaterConnectionRequest(WaterConnectionRequest waterConnectionRequest) {
        List<String> connectionIdList = idgenUtil.getIdList(
                waterConnectionRequest.getRequestInfo(),
                waterConnectionRequest.getWaterConnection().getTenantId(),
                "wc.connection.id", "", 1
        );

        WaterConnection connection = waterConnectionRequest.getWaterConnection();

        AuditDetails auditDetails = AuditDetails.builder()
                .createdBy(waterConnectionRequest.getRequestInfo().getUserInfo().getUuid())
                .createdTime(System.currentTimeMillis())
                .lastModifiedBy(waterConnectionRequest.getRequestInfo().getUserInfo().getUuid())
                .lastModifiedTime(System.currentTimeMillis())
                .build();

        connection.setAuditDetails(auditDetails);
        connection.setId(UUID.randomUUID().toString());
        connection.setConnectionNo(connectionIdList.get(0));
    }

    /**
     * Enrich the WaterConnectionRequest upon update with updated audit details.
     *
     * @param waterConnectionRequest WaterConnectionRequest object
     */
    public void enrichWaterConnectionUponUpdate(WaterConnectionRequest waterConnectionRequest) {
        WaterConnection connection = waterConnectionRequest.getWaterConnection();
        connection.getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
        connection.getAuditDetails().setLastModifiedBy(waterConnectionRequest.getRequestInfo().getUserInfo().getUuid());
    }

    public void enrichWaterConnectionOnSearch(WaterConnection waterConnection) {
        if (waterConnection != null) {
            // Preserving audit details from the existing data
            AuditDetails existingAuditDetails = waterConnection.getAuditDetails();

            // Creating enriched AuditDetails for search enrichment
            AuditDetails auditDetails = AuditDetails.builder()
                    .createdBy(existingAuditDetails != null ? existingAuditDetails.getCreatedBy() : null)  // Preserving createdBy
                    .createdTime(existingAuditDetails != null ? existingAuditDetails.getCreatedTime() : null)  // Preserving createdTime
                    .lastModifiedBy(UUID.randomUUID().toString())  // Generate a new UUID for modifiedBy
                    .lastModifiedTime(System.currentTimeMillis())  // Set current time for lastModifiedTime
                    .build();

            // Set enriched audit details back to the water connection
            waterConnection.setAuditDetails(auditDetails);

            // Example of logging the enriched data (can be used for debugging)
            log.info("Enriched water connection during search for connection number: " + waterConnection.getConnectionNo());
        } else {
            log.warn("WaterConnection object is null during search enrichment");
        }
    }
}
