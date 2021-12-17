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

package li.pitschmann.knx.api;

import li.pitschmann.knx.logic.components.InboxComponentImpl;
import li.pitschmann.knx.logic.components.LogicComponentImpl;
import li.pitschmann.knx.logic.components.OutboxComponentImpl;
import li.pitschmann.knx.logic.event.KnxEventChannel;
import li.pitschmann.knx.logic.event.VariableEventChannel;
import li.pitschmann.knx.logic.exceptions.NoLogicClassFound;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test for {@link ComponentFactory}
 */
class ComponentFactoryTest {

    @Test
    @DisplayName("Test basic methods of ComponentFactory")
    void testComponentFactory() {
        final var factory = new ComponentFactory();

        assertThat(factory.getLogicRepository()).isNotNull();
    }

    @Test
    @DisplayName("Test createInbox() for Variable")
    void testCreateInboxForVariable() {
        final var factory = new ComponentFactory();
        final var data = Map.of("name", "variableFooBar-Inbox");
        final var inboxComponent = factory.createInbox(VariableEventChannel.CHANNEL_ID, data);

        assertThat(inboxComponent).isInstanceOf(InboxComponentImpl.class);
        assertThat(inboxComponent.getEventKey().getChannel()).isEqualTo(VariableEventChannel.CHANNEL_ID);
        assertThat(inboxComponent.getEventKey().getIdentifier()).isEqualTo("variableFooBar-Inbox");
    }

    @Test
    @DisplayName("Test createInbox() for KNX")
    void testCreateInboxForKNX() {
        final var factory = new ComponentFactory();
        final var data = Map.of("groupAddress", "1234");
        final var inboxComponent = factory.createInbox(KnxEventChannel.CHANNEL_ID, data);

        assertThat(inboxComponent).isInstanceOf(InboxComponentImpl.class);
        assertThat(inboxComponent.getEventKey().getChannel()).isEqualTo(KnxEventChannel.CHANNEL_ID);
        assertThat(inboxComponent.getEventKey().getIdentifier()).isEqualTo("1234");
    }

    @Test
    @DisplayName("Test createOutbox() for Variable")
    void testCreateOutboxForVariable() {
        final var factory = new ComponentFactory();
        final var data = Map.of("name", "variableFooBar-Outbox");
        final var outboxComponent = factory.createOutbox(VariableEventChannel.CHANNEL_ID, data);

        assertThat(outboxComponent).isInstanceOf(OutboxComponentImpl.class);
        assertThat(outboxComponent.getEventKey().getChannel()).isEqualTo(VariableEventChannel.CHANNEL_ID);
        assertThat(outboxComponent.getEventKey().getIdentifier()).isEqualTo("variableFooBar-Outbox");
    }

    @Test
    @DisplayName("Test createOutbox() for KNX")
    void testCreateOutboxForKNX() {
        final var factory = new ComponentFactory();
        final var data = Map.of("groupAddress", "4321");
        final var outboxComponent = factory.createOutbox(KnxEventChannel.CHANNEL_ID, data);

        assertThat(outboxComponent).isInstanceOf(OutboxComponentImpl.class);
        assertThat(outboxComponent.getEventKey().getChannel()).isEqualTo(KnxEventChannel.CHANNEL_ID);
        assertThat(outboxComponent.getEventKey().getIdentifier()).isEqualTo("4321");
    }

    @Test
    @DisplayName("Test createInbox() and createOutbox() for Unknown Type")
    void testCreateInboxOutboxUnknown() {
        final var factory = new ComponentFactory();

        assertThatThrownBy(() -> factory.createInbox("unknown", Map.of()))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> factory.createOutbox("unknown", Map.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Test createLogic()")
    void testCreateLogic() throws IOException {
        final var factory = new ComponentFactory();
        final var data = Map.of("class", "my.logic.MyFooBarLogic");

        // no logic classes loaded -> not found
        assertThatThrownBy(() -> factory.createLogic(data)).isInstanceOf(NoLogicClassFound.class);

        // load logic classes -> should find now!
        factory.getLogicRepository().scanLogicClasses(Paths.get("."));
        final var logicComponent = factory.createLogic(data);

        assertThat(logicComponent).isInstanceOf(LogicComponentImpl.class);
    }
}
