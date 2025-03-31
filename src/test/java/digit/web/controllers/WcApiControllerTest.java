package digit.web.controllers;

import digit.web.models.RequestInfoWrapper;
import digit.web.models.WaterConnectionRequest;
import digit.web.models.WaterConnectionResponse;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import digit.TestConfiguration;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
* API tests for WcApiController
*/
@Ignore
@RunWith(SpringRunner.class)
@WebMvcTest(WcApiController.class)
@Import(TestConfiguration.class)
public class WcApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createWaterConnectionUsingPOSTSuccess() throws Exception {
        mockMvc.perform(post("/wc/_create").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void createWaterConnectionUsingPOSTFailure() throws Exception {
        mockMvc.perform(post("/wc/_create").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void encryptOldDataUsingPOSTSuccess() throws Exception {
        mockMvc.perform(post("/wc/_encryptOldData").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void encryptOldDataUsingPOSTFailure() throws Exception {
        mockMvc.perform(post("/wc/_encryptOldData").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void plainSearchUsingPOSTSuccess() throws Exception {
        mockMvc.perform(post("/wc/_plainsearch").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void plainSearchUsingPOSTFailure() throws Exception {
        mockMvc.perform(post("/wc/_plainsearch").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void searchUsingPOSTSuccess() throws Exception {
        mockMvc.perform(post("/wc/_search").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void searchUsingPOSTFailure() throws Exception {
        mockMvc.perform(post("/wc/_search").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void updateWaterConnectionUsingPOSTSuccess() throws Exception {
        mockMvc.perform(post("/wc/_update").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void updateWaterConnectionUsingPOSTFailure() throws Exception {
        mockMvc.perform(post("/wc/_update").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

}
