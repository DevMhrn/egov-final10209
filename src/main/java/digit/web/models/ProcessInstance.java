package digit.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import digit.web.models.Document;
import digit.web.models.State;
import digit.web.models.User;
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
public class ProcessInstance   {
        @JsonProperty("action")
          @NotNull

        @Size(max=128)         private String action = null;

        @JsonProperty("assignes")
          @Valid
                private List<User> assignes = null;

        @JsonProperty("businessId")
          @NotNull

        @Size(max=128)         private String businessId = null;

        @JsonProperty("businessService")
          @NotNull

        @Size(max=128)         private String businessService = null;

        @JsonProperty("comment")

                private String comment = null;

        @JsonProperty("documents")
          @Valid
                private List<Document> documents = null;

        @JsonProperty("id")

        @Size(max=64)         private String id = null;

        @JsonProperty("moduleName")
          @NotNull

        @Size(max=64)         private String moduleName = null;

        @JsonProperty("state")

          @Valid
                private State state = null;

        @JsonProperty("tenantId")
          @NotNull

        @Size(max=128)         private String tenantId = null;


        public ProcessInstance addAssignesItem(User assignesItem) {
            if (this.assignes == null) {
            this.assignes = new ArrayList<>();
            }
        this.assignes.add(assignesItem);
        return this;
        }

        public ProcessInstance addDocumentsItem(Document documentsItem) {
            if (this.documents == null) {
            this.documents = new ArrayList<>();
            }
        this.documents.add(documentsItem);
        return this;
        }

}
