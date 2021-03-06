package no.iot.weatherservice.utils.general;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

@ApplicationScoped
public class HttpConnector {

    private Logger logger = Logger.getLogger(getClass().getSimpleName());

    @Inject
    @ConfigProperty(name = "charset", defaultValue = "UTF-8")
    private String encoding;

    @Retry
    @Fallback(fallbackMethod = "fallback")
    public String executeHTTPGet(String url) throws IOException {
        logger.info(String.format("Invoking %s", url));
        HttpUriRequest request = new HttpGet(url);
        CloseableHttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        InputStream content = httpResponse.getEntity().getContent();
        String responseText = IOUtils.toString(content, encoding);
        httpResponse.close();
        return responseText;
    }


    private String fallback(String url) {
        return "ERROR: Could not connect to: " + url;
    }
}
