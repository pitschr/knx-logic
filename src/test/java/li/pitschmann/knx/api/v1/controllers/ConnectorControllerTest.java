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

package li.pitschmann.knx.api.v1.controllers;

import li.pitschmann.knx.api.UIDRegistry;
import li.pitschmann.knx.api.v1.services.ConnectorService;
import li.pitschmann.knx.logic.Router;
import li.pitschmann.knx.logic.connector.DynamicConnector;
import li.pitschmann.knx.logic.db.DatabaseManager;
import li.pitschmann.knx.logic.db.dao.PinLinksDao;
import li.pitschmann.knx.logic.pin.DynamicPin;
import li.pitschmann.knx.logic.uid.UIDFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.TestHelpers;
import test.components.LogicB;
import test.components.LogicD;
import test.components.logic.AndLogic;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.Path;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static test.TestHelpers.assertContextJsonErrorMessage;
import static test.TestHelpers.assertContextJsonResult;
import static test.TestHelpers.contextSpy;
import static test.TestHelpers.createLogicComponent;

/**
 * Test for {@link ConnectorController}
 */
class ConnectorControllerTest {
    @BeforeAll
    static void setUp() {
        TestHelpers.initJavalinJson();
    }

    @Test
    @DisplayName("Endpoint: Get One Connector (Bad Request)")
    void testGetOne_BadRequest() {
        final var controller = new ConnectorController(
                mock(ConnectorService.class),
                mock(UIDRegistry.class)
        );

        final var context = contextSpy();
        controller.getOne(context, "");

        verify(context).status(HttpServletResponse.SC_BAD_REQUEST);
        assertContextJsonErrorMessage(context, "No connector UID provided");
    }

    @Test
    @DisplayName("Endpoint: Get One Connector (Not Found)")
    void testGetOne_NotFound() {
        final var controller = new ConnectorController(
                mock(ConnectorService.class),
                mock(UIDRegistry.class)
        );

        final var context = contextSpy();
        controller.getOne(context, "does-not-exists");

        verify(context).status(HttpServletResponse.SC_NOT_FOUND);
        assertContextJsonErrorMessage(context, "No connector found with UID: does-not-exists");
    }

    @Test
    @DisplayName("Endpoint: Get One Connector (Found)")
    void testGetOne_Found() {
        final var component = createLogicComponent(LogicB.class);
        final var connector = component.getConnector("i");
        final var registry = new UIDRegistry();
        registry.register(component);

        final var controller = new ConnectorController(
                mock(ConnectorService.class),
                registry
        );

        final var context = contextSpy();
        controller.getOne(context, connector.getUid().toString());

        verify(context).status(HttpServletResponse.SC_OK);
        assertContextJsonResult(
                context,
                Path.of("responses/ConnectorControllerTest-testGetOne_Found.json")
        );
    }

    @Test
    @DisplayName("Endpoint: Add Pin To Non-existent Connector (Connector Not Found)")
    void testAddPin_ConnectorNotFound() {
        final var controller = new ConnectorController(
                mock(ConnectorService.class),
                mock(UIDRegistry.class)
        );

        final var context = contextSpy();
        controller.addPin(context, "does-not-exists", null);

        verify(context).status(HttpServletResponse.SC_NOT_FOUND);
        assertContextJsonErrorMessage(context, "No connector found with UID: does-not-exists");
    }

    @Test
    @DisplayName("Endpoint: Add Pin To Static Connector")
    void testAddPin_StaticConnector() {
        final var registry = new UIDRegistry();
        final var component = createLogicComponent(LogicB.class);
        registry.register(component);
        final var connectorUid = component.getConnector("i").getUid().toString();

        final var controller = new ConnectorController(
                mock(ConnectorService.class),
                registry
        );

        final var context = contextSpy();
        controller.addPin(context, connectorUid, null);

        verify(context).status(HttpServletResponse.SC_FORBIDDEN);
        assertContextJsonErrorMessage(context, "Connector is not dynamic: i");
    }

