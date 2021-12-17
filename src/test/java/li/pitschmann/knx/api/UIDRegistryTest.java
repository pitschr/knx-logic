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

import li.pitschmann.knx.logic.components.Component;
import li.pitschmann.knx.logic.components.LogicComponent;
import li.pitschmann.knx.logic.connector.DynamicConnector;
import li.pitschmann.knx.logic.connector.StaticConnector;
import li.pitschmann.knx.logic.diagram.Diagram;
import li.pitschmann.knx.logic.pin.DynamicPin;
import li.pitschmann.knx.logic.pin.StaticPin;
import li.pitschmann.knx.logic.uid.UIDFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UIDRegistryTest {

    @Test
    @DisplayName("Test UID registry to fetch all diagrams")
    void testAllDiagram() {
        assertThat(UIDRegistry.getDiagrams()).isEmpty();

        // register
        final var diagramMockA = mock(Diagram.class);
        when(diagramMockA.getUid()).thenReturn(UIDFactory.createUid("all-diagram-a"));
        final var diagramMockB = mock(Diagram.class);
        when(diagramMockB.getUid()).thenReturn(UIDFactory.createUid("all-diagram-b"));
        final var diagramMockC = mock(Diagram.class);
        when(diagramMockC.getUid()).thenReturn(UIDFactory.createUid("all-diagram-c"));

        UIDRegistry.register(diagramMockA);
        UIDRegistry.register(diagramMockB);
        UIDRegistry.register(diagramMockC);

        // get all
        assertThat(UIDRegistry.getDiagrams().stream().map(Diagram::getUid).map(Object::toString))
                .containsExactlyInAnyOrder("all-diagram-a", "all-diagram-b", "all-diagram-c");
    }

    @Test
    @DisplayName("Test UID registry to fetch all components")
    void testAllComponents() {
        assertThat(UIDRegistry.getComponents()).isEmpty();

        // register
        final var componentMockA = mock(Component.class);
        when(componentMockA.getUid()).thenReturn(UIDFactory.createUid("all-component-a"));
        final var componentMockB = mock(Component.class);
        when(componentMockB.getUid()).thenReturn(UIDFactory.createUid("all-component-b"));
        final var componentMockC = mock(Component.class);
        when(componentMockC.getUid()).thenReturn(UIDFactory.createUid("all-component-c"));

        UIDRegistry.register(componentMockA);
        UIDRegistry.register(componentMockB);
        UIDRegistry.register(componentMockC);

        // get all
        assertThat(UIDRegistry.getComponents().stream().map(Component::getUid).map(Object::toString))
                .containsExactlyInAnyOrder("all-component-a", "all-component-b", "all-component-c");
    }

    @Test
    @DisplayName("Test UID registry for diagram")
    void testDiagram() {
        // check #1 (non existent)
        assertThat(UIDRegistry.getDiagram("my-diagram")).isNull();

        // register
        final var diagramMock = mock(Diagram.class);
        final var diagramUID = UIDFactory.createUid("my-diagram");
        when(diagramMock.getUid()).thenReturn(diagramUID);
        UIDRegistry.register(diagramMock);

        // get
        assertThat(UIDRegistry.getDiagram("my-diagram")).isSameAs(diagramMock);

        // deregister
        UIDRegistry.deregister(diagramMock);

        // check #2 (non existent; again)
        assertThat(UIDRegistry.getDiagram("my-diagram")).isNull();
    }

    @Test
    @DisplayName("Test UID registry for component without connectors and pins")
    void testComponent() {
        // check #1 (non existent)
        assertThat(UIDRegistry.getComponent("my-component")).isNull();

        // register
        final var componentMock = mock(Component.class);
        final var componentUID = UIDFactory.createUid("my-component");
        when(componentMock.getUid()).thenReturn(componentUID);
        UIDRegistry.register(componentMock);

        // get
        assertThat(UIDRegistry.getComponent("my-component")).isSameAs(componentMock);

        // deregister
        UIDRegistry.deregister(componentMock);

        // check #2 (non existent; again)
        assertThat(UIDRegistry.getComponent("my-component")).isNull();
    }

    @Test
    @DisplayName("Test UID registry for logic component with connectors and pins")
    void testComponentWithConnector() {
        // check #1 (non existent)
        assertThat(UIDRegistry.getComponent("my-logic")).isNull();
        assertThat(UIDRegistry.getConnector("my-connector-a")).isNull();
        assertThat(UIDRegistry.getConnector("my-connector-b")).isNull();
        assertThat(UIDRegistry.getPin("my-pin-a")).isNull();
        assertThat(UIDRegistry.getPin("my-pin-b-1")).isNull();
        assertThat(UIDRegistry.getPin("my-pin-b-2")).isNull();
        assertThat(UIDRegistry.getPin("my-pin-b-3")).isNull();

        // register
        final var connector_a = mock(StaticConnector.class);
        when(connector_a.getUid()).thenReturn(UIDFactory.createUid("my-connector-a"));
        final var connector_b = mock(DynamicConnector.class);
        when(connector_b.getUid()).thenReturn(UIDFactory.createUid("my-connector-b"));

        final var pin_a = mock(StaticPin.class);
        when(pin_a.getUid()).thenReturn(UIDFactory.createUid("my-pin-a"));
        final var pin_b_1 = mock(DynamicPin.class);
        when(pin_b_1.getUid()).thenReturn(UIDFactory.createUid("my-pin-b-1"));
        final var pin_b_2 = mock(DynamicPin.class);
        when(pin_b_2.getUid()).thenReturn(UIDFactory.createUid("my-pin-b-2"));
        final var pin_b_3 = mock(DynamicPin.class);
        when(pin_b_3.getUid()).thenReturn(UIDFactory.createUid("my-pin-b-3"));

        final var logicMock = mock(LogicComponent.class);
        final var logicUID = UIDFactory.createUid("my-logic");
        when(logicMock.getUid()).thenReturn(logicUID);
        when(logicMock.getConnectors()).thenReturn(List.of(connector_a, connector_b));
        when(logicMock.getPins()).thenReturn(List.of(pin_a, pin_b_1, pin_b_2, pin_b_3));
        UIDRegistry.register(logicMock); // should register component, connectors and pins

        // get
        assertThat(UIDRegistry.getComponent("my-logic")).isSameAs(logicMock);
        assertThat(UIDRegistry.getConnector("my-connector-a")).isSameAs(connector_a);
        assertThat(UIDRegistry.getConnector("my-connector-b")).isSameAs(connector_b);
        assertThat(UIDRegistry.getPin("my-pin-a")).isSameAs(pin_a);
        assertThat(UIDRegistry.getPin("my-pin-b-1")).isSameAs(pin_b_1);
        assertThat(UIDRegistry.getPin("my-pin-b-2")).isSameAs(pin_b_2);
        assertThat(UIDRegistry.getPin("my-pin-b-3")).isSameAs(pin_b_3);

        // deregister
        UIDRegistry.deregister(logicMock);

        // check #2 (non existent; again)
        assertThat(UIDRegistry.getComponent("my-logic")).isNull();
        assertThat(UIDRegistry.getConnector("my-connector-a")).isNull();
        assertThat(UIDRegistry.getConnector("my-connector-b")).isNull();
        assertThat(UIDRegistry.getPin("my-pin-a")).isNull();
        assertThat(UIDRegistry.getPin("my-pin-b-1")).isNull();
        assertThat(UIDRegistry.getPin("my-pin-b-2")).isNull();
        assertThat(UIDRegistry.getPin("my-pin-b-3")).isNull();
    }

    @Test
    @DisplayName("Test UID registry with dynamic pin")
    void testDynamicPin() {
        // check #1 (non existent)
        assertThat(UIDRegistry.getPin("my-dynamic-pin")).isNull();

        // register
        final var dynamicPinMock = mock(DynamicPin.class);
        final var dynamicPinUID = UIDFactory.createUid("my-dynamic-pin");
        when(dynamicPinMock.getUid()).thenReturn(dynamicPinUID);
        UIDRegistry.register(dynamicPinMock);

        // get
        assertThat(UIDRegistry.getPin("my-dynamic-pin")).isSameAs(dynamicPinMock);

        // deregister
        UIDRegistry.deregister(dynamicPinMock);

        // check #2 (non existent; again)
        assertThat(UIDRegistry.getComponent("my-dynamic-pin")).isNull();
    }

    @Test
    @DisplayName("Constructor not instantiable")
    void testConstructorNonInstantiable() {
        assertThatThrownBy(() -> {
            final var ctor = UIDRegistry.class.getDeclaredConstructor();
            ctor.trySetAccessible();
            ctor.newInstance();
        }).hasCauseInstanceOf(AssertionError.class);
    }
}
