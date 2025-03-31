package digit.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import digit.web.models.AuditDetails;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * PlumberInfo
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2025-02-17T23:50:09.925204096+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlumberInfo   {
        @JsonProperty("additionalDetails")

                private Object additionalDetails = null;

        @JsonProperty("auditDetails")

          @Valid
                private AuditDetails auditDetails = null;

        @JsonProperty("correspondenceAddress")

        @Size(max=1024)         private String correspondenceAddress = null;

        @JsonProperty("fatherOrHusbandName")

        @Size(max=256)         private String fatherOrHusbandName = null;

        @JsonProperty("gender")

        @Size(max=256)         private String gender = null;

        @JsonProperty("id")

        @Size(max=256)         private String id = null;

        @JsonProperty("licenseNo")

        @Size(max=256)         private String licenseNo = null;

        @JsonProperty("mobileNumber")

        @Size(max=256)         private String mobileNumber = null;

        @JsonProperty("name")

        @Size(max=256)         private String name = null;

        @JsonProperty("relationship")

                private String relationship = null;


}
