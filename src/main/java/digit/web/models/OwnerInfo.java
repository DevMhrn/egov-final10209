package digit.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import digit.web.models.Document;
import digit.web.models.Role;
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
 * OwnerInfo
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2025-02-17T23:50:09.925204096+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OwnerInfo   {
        @JsonProperty("aadhaarNumber")

        @Pattern(regexp="^[0-9]{12}$")         private String aadhaarNumber = null;

        @JsonProperty("accountLocked")

                private Boolean accountLocked = null;

        @JsonProperty("active")

                private Boolean active = null;

        @JsonProperty("altContactNumber")

        @Size(max=50)         private String altContactNumber = null;

        @JsonProperty("bloodGroup")

        @Size(max=32)         private String bloodGroup = null;

        @JsonProperty("correspondenceAddress")

                private String correspondenceAddress = null;

        @JsonProperty("correspondenceCity")

        @Size(max=300)         private String correspondenceCity = null;

        @JsonProperty("correspondencePinCode")

        @Size(max=10)         private String correspondencePinCode = null;

        @JsonProperty("createdBy")

        @Size(max=64)         private String createdBy = null;

        @JsonProperty("createdDate")

                private Long createdDate = null;

        @JsonProperty("dob")

                private Long dob = null;

        @JsonProperty("documents")
          @Valid
                private List<Document> documents = null;

        @JsonProperty("emailId")

        @Size(max=128)         private String emailId = null;

        @JsonProperty("fatherOrHusbandName")

                private String fatherOrHusbandName = null;

        @JsonProperty("gender")

                private String gender = null;

        @JsonProperty("id")

                private Long id = null;

        @JsonProperty("identificationMark")

        @Size(max=300)         private String identificationMark = null;

        @JsonProperty("institutionId")

                private String institutionId = null;

        @JsonProperty("isPrimaryOwner")

                private Boolean isPrimaryOwner = null;

        @JsonProperty("lastModifiedBy")

        @Size(max=64)         private String lastModifiedBy = null;

        @JsonProperty("lastModifiedDate")

                private Long lastModifiedDate = null;

        @JsonProperty("locale")

        @Size(max=16)         private String locale = null;

        @JsonProperty("mobileNumber")
          @NotNull

                private String mobileNumber = null;

        @JsonProperty("name")
          @NotNull

        @Pattern(regexp="^[a-zA-Z0-9 \\-'`\\.]*$") @Size(max=100)         private String name = null;

        @JsonProperty("ownerInfoUuid")

                private String ownerInfoUuid = null;

        @JsonProperty("ownerShipPercentage")

                private Double ownerShipPercentage = null;

        @JsonProperty("ownerType")
          @NotNull

                private String ownerType = null;

        @JsonProperty("pan")

        @Size(max=10)         private String pan = null;

        @JsonProperty("password")

        @Size(max=64)         private String password = null;

        @JsonProperty("permanentAddress")

        @Size(max=300)         private String permanentAddress = null;

        @JsonProperty("permanentCity")

        @Size(max=300)         private String permanentCity = null;

        @JsonProperty("permanentPinCode")

        @Size(max=10)         private String permanentPinCode = null;

        @JsonProperty("photo")

        @Size(max=36)         private String photo = null;

        @JsonProperty("pwdExpiryDate")

                private Long pwdExpiryDate = null;

        @JsonProperty("relationship")

                private String relationship = null;

        @JsonProperty("roles")
          @Valid
                private List<Role> roles = null;

        @JsonProperty("salutation")

                private String salutation = null;

        @JsonProperty("signature")

        @Size(max=36)         private String signature = null;

            /**
            * Gets or Sets status
            */
            public enum StatusEnum {
                        ACTIVE("ACTIVE"),
                        
                        INACTIVE("INACTIVE"),
                        
                        INWORKFLOW("INWORKFLOW");
            
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

        @Size(max=256)         private String tenantId = null;

        @JsonProperty("type")

        @Size(max=50)         private String type = null;

        @JsonProperty("userName")

        @Size(max=64)         private String userName = null;

        @JsonProperty("uuid")

        @Size(max=64)         private String uuid = null;


        public OwnerInfo addDocumentsItem(Document documentsItem) {
            if (this.documents == null) {
            this.documents = new ArrayList<>();
            }
        this.documents.add(documentsItem);
        return this;
        }

        public OwnerInfo addRolesItem(Role rolesItem) {
            if (this.roles == null) {
            this.roles = new ArrayList<>();
            }
        this.roles.add(rolesItem);
        return this;
        }

}
