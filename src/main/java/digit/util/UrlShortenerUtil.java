package digit.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import digit.config.Configuration;
import static digit.config.ServiceConstants.*;

@Slf4j
@Component
public class UrlShortenerUtil {

    @Autowired
    private RestTemplate httpClient;

    @Autowired
    private Configuration appConfig;

    /**
     * Creates a shortened URL from a long URL
     * Falls back to original URL if shortening fails
     * 
     * @param originalUrl Full URL to be shortened
     * @return Shortened URL or original if shortening failed
     */
    public String getShortenedUrl(String originalUrl) {
        // Prepare shortening request
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put(URL, originalUrl);
        
        // Build shortener service endpoint
        StringBuilder serviceEndpoint = new StringBuilder(appConfig.getUrlShortnerHost());
        serviceEndpoint.append(appConfig.getUrlShortnerEndpoint());
        
        // Execute shortening request
        String shortenedUrl = httpClient.postForObject(
            serviceEndpoint.toString(), 
            requestBody, 
            String.class
        );

        // Validate response and handle errors
        if(StringUtils.isEmpty(shortenedUrl)) {
            // Log error and return original URL
            log.error(URL_SHORTENING_ERROR_CODE, URL_SHORTENING_ERROR_MESSAGE + originalUrl);
            return originalUrl;
        } else {
            // Return shortened URL
            return shortenedUrl;
        }
    }
}