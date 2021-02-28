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

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.components.Component;
import li.pitschmann.knx.logic.connector.Connector;
import li.pitschmann.knx.logic.connector.StaticConnector;
import li.pitschmann.knx.logic.db.jdbi.mappers.BindingType;
import li.pitschmann.knx.logic.db.models.ComponentModel;
import li.pitschmann.knx.logic.db.models.ConnectorModel;
import li.pitschmann.knx.logic.descriptor.FieldDescriptor;
import li.pitschmann.knx.logic.exceptions.LoaderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.BaseDatabaseSuite;
import test.components.LogicA;
import test.components.LogicB;
import test.components.LogicNoNonArg;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static test.assertions.model.DatabaseAssertions.assertThat;

//import li.pitschmann.knx.logic.db.loader.AbstractComponentLoader;

/**
 * Test for {@link AbstractComponentLoader}
 */
class AbstractComponentLoaderTest extends BaseDatabaseSuite {

    @Test
    @DisplayName("Test load a class from string and cast to correct class")
    void testLoadAndCast() {
        final var loader = new TestComponentLoader();
        assertThat(loader.loadClassAndCast("test.components.LogicA", Logic.class))
                .isInstanceOf(LogicA.class);
    }

    @Test
    @DisplayName("Load a non-existing class")
    void testLoadNonExistingClass() {
        final var loader = new TestComponentLoader();
        assertThatThrownBy(() -> loader.loadClassAndCast("test.components.FooBar", Logic.class))
                .isInstanceOf(LoaderException.class)
                .hasMessage("Could not find class: test.components.FooBar");
    }

    @Test
    @DisplayName("Load an interface")
    void testLoadInterface() {
        final var loader = new TestComponentLoader();
        assertThatThrownBy(() -> loader.loadClassAndCast("li.pitschmann.knx.logic.Logic", Logic.class))
                .isInstanceOf(LoaderException.class)
                .hasMessage("Is class an interface or has no non-arg constructor? " +
                        "Could not load class: li.pitschmann.knx.logic.Logic");
    }

    @Test
    @DisplayName("Load a class that has no non-arg constructor")
    void testLoadNoNonArgConstructor() {
        final var loader = new TestComponentLoader();
        assertThatThrownBy(() -> loader.loadClassAndCast("test.components.LogicNoNonArg", LogicNoNonArg.class))
                .isInstanceOf(LoaderException.class)
                .hasMessage("Is class an interface or has no non-arg constructor? " +
                        "Could not load class: test.components.LogicNoNonArg");
    }

    @Test
    @DisplayName("Load a class from string being incompatible with expected class")
    void testLoadAndCastWrong() {
        final var loader = new TestComponentLoader();
        assertThatThrownBy(() -> loader.loadClassAndCast("test.components.LogicA", LogicB.class))
                .isInstanceOf(InstantiationError.class)
                .hasMessage("Class 'test.components.LogicA' doesn't implements/extends: class test.components.LogicB");
    }

    @Test
    @DisplayName("Wrong cardinality when size of connector and connectorModel doesn't match")
    void testCheckConnectorWrongCardinality() {
        final var connectors = List.of(mock(Connector.class));
        final var connectorModels = List.of(mock(ConnectorModel.class), mock(ConnectorModel.class));

        final var loader = new TestComponentLoader();
        assertThatThrownBy(() -> loader.updateConnectors(connectors, connectorModels))
                .isInstanceOf(LoaderException.class)
                .hasMessage("Size of input connector doesn't match (class: 1, database: 2)");
    }


    @Test
    @DisplayName("[ERROR] Update static ConnectorModel with more than 1 pin")
    void testStaticConnectorModelWithMultiplePins() {
        executeSqlFile(new File(Sql.ErrorCases.STATIC_CONNECTOR_MODEL_MULTIPLE_PINS));

        final var descriptorMock = mock(FieldDescriptor.class);
        final var connectorMock = mock(StaticConnector.class);
        when(connectorMock.getDescriptor()).thenReturn(descriptorMock);
        when(descriptorMock.getName()).thenReturn("fooName");

        // connector model has multiple pins
        final var connectors = List.of(connectorMock);
        final var connectorModel = connectorsDao().getById(1);
        final var connectorModels = List.of(connectorModel);

        final var loader = new TestComponentLoader();
        assertThatThrownBy(() -> loader.updateConnectors(connectors, connectorModels))
                .isInstanceOf(LoaderException.class)
                .hasMessageStartingWith("Data integrity issue: 3 pins fetched although connector is static:");
    }

    @Test
    @DisplayName("[ERROR] Update Connector and ConnectorModel with incompatible connector types")
    void testUpdateWithIncompatibleConnectorTypes() {
        executeSqlFile(new File(Sql.ErrorCases.DYNAMIC_CONNECTOR_MODEL_STATIC_CONNECTOR));

        // connector is static
        final var descriptorMock = mock(FieldDescriptor.class);
        final var connectorMock = mock(StaticConnector.class);
        when(connectorMock.getDescriptor()).thenReturn(descriptorMock);
        when(descriptorMock.getName()).thenReturn("barName");

        // connector model is dynamic
        final var connectors = List.of(connectorMock);
        final var connectorModel = connectorsDao().getById(1);
        final var connectorModels = List.of(connectorModel);

        final var loader = new TestComponentLoader();
        assertThatThrownBy(() -> loader.updateConnectors(connectors, connectorModels))
                .isInstanceOf(LoaderException.class)
                .hasMessage("Incompatible match for connector and connectorModel: " +
                        "barName [connector(name)=StaticConnector, connectorModel(bindingType)=DYNAMIC]");
    }

    @Test
    @DisplayName("No matching name in connector and connectorModel")
    void testUpdateNoMatchingName() {
        // connector
        final var descriptorMock = mock(FieldDescriptor.class);
        final var connectorMock = mock(StaticConnector.class);
        when(connectorMock.getDescriptor()).thenReturn(descriptorMock);
        when(descriptorMock.getName()).thenReturn("FOO");

        // connector model with different name
        final var connectorModelMock = mock(ConnectorModel.class);
        when(connectorModelMock.getConnectorName()).thenReturn("BAR");
        when(connectorModelMock.getBindingType()).thenReturn(BindingType.STATIC);


        final var connectors = List.of(connectorMock);
        final var connectorModels = List.of(connectorModelMock);

        final var loader = new TestComponentLoader();
        assertThatThrownBy(() -> loader.updateConnectors(connectors, connectorModels))
                .isInstanceOf(LoaderException.class)
                .hasMessage("No connector model match found for 'FOO' in: [BAR]");
    }

    private static class TestComponentLoader extends AbstractComponentLoader<Component> {
        private TestComponentLoader() {
            super(AbstractComponentLoaderTest.databaseManager);
        }

        @Override
        public Component load(final ComponentModel model) {
            throw new UnsupportedOperationException(); // not subject to be tested here
        }
    }

}
