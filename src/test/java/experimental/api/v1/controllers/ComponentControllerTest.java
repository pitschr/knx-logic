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

package experimental.api.v1.controllers;

import experimental.api.v1.json.ComponentRequest;
import experimental.api.v1.json.ComponentResponse;
import experimental.api.v1.services.ComponentService;
import li.pitschmann.knx.api.UIDRegistry;
import li.pitschmann.knx.api.v1.ComponentFactory;
import li.pitschmann.knx.logic.LogicRepository;
import li.pitschmann.knx.logic.components.Component;
import li.pitschmann.knx.logic.components.LogicComponentImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.TestHelpers;
import test.components.LogicA;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static test.TestHelpers.assertContextJsonErrorMessage;
import static test.TestHelpers.assertContextJsonResult;
import static test.TestHelpers.contextSpy;
import static test.TestHelpers.createInboxComponent;
import static test.TestHelpers.createLogicComponent;
import static test.TestHelpers.createOutboxComponent;
import static test.TestHelpers.toJson;

/**
 * Test for {@link ComponentController}
 */
class ComponentControllerTest {
    @BeforeAll
    static void setUp() {
        TestHelpers.initJavalinJson();
    }

    @Test
    @DisplayName("Endpoint: Get All Components (no components registered yet)")
    void testAll_NoComponents() {
        final var serviceMock = mock(ComponentService.class);
        final var factoryMock = mock(ComponentFactory.class);
        final var registryMock = mock(UIDRegistry.class);

        final var controller = new ComponentController(serviceMock, factoryMock, registryMock);

        final var context = contextSpy();
        controller.getAll(context);

        verify(context).status(HttpServletResponse.SC_OK);
        verify(context).result("[]");
    }

    @Test
    @DisplayName("Endpoint: Get All Components (two components registered)")
    void testAll_TwoComponents() {
        final var serviceMock = mock(ComponentService.class);
        final var factoryMock = mock(ComponentFactory.class);
        final var registryMock = mock(UIDRegistry.class);

        // register two components
        final var component1 = new LogicComponentImpl(new LogicA());
        final var component2 = new LogicComponentImpl(new LogicA());
        doReturn(List.of(component1, component2)).when(registryMock).getComponents();

        final var controller = new ComponentController(serviceMock, factoryMock, registryMock);

        final var context = contextSpy();
        controller.getAll(context);

        verify(context).status(HttpServletResponse.SC_OK);
        verify(context).result(
                toJson(
                        ComponentResponse.from(component1),
                        ComponentResponse.from(component2)
                )
        );
    }

    @Test
    @DisplayName("Endpoint: Get One Component (Bad Request)")
    void testGetOne_BadRequest() {
        final var serviceMock = mock(ComponentService.class);
        final var factoryMock = mock(ComponentFactory.class);
        final var registryMock = mock(UIDRegistry.class);

        final var controller = new ComponentController(serviceMock, factoryMock, registryMock);

        final var context = contextSpy();
        controller.getOne(context, "");

        verify(context).status(HttpServletResponse.SC_BAD_REQUEST);
        assertContextJsonErrorMessage(context,"No component UID provided");
    }

    @Test
    @DisplayName("Endpoint: Get One Component (Not Found)")
    void testGetOne_ComponentNotFound() {
        final var serviceMock = mock(ComponentService.class);
        final var factoryMock = mock(ComponentFactory.class);
        final var registryMock = mock(UIDRegistry.class);

        final var controller = new ComponentController(serviceMock, factoryMock, registryMock);

        final var context = contextSpy();
        controller.getOne(context, "does-not-exists");

        verify(context).status(HttpServletResponse.SC_NOT_FOUND);
        assertContextJsonErrorMessage(context,"No component found with UID: does-not-exists");
    }

    @Test
    @DisplayName("Endpoint: Get One Component (Found)")
    void testGetOne_ComponentFound() {
        final var serviceMock = mock(ComponentService.class);
        final var factoryMock = mock(ComponentFactory.class);
        final var registryMock = mock(UIDRegistry.class);
        final var component = new LogicComponentImpl(new LogicA());
        doReturn(component).when(registryMock).getComponent(anyString());

        final var controller = new ComponentController(serviceMock, factoryMock, registryMock);

        final var context = contextSpy();
        controller.getOne(context, "component-uid");

        verify(context).status(HttpServletResponse.SC_OK);
        verify(context).result(toJson(ComponentResponse.from(component)));
    }

    @Test
    @DisplayName("Endpoint: Create Logic Component")
    void testCreate_Logic() {
        final var serviceMock = mock(ComponentService.class);
        final var registryMock = mock(UIDRegistry.class);
        final var logicRepositoryMock = spy(new LogicRepository());
        doReturn(LogicA.class).when(logicRepositoryMock).findLogicClass(anyString());
        final var factory = new ComponentFactory(logicRepositoryMock);
        final var controller = new ComponentController(serviceMock, factory, registryMock);

        final var request = new ComponentRequest();
        request.setType("logic");
        request.setData(Map.of("class", LogicA.class.getName()));

        final var context = contextSpy();
        controller.create(context, request);

        verify(context).status(HttpServletResponse.SC_CREATED);
        assertContextJsonResult(
                context,
                toJson(ComponentResponse.from(createLogicComponent(LogicA.class)))
        );
    }

