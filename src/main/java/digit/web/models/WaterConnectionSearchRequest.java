package digit.web.models;

import digit.repository.querybuilder.WaterConnectionSearchCriteria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.request.RequestInfo;

import javax.validation.Valid;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WaterConnectionSearchRequest {

    @Valid
    private RequestInfo requestInfo;

    @Valid
    private WaterConnectionSearchCriteria waterConnectionSearchCriteria;
}
