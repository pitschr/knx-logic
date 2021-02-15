package experimental;

import li.pitschmann.knx.logic.components.Component;
import li.pitschmann.knx.logic.pin.Pin;
import li.pitschmann.knx.logic.pin.PinAware;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UidRegistry {

    private final Map<String, Component> componentMap = new HashMap<>();
    private final Map<String, Pin> pinMap = new HashMap<>();

    public UidRegistry() {

    }

    public Pin findPinByUID(final String uidAsString) {
        return pinMap.get(uidAsString);
    }

    public Plan findPlanByUID(final String uidAsString) {
        return null;
    }

    public List<Component> getAllComponents() {
        return List.copyOf(componentMap.values());
    }

    public Component findComponentByUID(final String uidAsString) {
        return componentMap.get(uidAsString);
    }

    public void registerComponent(Component component) {
        componentMap.put(component.getUid().toString(), component);

        if (component instanceof PinAware) {
            ((PinAware)component).getPins().forEach(this::registerPin);
        }
    }

    public void registerPin(final Pin pin) {
        pinMap.put(pin.getUid().toString(), pin);
    }

    public void deregisterComponent(final Component component) {
        componentMap.remove(component.getUid().toString());

        if (component instanceof PinAware) {
            ((PinAware)component).getPins().forEach(this::deregisterPin);
        }
    }

    public void deregisterPin(final Pin pin) {
        pinMap.remove(pin.getUid().toString());
    }
}
