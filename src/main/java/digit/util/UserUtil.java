package digit.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.config.Configuration;
import static digit.config.ServiceConstants.*;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.User;
import org.egov.common.contract.user.UserDetailResponse;
import org.egov.common.contract.user.enums.UserType;
import digit.repository.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class UserUtil {

    @Autowired
    private ObjectMapper dataMapper;

    @Autowired
    private ServiceRequestRepository apiClient;

    @Autowired
    private Configuration systemConfig;

    @Autowired
    public UserUtil(ObjectMapper dataMapper, ServiceRequestRepository apiClient) {
        this.dataMapper = dataMapper;
        this.apiClient = apiClient;
    }

    /**
     * Interacts with identity service to process user data
     * Handles format conversions and date parsing automatically
     * 
     * @param requestPayload Request data for identity service
     * @param serviceEndpoint Target service endpoint
     * @return Processed user details from identity service
     */
    public UserDetailResponse processUserServiceRequest(Object requestPayload, StringBuilder serviceEndpoint) {
        // Determine appropriate date format based on endpoint
        String dateFormat = determineFormatForEndpoint(serviceEndpoint);
        
        try {
            // Execute API request and get raw response
            LinkedHashMap rawResponseData = (LinkedHashMap)apiClient.fetchResult(serviceEndpoint, requestPayload);
            
            // Process date fields in response
            normalizeResponseDateFields(rawResponseData, dateFormat);
            
            // Convert response to structured object
            UserDetailResponse userDetails = dataMapper.convertValue(rawResponseData, UserDetailResponse.class);
            return userDetails;
        }
        catch(IllegalArgumentException conversionError) {
            throw new CustomException(ILLEGAL_ARGUMENT_EXCEPTION_CODE, OBJECTMAPPER_UNABLE_TO_CONVERT);
        }
    }

    /**
     * Determines appropriate date format based on endpoint type
     * 
     * @param endpoint Service endpoint being called
     * @return Date format suitable for the endpoint
     */
    private String determineFormatForEndpoint(StringBuilder endpoint) {
        String endpointStr = endpoint.toString();
        if(endpointStr.contains(systemConfig.getUserSearchEndpoint()) || 
           endpointStr.contains(systemConfig.getUserUpdateEndpoint())) {
            return DOB_FORMAT_Y_M_D;
        } else if(endpointStr.contains(systemConfig.getUserCreateEndpoint())) {
            return DOB_FORMAT_D_M_Y;
        }
        return null;
    }

    /**
     * Normalizes date fields in user service response to long timestamps
     * Ensures consistent date handling across the application
     * 
     * @param responseData Raw response from user service
     * @param dobFormat Format for date of birth field
     */
    public void normalizeResponseDateFields(LinkedHashMap responseData, String dobFormat) {
        // Get users list from response
        List<LinkedHashMap> userRecords = (List<LinkedHashMap>)responseData.get(USER);
        String timestampFormat = DOB_FORMAT_D_M_Y_H_M_S;
        
        // Process each user record
        if(userRecords != null) {
            userRecords.forEach(userRecord -> {
                // Convert creation timestamp
                userRecord.put(CREATED_DATE, convertDateToLong((String)userRecord.get(CREATED_DATE), timestampFormat));
                
                // Convert modification timestamp if present
                if((String)userRecord.get(LAST_MODIFIED_DATE) != null)
                    userRecord.put(LAST_MODIFIED_DATE, convertDateToLong((String)userRecord.get(LAST_MODIFIED_DATE), timestampFormat));
                
                // Convert birth date if present
                if((String)userRecord.get(DOB) != null)
                    userRecord.put(DOB, convertDateToLong((String)userRecord.get(DOB), dobFormat));
                
                // Convert password expiry if present
                if((String)userRecord.get(PWD_EXPIRY_DATE) != null)
                    userRecord.put(PWD_EXPIRY_DATE, convertDateToLong((String)userRecord.get(PWD_EXPIRY_DATE), timestampFormat));
            });
        }
    }

    /**
     * Converts date string to long timestamp
     * Handles parsing exceptions with meaningful error messages
     * 
     * @param dateString Date in string format
     * @param formatPattern Format pattern for parsing
     * @return Long timestamp value
     */
    private Long convertDateToLong(String dateString, String formatPattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatPattern);
        Date parsedDate = null;
        try {
            parsedDate = formatter.parse(dateString);
        } catch (ParseException parseError) {
            throw new CustomException(INVALID_DATE_FORMAT_CODE, INVALID_DATE_FORMAT_MESSAGE);
        }
        return parsedDate.getTime();
    }

    /**
     * Adds default fields to newly created user
     * Sets up minimal required user properties
     * 
     * @param phoneNumber User's mobile number
     * @param jurisdictionId Tenant identifier
     * @param userProfile User profile being created
     * @param userCategory Type of user being created
     */
    public void addUserDefaultFields(String phoneNumber, String jurisdictionId, 
                                   User userProfile, UserType userCategory) {
        // Get citizen role for the jurisdiction
        Role defaultRole = createCitizenRole(jurisdictionId);
        
        // Setup basic user properties
        userProfile.setRoles((List<Role>) Collections.singleton(defaultRole));
        userProfile.setType(String.valueOf(userCategory));
        userProfile.setUserName(phoneNumber);
        userProfile.setTenantId(extractStateLevelTenant(jurisdictionId));
    }

    /**
     * Creates citizen role object for the given jurisdiction
     * 
     * @param jurisdictionId Tenant identifier
     * @return Role object for citizen
     */
    private Role createCitizenRole(String jurisdictionId) {
        Role citizenRole = Role.builder().build();
        citizenRole.setCode(CITIZEN_UPPER);
        citizenRole.setName(CITIZEN_LOWER);
        citizenRole.setTenantId(extractStateLevelTenant(jurisdictionId));
        return citizenRole;
    }

    /**
     * Extracts state-level tenant ID from full tenant ID
     * 
     * @param fullTenantId Complete tenant identifier
     * @return State-level tenant identifier
     */
    public String extractStateLevelTenant(String fullTenantId) {
        return fullTenantId.split("\\.")[0];
    }
}