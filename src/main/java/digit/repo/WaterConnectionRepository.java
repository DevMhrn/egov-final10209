package digit.repository;

import digit.repository.querybuilder.WaterConnectionQueryBuilder;
import digit.repository.querybuilder.WaterConnectionSearchCriteria;
import digit.repository.rowmapper.WaterConnectionRowMapper;
import digit.web.models.WaterConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Data access layer for water connection operations
 */
@Repository
public class WaterConnectionRepository {

    // Data access components
    private final JdbcTemplate dbOperations;
    private final WaterConnectionQueryBuilder sqlGenerator;
    private final WaterConnectionRowMapper resultTransformer;

    /**
     * Initializes repository with required dependencies
     */
    @Autowired
    public WaterConnectionRepository(
            JdbcTemplate dbOperations,
            WaterConnectionQueryBuilder sqlGenerator,
            WaterConnectionRowMapper resultTransformer) {
        this.dbOperations = dbOperations;
        this.sqlGenerator = sqlGenerator;
        this.resultTransformer = resultTransformer;
    }

    /**
     * Retrieves water connections matching specified filters
     * @param filterCriteria Filters to apply when retrieving connections
     * @return Collection of matching water connections
     */
    public List<WaterConnection> getConnections(WaterConnectionSearchCriteria filterCriteria) {
        // Generate dynamic query based on search parameters
        String sqlQuery = sqlGenerator.getWaterConnectionSearchQuery(filterCriteria);
        
        // Execute query and transform results
        WaterConnection result = dbOperations.query(sqlQuery, resultTransformer);
        
        // Package result for consumer
        return (result != null) 
            ? Collections.singletonList(result)
            : new ArrayList<>();
    }
}
