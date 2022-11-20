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

package li.pitschmann.knx.api.v1.services;

import li.pitschmann.knx.logic.Router;
import li.pitschmann.knx.logic.pin.Pin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.BaseDatabaseSuite;

import java.io.File;
import java.util.List;

import static li.pitschmann.knx.logic.uid.UIDFactory.createUid;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for {@link LinkService}
 */
class LinkServiceTest extends BaseDatabaseSuite {

    @Test
    @DisplayName("Test #addLink(Pin, Pin)")
    void test_addLink() {
        executeSqlFile(new File("src/test/resources/sql/testCases/LinkServiceTest-addLink.sql"));

        final var routerMock = mock(Router.class);
        final var linkService = new LinkService(databaseManager, routerMock);

        final var sourcePinMock = mock(Pin.class);
        when(sourcePinMock.getUid()).thenReturn(createUid("uid-pin-inbox-1"));
        final var targetPinMock = mock(Pin.class);
        when(targetPinMock.getUid()).thenReturn(createUid("uid-pin-outbox-1"));

        // Create Link
        // IN 1 (pin.id: 1) -> OUT 1 (pin.id: 3)
        linkService.addLink(sourcePinMock, targetPinMock);

        // Router#link(..) called
        verify(routerMock).link(eq(sourcePinMock), eq(targetPinMock));

        // DB table should contain one entry
        assertThat(pinLinksDao().size()).isOne();
        final var pinModel = pinLinksDao().find(1);
        assertThat(pinModel.getPin1()).isEqualTo(1);
        assertThat(pinModel.getPin2()).isEqualTo(3);
    }

    @Test
    @DisplayName("Test #addLink(Pin, Pin) with unknown id in database")
    void test_addLink_UnknownUID() {
        executeSqlFile(new File("src/test/resources/sql/testCases/LinkServiceTest-addLink.sql"));

        final var routerMock = mock(Router.class);
        final var linkService = new LinkService(databaseManager, routerMock);

        final var inboxPinMock = mock(Pin.class);
        when(inboxPinMock.getUid()).thenReturn(createUid("uid-pin-inbox-1"));
        final var fooPinMock = mock(Pin.class);
        when(fooPinMock.getUid()).thenReturn(createUid("foo"));

        assertThatThrownBy(() -> linkService.addLink(fooPinMock, inboxPinMock))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Could not find id for source pin 'foo'. Not persisted yet?");
        assertThatThrownBy(() -> linkService.addLink(inboxPinMock, fooPinMock))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Could not find id for target pin 'foo'. Not persisted yet?");

        // Router link never called
        verify(routerMock, never()).link(any(Pin.class), any(Pin.class));

        // DB table should be still empty
        assertThat(pinLinksDao().size()).isZero();
    }

    @Test
    @DisplayName("Test #deleteLink(Pin, Pin)")
    void test_deleteLink() {
        executeSqlFile(new File("src/test/resources/sql/testCases/LinkServiceTest-deleteLink.sql"));

        final var routerMock = mock(Router.class);
        final var linkService = new LinkService(databaseManager, routerMock);

        assertThat(pinLinksDao().size()).isEqualTo(3);
        assertThat(pinLinksDao().find(1)).isNotNull();
        assertThat(pinLinksDao().find(2)).isNotNull();
        assertThat(pinLinksDao().find(3)).isNotNull();

        // -------------------------------------
        // Delete Link
        // IN 1 (pin.id: 1) -> OUT 2 (pin.id: 4)
        // -------------------------------------
        final var inbox1PinMock = mock(Pin.class);
        when(inbox1PinMock.getUid()).thenReturn(createUid("uid-pin-inbox-1"));
        final var outbox2PinMock = mock(Pin.class);
        when(outbox2PinMock.getUid()).thenReturn(createUid("uid-pin-outbox-2"));

        linkService.deleteLink(inbox1PinMock, outbox2PinMock);

        // Router#unlink(Pin, Pin) called
        verify(routerMock).unlink(any(Pin.class), any(Pin.class));

        // DB table should now have only two rows (before three rows)
        assertThat(pinLinksDao().size()).isEqualTo(2);
        assertThat(pinLinksDao().find(1)).isNotNull();
        assertThat(pinLinksDao().find(2)).isNull();
        assertThat(pinLinksDao().find(3)).isNotNull();

        // -------------------------------------
        // Delete Link
        // OUT 3 (pin.id: 5) -> IN 2 (pin.id: 2)
        // -------------------------------------
        final var inbox2PinMock = mock(Pin.class);
        when(inbox2PinMock.getUid()).thenReturn(createUid("uid-pin-inbox-2"));
        final var outbox3PinMock = mock(Pin.class);
        when(outbox3PinMock.getUid()).thenReturn(createUid("uid-pin-outbox-3"));

        linkService.deleteLink(outbox3PinMock, inbox2PinMock);

        // Router#unlink(Pin, Pin) called
        verify(routerMock, times(2)).unlink(any(Pin.class), any(Pin.class));

        // DB table should now have only one row
        assertThat(pinLinksDao().size()).isEqualTo(1);
        assertThat(pinLinksDao().find(1)).isNotNull();
        assertThat(pinLinksDao().find(2)).isNull();
        assertThat(pinLinksDao().find(3)).isNull();
    }

