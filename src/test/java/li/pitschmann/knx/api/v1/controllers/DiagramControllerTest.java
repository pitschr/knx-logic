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
import li.pitschmann.knx.api.v1.json.DiagramRequest;
import li.pitschmann.knx.api.v1.services.DiagramService;
import li.pitschmann.knx.logic.components.Component;
import li.pitschmann.knx.logic.diagram.Diagram;
import li.pitschmann.knx.logic.diagram.DiagramImpl;
import li.pitschmann.knx.logic.uid.UIDFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import test.TestHelpers;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static test.TestHelpers.assertContextHasNoResponse;
import static test.TestHelpers.assertContextJsonErrorMessage;
import static test.TestHelpers.assertContextJsonResult;
import static test.TestHelpers.contextSpy;

/**
 * Test for {@link DiagramController}
 */
class DiagramControllerTest {
    @BeforeAll
    static void setUp() {
        TestHelpers.initJavalinJson();
    }

    @Test
    @DisplayName("Endpoint: Get All Diagrams")
    void testGetAll() {
        final var controller = newDiagramController();

        final var context = contextSpy();
        controller.getAll(context);

        verify(context).status(HttpServletResponse.SC_OK);
        assertContextJsonResult(
                context,
                Path.of("responses/DiagramControllerTest-testGetAll.json")
        );
    }

    @Test
    @DisplayName("Endpoint: Get One Diagram (Bad Request)")
    void testGetOne_BadRequest() {
        final var controller = newDiagramController();

        final var context = contextSpy();
        controller.getOne(context, "");

        verify(context).status(HttpServletResponse.SC_BAD_REQUEST);
        assertContextJsonErrorMessage(context, "No diagram UID provided");
    }

    @Test
    @DisplayName("Endpoint: Get One Diagram (Not Found)")
    void testGetOne_NotFound() {
        final var controller = newDiagramController();

        final var context = contextSpy();
        controller.getOne(context, "diagram-does-not-exists");

        verify(context).status(HttpServletResponse.SC_NOT_FOUND);
        assertContextJsonErrorMessage(context, "No diagram found with UID: diagram-does-not-exists");
    }

    @Test
    @DisplayName("Endpoint: Get One Diagram (Found)")
    void testGetOne_Found() {
        final var controller = newDiagramController();

        final var context = contextSpy();
        controller.getOne(context, "diagram-uid-1");

        verify(context).status(HttpServletResponse.SC_OK);
        assertContextJsonResult(
                context,
                Path.of("responses/DiagramControllerTest-testGetOne_Found.json")
        );
    }

    @Test
    @DisplayName("Endpoint: Create A New Diagram")
    void testCreate() {
        final var serviceMock = mock(DiagramService.class);
        final var registrySpy = spy(new UIDRegistry());
        final var controller = new DiagramController(serviceMock, registrySpy);

        final var request = new DiagramRequest();
        request.setName("New Diagram");
        request.setDescription("New Diagram Description");

        final var context = contextSpy();
        controller.create(context, request);

        verify(context).status(HttpServletResponse.SC_CREATED);
        assertContextJsonResult(
                context,
                Path.of("responses/DiagramControllerTest-testCreate.json")
        );
        verify(serviceMock).insertDiagram(any(Diagram.class));
        verify(serviceMock, never()).updateDiagram(any(Diagram.class));
        verify(registrySpy).register(any(Diagram.class));
    }

    @Test
    @DisplayName("Endpoint: Update An Existing Diagram (Not Found)")
    void testUpdate_NotFound() {
        final var controller = newDiagramController();

        final var request = new DiagramRequest();
        request.setName("Updated Diagram");
        request.setDescription("Diagram Description");

        final var context = contextSpy();
        controller.update(context, "diagram-does-not-exists", request);

        verify(context).status(HttpServletResponse.SC_NOT_FOUND);
        assertContextJsonErrorMessage(context, "No diagram found with UID: diagram-does-not-exists");
    }

    @Test
    @DisplayName("Endpoint: Update An Existing Diagram (Found)")
    void testUpdate_Found() {
        final var serviceMock = mock(DiagramService.class);
        final var registrySpy = spy(new UIDRegistry());
        final var controller = new DiagramController(serviceMock, registrySpy);

        // existing diagram
        final var diagram = new DiagramImpl();
        diagram.setUid(UIDFactory.createUid("existing-diagram"));
        diagram.setName("New Diagram");
        diagram.setDescription("New Diagram Description");
        registrySpy.register(diagram);
        Mockito.clearInvocations(registrySpy); // clear because of calling register above

        // now update the existing diagram
        final var request = new DiagramRequest();
        request.setName("Updated Diagram");
        request.setDescription("Updated Diagram Description");

        final var context = contextSpy();
        controller.update(context, "existing-diagram", request);

        verify(context).status(HttpServletResponse.SC_OK);
        assertContextJsonResult(
                context,
                Path.of("responses/DiagramControllerTest-testUpdate_Found.json")
        );
        verify(serviceMock, never()).insertDiagram(any(Diagram.class));
        verify(serviceMock).updateDiagram(any(Diagram.class));
        verify(registrySpy, never()).register(any(Diagram.class));
    }

    @Test
    @DisplayName("Endpoint: Delete Diagram (Not Found)")
    void testDelete_NotFound() {
        final var controller = newDiagramController();

        final var context = contextSpy();
        controller.delete(context, "diagram-does-not-exists");

        verify(context).status(HttpServletResponse.SC_NOT_FOUND);
        assertContextJsonErrorMessage(context, "No diagram found with UID: diagram-does-not-exists");
    }

    @Test
    @DisplayName("Endpoint: Delete Diagram (Found)")
    void testDelete_Found() {
        final var registrySpy = spy(new UIDRegistry());
        final var serviceMock = mock(DiagramService.class);
        final var controller = newDiagramController(serviceMock, registrySpy);

        // two components are associated with the diagram
        doReturn(List.of(mock(Component.class), mock(Component.class))).when(serviceMock).getDiagramComponents(any(Diagram.class), any());
        doReturn(mock(Component.class), mock(Component.class)).when(registrySpy).getComponent(anyString());
        doNothing().when(registrySpy).deregister(any(Component.class));

        final var context = contextSpy();
        controller.delete(context, "diagram-uid-1");

        verify(context).status(HttpServletResponse.SC_NO_CONTENT);
        assertContextHasNoResponse(context);
        verify(serviceMock).deleteDiagram(any(Diagram.class));
        verify(serviceMock).getDiagramComponents(any(Diagram.class), any());
        verify(serviceMock, times(2)).deleteDiagramComponent(any(Component.class));
        verify(registrySpy, times(2)).deregister(any(Component.class));
    }

    /*
     * Internal Test Method to create a new DiagramController
     */
    private DiagramController newDiagramController() {
        return newDiagramController(
                mock(DiagramService.class),
                spy(new UIDRegistry())
        );
    }

    private DiagramController newDiagramController(final DiagramService service, final UIDRegistry registry) {
        final var diagrams = IntStream.range(0, 3).mapToObj(i -> {
            final var diagram = new DiagramImpl();
            diagram.setUid(UIDFactory.createUid("diagram-uid-" + i));
            diagram.setName("Diagram Name " + i);
            diagram.setDescription("Diagram Description " + i);
            return diagram;
        }).collect(Collectors.toList());
        diagrams.forEach(registry::register);
        doReturn(diagrams).when(registry).getDiagrams();

        return new DiagramController(
                service,
                registry
        );
    }
}
