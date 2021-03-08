package li.pitschmann.knx.logic.db.persistence;

import li.pitschmann.knx.logic.connector.DynamicConnector;
import li.pitschmann.knx.logic.db.jdbi.mappers.BindingType;
import li.pitschmann.knx.logic.db.jdbi.mappers.ComponentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.BaseDatabaseSuite;
import test.components.AndLogicBooleanPrimitive;
import test.components.logic.EmptyLogic;
import test.components.logic.TextSizeLogic;

import static test.TestHelpers.createLogicComponent;
import static test.assertions.model.DatabaseAssertions.assertThat;

/**
 * Test case for writing logic components to database
 * {@link LogicComponentPersistenceStrategy}
 *
 * @author PITSCHR
 */
class LogicComponentPersistenceStrategyTest extends BaseDatabaseSuite {

    @Test
    @DisplayName("Test the INSERT persistence of Empty Logic Component")
    void testInsertEmptyLogic() {
        final var componentsDao = componentsDao();
        final var connectorsDao = connectorsDao();
        final var pinsDao = pinsDao();
        final var eventKeyDao = eventKeyDao();

        final var logic = createLogicComponent(EmptyLogic.class);
        save(logic);

        // ---------------------------------
        // Verification: Table Size
        // ---------------------------------
        assertThat(componentsDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isZero();
        assertThat(pinsDao.size()).isZero();
        assertThat(eventKeyDao.size()).isZero();

        // ---------------------------------
        // Verification: Component
        // ---------------------------------
        final var componentModel = componentsDao.find(1);
        assertThat(componentModel)
                .componentType(ComponentType.LOGIC)
                .className(EmptyLogic.class.getName());
        assertThat(componentModel.getUid()).isNotNull();
    }

    @Test
    @DisplayName("Test the INSERT persistence of Logic Component")
    void testInsertLogic() {
        final var componentsDao = componentsDao();
        final var connectorsDao = connectorsDao();
        final var pinsDao = pinsDao();
        final var eventKeyDao = eventKeyDao();

        final var logic = createLogicComponent(TextSizeLogic.class);
        save(logic);

        // ---------------------------------
        // Verification: Table Size
        // ---------------------------------
        assertThat(componentsDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(4);
        assertThat(pinsDao.size()).isEqualTo(5);
        assertThat(eventKeyDao.size()).isZero();

        // ---------------------------------
        // Verification: Component
        // ---------------------------------
        final var componentModel = componentsDao.find(1);
        assertThat(componentModel)
                .componentType(ComponentType.LOGIC)
                .className(TextSizeLogic.class.getName());
        assertThat(componentModel.getUid()).isNotNull();

        // ---------------------------------
        // Verification: Connectors
        // ---------------------------------
        final var connectorTexts = connectorsDao.find(1);
        assertThat(connectorTexts)
                .id(1)
                .componentId(1)
                .bindingType(BindingType.DYNAMIC)
                .connectorName("texts");
        assertThat(pinsDao.getByConnectorId(1)).hasSize(2);

        final var connectorTotalTextLength = connectorsDao.find(2);
        assertThat(connectorTotalTextLength)
                .id(2)
                .componentId(1)
                .bindingType(BindingType.STATIC)
                .connectorName("totalTextLength");
        assertThat(pinsDao.getByConnectorId(2)).hasSize(1);

        final var connectorNumberOfTexts = connectorsDao.find(3);
        assertThat(connectorNumberOfTexts)
                .id(3)
                .componentId(1)
                .bindingType(BindingType.STATIC)
                .connectorName("numberOfTexts");
        assertThat(pinsDao.getByConnectorId(3)).hasSize(1);

        final var connectorTextAverageSize = connectorsDao.find(4);
        assertThat(connectorTextAverageSize)
                .id(4)
                .componentId(1)
                .bindingType(BindingType.STATIC)
                .connectorName("textAverageSize");
        assertThat(pinsDao.getByConnectorId(4)).hasSize(1);

        // ---------------------------------
        // Verification: Pins
        // ---------------------------------
        final var pinTexts_1 = pinsDao.find(1);
        assertThat(pinTexts_1)
                .id(1)
                .connectorId(1)
                .index(0);
        assertThat(pinTexts_1.getUid()).isNotNull();

        final var pinTexts_2 = pinsDao.find(2);
        assertThat(pinTexts_2)
                .id(2)
                .connectorId(1)
                .index(1);
        assertThat(pinTexts_2.getUid()).isNotNull();

        final var pinTotalTextLength = pinsDao.find(3);
        assertThat(pinTotalTextLength)
                .id(3)
                .connectorId(2)
                .index(0);
        assertThat(pinTotalTextLength.getUid()).isNotNull();

        final var pinNumberOfTexts = pinsDao.find(4);
        assertThat(pinNumberOfTexts)
                .id(4)
                .connectorId(3)
                .index(0);
        assertThat(pinNumberOfTexts.getUid()).isNotNull();

        final var pinTextAverageSize = pinsDao.find(5);
        assertThat(pinTextAverageSize)
                .id(5)
                .connectorId(4)
                .index(0);
        assertThat(pinTextAverageSize.getUid()).isNotNull();
    }

    @Test
    @DisplayName("Test the UPDATE persistence of Logic Component (no change)")
    void testUpdateNoChange() {
        final var componentsDao = componentsDao();
        final var connectorsDao = connectorsDao();
        final var pinsDao = pinsDao();

        final var logic = createLogicComponent(AndLogicBooleanPrimitive.class);

        // First Save (Insert)
        save(logic);

        // Second Save (Update)
        save(logic);

        // ---------------------------------
        // Verification after update
        // ---------------------------------
        assertThat(componentsDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(3);
        assertThat(pinsDao.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("Test the UPDATE persistence of Logic Component (value change)")
    void testUpdateValueChange() {
        final var componentsDao = componentsDao();
        final var connectorsDao = connectorsDao();
        final var pinsDao = pinsDao();

        final var logic = createLogicComponent(TextSizeLogic.class);
        save(logic);

        // ---------------------------------
        // Verification before update (initial values)
        // ---------------------------------
        assertThat(componentsDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(4);
        assertThat(pinsDao.size()).isEqualTo(5);

        // ---------------------------------
        // Update Statement #1
        //   * Add new pin to 'texts'
        //   * Set values "Hello", "Everyone", "!"
        //   * Execute logic to calculate the output values
        //
        // Here we check if a new pin has been added and if all values
        // have been updated correctly
        // ---------------------------------
        final var connectorTexts = (DynamicConnector) logic.getInputConnector("texts");
        connectorTexts.addPin(); // add new pin (texts(index=2))

        final var pinTexts_0 = logic.getInputPin("texts[0]");
        final var pinTexts_1 = logic.getInputPin("texts[1]");
        final var pinTexts_2 = logic.getInputPin("texts[2]");
        pinTexts_0.setValue("Hello");
        pinTexts_1.setValue("Everyone");
        pinTexts_2.setValue("!");

        save(logic);

        // Verification after 1st update
        assertThat(componentsDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(4);
        assertThat(pinsDao.size()).isEqualTo(6);

        assertThat(pinsDao.find(1)).index(0);    // texts(index=0)
        assertThat(pinsDao.find(2)).index(1);    // texts(index=1)
        assertThat(pinsDao.find(6)).index(2);    // texts(index=2)

        // ---------------------------------
        // Update Statement #2
        //   * Remove 1st pin (will become "Hello", "!")
        //
        // Here we check if the pin has been correctly removed
        // ---------------------------------
        connectorTexts.removePin(1); // remove texts(index=1), id = 2

        save(logic);

        // Verification after 2nd update
        assertThat(componentsDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(4);
        assertThat(pinsDao.size()).isEqualTo(5);

        assertThat(pinsDao.find(1)).index(0);    // texts(index=0)
        assertThat(pinsDao.find(6)).index(1);    // texts(index=1)

        // ---------------------------------
        // Update Statement #3
        //   * Add new pins at beginning and end (will become "[", "Hello", "!", "]")
        //
        // Here we check if the index of pin has correctly been updated
        // ---------------------------------
        // add new pin at begin (will become texts[0])
        final var prependPin = connectorTexts.addPin(0);
        prependPin.setValue("[");
        // add new pin at end (will become texts[3])
        final var appendPin = connectorTexts.addPin();
        appendPin.setValue("]");

        save(logic);

        // Verification after 3rd update
        assertThat(componentsDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(4);
        assertThat(pinsDao.size()).isEqualTo(7);

        assertThat(pinsDao.find(1)).index(1);    // texts(index=1)
        assertThat(pinsDao.find(6)).index(2);    // texts(index=2)
        assertThat(pinsDao.find(7)).index(0);    // texts(index=0)
        assertThat(pinsDao.find(8)).index(3);    // texts(index=3)
    }

    @Test
    @DisplayName("Test updating a component having a new static connector")
    void testNewStaticConnector() {
        final var componentsDao = componentsDao();
        final var connectorsDao = connectorsDao();
        final var pinsDao = pinsDao();

        final var logic = createLogicComponent(TextSizeLogic.class);
        final var newComponentId = save(logic);

        // ---------------------------------
        // Verification before update (initial values)
        // ---------------------------------
        assertThat(componentsDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(4);
        assertThat(pinsDao.size()).isEqualTo(5);

        assertThat(connectorsDao.find(1).getConnectorName()).isEqualTo("texts");
        assertThat(connectorsDao.find(2).getConnectorName()).isEqualTo("totalTextLength");
        assertThat(connectorsDao.find(3).getConnectorName()).isEqualTo("numberOfTexts");
        assertThat(connectorsDao.find(4).getConnectorName()).isEqualTo("textAverageSize");
        assertThat(connectorsDao.find(5)).isNull(); // doesn't exists yet

        assertThat(pinsDao.find(3).getUid()).isEqualTo(logic.getPin("totalTextLength").getUid());
        assertThat(pinsDao.find(6)).isNull(); // doesn't exists yet

        // ---------------------------------
        // Remove the 'totalTextLength' static connector
        // ---------------------------------
        final var connectorIdToBeRemoved = connectorsDao.byComponentId(newComponentId)
                .stream()
                .filter(c -> "totalTextLength".equalsIgnoreCase(c.getConnectorName()))
                .findFirst().orElseThrow().getId();
        connectorsDao.delete(connectorIdToBeRemoved);

        assertThat(componentsDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(3); // 4-1 (one connector removed)
        assertThat(pinsDao.size()).isEqualTo(4);       // 5-1 (one pin removed)

        assertThat(connectorsDao.find(1).getConnectorName()).isEqualTo("texts");
        assertThat(connectorsDao.find(2)).isNull(); // has been removed
        assertThat(connectorsDao.find(3).getConnectorName()).isEqualTo("numberOfTexts");
        assertThat(connectorsDao.find(4).getConnectorName()).isEqualTo("textAverageSize");
        assertThat(connectorsDao.find(5)).isNull(); // doesn't exists yet

        assertThat(pinsDao.find(3)).isNull(); // pin 'totalTextLength' has been deleted
        assertThat(pinsDao.find(6)).isNull(); // doesn't exists yet

        // ---------------------------------
        // Update the logic, expected is that the connector 'totalTextLength'
        // and static pin has been re-added to the database
        // ---------------------------------
        save(logic); // update mechanism will detect a new connector

        assertThat(componentsDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(4);  // 3+1 (one connector added)
        assertThat(pinsDao.size()).isEqualTo(5);        // 4+1 (one pin added)

        assertThat(connectorsDao.find(1).getConnectorName()).isEqualTo("texts");
        assertThat(connectorsDao.find(2)).isNull(); // has been removed
        assertThat(connectorsDao.find(3).getConnectorName()).isEqualTo("numberOfTexts");
        assertThat(connectorsDao.find(4).getConnectorName()).isEqualTo("textAverageSize");
        assertThat(connectorsDao.find(5).getConnectorName()).isEqualTo("totalTextLength"); // new one!

        assertThat(pinsDao.find(3)).isNull(); // remain null, pin 'totalTextLength' will get a new primary key
        assertThat(pinsDao.find(6).getUid()).isEqualTo(logic.getPin("totalTextLength").getUid()); // new static pin
    }

    @Test
    @DisplayName("Test updating a component having a new dynamic connector")
    void testNewDynamicConnector() {
        final var componentsDao = componentsDao();
        final var connectorsDao = connectorsDao();
        final var pinsDao = pinsDao();

        final var logic = createLogicComponent(TextSizeLogic.class);
        final var newComponentId = save(logic);

        // ---------------------------------
        // Verification before update (initial values)
        // ---------------------------------
        assertThat(componentsDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(4);
        assertThat(pinsDao.size()).isEqualTo(5);

        assertThat(connectorsDao.find(1).getConnectorName()).isEqualTo("texts");
        assertThat(connectorsDao.find(2).getConnectorName()).isEqualTo("totalTextLength");
        assertThat(connectorsDao.find(3).getConnectorName()).isEqualTo("numberOfTexts");
        assertThat(connectorsDao.find(4).getConnectorName()).isEqualTo("textAverageSize");
        assertThat(connectorsDao.find(6)).isNull(); // doesn't exists yet

        assertThat(pinsDao.find(1).getUid()).isEqualTo(logic.getPin("texts[0]").getUid());
        assertThat(pinsDao.find(2).getUid()).isEqualTo(logic.getPin("texts[1]").getUid());
        assertThat(pinsDao.find(6)).isNull(); // doesn't exists yet
        assertThat(pinsDao.find(7)).isNull(); // doesn't exists yet

        // ---------------------------------
        // Remove the 'texts' connector
        //   This will also remove the associated pins: texts[0] and texts[1]
        // ---------------------------------
        final var connectorIdToBeRemoved = connectorsDao.byComponentId(newComponentId)
                .stream()
                .filter(c -> "texts".equalsIgnoreCase(c.getConnectorName()))
                .findFirst().orElseThrow().getId();
        connectorsDao.delete(connectorIdToBeRemoved);

        assertThat(componentsDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(3); // 4-1 (one connector removed)
        assertThat(pinsDao.size()).isEqualTo(3);       // 5-2 (two pins removed)

        assertThat(connectorsDao.find(1)).isNull(); // has been removed
        assertThat(connectorsDao.find(2).getConnectorName()).isEqualTo("totalTextLength");
        assertThat(connectorsDao.find(3).getConnectorName()).isEqualTo("numberOfTexts");
        assertThat(connectorsDao.find(4).getConnectorName()).isEqualTo("textAverageSize");
        assertThat(connectorsDao.find(5)).isNull(); // doesn't exists yet

        assertThat(pinsDao.find(1)).isNull(); // 'texts[0]' has been deleted
        assertThat(pinsDao.find(2)).isNull(); // 'texts[0]' has been deleted
        assertThat(pinsDao.find(6)).isNull(); // doesn't exists yet
        assertThat(pinsDao.find(7)).isNull(); // doesn't exists yet

        // ---------------------------------
        // Update the logic, expected is that the connector 'texts'
        // and pins 'texts[0]' and 'texts[1]' are re-added to the database
        // ---------------------------------
        save(logic); // update mechanism will detect a new connector

        assertThat(componentsDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(4);  // 3+1 (one connector added)
        assertThat(pinsDao.size()).isEqualTo(5);        // 3+2 (two pins added)

        assertThat(connectorsDao.find(1)).isNull();
        assertThat(connectorsDao.find(2).getConnectorName()).isEqualTo("totalTextLength");
        assertThat(connectorsDao.find(3).getConnectorName()).isEqualTo("numberOfTexts");
        assertThat(connectorsDao.find(4).getConnectorName()).isEqualTo("textAverageSize");
        assertThat(connectorsDao.find(5).getConnectorName()).isEqualTo("texts");

        assertThat(pinsDao.find(1)).isNull(); // remain null, texts[0] will get a new primary key
        assertThat(pinsDao.find(2)).isNull(); // remain null, texts[1] will get a new primary key
        assertThat(pinsDao.find(6).getUid()).isEqualTo(logic.getPin("texts[0]").getUid()); // new pin #1
        assertThat(pinsDao.find(7).getUid()).isEqualTo(logic.getPin("texts[1]").getUid()); // new pin #2
    }

}
