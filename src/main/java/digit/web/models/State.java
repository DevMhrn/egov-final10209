package digit.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import digit.web.models.Action;
import digit.web.models.AuditDetails;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * A Object holds the basic data for a Trade License
 */
@Schema(description = "A Object holds the basic data for a Trade License")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2025-02-17T23:50:09.925204096+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class State   {
        @JsonProperty("actions")
          @Valid
                private List<Action> actions = null;

        @JsonProperty("applicationStatus")

        @Size(max=256)         private String applicationStatus = null;

        @JsonProperty("auditDetails")

          @Valid
                private AuditDetails auditDetails = null;

        @JsonProperty("businessServiceId")

        @Size(max=256)         private String businessServiceId = null;

        @JsonProperty("docUploadRequired")

                private Boolean docUploadRequired = null;

        @JsonProperty("isStartState")

                private Boolean isStartState = null;

        @JsonProperty("isStateUpdatable")

                private Boolean isStateUpdatable = null;

        @JsonProperty("isTerminateState")

                private Boolean isTerminateState = null;

        @JsonProperty("sla")

                private Long sla = null;

        @JsonProperty("state")

        @Size(max=256)         private String state = null;

        @JsonProperty("tenantId")

        @Size(max=256)         private String tenantId = null;

        @JsonProperty("uuid")

        @Size(max=256)         private String uuid = null;


        public State addActionsItem(Action actionsItem) {
            if (this.actions == null) {
            this.actions = new ArrayList<>();
            }
        this.actions.add(actionsItem);
        return this;
        }

}
