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

package li.pitschmann.knx.api.v1.json;

import li.pitschmann.knx.logic.components.LogicComponentImpl;
import li.pitschmann.knx.logic.connector.DynamicConnector;
import li.pitschmann.knx.logic.connector.StaticConnector;
import li.pitschmann.knx.logic.uid.UIDFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.components.LogicB;
import test.components.LogicF;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Test for {@link PinResponse}
 */
class PinResponseTest {
    @Test
    @DisplayName("Test Static Pin Response")
    void testStaticPin() {
        final var component = new LogicComponentImpl(new LogicB());
        final var connector = spy((StaticConnector) component.getConnector("i"));
        when(connector.getUid()).thenReturn(UIDFactory.createUid("STATIC-CONNECTOR-UID"));
        final var staticPin = spy(connector.getPin());
        when(staticPin.getUid()).thenReturn(UIDFactory.createUid("STATIC-PIN-UID"));
        when(staticPin.getConnector()).thenReturn(connector);

        final var response = PinResponse.from(staticPin);
        assertThat(response.getUid()).isEqualTo("STATIC-PIN-UID");
        assertThat(response.getConnectorUid()).isEqualTo("STATIC-CONNECTOR-UID");
        assertThat(response.getValueType()).isEqualTo("java.lang.Boolean");
        assertThat(response.getValue()).isEqualTo("false");

        assertThat(response).hasToString(
                "PinResponse" + //
                        "{" +
                        "uid=STATIC-PIN-UID, " + //
                        "connectorUid=STATIC-CONNECTOR-UID, " + //
                        "valueType=java.lang.Boolean, " + //
                        "value=false" + //
                        "}"
        );
    }

    @Test
    @DisplayName("Test Dynamic Pin Response")
    void testDynamicPin() {
        final var component = new LogicComponentImpl(new LogicF());
        final var connector = spy((DynamicConnector) component.getConnector("inputs"));
        when(connector.getUid()).thenReturn(UIDFactory.createUid("DYNAMIC-CONNECTOR-UID"));
        final var dynamicPin = spy(connector.getPin(0));
        when(dynamicPin.getUid()).thenReturn(UIDFactory.createUid("DYNAMIC-PIN-UID"));
        when(dynamicPin.getConnector()).thenReturn(connector);
        dynamicPin.setValue("foobar");

        final var response = PinResponse.from(dynamicPin);
        assertThat(response.getUid()).isEqualTo("DYNAMIC-PIN-UID");
        assertThat(response.getConnectorUid()).isEqualTo("DYNAMIC-CONNECTOR-UID");
        assertThat(response.getValueType()).isEqualTo("java.lang.String");
        assertThat(response.getValue()).isEqualTo("foobar");

        assertThat(response).hasToString(
                "PinResponse" + //
                        "{" +
                        "uid=DYNAMIC-PIN-UID, " + //
                        "connectorUid=DYNAMIC-CONNECTOR-UID, " + //
                        "valueType=java.lang.String, " + //
                        "value=foobar" + //
                        "}"
        );
    }

    @Test
    @DisplayName("Test #fromWithoutConnectorInfo(Pin)")
    void testFromWithoutConnectorInfo() {
        final var pin = new LogicComponentImpl(new LogicB()).getPin("i");

        final var response = PinResponse.fromWithoutConnectorInfo(pin);
        assertThat(response.getUid()).isNotNull();
        assertThat(response.getConnectorUid()).isNull();
        assertThat(response.getValueType()).isNull();
        assertThat(response.getValue()).isNotNull();
    }

    @Test
    @DisplayName("Test #fromWithoutPinInfo(Pin)")
    void testFromWithoutPinInfo() {
        final var pin = new LogicComponentImpl(new LogicB()).getPin("i");

        final var response = PinResponse.fromWithoutPinInfo(pin);
        assertThat(response.getUid()).isNull();
        assertThat(response.getConnectorUid()).isNull();
        assertThat(response.getValueType()).isNotNull();
        assertThat(response.getValue()).isNotNull();
    }
}
