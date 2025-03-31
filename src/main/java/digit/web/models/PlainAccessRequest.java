package digit.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * PlainAccessRequest
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2025-02-17T23:50:09.925204096+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlainAccessRequest   {
        @JsonProperty("plainRequestFields")

                private List<String> plainRequestFields = null;

        @JsonProperty("recordId")

                private String recordId = null;


        public PlainAccessRequest addPlainRequestFieldsItem(String plainRequestFieldsItem) {
            if (this.plainRequestFields == null) {
            this.plainRequestFields = new ArrayList<>();
            }
        this.plainRequestFields.add(plainRequestFieldsItem);
        return this;
        }

}