    @Test
    @DisplayName("Endpoint: Add Pin To Dynamic Connector (OK, No Index)")
    void testAddPin_OK_NoIndex() {
        final var serviceMock = mock(ConnectorService.class);
        final var registrySpy = spy(new UIDRegistry());
        final var component = createLogicComponent(LogicD.class);
        registrySpy.register(component);
        final var connector = (DynamicConnector) component.getConnector("i");
        final var dynamicPinMock = mock(DynamicPin.class);
        doReturn(UIDFactory.createUid("dynamic-pin-uid")).when(dynamicPinMock).getUid();
        doReturn(dynamicPinMock).when(serviceMock).addPin(any(DynamicConnector.class));

        final var controller = new ConnectorController(
                serviceMock,
                registrySpy
        );

        final var context = contextSpy();
        controller.addPin(context, connector.getUid().toString(), null);

        verify(context).status(HttpServletResponse.SC_OK);
        assertContextJsonResult(
                context,
                Path.of("responses/ConnectorControllerTest-testAddPin_OK_NoIndex.json")
        );
        verify(serviceMock).addPin(connector);
        verify(registrySpy).register(dynamicPinMock);
    }

    @Test
    @DisplayName("Endpoint: Add Pin To Dynamic Connector (OK, With Index)")
    void testAddPin_OK_WithIndex() {
        final var serviceMock = mock(ConnectorService.class);
        final var registrySpy = spy(new UIDRegistry());
        final var component = createLogicComponent(LogicD.class);
        registrySpy.register(component);
        final var connector = (DynamicConnector) component.getConnector("i");
        final var dynamicPinMock = mock(DynamicPin.class);
        doReturn(UIDFactory.createUid("dynamic-pin-uid")).when(dynamicPinMock).getUid();
        doReturn(dynamicPinMock).when(serviceMock).addPin(any(DynamicConnector.class), anyInt());

        final var controller = new ConnectorController(
                serviceMock,
                registrySpy
        );

        final var context = contextSpy();
        controller.addPin(context, connector.getUid().toString(), 0);

        verify(context).status(HttpServletResponse.SC_OK);
        assertContextJsonResult(
                context,
                Path.of("responses/ConnectorControllerTest-testAddPin_OK_WithIndex.json")
        );
        verify(serviceMock).addPin(connector, 0);
        verify(registrySpy).register(dynamicPinMock);
    }

    @Test
    @DisplayName("Endpoint: Add Pin To Dynamic Connector (Out Of Range)")
    void testAddPin_OutOfRange() {
        final var serviceMock = mock(ConnectorService.class);
        final var registrySpy = spy(new UIDRegistry());
        final var component = createLogicComponent(LogicD.class);
        registrySpy.register(component);
        final var connector = (DynamicConnector) component.getConnector("i");

        final var controller = new ConnectorController(
                serviceMock,
                registrySpy
        );

        final var context = contextSpy();
        controller.addPin(context, connector.getUid().toString(), 999);

        verify(context).status(HttpServletResponse.SC_BAD_REQUEST);
        assertContextJsonErrorMessage(context,
                "Index of connector 'i' is out of range: 999 (min=0, max=0)"
        );
        verify(serviceMock, never()).addPin(any(DynamicConnector.class), anyInt());
        verify(registrySpy, never()).register(any(DynamicPin.class));
    }

    @Test
    @DisplayName("Endpoint: Add Pin To Dynamic Connector (Maximum Bound Exception)")
    void testAddPin_MaximumBoundException() {
        final var serviceSpy = spy(new ConnectorService(mock(DatabaseManager.class), mock(Router.class)));
        final var registrySpy = spy(new UIDRegistry());
        final var component = createLogicComponent(AndLogic.class);
        registrySpy.register(component);
        final var connector = (DynamicConnector) component.getConnector("inputs");

        final var controller = new ConnectorController(
                serviceSpy,
                registrySpy
        );

        final var context = contextSpy();
        controller.addPin(context, connector.getUid().toString(), null);

        verify(context).status(HttpServletResponse.SC_BAD_REQUEST);
        assertContextJsonErrorMessage(context,
                "Maximum number of pin already reached for connector " +
                        "'inputs': maximum=2, actual=2"
        );
        verify(serviceSpy).addPin(any(DynamicConnector.class));
        verify(registrySpy, never()).register(any(DynamicPin.class));
    }

