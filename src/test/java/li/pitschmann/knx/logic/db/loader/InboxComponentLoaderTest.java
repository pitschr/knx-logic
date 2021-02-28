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
 * Test case for {@link InboxComponentLoader}
 *
 * @author PITSCHR
 */
class InboxComponentLoaderTest extends BaseDatabaseSuite {

    @Test
    @DisplayName("Inbox: KNX DPT-1 (bool value)")
    void testInboxWithKnxDpt1() {
        executeSqlFile(new File(Sql.Inbox.DPT1));

        final var component = new InboxComponentLoader(databaseManager).loadById(1);

        assertThat(component.getUid()).hasToString("uid-component-inbox-DPT1");
        assertThat(component.getOutputConnectors()).hasSize(1);
        assertThat(component.getOutputPins()).hasSize(1);
        assertThat(component.getOutputPin("boolValue").getUid()).hasToString("uid-pin-inbox-DPT1#boolValue");

        // input specific
        assertThat(component.getEventKey().getChannel()).isEqualTo("knx");
        assertThat(component.getEventKey().getIdentifier()).isEqualTo("4711");
    }

    @Test
    @DisplayName("Inbox: KNX DPT-2 (controlled, bool value)")
    void testInboxWithKnxDpt2() {
        executeSqlFile(new File(Sql.Inbox.DPT2));

        final var component = new InboxComponentLoader(databaseManager).loadById(1);

        assertThat(component.getUid()).hasToString("uid-component-inbox-DPT2");
        assertThat(component.getOutputConnectors()).hasSize(2);
        assertThat(component.getOutputPins()).hasSize(2);
        assertThat(component.getOutputPin("controlled").getUid()).hasToString("uid-pin-inbox-DPT2#controlled");
        assertThat(component.getOutputPin("boolValue").getUid()).hasToString("uid-pin-inbox-DPT2#boolValue");

        // input specific
        assertThat(component.getEventKey().getChannel()).isEqualTo("knx");
        assertThat(component.getEventKey().getIdentifier()).isEqualTo("3171");
    }

    @Test
    @DisplayName("Inbox: VARIABLE (string)")
    void testInboxWithVariableChannel() {
        executeSqlFile(new File(Sql.Inbox.VAR));

        final var component = new InboxComponentLoader(databaseManager).loadById(1);

        assertThat(component.getUid()).hasToString("uid-component-inbox-VAR");
        assertThat(component.getOutputConnectors()).hasSize(1);
        assertThat(component.getOutputPins()).hasSize(1);
        assertThat(component.getOutputPin("data").getUid()).hasToString("uid-pin-inbox-VAR#data");

        // input specific
        assertThat(component.getEventKey().getChannel()).isEqualTo("var");
        assertThat(component.getEventKey().getIdentifier()).isEqualTo("foobar");
    }
}
