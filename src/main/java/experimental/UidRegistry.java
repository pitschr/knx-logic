package experimental;

import li.pitschmann.knx.logic.components.Component;
import li.pitschmann.knx.logic.connector.Connector;
import li.pitschmann.knx.logic.connector.ConnectorAware;
import li.pitschmann.knx.logic.pin.Pin;
import li.pitschmann.knx.logic.pin.PinAware;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class UidRegistry {
    private static final Map<String, Plan> planMap = new HashMap<>();
    private static final Map<String, Component> componentMap = new HashMap<>();
    private static final Map<String, Connector> connectorMap = new HashMap<>();
    private static final Map<String, Pin> pinMap = new HashMap<>();

    private UidRegistry() {
    }

    public static Plan findPlanByUID(final String uidAsString) {
        return planMap.get(uidAsString);
    }

    public static List<Plan> getAllPlans() {
        return List.copyOf(planMap.values());
    }

    public static List<Component> getAllComponents() {
        return List.copyOf(componentMap.values());
    }

    public static Component findComponentByUID(final String uidAsString) {
        return componentMap.get(uidAsString);
    }

    public static Connector findConnectorByUID(final String uidAsString) {
        return connectorMap.get(uidAsString);
    }

    public static Pin findPinByUID(final String uidAsString) {
        return pinMap.get(uidAsString);
    }

    public static void register(Plan plan) {
        planMap.put(plan.getName(), plan);
    }

    public static void register(Component component) {
        componentMap.put(component.getUid().toString(), component);

        if (component instanceof ConnectorAware) {
            ((ConnectorAware)component).getConnectors().forEach(connector -> connectorMap.put(connector.getUid().toString(), connector));
        }
        if (component instanceof PinAware) {
            ((PinAware)component).getPins().forEach(UidRegistry::register);
        }
    }

    public static void deregister(final Component component) {
        componentMap.remove(component.getUid().toString());

        if (component instanceof ConnectorAware) {
            ((ConnectorAware)component).getConnectors().forEach(connector -> connectorMap.remove(connector.getUid().toString()));
        }
        if (component instanceof PinAware) {
            ((PinAware)component).getPins().forEach(UidRegistry::deregister);
        }
    }

    public static void register(final Pin pin) {
        pinMap.put(pin.getUid().toString(), pin);
    }

    public static void deregister(final Pin pin) {
        pinMap.remove(pin.getUid().toString());
    }
}
