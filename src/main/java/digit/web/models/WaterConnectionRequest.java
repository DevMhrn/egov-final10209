package digit.web.models;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
//import digit.web.models.RequestInfo;
import org.egov.common.contract.request.RequestInfo;
import digit.web.models.WaterConnection;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * Contract class to receive request. Array of Property items  are used in case of create . Where as single Property item is used for update
 */
@Schema(description = "Contract class to receive request. Array of Property items  are used in case of create . Where as single Property item is used for update")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2025-02-17T23:50:09.925204096+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WaterConnectionRequest   {
        @JsonProperty("RequestInfo")

          @Valid
                private RequestInfo requestInfo = null;

        @JsonProperty("WaterConnection")

          @Valid
                private WaterConnection waterConnection = null;

        @JsonProperty("disconnectRequest")

                private Boolean disconnectRequest = null;

        @JsonProperty("isCreateCall")

                private Boolean isCreateCall = null;

        @JsonProperty("isOldDataEncryptionRequest")

                private Boolean isOldDataEncryptionRequest = null;


    public List<WaterConnection> getConnections() {
        return waterConnection != null ? Collections.singletonList(waterConnection) : Collections.emptyList();
    }
}
