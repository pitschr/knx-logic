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
import li.pitschmann.knx.logic.components.Component;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.BaseDatabaseSuite;
import test.components.logic.NegationLogic;

import java.io.File;

import static li.pitschmann.knx.logic.uid.UIDFactory.createUid;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static test.TestHelpers.createInboxComponent;
import static test.TestHelpers.createLogicComponent;
import static test.TestHelpers.createOutboxComponent;

/**
 * Test for {@link ComponentService}
 */
class ComponentServiceTest extends BaseDatabaseSuite {

    @Test
    @DisplayName("Test #addComponent(Component) for Logic")
    void test_addComponent_Logic() {
        final var routerMock = mock(Router.class);
        final var service = new ComponentService(databaseManager, routerMock);

        // ----------------------
        // Add Logic Component
        // ----------------------
        final var notLogic = createLogicComponent(new NegationLogic());
        service.addComponent(notLogic);

        // verify if called
        verify(routerMock).register(notLogic);

        // verify if persisted to database
        assertThat(componentsDao().size()).isOne();
        assertThat(connectorsDao().size()).isEqualTo(2);
        assertThat(pinsDao().size()).isEqualTo(2);
        assertThat(eventKeyDao().size()).isZero();
    }

    @Test
    @DisplayName("Test #addComponent(Component) for Inbox")
    void test_addComponent_Inbox() {
        final var routerMock = mock(Router.class);
        final var service = new ComponentService(databaseManager, routerMock);

        // ----------------------
        // Add Inbox Component
        // ----------------------
        final var inbox = createInboxComponent("inboxKey");
        service.addComponent(inbox);

        // verify if called
        verify(routerMock).register(inbox);

        // verify if persisted to database
        assertThat(componentsDao().size()).isOne();
        assertThat(connectorsDao().size()).isOne();
        assertThat(pinsDao().size()).isOne();
        assertThat(eventKeyDao().size()).isOne();

        final var eventKey = eventKeyDao().getByComponentId(1);
        assertThat(eventKey.getKey()).isEqualTo("inboxKey");
        assertThat(eventKey.getChannel()).isEqualTo("var");
    }

    @Test
    @DisplayName("Test #addComponent(Component) for Outbox")
    void test_addComponent_Outbox() {
        final var routerMock = mock(Router.class);
        final var service = new ComponentService(databaseManager, routerMock);

        // ----------------------
        // Add Outbox Component
        // ----------------------
        final var outbox = createOutboxComponent("outboxKey");
        service.addComponent(outbox);

        // verify if called
        verify(routerMock).register(outbox);

        // verify if persisted to database
        assertThat(componentsDao().size()).isOne();
        assertThat(connectorsDao().size()).isOne();
        assertThat(pinsDao().size()).isOne();
        assertThat(eventKeyDao().size()).isOne();

        final var eventKey = eventKeyDao().getByComponentId(1);
        assertThat(eventKey.getKey()).isEqualTo("outboxKey");
        assertThat(eventKey.getChannel()).isEqualTo("var");
    }

    @Test
    @DisplayName("Test #removeComponent(Component)")
    void test_removeComponent() {
        executeSqlFile(new File("src/test/resources/sql/testCases/ComponentServiceTest-removeComponent.sql"));

        final var componentMock = mock(Component.class);
        when(componentMock.getUid()).thenReturn(createUid("uid-component-logic"));

        final var routerMock = mock(Router.class);
        final var service = new ComponentService(databaseManager, routerMock);

        assertThat(componentsDao().size()).isEqualTo(3);
        assertThat(componentsDao().find(1)).isNotNull();  // Inbox: IN
        assertThat(componentsDao().find(2)).isNotNull();  // Logic: NOT
        assertThat(componentsDao().find(3)).isNotNull();  // Outbox: OUT

        // ----------------------
        // Delete Component
        // ----------------------
        service.removeComponent(componentMock);

        // verify if called
        verify(routerMock).deregister(componentMock);

        // verify if logic component was deleted from database
        assertThat(componentsDao().size()).isEqualTo(2);
        assertThat(componentsDao().find(1)).isNotNull();
        assertThat(componentsDao().find(2)).isNull();
        assertThat(componentsDao().find(3)).isNotNull();
    }
}