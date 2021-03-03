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

package li.pitschmann.knx.logic.db.loader;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.BaseDatabaseSuite;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test case for {@link OutboxComponentLoader}
 *
 * @author PITSCHR
 */
class OutboxComponentLoaderTest extends BaseDatabaseSuite {

    @Test
    @DisplayName("Outbox: KNX DPT-1 (bool value)")
    void testOutboxWithKnxDpt1() {
        executeSqlFile(new File(Sql.Outbox.DPT1));

        final var component = new OutboxComponentLoader(databaseManager).loadById(1);

        assertThat(component.getUid()).hasToString("uid-component-outbox-DPT1");
        assertThat(component.getInputConnectors()).hasSize(1);
        assertThat(component.getInputPins()).hasSize(1);
        assertThat(component.getInputPin("boolValue").getUid()).hasToString("uid-pin-outbox-DPT1#boolValue");

        // input specific
        assertThat(component.getEventKey().getChannel()).isEqualTo("knx");
        assertThat(component.getEventKey().getIdentifier()).isEqualTo("8711");
    }

    @Test
    @DisplayName("Outbox: KNX DPT-2 (controlled, bool value)")
    void testOutboxWithKnxDpt2() {
        executeSqlFile(new File(Sql.Outbox.DPT2));

        final var component = new OutboxComponentLoader(databaseManager).loadById(1);

        assertThat(component.getUid()).hasToString("uid-component-outbox-DPT2");
        assertThat(component.getInputConnectors()).hasSize(2);
        assertThat(component.getInputPins()).hasSize(2);
        assertThat(component.getInputPin("controlled").getUid()).hasToString("uid-pin-outbox-DPT2#controlled");
        assertThat(component.getInputPin("boolValue").getUid()).hasToString("uid-pin-outbox-DPT2#boolValue");

        // input specific
        assertThat(component.getEventKey().getChannel()).isEqualTo("knx");
        assertThat(component.getEventKey().getIdentifier()).isEqualTo("6733");
    }

    @Test
    @DisplayName("Outbox: VARIABLE (string)")
    void testOutboxWithVariableChannel() {
        executeSqlFile(new File(Sql.Outbox.VAR));

        final var component = new OutboxComponentLoader(databaseManager).loadById(1);

        assertThat(component.getUid()).hasToString("uid-component-outbox-VAR");
        assertThat(component.getInputConnectors()).hasSize(1);
        assertThat(component.getInputPins()).hasSize(1);
        assertThat(component.getInputPin("data").getUid()).hasToString("uid-pin-outbox-VAR#data");

        // input specific
        assertThat(component.getEventKey().getChannel()).isEqualTo("var");
        assertThat(component.getEventKey().getIdentifier()).isEqualTo("barfoo");
    }
}
