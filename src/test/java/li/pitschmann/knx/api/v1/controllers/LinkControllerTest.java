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
import li.pitschmann.knx.api.v1.services.LinkService;
import li.pitschmann.knx.logic.pin.Pin;
import li.pitschmann.knx.logic.uid.UIDFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.TestHelpers;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.Path;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static test.TestHelpers.assertContextHasNoResponse;
import static test.TestHelpers.assertContextJsonErrorMessage;
import static test.TestHelpers.assertContextJsonResult;
import static test.TestHelpers.contextSpy;

/**
 * Test for {@link PinController}
 */
class LinkControllerTest {
    @BeforeAll
    static void setUp() {
        TestHelpers.initJavalinJson();
    }

    @Test
    @DisplayName("Endpoint: Add Link - No Source Pin UID Provided")
    void testAddLink_Source_BadRequest() {
        final var service = mock(LinkService.class);
        final var controller = newLinkController(service);

        final var context = contextSpy();
        controller.addLink(context, "", "");

        verify(context).status(HttpServletResponse.SC_BAD_REQUEST);
        assertContextJsonErrorMessage(context, "No Source Pin UID provided");
    }

    @Test
    @DisplayName("Endpoint: Add Link - Source Pin Not Found")
    void testAddLink_Source_NotFound() {
        final var service = mock(LinkService.class);
        final var controller = newLinkController(service);

        final var context = contextSpy();
        controller.addLink(context, "source-pin-does-not-exists", "");

        verify(context).status(HttpServletResponse.SC_NOT_FOUND);
        assertContextJsonErrorMessage(context, "No Source Pin found with UID: source-pin-does-not-exists");
        verify(service, never()).addLink(any(Pin.class), any(Pin.class));
    }

    @Test
    @DisplayName("Endpoint: Add Link - No Target Pin UID Provided")
    void testAddLink_Target_BadRequest() {
        final var service = mock(LinkService.class);
        final var controller = newLinkController(service);

        final var context = contextSpy();
        controller.addLink(context, "source-pin", "");

        verify(context).status(HttpServletResponse.SC_BAD_REQUEST);
        assertContextJsonErrorMessage(context, "No Target Pin UID provided");
        verify(service, never()).addLink(any(Pin.class), any(Pin.class));
    }

    @Test
    @DisplayName("Endpoint: Add Link - Target Pin Not Found")
    void testAddLink_Target_NotFound() {
        final var service = mock(LinkService.class);
        final var controller = newLinkController(service);

        final var context = contextSpy();
        controller.addLink(context, "source-pin", "target-pin-does-not-exists");

        verify(context).status(HttpServletResponse.SC_NOT_FOUND);
        assertContextJsonErrorMessage(context, "No Target Pin found with UID: target-pin-does-not-exists");
        verify(service, never()).addLink(any(Pin.class), any(Pin.class));
    }

    @Test
    @DisplayName("Endpoint: Add Link (OK)")
    void testAddLink_OK() {
        final var service = mock(LinkService.class);
        final var controller = newLinkController(service);

        final var context = contextSpy();
        controller.addLink(context, "source-pin", "target-pin");

        verify(context).status(HttpServletResponse.SC_NO_CONTENT);
        assertContextHasNoResponse(context);
        verify(service).addLink(any(Pin.class), any(Pin.class));
    }

    @Test
    @DisplayName("Endpoint: Delete Link - No Source Pin UID Provided")
    void testDeleteLink_Source_BadRequest() {
        final var service = mock(LinkService.class);
        final var controller = newLinkController(service);

        final var context = contextSpy();
        controller.deleteLink(context, "", "");

        verify(context).status(HttpServletResponse.SC_BAD_REQUEST);
        assertContextJsonErrorMessage(context, "No Source Pin UID provided");
        verify(service, never()).deleteLink(any(Pin.class), any(Pin.class));
    }

    @Test
    @DisplayName("Endpoint: Delete Link - Source Pin Not Found")
    void testDeleteLink_Source_NotFound() {
        final var service = mock(LinkService.class);
        final var controller = newLinkController(service);

        final var context = contextSpy();
        controller.deleteLink(context, "source-pin-does-not-exists", "");

        verify(context).status(HttpServletResponse.SC_NOT_FOUND);
        assertContextJsonErrorMessage(context, "No Source Pin found with UID: source-pin-does-not-exists");
        verify(service, never()).deleteLink(any(Pin.class), any(Pin.class));
    }

