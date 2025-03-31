package digit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;

/**
 * Central configuration class for eGov application endpoints and services
 * Manages all external service integration points and connection details
 */
@Component
@Data
@Import({TracerConfiguration.class})
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Configuration {

    // User service integration points
    @Value("${egov.user.host}")
    private String userServiceBaseUrl;

    @Value("${egov.user.context.path}")
    private String userServiceContextPath;

    @Value("${egov.user.create.path}")
    private String userCreationEndpoint;

    @Value("${egov.user.search.path}")
    private String userLookupEndpoint;

    @Value("${egov.user.update.path}")
    private String userModificationEndpoint;

    // ID Generation service configuration
    @Value("${egov.idgen.host}")
    private String idGenerationHost;

    @Value("${egov.idgen.path}")
    private String idGenerationPath;

    // Workflow management configuration
    @Value("${egov.workflow.host}")
    private String workflowServiceHost;

    @Value("${egov.workflow.transition.path}")
    private String workflowTransitionEndpoint;

    @Value("${egov.workflow.businessservice.search.path}")
    private String workflowBusinessServiceLookupPath;

    @Value("${egov.workflow.processinstance.search.path}")
    private String workflowInstanceSearchPath;

    // Master Data Management System configuration
    @Value("${egov.mdms.host}")
    private String masterDataServiceHost;

    @Value("${egov.mdms.search.endpoint}")
    private String masterDataLookupEndpoint;

    // Human Resource Management System integration
    @Value("${egov.hrms.host}")
    private String humanResourceServiceBaseUrl;

    @Value("${egov.hrms.search.endpoint}")
    private String employeeSearchEndpoint;

    // URL Shortening service configuration
    @Value("${egov.url.shortner.host}")
    private String urlCompressionServiceHost;

    @Value("${egov.url.shortner.endpoint}")
    private String urlCompressionEndpoint;

    // Notification service configuration
    @Value("${egov.sms.notification.topic}")
    private String notificationServiceTopic;
}