    @Test
    @DisplayName("Endpoint: Add Pin From Non-existent Connector (Connector Not Found)")
    void testDeletePin_ConnectorNotFound() {
        final var controller = new ConnectorController(
                mock(ConnectorService.class),
                mock(UIDRegistry.class)
        );

        final var context = contextSpy();
        controller.deletePin(context, "does-not-exists", 0);

        verify(context).status(HttpServletResponse.SC_NOT_FOUND);
        assertContextJsonErrorMessage(context, "No connector found with UID: does-not-exists");
    }

    @Test
    @DisplayName("Endpoint: Delete Pin From Static Connector")
    void testDeletePin_StaticConnector() {
        final var registry = new UIDRegistry();
        final var component = createLogicComponent(LogicB.class);
        registry.register(component);
        final var connector = component.getConnector("i");

        final var controller = new ConnectorController(
                mock(ConnectorService.class),
                registry
        );

        final var context = contextSpy();
        controller.deletePin(context, connector.getUid().toString(), 0);

        verify(context).status(HttpServletResponse.SC_FORBIDDEN);
        assertContextJsonErrorMessage(context, "Connector is not dynamic: i");
    }

    @Test
    @DisplayName("Endpoint: Delete Pin From Dynamic Connector (OK)")
    void testDeletePin_OK() {
        final var databaseManagerMock = mock(DatabaseManager.class);
        doReturn(mock(PinLinksDao.class)).when(databaseManagerMock).dao(PinLinksDao.class);
        final var serviceSpy = spy(new ConnectorService(databaseManagerMock, mock(Router.class)));
        final var registrySpy = spy(new UIDRegistry());
        final var component = createLogicComponent(LogicD.class);
        registrySpy.register(component);
        final var connector = (DynamicConnector) component.getConnector("i");
        connector.addPin(); // index=1 (index 0 has already been initialized)
        final var dynamicPin = (DynamicPin) component.getPin("i[1]");

        final var controller = new ConnectorController(
                serviceSpy,
                registrySpy
        );

        final var context = contextSpy();
        controller.deletePin(context, connector.getUid().toString(), 1);

        verify(context).status(HttpServletResponse.SC_OK);
        assertContextJsonResult(
                context,
                Path.of("responses/ConnectorControllerTest-testDeletePin_OK.json")
        );
        verify(serviceSpy).removePin(connector, 1);
        verify(registrySpy).deregister(dynamicPin);
    }

    @Test
    @DisplayName("Endpoint: Delete Pin From Dynamic Connector (Out Of Range)")
    void testDeletePin_OutOfRange() {
        final var serviceMock = mock(ConnectorService.class);
        final var registrySpy = spy(new UIDRegistry());
        final var component = createLogicComponent(LogicD.class);
        registrySpy.register(component);
        final var connector = (DynamicConnector) component.getConnector("i");

        final var controller = new ConnectorController(
                serviceMock,
                registrySpy
        );

        final var context = contextSpy();
        controller.deletePin(context, connector.getUid().toString(), 999);

        verify(context).status(HttpServletResponse.SC_BAD_REQUEST);
        assertContextJsonErrorMessage(context,
                "Index of connector 'i' is out of range: 999 (min=0, max=0)"
        );
        verify(serviceMock, never()).removePin(any(DynamicConnector.class), anyInt());
        verify(registrySpy, never()).deregister(any(DynamicPin.class));
    }

    @Test
    @DisplayName("Endpoint: Delete Pin From Dynamic Connector (Minimum Bound Exception)")
    void testDeletePin_MinimumBoundException() {
        final var serviceSpy = spy(new ConnectorService(mock(DatabaseManager.class), mock(Router.class)));
        final var registrySpy = spy(new UIDRegistry());
        final var component = createLogicComponent(AndLogic.class);
        registrySpy.register(component);
        final var connector = (DynamicConnector) component.getConnector("inputs");

        final var controller = new ConnectorController(
                serviceSpy,
                registrySpy
        );

        final var context = contextSpy();
        controller.deletePin(context, connector.getUid().toString(), 0);

        verify(context).status(HttpServletResponse.SC_BAD_REQUEST);
        assertContextJsonErrorMessage(context,
                "Minimum number of pins already reached for connector " +
                        "'inputs': minimum=2, actual=2"
        );
        verify(serviceSpy).removePin(connector, 0);
        verify(registrySpy, never()).deregister(any(DynamicPin.class));
    }
}
