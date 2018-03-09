package no.cx.iot.philipshueapi.hueController.rest.weatherConnector;

import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import lombok.Getter;
import no.cx.iot.philipshueapi.hueController.rest.InputProvider;
import no.cx.iot.philipshueapi.hueController.rest.hueAPI.HttpConnector;

import static no.cx.iot.philipshueapi.hueController.rest.infrastructure.ExceptionWrapper.wrapExceptions;

@Getter
public class WeatherRestConnector implements InputProvider<Weather> {

    @Inject
    @ConfigProperty(name = "weatherHost", defaultValue = "localhost")
    private String host;

    @Inject
    @ConfigProperty(name = "weatherPort", defaultValue = "8083")
    private String port;

    @Inject
    @ConfigProperty(name = "weatherPath", defaultValue = "weatherservice")
    private String path;

    @Inject
    private HttpConnector connector;

    @Inject
    private WeatherToLightStateConverter converter;

    @Override
    public Weather getDataForLight(int lightIndex) {
        return getWeather();
    }

    @Override
    public String canConnect() {
        try {
            return "OK, the current weather is " + getWeather();
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    private Weather getWeather() {
        return new Weather(wrapExceptions(() -> connector.executeHTTPGet(getFullURL(), String.class)));
        // TODO JSON-ify the connection, so we kan skip this manual wrapping
    }

}