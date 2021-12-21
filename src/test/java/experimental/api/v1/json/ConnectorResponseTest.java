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
import li.pitschmann.knx.logic.connector.DynamicConnector;
import li.pitschmann.knx.logic.uid.UIDFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.components.LogicB;
import test.components.LogicD;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Test for {@link ConnectorResponse}
 */
class ConnectorResponseTest {
    @Test
    @DisplayName("Test connector with static pin")
    void testWithStaticPin() {
        final var component = new LogicComponentImpl(new LogicB());
        final var connector = spy(component.getConnector("i"));
        when(connector.getUid()).thenReturn(UIDFactory.createUid("STATIC-CONNECTOR-UID"));
        final var staticPin = spy(component.getPin("i"));
        when(staticPin.getUid()).thenReturn(UIDFactory.createUid("STATIC-PIN-UID"));
        when(connector.getPinStream()).thenReturn(Stream.of(staticPin));

        final var response = ConnectorResponse.from(connector);
        assertThat(response.getUid()).isEqualTo("STATIC-CONNECTOR-UID");
        assertThat(response.getName()).isEqualTo("i");
        assertThat(response.isDynamic()).isFalse();
        assertThat(response.getPinType()).isEqualTo("java.lang.Boolean");
        assertThat(response.getPins()).hasSize(1);
        assertThat(response.getPins().get(0).getUid()).isEqualTo("STATIC-PIN-UID");

        assertThat(response).hasToString(
                "ConnectorResponse" + //
                        "{" +
                        "uid=STATIC-CONNECTOR-UID, " + //
                        "name=i, " + //
                        "dynamic=false, " + //
                        "pinType=java.lang.Boolean, " + //
                        "pins=[STATIC-PIN-UID]" + //
                        "}"
        );
    }

    @Test
    @DisplayName("Test connector with dynamic pin")
    void testWithDynamicPin() {
        final var component = new LogicComponentImpl(new LogicD());
        final var connector = spy((DynamicConnector) component.getConnector("i"));
        when(connector.getUid()).thenReturn(UIDFactory.createUid("DYNAMIC-CONNECTOR-UID"));

        // increase to 3 pins
        connector.tryIncrease(3);
        final var dynamicPin_0 = spy(component.getPin("i[0]"));
        when(dynamicPin_0.getUid()).thenReturn(UIDFactory.createUid("DYNAMIC-PIN-UID-0"));
        final var dynamicPin_1 = spy(component.getPin("i[1]"));
        when(dynamicPin_1.getUid()).thenReturn(UIDFactory.createUid("DYNAMIC-PIN-UID-1"));
        final var dynamicPin_2 = spy(component.getPin("i[2]"));
        when(dynamicPin_2.getUid()).thenReturn(UIDFactory.createUid("DYNAMIC-PIN-UID-2"));
        when(connector.getPinStream()).thenReturn(Stream.of(dynamicPin_0, dynamicPin_1, dynamicPin_2));

        final var response = ConnectorResponse.from(connector);
        assertThat(response.getUid()).isEqualTo("DYNAMIC-CONNECTOR-UID");
        assertThat(response.getName()).isEqualTo("i");
        assertThat(response.isDynamic()).isTrue();
        assertThat(response.getPinType()).isEqualTo("java.lang.Boolean");
        assertThat(response.getPins()).hasSize(3);
        assertThat(response.getPins().get(0).getUid()).isEqualTo("DYNAMIC-PIN-UID-0");
        assertThat(response.getPins().get(1).getUid()).isEqualTo("DYNAMIC-PIN-UID-1");
        assertThat(response.getPins().get(2).getUid()).isEqualTo("DYNAMIC-PIN-UID-2");

        assertThat(response).hasToString(
                "ConnectorResponse" + //
                        "{" +
                        "uid=DYNAMIC-CONNECTOR-UID, " + //
                        "name=i, " + //
                        "dynamic=true, " + //
                        "pinType=java.lang.Boolean, " + //
                        "pins=[DYNAMIC-PIN-UID-0, DYNAMIC-PIN-UID-1, DYNAMIC-PIN-UID-2]" + //
                        "}"
        );
    }
}
