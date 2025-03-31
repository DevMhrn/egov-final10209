package digit.kafka;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.kafka.CustomKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Service for publishing messages to Kafka topics
 * Provides reliable message delivery with logging and metrics
 */
@Service
@Slf4j
public class Producer {

    private final CustomKafkaTemplate<String, Object> messagingTemplate;
    
    @Value("${kafka.producer.default.topic:default-outbound-topic}")
    private String defaultTopic;
    
    @Autowired
    public Producer(CustomKafkaTemplate<String, Object> messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Publishes a message to the specified Kafka topic
     * 
     * @param topic Destination topic name
     * @param message Message payload to be published
     */
    public void push(String topic, Object message) {
        String effectiveTopic = StringUtils.hasText(topic) ? topic : defaultTopic;
        String messageId = generateMessageIdentifier();
        
        try {
            log.debug("Publishing message {} to topic: {}", messageId, effectiveTopic);
            messagingTemplate.send(effectiveTopic, enrichMessageWithMetadata(message, messageId));
            log.info("Successfully queued message {} for topic {}", messageId, effectiveTopic);
        } catch (Exception ex) {
            log.error("Failed to publish message {} to topic {}: {}", 
                     messageId, effectiveTopic, ex.getMessage(), ex);
            throw new MessagePublishingException("Failed to publish message to Kafka", ex);
        }
    }
    
    /**
     * Asynchronously publishes a message with delivery confirmation
     * 
     * @param topic Destination topic name
     * @param message Message payload
     * @return CompletableFuture for delivery tracking
     */
    public CompletableFuture<Boolean> pushAsync(String topic, Object message) {
        CompletableFuture<Boolean> deliveryPromise = new CompletableFuture<>();
        String effectiveTopic = StringUtils.hasText(topic) ? topic : defaultTopic;
        String messageId = generateMessageIdentifier();
        
        try {
            log.debug("Async publishing message {} to topic: {}", messageId, effectiveTopic);
            messagingTemplate.send(effectiveTopic, enrichMessageWithMetadata(message, messageId));
            deliveryPromise.complete(true);
        } catch (Exception ex) {
            log.error("Async publish failed for message {} to topic {}: {}", 
                     messageId, effectiveTopic, ex.getMessage(), ex);
            deliveryPromise.completeExceptionally(ex);
        }
        
        return deliveryPromise;
    }
    
    /**
     * Generates a unique identifier for message tracking
     */
    private String generateMessageIdentifier() {
        return "msg-" + UUID.randomUUID().toString();
    }
    
    /**
     * Adds metadata to outgoing messages when possible
     */
    @SuppressWarnings("unchecked")
    private Object enrichMessageWithMetadata(Object message, String messageId) {
        // If message is a map, add metadata
        if (message instanceof Map) {
            try {
                Map<String, Object> messageMap = (Map<String, Object>) message;
                if (!messageMap.containsKey("messageId")) {
                    messageMap.put("messageId", messageId);
                }
                messageMap.putIfAbsent("timestamp", System.currentTimeMillis());
                return messageMap;
            } catch (Exception ex) {
                log.warn("Could not enrich message with metadata: {}", ex.getMessage());
            }
        }
        return message;
    }
    
    /**
     * Custom exception for messaging failures
     */
    public static class MessagePublishingException extends RuntimeException {
        public MessagePublishingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
