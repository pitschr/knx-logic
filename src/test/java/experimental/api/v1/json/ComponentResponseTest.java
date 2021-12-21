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

package experimental.api.v1.json;

import li.pitschmann.knx.logic.components.LogicComponentImpl;
import li.pitschmann.knx.logic.uid.UIDFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.components.LogicA;
import test.components.LogicC;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Test for {@link ComponentResponse}
 */
class ComponentResponseTest {
    @Test
    @DisplayName("Test component without connectors")
    void testWithoutConnectors() {
        final var component = spy(new LogicComponentImpl(new LogicA()));
        when(component.getUid()).thenReturn(UIDFactory.createUid("COMPONENT-UID"));

        final var response = ComponentResponse.from(component);
        assertThat(response.getUid()).isEqualTo("COMPONENT-UID");
        assertThat(response.getClassName()).isEqualTo("test.components.LogicA");

        assertThat(response).hasToString(
                "ComponentResponse" + //
                        "{" +
                        "uid=COMPONENT-UID, " + //
                        "className=test.components.LogicA, " + //
                        "inputs=[], " + //
                        "outputs=[]" + //
                        "}"
        );
    }

    @Test
    @DisplayName("Test component with connectors")
    void testWithConnectors() {
        final var component = spy(new LogicComponentImpl(new LogicC()));
        when(component.getUid()).thenReturn(UIDFactory.createUid("COMPONENT-UID"));
        final var connector_i = spy(component.getConnector("i"));
        when(connector_i.getUid()).thenReturn(UIDFactory.createUid("CONNECTOR-I-UID"));
        final var connector_o = spy(component.getConnector("o"));
        when(connector_o.getUid()).thenReturn(UIDFactory.createUid("CONNECTOR-O-UID"));

        when(component.getInputConnectors()).thenReturn(List.of(connector_i));
        when(component.getOutputConnectors()).thenReturn(List.of(connector_o));

        final var response = ComponentResponse.from(component);
        assertThat(response.getUid()).isEqualTo("COMPONENT-UID");
        assertThat(response.getClassName()).isEqualTo("test.components.LogicC");
        assertThat(response.getInputs()).hasSize(1);
        assertThat(response.getInputs().get(0).getUid()).isEqualTo("CONNECTOR-I-UID");
        assertThat(response.getOutputs()).hasSize(1);
        assertThat(response.getOutputs().get(0).getUid()).isEqualTo("CONNECTOR-O-UID");

        assertThat(response).hasToString(
                "ComponentResponse" + //
                        "{" +
                        "uid=COMPONENT-UID, " + //
                        "className=test.components.LogicC, " + //
                        "inputs=[CONNECTOR-I-UID], " + //
                        "outputs=[CONNECTOR-O-UID]" + //
                        "}"
        );
    }
}
