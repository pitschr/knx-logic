package li.pitschmann.knx.logic.db;

import li.pitschmann.knx.logic.components.Component;
import li.pitschmann.knx.logic.db.dao.ComponentsDao;
import li.pitschmann.knx.logic.db.dao.ConnectorsDao;
import li.pitschmann.knx.logic.db.dao.PinsDao;
import li.pitschmann.knx.logic.db.jdbi.mappers.BindingType;
import li.pitschmann.knx.logic.db.models.ComponentModel;
import li.pitschmann.knx.logic.db.models.ConnectorModel;
import li.pitschmann.knx.logic.pin.PinAware;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.BaseDatabaseSuite;
import test.assertions.model.DatabaseAssertions;
import test.components.LogicA;
import test.components.LogicH;
import test.components.LogicJ;

import static test.TestHelpers.createLogicComponent;
import static test.assertions.model.DatabaseAssertions.assertThat;

/**
 * Test case for writing logic components to database
 *
 * @author PITSCHR
 */
class ComponentsWriteToDatabaseTest extends BaseDatabaseSuite {

    /**
     * Saves an empty logic instance to database
     */
    @Test
    void saveEmptyLogicInstance() {
        final var componentsDao = componentsDao();
        final var connectorsDao = connectorsDao();
        final var pinsDao = pinsDao();

        // given component to be stored to DB
        final var logic = createLogicComponent(LogicA.class);

        // get size of tables before writing
        final int componentsSizeBefore = componentsDao.size();
        final int connectorsSizeBefore = connectorsDao.size();
        final int fieldsSizeBefore = pinsDao.size();

        // write to database
        final int newComponentId = save(logic);

        // ------------------------------------
        // verify table: Components
        // ------------------------------------
        // ONLY ONE NEW ENTRY
        assertThat(dao(ComponentsDao.class).size()).isEqualTo(componentsSizeBefore + 1);
        final var model = componentsDao.getById(newComponentId);
        DatabaseAssertions.assertThat(model).isNotNull().id(newComponentId).uid(logic).className(LogicA.class);

        // ------------------------------------
        // verify table: Connectors
        // ------------------------------------
        // NO NEW ENTRY
        assertThat(dao(ConnectorsDao.class).size()).isEqualTo(connectorsSizeBefore);

        // ------------------------------------
        // verify table: Pins
        // ------------------------------------
        // NO NEW ENTRY
        assertThat(dao(PinsDao.class).size()).isEqualTo(fieldsSizeBefore);

    }

    /**
     * Saves a simple logic instance containing Input/Output only to database
     *
     * @see LogicJ
     */
    @Test
    void saveSimpleLogic() {
        final var componentsDao = componentsDao();
        final var connectorsDao = connectorsDao();

        // given component to be stored to DB
        final var logic = createLogicComponent(LogicJ.class);

        // get size of tables before writing
        final var componentsSizeBefore = componentsDao.size();
        final var connectorsSizeBefore = connectorsDao.size();

        // write to database
        final int newComponentId = save(logic);

        // ------------------------------------
        // verify table: Components
        // ------------------------------------
        assertThat(dao(ComponentsDao.class).size()).isEqualTo(componentsSizeBefore + 1);
        final var model = componentsDao.getById(newComponentId);
        assertThat(model).isNotNull().id(newComponentId).uid(logic).className(LogicJ.class);

        // ------------------------------------
        // verify table: Connectors
        // ------------------------------------
        assertThat(dao(ConnectorsDao.class).size()).isEqualTo(connectorsSizeBefore + 4);
        final var connectors = connectorsDao.getByComponentId(model.getId());
        assertThat(connectors).hasSize(4); // inputFirst, inputSecond, outputFirst and outputSecond

        assertStaticConnectors(connectors.get(0), model, "inputFirst");
        assertStaticConnectors(connectors.get(1), model, "inputSecond");
        assertStaticConnectors(connectors.get(2), model, "outputFirst");
        assertStaticConnectors(connectors.get(3), model, "outputSecond");

        // ------------------------------------
        // verify table: Pins
        // ------------------------------------
        assertStaticPins(logic, connectors.get(0));
        assertStaticPins(logic, connectors.get(1));
        assertStaticPins(logic, connectors.get(2));
        assertStaticPins(logic, connectors.get(3));
    }

