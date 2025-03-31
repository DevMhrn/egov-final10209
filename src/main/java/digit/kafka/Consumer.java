package digit.kafka;

//import org.springframework.kafka.annotation.KafkaListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import digit.repository.WaterConnectionRepository;
import digit.web.models.WaterConnection;
import digit.web.models.WaterConnectionRequest;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Message consumer for processing Kafka events
 * Handles asynchronous event processing from message queue
 * Supports water connection lifecycle operations
 */
@Component
@Slf4j
public class Consumer {

    @Value("${kafka.topics.consumer:default-consumer-topic}")
    private String consumerTopic;
    
    @Value("${water.connection.save.topic:save-wc-application}")
    private String saveWaterConnectionTopic;
    
    @Value("${water.connection.update.topic:update-wc-application}")
    private String updateWaterConnectionTopic;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private WaterConnectionRepository waterConnectionRepository;
    
    /*
     * To activate message consumption:
     * 1. Uncomment the KafkaListener annotation
     * 2. Ensure kafka.topics.consumer is properly set in application.properties
     * 3. Configure appropriate consumer group if needed
     */
    //@KafkaListener(topics = {"${kafka.topics.consumer}", "${water.connection.save.topic}", "${water.connection.update.topic}"}, 
    //              groupId = "${spring.kafka.consumer.group-id:water-services-group}")
    public void processIncomingMessage(final HashMap<String, Object> messagePayload) {
        if (messagePayload == null) {
            log.error("Received null message payload from topic");
            return;
        }
        
        try {
            // Extract message metadata if available
            String messageType = extractMessageType(messagePayload);
            String messageId = extractMessageIdentifier(messagePayload);
            String sourceTopic = extractSourceTopic(messagePayload);
            
            log.info("Processing message {} of type {} from topic {}", messageId, messageType, sourceTopic);
            
            // Route message to appropriate handler based on topic and type
            boolean processed = routeMessageToHandler(messagePayload, sourceTopic, messageType, messageId);
            
            if (processed) {
                log.info("Successfully processed message: {}", messageId);
            } else {
                log.warn("Message {} could not be processed - no matching handler", messageId);
            }
        } catch (Exception ex) {
            log.error("Error processing kafka message: {}", ex.getMessage(), ex);
            handleProcessingFailure(messagePayload, ex);
        }
    }
    
    /**
     * Routes incoming message to appropriate handler based on topic and message type
     * 
     * @return true if message was successfully routed to a handler
     */
    private boolean routeMessageToHandler(HashMap<String, Object> payload, String topic, String messageType, String messageId) {
        // Water connection creation
        if (saveWaterConnectionTopic.equals(topic)) {
            return handleWaterConnectionCreation(payload, messageId);
        }
        
        // Water connection update
        if (updateWaterConnectionTopic.equals(topic)) {
            return handleWaterConnectionUpdate(payload, messageId);
        }
        
        // Default topic processing
        if (consumerTopic.equals(topic)) {
            return handleGeneralMessage(payload, messageType, messageId);
        }
        
        log.debug("No specific handler for topic: {}", topic);
        return false;
    }
    
    /**
     * Handles water connection creation messages
     */
    private boolean handleWaterConnectionCreation(HashMap<String, Object> payload, String messageId) {
        try {
            log.debug("Processing water connection creation: {}", messageId);
            WaterConnectionRequest request = objectMapper.convertValue(payload, WaterConnectionRequest.class);
            
            if (request == null || request.getWaterConnection() == null) {
                throw new CustomException("INVALID_CONNECTION_REQUEST", "Water connection data missing");
            }
            
            // Persist water connection
            waterConnectionRepository.saveWaterConnection(request.getWaterConnection());
            log.info("Successfully saved water connection: {}", request.getWaterConnection().getConnectionNo());
            return true;
        } catch (Exception ex) {
            log.error("Failed to process water connection creation: {}", ex.getMessage(), ex);
            throw new CustomException("WATER_CONNECTION_CREATE_ERROR", "Failed to create water connection: " + ex.getMessage());
        }
    }
    
    /**
     * Handles water connection update messages
     */
    private boolean handleWaterConnectionUpdate(HashMap<String, Object> payload, String messageId) {
        try {
            log.debug("Processing water connection update: {}", messageId);
            WaterConnectionRequest request = objectMapper.convertValue(payload, WaterConnectionRequest.class);
            
            if (request == null || request.getWaterConnection() == null) {
                throw new CustomException("INVALID_UPDATE_REQUEST", "Water connection update data missing");
            }
            
            WaterConnection connection = request.getWaterConnection();
            
            // Update water connection
            waterConnectionRepository.updateWaterConnection(connection);
            log.info("Successfully updated water connection: {}", connection.getConnectionNo());
            return true;
        } catch (Exception ex) {
            log.error("Failed to process water connection update: {}", ex.getMessage(), ex);
            throw new CustomException("WATER_CONNECTION_UPDATE_ERROR", "Failed to update water connection: " + ex.getMessage());
        }
    }
    
    /**
     * Handles general messages from default topic
     */
    private boolean handleGeneralMessage(HashMap<String, Object> payload, String messageType, String messageId) {
        log.debug("Processing general message type: {}", messageType);
        
        // Implementation for different message types can be added here
        // For example: Notifications, status updates, etc.
        
        switch (messageType) {
            case "STATUS_UPDATE":
                return processStatusUpdate(payload);
            case "NOTIFICATION":
                return processNotification(payload);
            default:
                log.debug("No specific handler for message type: {}", messageType);
                return false;
        }
    }
    
    /**
     * Processes status update messages
     */
    private boolean processStatusUpdate(HashMap<String, Object> payload) {
        log.debug("Processing status update message");
        // Implementation for status updates
        return true;
    }
    
    /**
     * Processes notification messages
     */
    private boolean processNotification(HashMap<String, Object> payload) {
        log.debug("Processing notification message");
        // Implementation for notifications
        return true;
    }
    
    /**
     * Handles message processing failures
     */
    private void handleProcessingFailure(HashMap<String, Object> payload, Exception error) {
        // Implement dead-letter queue or retry logic
        // For example: Push to error topic, store in error log, etc.
        
        String messageId = extractMessageIdentifier(payload);
        log.error("Message processing failed for {}: {}", messageId, error.getMessage());
        
        // Could implement retry logic or push to dead-letter queue
        // messageBroker.push("error-messages", buildErrorPayload(payload, error));
    }
    
    /**
     * Extracts message type from payload for routing
     */
    private String extractMessageType(Map<String, Object> payload) {
        Object typeField = payload.get("messageType");
        
        if (typeField == null) {
            // Try to determine message type from structure
            if (payload.containsKey("WaterConnection") || payload.containsKey("waterConnection")) {
                return "WATER_CONNECTION";
            }
        }
        
        return typeField != null ? typeField.toString() : "UNKNOWN";
    }
    
    /**
     * Extracts unique message identifier for tracking
     */
    private String extractMessageIdentifier(Map<String, Object> payload) {
        // Try various common ID fields
        for (String idField : new String[]{"messageId", "id", "uuid", "requestId"}) {
            if (payload.containsKey(idField) && payload.get(idField) != null) {
                return payload.get(idField).toString();
            }
        }
        
        // Generate unique ID if none exists
        return "msg-" + System.currentTimeMillis() + "-" + Math.round(Math.random() * 10000);
    }
    
    /**
     * Extracts source topic from payload metadata
     */
    private String extractSourceTopic(Map<String, Object> payload) {
        Object topicField = payload.get("sourceTopic");
        return topicField != null ? topicField.toString() : "unknown-topic";
    }
}
