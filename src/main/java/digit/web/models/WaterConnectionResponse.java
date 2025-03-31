package digit.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import digit.web.models.ResponseInfo;
import digit.web.models.WaterConnection;
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
 * Contains the ResponseHeader and the created/updated property
 */
@Schema(description = "Contains the ResponseHeader and the created/updated property")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2025-02-17T23:50:09.925204096+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WaterConnectionResponse   {
        @JsonProperty("ResponseInfo")

          @Valid
                private ResponseInfo responseInfo = null;

        @JsonProperty("TotalCount")

                private Integer totalCount = null;

        @JsonProperty("WaterConnection")
          @Valid
                private List<WaterConnection> waterConnection = null;


        public WaterConnectionResponse addWaterConnectionItem(WaterConnection waterConnectionItem) {
            if (this.waterConnection == null) {
            this.waterConnection = new ArrayList<>();
            }
        this.waterConnection.add(waterConnectionItem);
        return this;
        }

}
