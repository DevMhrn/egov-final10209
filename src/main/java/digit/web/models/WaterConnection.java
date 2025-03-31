package digit.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import digit.web.models.AuditDetails;
import digit.web.models.Document;
import digit.web.models.OwnerInfo;
import digit.web.models.PlumberInfo;
import digit.web.models.ProcessInstance;
import digit.web.models.RoadCuttingInfo;
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
 * This is lightweight property object that can be used as reference by definitions needing property linking. Actual Property Object extends this to include more elaborate attributes of the property.
 */
@Schema(description = "This is lightweight property object that can be used as reference by definitions needing property linking. Actual Property Object extends this to include more elaborate attributes of the property.")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2025-02-17T23:50:09.925204096+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WaterConnection   {
        @JsonProperty("additionalDetails")

                private Object additionalDetails = null;

        @JsonProperty("applicationNo")

        @Size(min=1,max=64)         private String applicationNo = null;

        @JsonProperty("applicationStatus")

                private String applicationStatus = null;

        @JsonProperty("applicationType")

                private String applicationType = null;

        @JsonProperty("auditDetails")

          @Valid
                private AuditDetails auditDetails = null;

        @JsonProperty("channel")

                private String channel = null;

        @JsonProperty("connectionCategory")
          @NotNull

        @Size(min=2,max=32)         private String connectionCategory = null;

        @JsonProperty("connectionExecutionDate")

                private Long connectionExecutionDate = null;

        @JsonProperty("connectionHolders")
          @Valid
                private List<OwnerInfo> connectionHolders = null;

        @JsonProperty("connectionNo")

        @Size(min=1,max=64)         private String connectionNo = null;

        @JsonProperty("connectionType")
          @NotNull

        @Size(min=2,max=32)         private String connectionType = null;

        @JsonProperty("dateEffectiveFrom")

                private Long dateEffectiveFrom = null;

        @JsonProperty("disconnectionExecutionDate")

                private Long disconnectionExecutionDate = null;

        @JsonProperty("disconnectionReason")

                private String disconnectionReason = null;

        @JsonProperty("documents")
          @Valid
                private List<Document> documents = null;

        @JsonProperty("id")

        @Size(min=1,max=64)         private String id = null;

        @JsonProperty("isDisconnectionTemporary")

                private Boolean isDisconnectionTemporary = null;

        @JsonProperty("meterId")

                private String meterId = null;

        @JsonProperty("meterInstallationDate")

                private Long meterInstallationDate = null;

        @JsonProperty("noOfTaps")

                private Integer noOfTaps = null;

        @JsonProperty("oldApplication")

                private Boolean oldApplication = null;

        @JsonProperty("oldConnectionNo")

        @Size(min=1,max=64)         private String oldConnectionNo = null;

        @JsonProperty("pipeSize")

                private Double pipeSize = null;

        @JsonProperty("plumberInfo")
          @Valid
                private List<PlumberInfo> plumberInfo = null;

        @JsonProperty("processInstance")

          @Valid
                private ProcessInstance processInstance = null;

        @JsonProperty("propertyId")

                private String propertyId = null;

        @JsonProperty("proposedPipeSize")

                private Double proposedPipeSize = null;

        @JsonProperty("proposedTaps")

                private Integer proposedTaps = null;

        @JsonProperty("roadCuttingArea")

                private Float roadCuttingArea = null;

        @JsonProperty("roadCuttingInfo")
          @Valid
                private List<RoadCuttingInfo> roadCuttingInfo = null;

        @JsonProperty("roadType")

        @Size(min=2,max=32)         private String roadType = null;

            /**
            * Gets or Sets status
            */
            public enum StatusEnum {
                        ACTIVE("Active"),

                        IN_PROGRESS("In progress"),
                        
                        INACTIVE("Inactive");
            
            private String value;
            
            StatusEnum(String value) {
            this.value = value;
            }
            
            @Override
            @JsonValue
            public String toString() {
            return String.valueOf(value);
            }
            
            @JsonCreator
            public static StatusEnum fromValue(String text) {
            for (StatusEnum b : StatusEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
            return b;
            }
            }
            return null;
            }
            }        @JsonProperty("status")

                private StatusEnum status = null;

        @JsonProperty("tenantId")

        @Size(min=2,max=256)         private String tenantId = null;

        @JsonProperty("waterSource")

                private String waterSource = null;


        public WaterConnection addConnectionHoldersItem(OwnerInfo connectionHoldersItem) {
            if (this.connectionHolders == null) {
            this.connectionHolders = new ArrayList<>();
            }
        this.connectionHolders.add(connectionHoldersItem);
        return this;
        }

        public WaterConnection addDocumentsItem(Document documentsItem) {
            if (this.documents == null) {
            this.documents = new ArrayList<>();
            }
        this.documents.add(documentsItem);
        return this;
        }

        public WaterConnection addPlumberInfoItem(PlumberInfo plumberInfoItem) {
            if (this.plumberInfo == null) {
            this.plumberInfo = new ArrayList<>();
            }
        this.plumberInfo.add(plumberInfoItem);
        return this;
        }

        public WaterConnection addRoadCuttingInfoItem(RoadCuttingInfo roadCuttingInfoItem) {
            if (this.roadCuttingInfo == null) {
            this.roadCuttingInfo = new ArrayList<>();
            }
        this.roadCuttingInfo.add(roadCuttingInfoItem);
        return this;
        }

}
