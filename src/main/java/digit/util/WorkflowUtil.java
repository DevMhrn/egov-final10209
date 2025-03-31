package digit.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.config.Configuration;
import static digit.config.ServiceConstants.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.contract.workflow.*;
import org.egov.common.contract.models.*;
import digit.repository.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WorkflowUtil {

    @Autowired
    private ServiceRequestRepository dataFetcher;

    @Autowired
    private ObjectMapper jsonConverter;

    @Autowired
    private Configuration appSettings;

    /**
    * Obtains business service definition corresponding to service code
    * Throws appropriate exceptions when service definition cannot be found
    * 
    * @param reqInfo Client request metadata 
    * @param jurisdictionId Tenant jurisdiction identifier
    * @param serviceCode Business service type identifier
    * @return Service definition object with workflow states
    */
    public BusinessService getBusinessService(RequestInfo reqInfo, String jurisdictionId, String serviceCode) {
        // Construct endpoint with appropriate parameters
        StringBuilder serviceEndpoint = buildSearchEndpointWithParams(jurisdictionId, serviceCode);
        
        // Prepare request wrapper with metadata
        RequestInfoWrapper reqWrapper = RequestInfoWrapper.builder()
                                        .requestInfo(reqInfo)
                                        .build();
        
        // Execute service definition lookup
        Object serviceResult = dataFetcher.fetchResult(serviceEndpoint, reqWrapper);
        BusinessServiceResponse serviceDefResponse = null;
        
        // Handle response mapping
        try {
            serviceDefResponse = jsonConverter.convertValue(serviceResult, BusinessServiceResponse.class);
        } catch (IllegalArgumentException mappingError) {
            throw new CustomException(PARSING_ERROR, FAILED_TO_PARSE_BUSINESS_SERVICE_SEARCH);
        }

        // Validate service definition exists
        if (CollectionUtils.isEmpty(serviceDefResponse.getBusinessServices()))
            throw new CustomException(BUSINESS_SERVICE_NOT_FOUND, 
                                    THE_BUSINESS_SERVICE + serviceCode + NOT_FOUND);

        // Return first matching service definition
        return serviceDefResponse.getBusinessServices().get(0);
    }

    /**
    * Executes workflow transition and returns updated application status
    * Manages complete interaction with workflow engine
    * 
    * @param reqInfo Client request metadata
    * @param jurisdictionId Tenant jurisdiction identifier 
    * @param applicationId Business entity identifier
    * @param serviceCode Business service type identifier
    * @param workflowAction Action to be performed
    * @param moduleName System module handling the workflow
    * @return Updated status after workflow execution
    */
    public String updateWorkflowStatus(RequestInfo reqInfo, String jurisdictionId,
        String applicationId, String serviceCode, Workflow workflowAction, String moduleName) {
        
        // Prepare workflow execution context
        ProcessInstance workflowContext = buildProcessInstanceForWorkflow(
            reqInfo, jurisdictionId, applicationId, serviceCode, workflowAction, moduleName);
        
        // Create workflow transaction request
        ProcessInstanceRequest workflowTransactionRequest = new ProcessInstanceRequest(
            reqInfo, Collections.singletonList(workflowContext));
        
        // Execute workflow transition
        State resultState = executeWorkflowTransaction(workflowTransactionRequest);

        // Return updated application status
        return resultState.getApplicationStatus();
    }

    /**
    * Builds workflow service lookup endpoint with appropriate parameters
    * 
    * @param jurisdictionId Tenant jurisdiction identifier
    * @param serviceCode Business service type identifier
    * @return Fully formed endpoint URI
    */
    private StringBuilder buildSearchEndpointWithParams(String jurisdictionId, String serviceCode) {
        StringBuilder endpoint = new StringBuilder(appSettings.getWfHost());
        endpoint.append(appSettings.getWfBusinessServiceSearchPath());
        endpoint.append(TENANTID);
        endpoint.append(jurisdictionId);
        endpoint.append(BUSINESS_SERVICES);
        endpoint.append(serviceCode);
        return endpoint;
    }

    /**
    * Creates workflow process instance for execution
    * Enriches with all required metadata
    * 
    * @param reqInfo Client request metadata
    * @param jurisdictionId Tenant jurisdiction identifier
    * @param applicationId Business entity identifier
    * @param serviceCode Business service type identifier
    * @param workflowAction Action to be performed
    * @param moduleName System module handling the workflow
    * @return Fully formed process instance ready for execution
    */
    private ProcessInstance buildProcessInstanceForWorkflow(RequestInfo reqInfo, String jurisdictionId,
        String applicationId, String serviceCode, Workflow workflowAction, String moduleName) {

        // Initialize workflow execution context
        ProcessInstance workflowContext = new ProcessInstance();
        
        // Set core workflow execution parameters
        workflowContext.setBusinessId(applicationId);
        workflowContext.setAction(workflowAction.getAction());
        workflowContext.setModuleName(moduleName);
        workflowContext.setTenantId(jurisdictionId);
        
        // Resolve business service definition and set in context
        workflowContext.setBusinessService(
            getBusinessService(reqInfo, jurisdictionId, serviceCode).getBusinessService());
        
        // Add workflow transition comments
        workflowContext.setComment(workflowAction.getComments());

        // Handle assignee information if provided
        if(!CollectionUtils.isEmpty(workflowAction.getAssignes())) {
            List<User> assigneeList = new ArrayList<>();

            // Map assignee IDs to user objects
            workflowAction.getAssignes().forEach(assigneeId -> {
                User assignee = new User();
                assignee.setUuid(assigneeId);
                assigneeList.add(assignee);
            });

            // Set assignees in workflow context
            workflowContext.setAssignes(assigneeList);
        }

        return workflowContext;
    }

    /**
    * Extracts workflow actions from process instances
    * Maps business IDs to corresponding workflow actions
    * 
    * @param processContexts List of workflow process instances
    * @return Map of business IDs to workflow actions
    */
    public Map<String, Workflow> getWorkflow(List<ProcessInstance> processContexts) {
        // Initialize result map
        Map<String, Workflow> entityToWorkflowMap = new HashMap<>();

        // Process each workflow instance
        processContexts.forEach(processContext -> {
            List<String> assigneeIds = null;

            // Extract assignee IDs if present
            if(!CollectionUtils.isEmpty(processContext.getAssignes())){
                assigneeIds = processContext.getAssignes().stream()
                             .map(User::getUuid)
                             .collect(Collectors.toList());
            }

            // Create workflow action summary
            Workflow workflowSummary = Workflow.builder()
                .action(processContext.getAction())
                .assignes(assigneeIds)
                .comments(processContext.getComment())
                .build();

            // Map to business entity
            entityToWorkflowMap.put(processContext.getBusinessId(), workflowSummary);
        });

        return entityToWorkflowMap;
    }

    /**
    * Executes workflow transition and returns resulting state
    * 
    * @param workflowTransactionRequest Workflow execution request
    * @return Resulting workflow state after execution
    */
    private State executeWorkflowTransaction(ProcessInstanceRequest workflowTransactionRequest) {
        // Build workflow execution endpoint
        StringBuilder endpoint = new StringBuilder(
            appSettings.getWfHost().concat(appSettings.getWfTransitionPath()));
        
        // Execute workflow transition
        Object responseObj = dataFetcher.fetchResult(endpoint, workflowTransactionRequest);
        
        // Parse workflow execution response
        ProcessInstanceResponse executionResponse = 
            jsonConverter.convertValue(responseObj, ProcessInstanceResponse.class);
        
        // Return resulting state
        return executionResponse.getProcessInstances().get(0).getState();
    }
}