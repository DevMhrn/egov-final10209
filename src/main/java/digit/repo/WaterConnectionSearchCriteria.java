package digit.repository.querybuilder;

/**
 * Encapsulates parameters for water connection searches
 */
public class WaterConnectionSearchCriteria {
    // Search parameters
    private Long waterConnectionId;
    private Status connectionStatus;
    private Long uniqueIdentifier;

    // Standard accessors
    public Long getConnectionNumber() {
        return waterConnectionId;
    }

    public void setConnectionNumber(Long waterConnectionId) {
        this.waterConnectionId = waterConnectionId;
    }

    public Status getStatus() {
        return connectionStatus;
    }

    public void setStatus(Status connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public Long getId() {
        return uniqueIdentifier;
    }

    public void setId(Long uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    // Factory method for fluent construction
    public static CriteriaConstructor initialize() {
        return new CriteriaConstructor();
    }

    /**
     * Inner class facilitating fluent construction pattern
     */
    public static class CriteriaConstructor {
        // Builder fields
        private Long waterConnectionId;
        private Status connectionStatus;
        private Long uniqueIdentifier;

        public CriteriaConstructor withConnectionNumber(Long waterConnectionId) {
            this.waterConnectionId = waterConnectionId;
            return this;
        }

        public CriteriaConstructor withStatus(Status connectionStatus) {
            this.connectionStatus = connectionStatus;
            return this;
        }

        public CriteriaConstructor withId(Long uniqueIdentifier) {
            this.uniqueIdentifier = uniqueIdentifier;
            return this;
        }

        public WaterConnectionSearchCriteria construct() {
            WaterConnectionSearchCriteria searchParams = new WaterConnectionSearchCriteria();
            searchParams.setConnectionNumber(this.waterConnectionId);
            searchParams.setStatus(this.connectionStatus);
            searchParams.setId(this.uniqueIdentifier);
            return searchParams;
        }
    }
    
    // Compatibility method for existing code
    public static CriteriaConstructor builder() {
        return initialize();
    }
}