    @Test
    @DisplayName("Endpoint: Delete Link - No Target Pin UID Provided")
    void testDeleteLink_Target_BadRequest() {
        final var service = mock(LinkService.class);
        final var controller = newLinkController(service);

        final var context = contextSpy();
        controller.deleteLink(context, "source-pin", "");

        verify(context).status(HttpServletResponse.SC_BAD_REQUEST);
        assertContextJsonErrorMessage(context, "No Target Pin UID provided");
        verify(service, never()).deleteLink(any(Pin.class), any(Pin.class));
    }

    @Test
    @DisplayName("Endpoint: Delete Link - Target Pin Not Found")
    void testDeleteLink_Target_NotFound() {
        final var service = mock(LinkService.class);
        final var controller = newLinkController(service);

        final var context = contextSpy();
        controller.deleteLink(context, "source-pin", "target-pin-does-not-exists");

        verify(context).status(HttpServletResponse.SC_NOT_FOUND);
        assertContextJsonErrorMessage(context, "No Target Pin found with UID: target-pin-does-not-exists");
        verify(service, never()).deleteLink(any(Pin.class), any(Pin.class));
    }

    @Test
    @DisplayName("Endpoint: Delete Link (OK)")
    void testDeleteLink_OK() {
        final var service = mock(LinkService.class);
        final var controller = newLinkController(service);

        final var context = contextSpy();
        controller.deleteLink(context, "source-pin", "target-pin");

        verify(context).status(HttpServletResponse.SC_NO_CONTENT);
        assertContextHasNoResponse(context);
        verify(service).deleteLink(any(Pin.class), any(Pin.class));
    }

    @Test
    @DisplayName("Endpoint: Delete Links - Pin Not Found")
    void testDeleteLinks_NotFound() {
        final var service = mock(LinkService.class);
        final var controller = newLinkController(service);

        final var context = contextSpy();
        controller.deleteLinks(context, "pin-does-not-exists");

        verify(context).status(HttpServletResponse.SC_NOT_FOUND);
        assertContextJsonErrorMessage(context, "No Pin found with UID: pin-does-not-exists");
        verify(service, never()).deleteLinks(any(Pin.class));
    }

    @Test
    @DisplayName("Endpoint: Delete Links (OK)")
    void testDeleteLinks_OK() {
        final var service = mock(LinkService.class);
        final var controller = newLinkController(service);

        final var context = contextSpy();
        controller.deleteLinks(context, "source-pin");

        verify(context).status(HttpServletResponse.SC_NO_CONTENT);
        assertContextHasNoResponse(context);
        verify(service).deleteLinks(any(Pin.class));
    }

    @Test
    @DisplayName("Endpoint: Get Links - Pin Not Found")
    void testGetLinks_NotFound() {
        final var service = mock(LinkService.class);
        final var controller = newLinkController(service);

        final var context = contextSpy();
        controller.getLinks(context, "pin-does-not-exists");

        verify(context).status(HttpServletResponse.SC_NOT_FOUND);
        assertContextJsonErrorMessage(context, "No Pin found with UID: pin-does-not-exists");
        verify(service, never()).getLinkedUIDs(any(Pin.class));
    }

    @Test
    @DisplayName("Endpoint: Get Links (OK)")
    void testGetLinks_OK() {
        final var service = mock(LinkService.class);
        doReturn(List.of(
                UIDFactory.createUid("dummy-pin-1"),
                UIDFactory.createUid("dummy-pin-2"),
                UIDFactory.createUid("dummy-pin-3"))
        ).when(service).getLinkedUIDs(any(Pin.class));
        final var controller = newLinkController(service);

        final var context = contextSpy();
        controller.getLinks(context, "source-pin");

        verify(context).status(HttpServletResponse.SC_OK);
        assertContextJsonResult(
                context,
                Path.of("responses/LinkControllerTest-testGetLinks_OK.json")
        );
    }

    /*
     * Internal Test Method to create a new LinkController with
     * given LinkService for verification/mocking
     */
    private LinkController newLinkController(final LinkService linkService) {
        final var registryMock = mock(UIDRegistry.class);
        doReturn(mock(Pin.class)).when(registryMock).getPin("source-pin");
        doReturn(mock(Pin.class)).when(registryMock).getPin("target-pin");

        return new LinkController(
                linkService,
                registryMock
        );
    }
}