    @Test
    @DisplayName("Endpoint: Create Logic Component (unknown logic class)")
    void testCreate_Logic_UnknownClass() {
        final var serviceMock = mock(ComponentService.class);
        final var registryMock = mock(UIDRegistry.class);
        final var factory = new ComponentFactory(new LogicRepository());
        final var controller = new ComponentController(serviceMock, factory, registryMock);

        final var request = new ComponentRequest();
        request.setType("logic");
        request.setData(Map.of("class", "does.not.exists.Class"));

        final var context = contextSpy();
        controller.create(context, request);

        verify(context).status(HttpServletResponse.SC_BAD_REQUEST);
        assertContextJsonErrorMessage(context,"No Logic Class found: does.not.exists.Class");
        verify(serviceMock, never()).addComponent(any(Component.class));
        verify(registryMock, never()).register(any(Component.class));
    }

    @Test
    @DisplayName("Endpoint: Create Inbox Component")
    void testCreate_Inbox() {
        final var serviceMock = mock(ComponentService.class);
        final var registryMock = mock(UIDRegistry.class);
        final var factory = new ComponentFactory(new LogicRepository());
        final var controller = new ComponentController(serviceMock, factory, registryMock);

        final var request = new ComponentRequest();
        request.setType("inbox");
        request.setEvent("var");
        request.setData(Map.of("name", "inbox-variable"));

        final var context = contextSpy();
        controller.create(context, request);

        verify(context).status(HttpServletResponse.SC_CREATED);
        assertContextJsonResult(
                context,
                toJson(ComponentResponse.from(createInboxComponent("inbox-variable")))
        );
        verify(serviceMock).addComponent(any(Component.class));
        verify(registryMock).register(any(Component.class));
    }

    @Test
    @DisplayName("Endpoint: Create Outbox Component")
    void testCreate_Outbox() {
        final var serviceMock = mock(ComponentService.class);
        final var registryMock = mock(UIDRegistry.class);
        final var factory = new ComponentFactory(new LogicRepository());
        final var controller = new ComponentController(serviceMock, factory, registryMock);

        final var request = new ComponentRequest();
        request.setType("outbox");
        request.setEvent("var");
        request.setData(Map.of("name", "outbox-variable"));

        final var context = contextSpy();
        controller.create(context, request);

        verify(context).status(HttpServletResponse.SC_CREATED);
        assertContextJsonResult(
                context,
                toJson(ComponentResponse.from(createOutboxComponent("outbox-variable")))
        );
        verify(serviceMock).addComponent(any(Component.class));
        verify(registryMock).register(any(Component.class));
    }

    @Test
    @DisplayName("Endpoint: Create Component with unsupported type")
    void testCreate_UnknownType() {
        final var serviceMock = mock(ComponentService.class);
        final var factoryMock = mock(ComponentFactory.class);
        final var registryMock = mock(UIDRegistry.class);
        final var controller = new ComponentController(serviceMock, factoryMock, registryMock);

        final var request = new ComponentRequest();
        request.setType("UNKNOWN-TYPE");

        final var context = contextSpy();
        controller.create(context, request);

        verify(context).status(HttpServletResponse.SC_BAD_REQUEST);
        assertContextJsonErrorMessage(context,"Unsupported Component Type: UNKNOWN-TYPE. Supported are: logic, inbox and outbox.");
        verify(serviceMock, never()).addComponent(any(Component.class));
        verify(registryMock, never()).register(any(Component.class));
    }

    @Test
    @DisplayName("Endpoint: Delete Component (Not Found)")
    void testDelete_ComponentNotFound() {
        final var serviceMock = mock(ComponentService.class);
        final var factoryMock = mock(ComponentFactory.class);
        final var registryMock = mock(UIDRegistry.class);

        final var controller = new ComponentController(serviceMock, factoryMock, registryMock);

        final var context = contextSpy();
        controller.delete(context, "does-not-exists");

        verify(context).status(HttpServletResponse.SC_NO_CONTENT);
        verify(serviceMock, never()).removeComponent(any(Component.class));
        verify(registryMock, never()).deregister(any(Component.class));
    }

    @Test
    @DisplayName("Endpoint: Delete Component (Found)")
    void testDelete_ComponentFound() {
        final var serviceMock = mock(ComponentService.class);
        final var factoryMock = mock(ComponentFactory.class);
        final var registryMock = mock(UIDRegistry.class);
        final var component = new LogicComponentImpl(new LogicA());
        doReturn(component).when(registryMock).getComponent(anyString());

        final var controller = new ComponentController(serviceMock, factoryMock, registryMock);

        final var context = contextSpy();
        controller.delete(context, "component-uid");

        verify(context).status(HttpServletResponse.SC_NO_CONTENT);
        verify(serviceMock).removeComponent(any(Component.class));
        verify(registryMock).deregister(any(Component.class));
    }
}
