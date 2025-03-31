package digit.repository.rowmapper;

import digit.web.models.AuditDetails;
import digit.web.models.WaterConnection;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Component
public class WaterConnectionRowMapper implements ResultSetExtractor<WaterConnection> {

    /*
     * Transforms database result records to domain objects
     */
    @Override
    public WaterConnection extractData(ResultSet resultData) throws SQLException {
        // Initialize domain entity
        WaterConnection connectionEntity = new WaterConnection();

        // Transfer primary attributes
        populateBasicAttributes(connectionEntity, resultData);
        
        // Handle composite structures
        connectionEntity.setAuditDetails(createAuditInfo(resultData));

        return connectionEntity;
    }
    
    /**
     * Maps core connection properties from database record
     */
    private void populateBasicAttributes(WaterConnection entity, ResultSet data) throws SQLException {
        entity.setId(data.getString("id"));
        entity.setTenantId(data.getString("tenantId"));
        entity.setConnectionNo(data.getString("connectionNo"));
        
        // Handle enumerated type conversion
        String statusValue = data.getString("status");
        entity.setStatus(WaterConnection.StatusEnum.valueOf(statusValue));
    }

    /**
     * Creates audit information from database record
     */
    private AuditDetails createAuditInfo(ResultSet data) throws SQLException {
        AuditDetails audit = new AuditDetails();
        
        // Populate audit trail information
        audit.setCreatedBy(data.getString("createdBy"));
        audit.setLastModifiedBy(data.getString("lastModifiedBy"));
        audit.setCreatedTime(data.getLong("createdTime"));
        audit.setLastModifiedTime(data.getLong("lastModifiedTime"));
        
        return audit;
    }
}
