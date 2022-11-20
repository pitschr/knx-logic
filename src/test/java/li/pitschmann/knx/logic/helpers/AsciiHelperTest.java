/*
 * Copyright (C) 2022 Pitschmann Christoph
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

package li.pitschmann.knx.logic.helpers;

import li.pitschmann.knx.logic.components.inbox.DPT10Inbox;
import li.pitschmann.knx.logic.components.outbox.DPT3Outbox;
import org.junit.jupiter.api.Test;
import test.TestHelpers;
import test.components.LogicF;
import test.components.RefreshAlwaysTriggerInputOnlyLogic;
import test.components.logic.JoinerLogic;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link AsciiHelper}
 */
class AsciiHelperTest {

    @Test
    void testInboxComponent() {
        var inbox = TestHelpers.createInboxComponent(new DPT10Inbox());
        var inboxAsAscii = AsciiHelper.toAsciiString(inbox);

        assertThat(inboxAsAscii).isEqualTo("" +
                ".------------.   " + System.lineSeparator() +
                "| DPT10Inbox |   " + System.lineSeparator() +
                "|  .---------+-. " + System.lineSeparator() +
                "|  | dayOfWeek |=" + System.lineSeparator() +
                "|  '---------+-' " + System.lineSeparator() +
                "|       .----+-. " + System.lineSeparator() +
                "|       | time |=" + System.lineSeparator() +
                "|       '----+-' " + System.lineSeparator() +
                "'------------'   " + System.lineSeparator()
        );
    }

    @Test
    void testOutboxComponent() {
        var outbox = TestHelpers.createOutboxComponent(new DPT3Outbox());
        var outboxAsAscii = AsciiHelper.toAsciiString(outbox);

        assertThat(outboxAsAscii).isEqualTo("" +
                "   .-------------." + System.lineSeparator() +
                "   | DPT3Outbox  |" + System.lineSeparator() +
                " .-+----------.  |" + System.lineSeparator() +
                "=| controlled |  |" + System.lineSeparator() +
                " '-+----------'  |" + System.lineSeparator() +
                " .-+------------.|" + System.lineSeparator() +
                "=| stepInterval ||" + System.lineSeparator() +
                " '-+------------'|" + System.lineSeparator() +
                "   '-------------'" + System.lineSeparator()
        );
    }

    @Test
    void testLogicFComponent() {
        var logic = TestHelpers.createLogicComponent(new LogicF());
        var logicAsAscii = AsciiHelper.toAsciiString(logic);

        assertThat(logicAsAscii).isEqualTo("" +
                "   .----------------------.   " + System.lineSeparator() +
                "   | LogicF               |   " + System.lineSeparator() +
                " .-+-----.         .------+-. " + System.lineSeparator() +
                "=| input |         | output |=" + System.lineSeparator() +
                " '-+-----'         '------+-' " + System.lineSeparator() +
                " .-+---------. .----------+-. " + System.lineSeparator() +
                "=| inputs[0] | | outputs[0] |=" + System.lineSeparator() +
                " '-+---------' '----------+-' " + System.lineSeparator() +
                "   '----------------------'   " + System.lineSeparator()
        );
    }

    @Test
    void testJoinerLogicComponent() {
        var logic = TestHelpers.createLogicComponent(new JoinerLogic());
        var logicAsAscii = AsciiHelper.toAsciiString(logic);

        assertThat(logicAsAscii).isEqualTo("" +
                "   .------------------.   " + System.lineSeparator() +
                "   | JoinerLogic      |   " + System.lineSeparator() +
                " .-+---------. .------+-. " + System.lineSeparator() +
                "=| inputs[0] | | output |=" + System.lineSeparator() +
                "=| inputs[1] | '------+-' " + System.lineSeparator() +
                " '-+---------'        |   " + System.lineSeparator() +
                "   '------------------'   " + System.lineSeparator()
        );
    }

    @Test
    void testLogicComponentWithLongName() {
        var logic = TestHelpers.createLogicComponent(new RefreshAlwaysTriggerInputOnlyLogic());
        var logicAsAscii = AsciiHelper.toAsciiString(logic);

        assertThat(logicAsAscii).isEqualTo("" +
                "   .------------------------------------.   " + System.lineSeparator() +
                "   | RefreshAlwaysTriggerInputOnlyLogic |   " + System.lineSeparator() +
                " .-+-----.                       .------+-. " + System.lineSeparator() +
                "=| input |                       | output |=" + System.lineSeparator() +
                " '-+-----'                       '------+-' " + System.lineSeparator() +
                "   |                            .-------+-. " + System.lineSeparator() +
                "   |                            | output2 |=" + System.lineSeparator() +
                "   |                            '-------+-' " + System.lineSeparator() +
                "   '------------------------------------'   " + System.lineSeparator()
        );
    }
}