    @Test
    @DisplayName("Save complex Logic Component to database")
    void saveComplexLogic() {
        final var componentsDao = componentsDao();
        final var connectorsDao = connectorsDao();
        final var pinsDao = pinsDao();

        // given component to be stored to DB
        final var logic = createLogicComponent(LogicH.class);

        // get size of tables before writing
        final var componentsSizeBefore = componentsDao.size();
        final var connectorsSizeBefore = connectorsDao.size();
        final var fieldsSizeBefore = pinsDao.size();

        // write to database
        final int newComponentId = save(logic);

        // ------------------------------------
        // verify table: Components
        // ------------------------------------
        assertThat(dao(ComponentsDao.class).size()).isEqualTo(componentsSizeBefore + 1);
        final var model = componentsDao.getById(newComponentId);
        assertThat(model).isNotNull().id(newComponentId).uid(logic).className(LogicH.class);

        // ------------------------------------
        // verify table: Connectors
        // ------------------------------------
        assertThat(dao(ConnectorsDao.class).size()).isEqualTo(connectorsSizeBefore + 17);
        final var connectors = connectorsDao.getByComponentId(model.getId());
        assertThat(connectors).hasSize(17); // LogicH: 8 inputs, 9 outputs = 17

        assertStaticConnectors(connectors.get(0), model, "inputBooleanPrimitive");
        assertStaticConnectors(connectors.get(1), model, "inputBytePrimitive");
        assertStaticConnectors(connectors.get(2), model, "inputCharacterPrimitive");
        assertStaticConnectors(connectors.get(3), model, "inputDoublePrimitive");
        assertStaticConnectors(connectors.get(4), model, "inputFloatPrimitive");
        assertStaticConnectors(connectors.get(5), model, "inputIntegerPrimitive");
        assertStaticConnectors(connectors.get(6), model, "inputLongPrimitive");
        assertStaticConnectors(connectors.get(7), model, "inputShortPrimitive");
        assertStaticConnectors(connectors.get(8), model, "outputBooleanObject");
        assertStaticConnectors(connectors.get(9), model, "outputByteObject");
        assertStaticConnectors(connectors.get(10), model, "outputCharacterObject");
        assertStaticConnectors(connectors.get(11), model, "outputDoubleObject");
        assertStaticConnectors(connectors.get(12), model, "outputFloatObject");
        assertStaticConnectors(connectors.get(13), model, "outputIntegerObject");
        assertStaticConnectors(connectors.get(14), model, "outputLongObject");
        assertStaticConnectors(connectors.get(15), model, "outputShortObject");
        assertStaticConnectors(connectors.get(16), model, "outputString");

        // ------------------------------------
        // verify table: Pins
        // ------------------------------------
        assertThat(dao(PinsDao.class).size()).isEqualTo(fieldsSizeBefore + 17);

        // 1) inputs
        assertStaticPins(logic, connectors.get(0));
        assertStaticPins(logic, connectors.get(1));
        assertStaticPins(logic, connectors.get(2));
        assertStaticPins(logic, connectors.get(3));
        assertStaticPins(logic, connectors.get(4));
        assertStaticPins(logic, connectors.get(5));
        assertStaticPins(logic, connectors.get(6));
        assertStaticPins(logic, connectors.get(7));

        // 2) outputs
        assertStaticPins(logic, connectors.get(8));
        assertStaticPins(logic, connectors.get(9));
        assertStaticPins(logic, connectors.get(10));
        assertStaticPins(logic, connectors.get(11));
        assertStaticPins(logic, connectors.get(12));
        assertStaticPins(logic, connectors.get(13));
        assertStaticPins(logic, connectors.get(14));
        assertStaticPins(logic, connectors.get(15));
        assertStaticPins(logic, connectors.get(16));
    }

    /*
     * Asserts STATIC connectors
     */
    private void assertStaticConnectors(final ConnectorModel connectorModel,
                                        final ComponentModel componentModel,
                                        final String connectorName) {
        assertConnectors(connectorModel, componentModel, BindingType.STATIC, connectorName);
    }

    /*
     * Assert connectors
     */
    private void assertConnectors(final ConnectorModel connectorModel,
                                  final ComponentModel componentModel,
                                  final BindingType bindingType,
                                  final String connectorName) {
        assertThat(connectorModel)
                .isNotNull()
                .componentId(componentModel.getId())
                .bindingType(bindingType)
                .connectorName(connectorName);
    }

    /*
     * Asserts pins
     */
    private void assertStaticPins(final Component component,
                                  final ConnectorModel connectorModel) {

        final var pinsDao = pinsDao();
        final var pinModels = pinsDao.getByConnectorId(connectorModel.getId());
        assertThat(pinModels).hasSize(1);
        final var pinModel = pinModels.get(0);

        if (component instanceof PinAware) {
            final var pin = ((PinAware) component).getPin(pinModel.getUid());
            assertThat(pinModel)
                    .connectorId(connectorModel.getId())
                    .uid(pin)
                    .index(0);

            // compare the value
            assertThat(pinsDao.getLastValueById(pinModel.getId()))
                    .isEqualTo(pin.getValue());
        } else {
            throw new AssertionError("Given logic is not PinAware");
        }
    }
}
