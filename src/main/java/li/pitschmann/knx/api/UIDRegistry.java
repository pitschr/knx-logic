/*
 * Copyright (C) 2021 Pitschmann Christoph
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package li.pitschmann.knx.api;

import li.pitschmann.knx.core.annotations.Nullable;
import li.pitschmann.knx.core.utils.Strings;
import li.pitschmann.knx.logic.components.Component;
import li.pitschmann.knx.logic.connector.Connector;
import li.pitschmann.knx.logic.connector.ConnectorAware;
import li.pitschmann.knx.logic.diagram.Diagram;
import li.pitschmann.knx.logic.pin.DynamicPin;
import li.pitschmann.knx.logic.pin.Pin;
import li.pitschmann.knx.logic.pin.PinAware;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * UID Registry (application wide)
 * <p>
 * This registry maps UIDs in string format with an object and
 * is used for a fast look-up for API endpoints. It does not
 * contain any logic / behavior, all those needs to be done
 * outside this class.
 *
 * @author PITSCHR
 */
public final class UIDRegistry {
    private final Map<String, Diagram> diagramMap = new LinkedHashMap<>(); // because of order-guarantee
    private final Map<String, Component> componentMap = new LinkedHashMap<>(); // because of order-guarantee
    private final Map<String, Connector> connectorMap = new HashMap<>();
    private final Map<String, Pin> pinMap = new HashMap<>();

    private static <T> String toStringHelper(final Map<String, T> map, final Function<Map.Entry<String, T>, String> function) {
        return "{\n\t\t" + map.entrySet().stream().map(function).sorted(
                // input is: <uid>=<name>
                // the comparison should be based on name (after "=" char)
                (String o1, String o2) -> {
                    var o1Str = o1.substring(o1.indexOf("="));
                    var o2Str = o2.substring(o2.indexOf("="));
                    return o1Str.compareTo(o2Str);
                }
        ).collect(Collectors.joining(", \n\t\t")) + "\n\t}";
    }

    /**
     * Returns all diagrams that is known to the {@link UIDRegistry}
     *
     * @return immutable list of {@link Diagram}
     */
    public List<Diagram> getDiagrams() {
        return List.copyOf(diagramMap.values());
    }

    /**
     * Returns all components that is known to the {@link UIDRegistry}
     *
     * @return immutable list of {@link Component}
     */
    public List<Component> getComponents() {
        return List.copyOf(componentMap.values());
    }

    /**
     * Returns the {@link Diagram} by given {@code uid}
     *
     * @param uid the UID as string representation; may not be null
     * @return a {@link Diagram} if found, otherwise {@code null}
     */
    @Nullable
    public Diagram getDiagram(final String uid) {
        return diagramMap.get(Objects.requireNonNull(uid));
    }

    /**
     * Returns the {@link Component} by given {@code uid}
     *
     * @param uid the UID as string representation; may not be null
     * @return a {@link Component} if found, otherwise {@code null}
     */
    @Nullable
    public Component getComponent(final String uid) {
        return componentMap.get(Objects.requireNonNull(uid));
    }

    /**
     * Returns the {@link Connector} by given {@code uid}
     *
     * @param uid the UID as string representation; may not be null
     * @return a {@link Connector} if found, otherwise {@code null}
     */
    @Nullable
    public Connector getConnector(final String uid) {
        return connectorMap.get(Objects.requireNonNull(uid));
    }

    /**
     * Returns the {@link Pin} by given {@code uid}
     *
     * @param uid the UID as string representation; may not be null
     * @return a {@link Pin} if found, otherwise {@code null}
     */
    @Nullable
    public Pin getPin(final String uid) {
        return pinMap.get(Objects.requireNonNull(uid));
    }

    /**
     * Registers the given {@link Diagram} to {@link UIDRegistry}.
     *
     * @param diagram the diagram to be registered; may not be null
     */
    public void register(final Diagram diagram) {
        diagramMap.put(diagram.getUid().toString(), diagram);
    }

    /**
     * Registers the given {@link Component} to {@link UIDRegistry}.
     * <p>
     * It will also automatically register all related {@link Connector}s
     * and {@link Pin}s.
     *
     * @param component the component to be registered; may not be null
     */
    public void register(final Component component) {
        componentMap.put(component.getUid().toString(), component);

        if (component instanceof ConnectorAware) {
            ((ConnectorAware) component).getConnectors().forEach(connector -> connectorMap.put(connector.getUid().toString(), connector));
        }
        if (component instanceof PinAware) {
            ((PinAware) component).getPins().forEach(this::registerPinInternal);
        }
    }

    /**
     * Registers a given {@link DynamicPin} to {@link UIDRegistry}.
     * <p>
     * This method should be called only when you added a dynamic pin from
     * a component.
     *
     * @param pin the dynamic pin to be registered; may not be null
     */
    public void register(final DynamicPin pin) {
        registerPinInternal(pin);
    }

    /**
     * For Internal-Use only. Register any type of Pin
     *
     * @param pin the pin to be registered; may not be null
     */
    private void registerPinInternal(final Pin pin) {
        pinMap.put(pin.getUid().toString(), pin);
    }

    /**
     * De-Registers the given {@link Diagram} from {@link UIDRegistry}
     *
     * @param diagram the diagram to be de-registered; may not be null
     */
    public void deregister(final Diagram diagram) {
        diagramMap.remove(diagram.getUid().toString());
    }

    /**
     * De-Registers the given {@link Component} from {@link UIDRegistry}.
     * <p>
     * It will also automatically de-register all related {@link Connector}s
     * and {@link Pin}s.
     *
     * @param component the component to be de-registered; may not be null
     */
    public void deregister(final Component component) {
        componentMap.remove(component.getUid().toString());

        if (component instanceof ConnectorAware) {
            ((ConnectorAware) component).getConnectors().forEach(connector -> connectorMap.remove(connector.getUid().toString()));
        }
        if (component instanceof PinAware) {
            ((PinAware) component).getPins().forEach(this::deregisterPinInternal);
        }
    }

    /**
     * De-Registers a given {@link DynamicPin} from {@link UIDRegistry}.
     * <p>
     * This method should be called only when you removed a dynamic pin from
     * a component.
     *
     * @param pin the dynamic pin to be de-registered; may not be null
     */
    public void deregister(final DynamicPin pin) {
        deregisterPinInternal(pin);
    }

    /**
     * For Internal-Use only. De-Register any type of Pin
     *
     * @param pin the pin to be de-registered; may not be null
     */
    private void deregisterPinInternal(final Pin pin) {
        pinMap.remove(pin.getUid().toString());
    }

    @Override
    public String toString() {
        final var diagramMapNames = toStringHelper(diagramMap, e -> e.getKey() + "=" + e.getValue().getName());
        final var componentMapNames = toStringHelper(componentMap, e -> e.getKey() + "=" + e.getValue().getName());
        final var connectorMapNames = toStringHelper(connectorMap, e -> e.getKey() + "=" + e.getValue().getName());
        final var pinMapNames = toStringHelper(pinMap, e -> e.getKey() + "=" + e.getValue().getName());

        return Strings.toStringHelper(this)
                .add("\n\tdiagramMap", diagramMapNames)
                .add("\n\tcomponentMap", componentMapNames)
                .add("\n\tconnectorMap", connectorMapNames)
                .add("\n\tpinMap", pinMapNames + '\n')
                .toString();
    }
}
