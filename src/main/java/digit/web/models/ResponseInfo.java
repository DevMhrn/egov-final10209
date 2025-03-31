package digit.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * ResponseInfo
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2025-02-17T23:50:09.925204096+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseInfo   {
        @JsonProperty("apiId")

                private String apiId = null;

        @JsonProperty("msgId")

                private String msgId = null;

        @JsonProperty("resMsgId")

                private String resMsgId = null;

        @JsonProperty("status")

                private String status = null;

        @JsonProperty("ts")

                private Long ts = null;

        @JsonProperty("ver")

                private String ver = null;


}
