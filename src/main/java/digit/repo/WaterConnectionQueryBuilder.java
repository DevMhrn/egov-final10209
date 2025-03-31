package digit.repository.querybuilder;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates SQL queries for water connection data access
 */
@Component
public class WaterConnectionQueryBuilder {

    // Core query definition with placeholder for dynamic conditions
    private static final String QUERY_TEMPLATE = 
        "SELECT * FROM water_connections WHERE tenantId = :tenantId";

    /**
     * Generates a SQL query with dynamic conditions based on provided criteria
     * @param criteria The filtering parameters
     * @return Complete SQL query string
     */
    public String getWaterConnectionSearchQuery(WaterConnectionSearchCriteria criteria) {
        // Start with base template
        StringBuilder dynamicQuery = new StringBuilder(QUERY_TEMPLATE);
        
        // Apply filtering expressions
        appendFilterCondition(dynamicQuery, "connectionNumber", criteria.getConnectionNumber());
        appendFilterCondition(dynamicQuery, "status", criteria.getStatus());
        appendFilterCondition(dynamicQuery, "id", criteria.getId());

        return dynamicQuery.toString();
    }

    /**
     * Appends a condition to the query when the parameter has a value
     * @param queryBuffer Query being constructed
     * @param fieldName Database column name
     * @param paramValue Parameter value to filter by
     */
    private void appendFilterCondition(StringBuilder queryBuffer, String fieldName, Object paramValue) {
        // Only add conditions for non-empty values
        if (paramValue != null && !isEmpty(paramValue)) {
            queryBuffer.append(" AND ")
                       .append(fieldName)
                       .append(" = :")
                       .append(fieldName);
        }
    }
    
    /**
     * Utility to check if a value should be considered empty
     */
    private boolean isEmpty(Object value) {
        return value == null || 
               (value instanceof String && ((String)value).trim().isEmpty());
    }
}
