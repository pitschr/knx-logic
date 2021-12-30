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
import li.pitschmann.knx.logic.components.LogicComponentImpl;
import li.pitschmann.knx.logic.connector.DynamicConnector;
import li.pitschmann.knx.logic.connector.StaticConnector;
import li.pitschmann.knx.logic.diagram.Diagram;
import li.pitschmann.knx.logic.diagram.DiagramImpl;
import li.pitschmann.knx.logic.pin.DynamicPin;
import li.pitschmann.knx.logic.pin.StaticPin;
import li.pitschmann.knx.logic.uid.UIDFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.components.AndLogicBooleanListInit;
import test.components.AndLogicBooleanPrimitive;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class UIDRegistryTest {

    @Test
    @DisplayName("Test UID registry to fetch all diagrams")
    void testAllDiagram() {
        final var uidRegistry = new UIDRegistry();
        assertThat(uidRegistry.getDiagrams()).isEmpty();

        // register
        final var diagramMockA = mock(Diagram.class);
        when(diagramMockA.getUid()).thenReturn(UIDFactory.createUid("all-diagram-a"));
        final var diagramMockB = mock(Diagram.class);
        when(diagramMockB.getUid()).thenReturn(UIDFactory.createUid("all-diagram-b"));
        final var diagramMockC = mock(Diagram.class);
        when(diagramMockC.getUid()).thenReturn(UIDFactory.createUid("all-diagram-c"));

        uidRegistry.register(diagramMockA);
        uidRegistry.register(diagramMockB);
        uidRegistry.register(diagramMockC);

        // get all
        assertThat(uidRegistry.getDiagrams().stream().map(Diagram::getUid).map(Object::toString))
                .containsExactlyInAnyOrder("all-diagram-a", "all-diagram-b", "all-diagram-c");
    }

    @Test
    @DisplayName("Test UID registry to fetch all components")
    void testAllComponents() {
        final var uidRegistry = new UIDRegistry();
        assertThat(uidRegistry.getComponents()).isEmpty();

        // register
        final var componentMockA = mock(Component.class);
        when(componentMockA.getUid()).thenReturn(UIDFactory.createUid("all-component-a"));
        final var componentMockB = mock(Component.class);
        when(componentMockB.getUid()).thenReturn(UIDFactory.createUid("all-component-b"));
        final var componentMockC = mock(Component.class);
        when(componentMockC.getUid()).thenReturn(UIDFactory.createUid("all-component-c"));

        uidRegistry.register(componentMockA);
        uidRegistry.register(componentMockB);
        uidRegistry.register(componentMockC);

        // get all
        assertThat(uidRegistry.getComponents().stream().map(Component::getUid).map(Object::toString))
                .containsExactlyInAnyOrder("all-component-a", "all-component-b", "all-component-c");
    }

    @Test
    @DisplayName("Test UID registry for diagram")
    void testDiagram() {
        final var uidRegistry = new UIDRegistry();

        // check #1 (non existent)
        assertThat(uidRegistry.getDiagram("my-diagram")).isNull();

        // register
        final var diagramMock = mock(Diagram.class);
        final var diagramUID = UIDFactory.createUid("my-diagram");
        when(diagramMock.getUid()).thenReturn(diagramUID);
        uidRegistry.register(diagramMock);

        // get
        assertThat(uidRegistry.getDiagram("my-diagram")).isSameAs(diagramMock);

        // deregister
        uidRegistry.deregister(diagramMock);

        // check #2 (non existent; again)
        assertThat(uidRegistry.getDiagram("my-diagram")).isNull();
    }

    @Test
    @DisplayName("Test UID registry for component without connectors and pins")
    void testComponent() {
        final var uidRegistry = new UIDRegistry();

        // check #1 (non existent)
        assertThat(uidRegistry.getComponent("my-component")).isNull();

        // register
        final var componentMock = mock(Component.class);
        final var componentUID = UIDFactory.createUid("my-component");
        when(componentMock.getUid()).thenReturn(componentUID);
        uidRegistry.register(componentMock);

        // get
        assertThat(uidRegistry.getComponent("my-component")).isSameAs(componentMock);

        // deregister
        uidRegistry.deregister(componentMock);

        // check #2 (non existent; again)
        assertThat(uidRegistry.getComponent("my-component")).isNull();
    }

    @Test
    @DisplayName("Test UID registry for logic component with connectors and pins")
    void testComponentWithConnector() {
        final var uidRegistry = new UIDRegistry();

        // check #1 (non existent)
        assertThat(uidRegistry.getComponent("my-logic")).isNull();
        assertThat(uidRegistry.getConnector("my-connector-a")).isNull();
        assertThat(uidRegistry.getConnector("my-connector-b")).isNull();
        assertThat(uidRegistry.getPin("my-pin-a")).isNull();
        assertThat(uidRegistry.getPin("my-pin-b-1")).isNull();
        assertThat(uidRegistry.getPin("my-pin-b-2")).isNull();
        assertThat(uidRegistry.getPin("my-pin-b-3")).isNull();

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
        uidRegistry.register(logicMock); // should register component, connectors and pins

        // get
        assertThat(uidRegistry.getComponent("my-logic")).isSameAs(logicMock);
        assertThat(uidRegistry.getConnector("my-connector-a")).isSameAs(connector_a);
        assertThat(uidRegistry.getConnector("my-connector-b")).isSameAs(connector_b);
        assertThat(uidRegistry.getPin("my-pin-a")).isSameAs(pin_a);
        assertThat(uidRegistry.getPin("my-pin-b-1")).isSameAs(pin_b_1);
        assertThat(uidRegistry.getPin("my-pin-b-2")).isSameAs(pin_b_2);
        assertThat(uidRegistry.getPin("my-pin-b-3")).isSameAs(pin_b_3);

        // deregister
        uidRegistry.deregister(logicMock);

        // check #2 (non existent; again)
        assertThat(uidRegistry.getComponent("my-logic")).isNull();
        assertThat(uidRegistry.getConnector("my-connector-a")).isNull();
        assertThat(uidRegistry.getConnector("my-connector-b")).isNull();
        assertThat(uidRegistry.getPin("my-pin-a")).isNull();
        assertThat(uidRegistry.getPin("my-pin-b-1")).isNull();
        assertThat(uidRegistry.getPin("my-pin-b-2")).isNull();
        assertThat(uidRegistry.getPin("my-pin-b-3")).isNull();
    }

    @Test
    @DisplayName("Test UID registry with dynamic pin")
    void testDynamicPin() {
        final var uidRegistry = new UIDRegistry();

        // check #1 (non existent)
        assertThat(uidRegistry.getPin("my-dynamic-pin")).isNull();

        // register
        final var dynamicPinMock = mock(DynamicPin.class);
        final var dynamicPinUID = UIDFactory.createUid("my-dynamic-pin");
        when(dynamicPinMock.getUid()).thenReturn(dynamicPinUID);
        uidRegistry.register(dynamicPinMock);

        // get
        assertThat(uidRegistry.getPin("my-dynamic-pin")).isSameAs(dynamicPinMock);

        // deregister
        uidRegistry.deregister(dynamicPinMock);

        // check #2 (non existent; again)
        assertThat(uidRegistry.getComponent("my-dynamic-pin")).isNull();
    }

    @Test
    @DisplayName("Test #toString()")
    void testToString() {
        final var uidRegistry = new UIDRegistry();

        // set some data stuff
        final var diagram_a = new DiagramImpl();
        diagram_a.setUid(UIDFactory.createUid("diagram-a-uid"));
        diagram_a.setName("diagram-a-name");
        diagram_a.setDescription("diagram-a-description");
        final var diagram_b = new DiagramImpl();
        diagram_b.setUid(UIDFactory.createUid("diagram-b-uid"));
        diagram_b.setName("diagram-b-name");
        diagram_b.setDescription("diagram-b-description");

        // some mocks for stable string output
        final var component_a = spy(new LogicComponentImpl(new AndLogicBooleanPrimitive()));
        component_a.setUid(UIDFactory.createUid("component-a-uid"));
        final var connector_a_i1 = spy(component_a.getConnector("i1"));
        when(connector_a_i1.getUid()).thenReturn(UIDFactory.createUid("connector-a-i1"));
        final var connector_a_i2 = spy(component_a.getConnector("i2"));
        when(connector_a_i2.getUid()).thenReturn(UIDFactory.createUid("connector-a-i2"));
        final var connector_a_o = spy(component_a.getConnector("o"));
        when(connector_a_o.getUid()).thenReturn(UIDFactory.createUid("connector-a-o"));
        when(component_a.getConnectors()).thenReturn(List.of(connector_a_i1, connector_a_i2, connector_a_o));

        final var pin_a_i1 = spy(component_a.getPin("i1"));
        when(pin_a_i1.getUid()).thenReturn(UIDFactory.createUid("pin-a-i1"));
        final var pin_a_i2 = spy(component_a.getPin("i2"));
        when(pin_a_i2.getUid()).thenReturn(UIDFactory.createUid("pin-a-i2"));
        final var pin_a_o = spy(component_a.getPin("o"));
        when(pin_a_o.getUid()).thenReturn(UIDFactory.createUid("pin-a-o"));
        doReturn(List.of(pin_a_i1, pin_a_i2, pin_a_o)).when(component_a).getPins();

        final var component_b = spy(new LogicComponentImpl(new AndLogicBooleanListInit()));
        component_b.setUid(UIDFactory.createUid("component-b-uid"));
        final var connector_b_i = spy(component_b.getConnector("i"));
        when(connector_b_i.getUid()).thenReturn(UIDFactory.createUid("connector-b-i"));
        final var connector_b_o = spy(component_b.getConnector("o"));
        when(connector_b_o.getUid()).thenReturn(UIDFactory.createUid("connector-b-o"));
        when(component_b.getConnectors()).thenReturn(List.of(connector_b_i, connector_b_o));

        final var pin_b_i_0 = spy(component_b.getPin("i[0]"));
        when(pin_b_i_0.getUid()).thenReturn(UIDFactory.createUid("pin-b-i[0]"));
        final var pin_b_i_1 = spy(component_b.getPin("i[1]"));
        when(pin_b_i_1.getUid()).thenReturn(UIDFactory.createUid("pin-b-i[1]"));
        final var pin_b_o = spy(component_b.getPin("o"));
        when(pin_b_o.getUid()).thenReturn(UIDFactory.createUid("pin-b-o"));
        doReturn(List.of(pin_b_i_0, pin_b_i_1, pin_b_o)).when(component_b).getPins();

        // register them
        uidRegistry.register(diagram_a);
        uidRegistry.register(diagram_b);
        uidRegistry.register(component_a);
        uidRegistry.register(component_b);

        // @formatter:off
        assertThat(uidRegistry).hasToString(
                "UIDRegistry{" + //
                        "\n\tdiagramMap={" + //
                            "\n\t\tdiagram-a-uid=diagram-a-name, " + //
                            "\n\t\tdiagram-b-uid=diagram-b-name" + //
                        "\n\t}, " + //
                        "\n\tcomponentMap={" + //
                            "\n\t\tcomponent-b-uid=test.components.AndLogicBooleanListInit, " + //
                            "\n\t\tcomponent-a-uid=test.components.AndLogicBooleanPrimitive" + //
                        "\n\t}, " + //
                        "\n\tconnectorMap={" + //
                            "\n\t\tconnector-b-i=test.components.AndLogicBooleanListInit#i, " + //
                            "\n\t\tconnector-b-o=test.components.AndLogicBooleanListInit#o, " + //
                            "\n\t\tconnector-a-i1=test.components.AndLogicBooleanPrimitive#i1, " + //
                            "\n\t\tconnector-a-i2=test.components.AndLogicBooleanPrimitive#i2, " + //
                            "\n\t\tconnector-a-o=test.components.AndLogicBooleanPrimitive#o" + //
                        "\n\t}, " + //
                        "\n\tpinMap={" + //
                            "\n\t\tpin-b-i[0]=test.components.AndLogicBooleanListInit#i[0], " + //
                            "\n\t\tpin-b-i[1]=test.components.AndLogicBooleanListInit#i[1], " + //
                            "\n\t\tpin-b-o=test.components.AndLogicBooleanListInit#o, " + //
                            "\n\t\tpin-a-i1=test.components.AndLogicBooleanPrimitive#i1, " + //
                            "\n\t\tpin-a-i2=test.components.AndLogicBooleanPrimitive#i2, " + //
                            "\n\t\tpin-a-o=test.components.AndLogicBooleanPrimitive#o" + //
                        "\n\t}" + //
                "\n}"
                // @formatter:on
        );
    }
}