    @Test
    @DisplayName("Test #deleteLinks(Pin)")
    void test_deleteLinks() {
        executeSqlFile(new File("src/test/resources/sql/testCases/LinkServiceTest-deleteLink.sql"));

        final var routerMock = mock(Router.class);
        final var linkService = new LinkService(databaseManager, routerMock);

        assertThat(pinLinksDao().size()).isEqualTo(3);
        assertThat(pinLinksDao().find(1)).isNotNull();
        assertThat(pinLinksDao().find(2)).isNotNull();
        assertThat(pinLinksDao().find(3)).isNotNull();

        // -------------------------------------
        // Delete Links
        // IN 1 (pin.id: 1) -> OUT 1 (pin.id: 3)
        //                  -> OUT 2 (pin.id: 4)
        // -------------------------------------
        final var inbox1PinMock = mock(Pin.class);
        when(inbox1PinMock.getUid()).thenReturn(createUid("uid-pin-inbox-1"));

        linkService.deleteLinks(inbox1PinMock);

        // Router#unlink(Pin) called
        verify(routerMock).unlink(any(Pin.class));

        // DB table should now have only one row (before three rows)
        assertThat(pinLinksDao().size()).isEqualTo(1);
        assertThat(pinLinksDao().find(1)).isNull();
        assertThat(pinLinksDao().find(2)).isNull();
        assertThat(pinLinksDao().find(3)).isNotNull();

        // -------------------------------------
        // Delete Links
        // IN 2 (pin.id: 2) -> OUT 3 (pin.id: 5)
        // -------------------------------------
        final var inbox2PinMock = mock(Pin.class);
        when(inbox2PinMock.getUid()).thenReturn(createUid("uid-pin-inbox-2"));

        linkService.deleteLinks(inbox2PinMock);

        // Router#unlink(Pin) called 2nd time
        verify(routerMock, times(2)).unlink(any(Pin.class));

        // DB table should now be empty
        assertThat(pinLinksDao().size()).isZero();
    }

    @Test
    @DisplayName("Test #getLinkUIDs(Pin) with one pin linked to two pins")
    void test_getLinkUIDs_Inbox() {
        final var pin1Mock = mock(Pin.class);
        when(pin1Mock.getUid()).thenReturn(createUid("UID-1"));
        final var pin2Mock = mock(Pin.class);
        when(pin2Mock.getUid()).thenReturn(createUid("UID-2"));

        final var routerMock = mock(Router.class);
        when(routerMock.findLinkedPins(any(Pin.class))).thenReturn(List.of(pin1Mock, pin2Mock));

        final var linkService = new LinkService(databaseManager, routerMock);

        final var uids = linkService.getLinkedUIDs(mock(Pin.class));
        assertThat(uids).containsExactly(
                createUid("UID-1"),
                createUid("UID-2")
        );
    }
}
