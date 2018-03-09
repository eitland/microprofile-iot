package no.cx.iot.philipshueapi.hueController.rest;

import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import no.cx.iot.philipshueapi.hueController.rest.hueAPI.PhilipsHueConnector;
import no.cx.iot.philipshueapi.hueController.rest.lights.LightState;

import static no.cx.iot.philipshueapi.hueController.rest.infrastructure.ExceptionWrapper.wrapExceptions;

@ApplicationScoped
@SuppressWarnings("unused")
public class LightStateSwitcher {

    @Inject
    private PhilipsHueConnector connector;

    @Inject
    private ProposedLightStatesFinder proposedLightStatesFinder;

    @Inject
    private Logger logger;

    public void registerInputProvider(InputProvider inputProvider) {
        proposedLightStatesFinder.addInputProvider(inputProvider);
    }

    public String switchStateOfLights() {
        return IntStream.range(0, getAllLights())
                .mapToObj(this::switchStateOfLight)
                .peek(light -> logger.info("State of this light: " + light))
                .collect(Collectors.joining("\n"));
    }

    private int getAllLights() {
        return wrapExceptions(connector::getAllLights);
    }

    private String switchStateOfLight(int lightIndex) {
        return wrapExceptions(() -> connector.switchStateOfLight(lightIndex, getNewStateForLight(lightIndex)));
    }

    private LightState getNewStateForLight(int lightIndex) {
        return proposedLightStatesFinder.getNewStateForLight(lightIndex);
    }

}