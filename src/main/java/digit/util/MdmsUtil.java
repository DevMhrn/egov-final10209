package digit.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.config.Configuration;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static digit.config.ServiceConstants.*;

@Slf4j
@Component
public class MdmsUtil {

    @Autowired
    private RestTemplate httpClient;

    @Autowired
    private ObjectMapper jsonMapper;

    @Autowired
    private Configuration appSettings;

    /**
     * Retrieves master data from MDMS for given criteria
     * Handles error cases gracefully
     * 
     * @param requestMetadata Client request information
     * @param jurisdictionId Tenant identifier
     * @param moduleCode Module containing required master data
     * @param masterList List of master data sets to retrieve
     * @return Map of module and master data JSONArrays
     */
    public Map<String, Map<String, JSONArray>> fetchMdmsData(
            RequestInfo requestMetadata, 
            String jurisdictionId, 
            String moduleCode,
            List<String> masterList) {
        
        // Build MDMS service endpoint
        StringBuilder serviceEndpoint = new StringBuilder();
        serviceEndpoint.append(appSettings.getMdmsHost())
                      .append(appSettings.getMdmsEndPoint());
        
        // Prepare MDMS request
        MdmsCriteriaReq mdmsRequest = buildMdmsRequest(
            requestMetadata, 
            jurisdictionId, 
            moduleCode, 
            masterList
        );
        
        // Initialize response containers
        Object rawResponse = new HashMap<>();
        MdmsResponse processedResponse = new MdmsResponse();
        
        // Execute MDMS request with error handling
        try {
            rawResponse = httpClient.postForObject(
                serviceEndpoint.toString(), 
                mdmsRequest, 
                Map.class
            );
            
            // Convert response to structured format
            processedResponse = jsonMapper.convertValue(rawResponse, MdmsResponse.class);
        } catch(Exception error) {
            log.error(ERROR_WHILE_FETCHING_FROM_MDMS, error);
        }

        // Return master data from response
        return processedResponse.getMdmsRes();
    }

    /**
     * Builds MDMS request with appropriate structure
     * 
     * @param requestMetadata Client request information
     * @param jurisdictionId Tenant identifier
     * @param moduleCode Module containing required master data
     * @param masterList List of master data sets to retrieve
     * @return Structured MDMS request
     */
    private MdmsCriteriaReq buildMdmsRequest(
            RequestInfo requestMetadata, 
            String jurisdictionId,
            String moduleCode, 
            List<String> masterList) {
        
        // Prepare master details
        List<MasterDetail> masterDetails = new ArrayList<>();
        for(String masterName: masterList) {
            MasterDetail masterDetail = new MasterDetail();
            masterDetail.setName(masterName);
            masterDetails.add(masterDetail);
        }

        // Prepare module details
        ModuleDetail moduleDetail = new ModuleDetail();
        moduleDetail.setMasterDetails(masterDetails);
        moduleDetail.setModuleName(moduleCode);
        List<ModuleDetail> moduleDetails = new ArrayList<>();
        moduleDetails.add(moduleDetail);

        // Prepare MDMS criteria
        MdmsCriteria criteria = new MdmsCriteria();
        criteria.setTenantId(jurisdictionId.split("\\.")[0]);
        criteria.setModuleDetails(moduleDetails);

        // Prepare complete MDMS request
        MdmsCriteriaReq mdmsRequest = new MdmsCriteriaReq();
        mdmsRequest.setMdmsCriteria(criteria);
        mdmsRequest.setRequestInfo(requestMetadata);

        return mdmsRequest;
    }
}