package no.cx.iot.philipshueapi.hueController.rest.hueAPI;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class HueURL {

    @Inject
    @ConfigProperty(name="facadehost")
    private String host;

    @Inject
    @ConfigProperty(name="port", defaultValue = "8083")
    private String port;

    String getFullURL() {
        return "http://" + host + ":" + port +"/hue/";
    }
}
