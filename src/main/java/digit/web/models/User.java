package digit.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * User
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2025-02-17T23:50:09.925204096+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User   {
        @JsonProperty("emailId")

        @Size(max=300)         private String emailId = null;

        @JsonProperty("id")

                private Long id = null;

        @JsonProperty("mobileNumber")

        @Size(max=150)         private String mobileNumber = null;

        @JsonProperty("name")

        @Size(max=250)         private String name = null;

        @JsonProperty("roles")
          @Valid
                private List<Role> roles = null;

        @JsonProperty("tenantId")

        @Size(max=256)         private String tenantId = null;

        @JsonProperty("type")

        @Size(max=50)         private String type = null;

        @JsonProperty("userName")

        @Size(max=180)         private String userName = null;

        @JsonProperty("uuid")

        @Size(max=36)         private String uuid = null;


        public User addRolesItem(Role rolesItem) {
            if (this.roles == null) {
            this.roles = new ArrayList<>();
            }
        this.roles.add(rolesItem);
        return this;
        }

}
