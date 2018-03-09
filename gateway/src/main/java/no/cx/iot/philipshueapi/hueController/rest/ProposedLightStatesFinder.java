package no.cx.iot.philipshueapi.hueController.rest;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import no.cx.iot.philipshueapi.hueController.rest.lights.LightState;
import no.cx.iot.philipshueapi.hueController.rest.lights.LightStateComputer;

@ApplicationScoped
public class ProposedLightStatesFinder {

    @SuppressWarnings("unused")
    @Inject
    private LightStateComputer lightStateComputer;

    @SuppressWarnings("unused")
    @Inject
    private Logger logger;

    private Set<InputProvider> inputProviders;

    public ProposedLightStatesFinder() {
        inputProviders = new HashSet<>();
    }

    public void addInputProvider(InputProvider inputProvider) {
        inputProviders.add(inputProvider);
    }

    LightState getNewStateForLight(int lightIndex) {
        Set<LightState> proposedLightStates = getProposedLightStates(lightIndex);
        return lightStateComputer.getNewStateForLight(proposedLightStates);
    }

    private Set<LightState> getProposedLightStates(int lightIndex) {
        return inputProviders.stream()
                .peek(inputProvider -> logger.info(inputProvider.toString()))
                .map(inputProvider -> inputProvider.getNewStateForLight(lightIndex))
                .peek(state -> logger.info("Inputprovider suggested " + state + " for light " + lightIndex))
                .collect(Collectors.toSet());
    }
}