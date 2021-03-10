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

package experimental.api.v1.services;

import li.pitschmann.knx.logic.Router;
import li.pitschmann.knx.logic.components.LogicComponent;
import li.pitschmann.knx.logic.connector.DynamicConnector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.BaseDatabaseSuite;

import java.io.File;

import static li.pitschmann.knx.logic.uid.UIDFactory.createUid;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Test for {@link ConnectorService}
 */
class ConnectorServiceTest extends BaseDatabaseSuite {

    @Test
    @DisplayName("Test #addPin(DynamicConnector)")
    void test_addPin_lastIndex() {
        executeSqlFile(new File("src/test/resources/sql/testCases/ConnectorServiceTest-joinerLogic.sql"));

        final var component = this.<LogicComponent>loadComponentById(1);
        final var connector = (DynamicConnector) component.getConnector("inputs");

        final var service = new ConnectorService(databaseManager, mock(Router.class));
        final var newPin = service.addPin(connector);

        assertThat(pinsDao().size()).isEqualTo(4);
        assertThat(pinsDao().find(1).getUid()).isEqualTo(createUid("uid-pin-logic-input[0]"));
        assertThat(pinsDao().find(2).getUid()).isEqualTo(createUid("uid-pin-logic-input[1]"));
        assertThat(pinsDao().find(3).getUid()).isEqualTo(createUid("uid-pin-logic-output"));

        // new pin --> before: N/A, after: index=2
        final var pinModel4 = pinsDao().find(4);
        assertThat(pinModel4.getUid()).isEqualTo(newPin.getUid()); // dynamic generated UID
        assertThat(pinModel4.getIndex()).isEqualTo(2);
    }

    @Test
    @DisplayName("Test #addPin(DynamicConnector, int)")
    void test_addPin_specificIndex() {
        executeSqlFile(new File("src/test/resources/sql/testCases/ConnectorServiceTest-joinerLogic.sql"));

        final var service = new ConnectorService(databaseManager, mock(Router.class));

        final var component = this.<LogicComponent>loadComponentById(1);
        final var connector = (DynamicConnector) component.getConnector("inputs");

        final var newPin = service.addPin(connector, 1); // add at 1st index

        assertThat(pinsDao().size()).isEqualTo(4);

        // uid-pin-logic-input[0] --> before: index=0, after: index=0
        final var pinModel1 = pinsDao().find(1);
        assertThat(pinModel1.getUid()).isEqualTo(createUid("uid-pin-logic-input[0]"));
        assertThat(pinModel1.getIndex()).isEqualTo(0);

        // uid-pin-logic-input[1] --> before: index=1, after: index=2
        final var pinModel2 = pinsDao().find(2);
        assertThat(pinModel2.getUid()).isEqualTo(createUid("uid-pin-logic-input[1]"));
        assertThat(pinModel2.getIndex()).isEqualTo(2);

        // uid-pin-logic-output --> no change (static: index=0)
        final var pinModel3 = pinsDao().find(3);
        assertThat(pinModel3.getUid()).isEqualTo(createUid("uid-pin-logic-output"));
        assertThat(pinModel3.getIndex()).isEqualTo(0);

        // new pin --> before: N/A, after: index=1
        final var pinModel4 = pinsDao().find(4);
        assertThat(pinModel4.getUid()).isEqualTo(newPin.getUid()); // dynamic generated UID
        assertThat(pinModel4.getIndex()).isEqualTo(1);
    }

    @Test
    @DisplayName("Test adding two pins and then #removePin(DynamicConnector, int)")
    void test_addPin_and_then_removePin() {
        executeSqlFile(new File("src/test/resources/sql/testCases/ConnectorServiceTest-joinerLogic.sql"));

        final var routerMock = mock(Router.class);
        final var service = new ConnectorService(databaseManager, routerMock);

        assertThat(pinsDao().size()).isEqualTo(3);
        assertThat(pinsDao().find(1)).isNotNull();
        assertThat(pinsDao().find(2)).isNotNull();
        assertThat(pinsDao().find(3)).isNotNull();

        final var component = this.<LogicComponent>loadComponentById(1);
        final var connector = (DynamicConnector) component.getConnector("inputs");

        // Action #1
        final var newPin = service.addPin(connector, 1);
        // Action #2
        final var newPin2 = service.addPin(connector, 1);
        // Action #3
        final var deletedPin = service.removePin(connector, 0);

        assertThat(pinsDao().size()).isEqualTo(4);

        // uid-pin-logic-input[0]
        // Action #1 --> no change
        // Action #2 --> no change
        // Action #3 --> deleted
        assertThat(pinsDao().find(1)).isNull();

        // uid-pin-logic-input[1]
        // Action #1 --> before: index=1, after: index=2
        // Action #2 -->                  after: index=3 (two pins have been added)
        // Action #3 -->                  after: index=2
        final var pinModel2 = pinsDao().find(2);
        assertThat(pinModel2.getUid()).isEqualTo(createUid("uid-pin-logic-input[1]"));
        assertThat(pinModel2.getIndex()).isEqualTo(2);

        // uid-pin-logic-output --> no change (static: index=0)
        final var pinModel3 = pinsDao().find(3);
        assertThat(pinModel3.getUid()).isEqualTo(createUid("uid-pin-logic-output"));
        assertThat(pinModel3.getIndex()).isEqualTo(0);

        // newPin
        // Action #1 --> before: N/A, after: index=1
        // Action #2 -->              after: index=2
        // Action #3 -->              after: index=1
        final var pinModel4 = pinsDao().find(4);
        assertThat(pinModel4.getUid()).isEqualTo(newPin.getUid()); // dynamic generated UID
        assertThat(pinModel4.getIndex()).isEqualTo(1);

        // newPin2
        // Action #1 --> N/A
        // Action #2 --> before: N/A, after: index=1
        // Action #3 -->              after: index=0
        final var pinModel5 = pinsDao().find(5);
        assertThat(pinModel5.getUid()).isEqualTo(newPin2.getUid()); // dynamic generated UID
        assertThat(pinModel5.getIndex()).isEqualTo(0);
    }

}