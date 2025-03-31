package digit.repository.querybuilder;

/**
 * Represents possible states of a water connection in the system
 */
public enum Status {
    // Base states
    ACTIVE("Connection is operational"),
    INACTIVE("Connection is temporarily suspended"),
    
    // Processing states
    PENDING("Awaiting approval or processing"),
    APPROVED("Officially authorized for use"),
    REJECTED("Connection request denied");
    
    // Additional metadata
    private final String description;
    
    /**
     * Creates a status with descriptive text
     */
    Status(String description) {
        this.description = description;
    }
    
    /**
     * Returns human-readable explanation of this status
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Returns if this status represents an active state
     */
    public boolean isActive() {
        return this == ACTIVE || this == APPROVED;
    }
}
