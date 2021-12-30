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

import experimental.api.v1.json.PinSetValueRequest;
import experimental.api.v1.services.PinService;
import li.pitschmann.knx.api.UIDRegistry;
import li.pitschmann.knx.logic.pin.Pin;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.TestHelpers;
import test.components.LogicI;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.Path;
import java.util.function.Consumer;

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
import static test.TestHelpers.createLogicComponent;

/**
 * Test for {@link PinController}
 */
class PinControllerTest {
    @BeforeAll
    static void setUp() {
        TestHelpers.initJavalinJson();
    }

    @Test
    @DisplayName("Endpoint: Get One Pin (Bad Request)")
    void testGetOne_BadRequest() {
        final var controller = newPinController();

        final var context = contextSpy();
        controller.getOne(context, "");

        verify(context).status(HttpServletResponse.SC_BAD_REQUEST);
        assertContextJsonErrorMessage(context, "No pin UID provided");
    }

    @Test
    @DisplayName("Endpoint: Get One Pin (Not Found)")
    void testGetOne_NotFound() {
        final var controller = newPinController();

        final var context = contextSpy();
        controller.getOne(context, "pin-does-not-exists");

        verify(context).status(HttpServletResponse.SC_NOT_FOUND);
        assertContextJsonErrorMessage(context, "No pin found with UID: pin-does-not-exists");
    }

    @Test
    @DisplayName("Endpoint: Get One Pin (Found)")
    void testGetOne_Found() {
        final var controller = newPinController();

        final var context = contextSpy();
        controller.getOne(context, "existing-pin");

        verify(context).status(HttpServletResponse.SC_OK);
        assertContextJsonResult(
                context,
                Path.of("responses/PinControllerTest-testGetOne_Found.json")
        );
    }

    @Test
    @DisplayName("Endpoint: Get Pin Value (Not Found)")
    void testGetValue_NotFound() {
        final var controller = newPinController();

        final var context = contextSpy();
        controller.getValue(context, "pin-get-value-does-not-exists");

        verify(context).status(HttpServletResponse.SC_NOT_FOUND);
        assertContextJsonErrorMessage(context, "No pin found with UID: pin-get-value-does-not-exists");
    }

    @Test
    @DisplayName("Endpoint: Get Pin Value (Found)")
    void testGetValue_OK() {
        final var controller = newPinController(
                mock(PinService.class),
                pin -> pin.setValue(4711)
        );

        final var context = contextSpy();
        controller.getValue(context, "existing-pin");

        verify(context).status(HttpServletResponse.SC_OK);
        assertContextJsonResult(
                context,
                Path.of("responses/PinControllerTest-testGetValue_OK.json")
        );
    }

    @Test
    @DisplayName("Endpoint: Set Pin Value (Not Found)")
    void testSetValue_NotFound() {
        final var controller = newPinController();

        final var context = contextSpy();
        controller.getValue(context, "pin-set-value-does-not-exists");

        verify(context).status(HttpServletResponse.SC_NOT_FOUND);
        assertContextJsonErrorMessage(context, "No pin found with UID: pin-set-value-does-not-exists");
    }

    @Test
    @DisplayName("Endpoint: Set Pin Value (Found)")
    void testSetValue_OK() {
        final var service = spy(new PinService());
        final var controller = newPinController(service, pin -> {
        });

        final var request = new PinSetValueRequest();
        request.setValue("1317");
        final var context = contextSpy();
        controller.setValue(context, "existing-pin", request);

        verify(context).status(HttpServletResponse.SC_ACCEPTED);
        verify(service).setValue(any(Pin.class), anyString());
        assertContextJsonResult(
                context,
                Path.of("responses/PinControllerTest-testSetValue_OK.json")
        );
    }

    @Test
    @DisplayName("Endpoint: Set Pin Value on Output Pin (Error)")
    void testSetValue_Err_OutputPin() {
        final var service = mock(PinService.class);
        final var controller = newPinController(service, pin -> {
        });

        final var request = new PinSetValueRequest();
        request.setValue("1317");
        final var context = contextSpy();
        controller.setValue(context, "outputFirst", request);

        verify(context).status(HttpServletResponse.SC_FORBIDDEN);
        assertContextJsonErrorMessage(context, "Pin is declared as an output pin, " +
                "and therefore not suitable to set the value: test.components.LogicI#outputFirst");
        verify(service, never()).setValue(any(Pin.class), anyString());
    }

    /*
     * Internal Test Method to create a new PinController
     */
    private PinController newPinController() {
        return newPinController(mock(PinService.class), pin -> {
        });
    }

    /*
     * Internal Test Method to create a new PinController with
     * given PinService for verification/mocking
     */
    private PinController newPinController(final PinService pinService, final Consumer<Pin> pinConsumer) {
        final var component = createLogicComponent(LogicI.class);
        final var pinInput = component.getPin("inputFirst");
        final var pinOutput = component.getPin("outputFirst");
        final var registrySpy = spy(new UIDRegistry());
        registrySpy.register(component);
        doReturn(pinInput).when(registrySpy).getPin("existing-pin");
        doReturn(pinInput).when(registrySpy).getPin("inputFirst");
        doReturn(pinOutput).when(registrySpy).getPin("outputFirst");
        pinConsumer.accept(pinInput);

        return new PinController(
                pinService,
                registrySpy
        );
    }
}
