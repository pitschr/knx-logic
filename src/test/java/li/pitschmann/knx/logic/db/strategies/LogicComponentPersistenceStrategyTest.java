package li.pitschmann.knx.logic.db.strategies;

import li.pitschmann.knx.logic.connector.DynamicConnector;
import li.pitschmann.knx.logic.db.jdbi.mappers.BindingType;
import li.pitschmann.knx.logic.db.jdbi.mappers.ComponentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.BaseDatabaseSuite;
import test.TestHelpers;
import test.components.AndLogicBooleanPrimitive;
import test.components.logic.TextSizeLogic;

import java.math.BigDecimal;

import static test.assertions.model.DatabaseAssertions.assertThat;

/**
 * Test case for writing logic components to database
 * {@link li.pitschmann.knx.logic.db.strategies.LogicComponentPersistenceStrategy}
 *
 * @author PITSCHR
 */
class LogicComponentPersistenceStrategyTest extends BaseDatabaseSuite {

    @Test
    @DisplayName("Test the INSERT persistence of Logic Component")
    void testInsert() {
        final var componentDao = componentsDao();
        final var connectorsDao = connectorsDao();
        final var pinsDao = pinsDao();
        final var eventKeyDao = eventKeyDao();

        final var logic = TestHelpers.createLogicComponent(TextSizeLogic.class);
        save(logic);

        // ---------------------------------
        // Verification: Table Size
        // ---------------------------------
        assertThat(componentDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(4);
        assertThat(pinsDao.size()).isEqualTo(5);
        assertThat(eventKeyDao.size()).isZero();

        // ---------------------------------
        // Verification: Component
        // ---------------------------------
        final var componentModel = componentDao.getById(1);
        assertThat(componentModel)
                .componentType(ComponentType.LOGIC)
                .className(TextSizeLogic.class);
        assertThat(componentModel.getUid()).isNotNull();

        // ---------------------------------
        // Verification: Connectors
        // ---------------------------------
        final var connectorTexts = connectorsDao.getById(1);
        assertThat(connectorTexts)
                .id(1)
                .componentId(1)
                .bindingType(BindingType.DYNAMIC)
                .connectorName("texts");
        assertThat(pinsDao.getByConnectorId(1)).hasSize(2);

        final var connectorTotalTextLength = connectorsDao.getById(2);
        assertThat(connectorTotalTextLength)
                .id(2)
                .componentId(1)
                .bindingType(BindingType.STATIC)
                .connectorName("totalTextLength");
        assertThat(pinsDao.getByConnectorId(2)).hasSize(1);

        final var connectorNumberOfTexts = connectorsDao.getById(3);
        assertThat(connectorNumberOfTexts)
                .id(3)
                .componentId(1)
                .bindingType(BindingType.STATIC)
                .connectorName("numberOfTexts");
        assertThat(pinsDao.getByConnectorId(3)).hasSize(1);

        final var connectorTextAverageSize = connectorsDao.getById(4);
        assertThat(connectorTextAverageSize)
                .id(4)
                .componentId(1)
                .bindingType(BindingType.STATIC)
                .connectorName("textAverageSize");
        assertThat(pinsDao.getByConnectorId(4)).hasSize(1);

        // ---------------------------------
        // Verification: Pins
        // ---------------------------------
        final var pinTexts_1 = pinsDao.getById(1);
        assertThat(pinTexts_1)
                .id(1)
                .connectorId(1)
                .index(0);
        assertThat(pinTexts_1.getUid()).isNotNull();
        assertThat((String)pinsDao.getLastValueById(1)).isEmpty();          // empty string because of initial value

        final var pinTexts_2 = pinsDao.getById(2);
        assertThat(pinTexts_2)
                .id(2)
                .connectorId(1)
                .index(1);
        assertThat(pinTexts_2.getUid()).isNotNull();
        assertThat((String)pinsDao.getLastValueById(2)).isEmpty();          // empty string because of initial value

        final var pinTotalTextLength = pinsDao.getById(3);
        assertThat(pinTotalTextLength)
                .id(3)
                .connectorId(2)
                .index(0);
        assertThat(pinTotalTextLength.getUid()).isNotNull();
        assertThat((Integer)pinsDao.getLastValueById(3)).isZero();          // zero because of initial value

        final var pinNumberOfTexts = pinsDao.getById(4);
        assertThat(pinNumberOfTexts)
                .id(4)
                .connectorId(3)
                .index(0);
        assertThat(pinNumberOfTexts.getUid()).isNotNull();
        assertThat((Integer)pinsDao.getLastValueById(4)).isZero();          // zero because of initial value

        final var pinTextAverageSize = pinsDao.getById(5);
        assertThat(pinTextAverageSize)
                .id(5)
                .connectorId(4)
                .index(0);
        assertThat(pinTextAverageSize.getUid()).isNotNull();
        assertThat(pinsDao.getLastValueById(5)).isNull();                   // 'null' because of initial value due no default value for BigDecimal
    }

    @Test
    @DisplayName("Test the UPDATE persistence of Logic Component (no change)")
    void testUpdateNoChange() {
        final var componentDao = componentsDao();
        final var connectorsDao = connectorsDao();
        final var pinsDao = pinsDao();

        final var logic = TestHelpers.createLogicComponent(AndLogicBooleanPrimitive.class);

        // First Save (Insert)
        save(logic);

        // Second Save (Update)
        save(logic);

        // ---------------------------------
        // Verification after update
        // ---------------------------------
        assertThat(componentDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(3);
        assertThat(pinsDao.size()).isEqualTo(3);

        assertThat((Boolean)pinsDao.getLastValueById(1)).isFalse();
        assertThat((Boolean)pinsDao.getLastValueById(2)).isTrue();
        assertThat((Boolean)pinsDao.getLastValueById(3)).isFalse();
    }

    @Test
    @DisplayName("Test the UPDATE persistence of Logic Component (value change)")
    void testUpdateValueChange() {
        final var componentDao = componentsDao();
        final var connectorsDao = connectorsDao();
        final var pinsDao = pinsDao();

        final var logic = TestHelpers.createLogicComponent(TextSizeLogic.class);
        save(logic);

        // ---------------------------------
        // Verification before update (initial values)
        // ---------------------------------
        assertThat(componentDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(4);
        assertThat(pinsDao.size()).isEqualTo(5);

        assertThat((String)pinsDao.getLastValueById(1)).isEmpty(); // texts(index=0)
        assertThat((String)pinsDao.getLastValueById(2)).isEmpty(); // texts(index=1)
        assertThat((Integer)pinsDao.getLastValueById(3)).isZero(); // totalTextLength
        assertThat((Integer)pinsDao.getLastValueById(4)).isZero(); // numberOfTexts
        assertThat(pinsDao.getLastValueById(5)).isNull();          // textAverageSize

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

        logic.execute();
        save(logic);

        // Verification after 1st update
        assertThat(componentDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(4);
        assertThat(pinsDao.size()).isEqualTo(6);

        assertThat(pinsDao.getById(1)).index(0);
        assertThat(pinsDao.getById(2)).index(1);
        assertThat(pinsDao.getById(6)).index(2);

        assertThat((String)pinsDao.getLastValueById(1)).isEqualTo("Hello");                        // texts(index=0)
        assertThat((String)pinsDao.getLastValueById(2)).isEqualTo("Everyone");                     // texts(index=1)
        assertThat((Integer)pinsDao.getLastValueById(3)).isEqualTo(14);                            // totalTextLength
        assertThat((Integer)pinsDao.getLastValueById(4)).isEqualTo(3);                             // numberOfTexts
        assertThat((BigDecimal)pinsDao.getLastValueById(5)).isEqualTo(new BigDecimal("4.67")); // textAverageSize  (14 / 3 = 4.6666...)
        assertThat((String)pinsDao.getLastValueById(6)).isEqualTo("!");                            // texts(index=2)

        // ---------------------------------
        // Update Statement #2
        //   * Remove 1st pin (will become "Hello", "!")
        //   * Execute logic to re-calculate the output values
        //
        // Here we check if the pin has been correctly removed
        // ---------------------------------
        connectorTexts.removePin(1); // remove texts(index=1), id = 2

        logic.execute();
        save(logic);

        // Verification after 2nd update
        assertThat(componentDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(4);
        assertThat(pinsDao.size()).isEqualTo(5);

        assertThat(pinsDao.getById(1)).index(0);
        assertThat(pinsDao.getById(6)).index(1);

        assertThat((String)pinsDao.getLastValueById(1)).isEqualTo("Hello");                        // texts(index=0)
        assertThat(pinsDao.getLastValueById(2)).isNull();                                          // id = 2 has been deleted
        assertThat((Integer)pinsDao.getLastValueById(3)).isEqualTo(6);                             // totalTextLength
        assertThat((Integer)pinsDao.getLastValueById(4)).isEqualTo(2);                             // numberOfTexts
        assertThat((BigDecimal)pinsDao.getLastValueById(5)).isEqualTo(new BigDecimal("3.00")); // textAverageSize (6 / 2 = 3.0)
        assertThat((String)pinsDao.getLastValueById(6)).isEqualTo("!");                            // texts(index=1)

        // ---------------------------------
        // Update Statement #3
        //   * Add new pins at beginning and end (will become "[", "Hello", "!", "]")
        //   * Execute logic to re-calculate the output values
        //
        // Here we check if the index of pin has correctly been updated
        // ---------------------------------
        // add new pin at begin (will become texts[0])
        final var prependPin = connectorTexts.addPin(0);
        prependPin.setValue("[");
        // add new pin at end (will become texts[3])
        final var appendPin = connectorTexts.addPin();
        appendPin.setValue("]");

        logic.execute();
        save(logic);

        // Verification after 3rd update
        assertThat(componentDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(4);
        assertThat(pinsDao.size()).isEqualTo(7);

        assertThat(pinsDao.getById(1)).index(1);
        assertThat(pinsDao.getById(6)).index(2);
        assertThat(pinsDao.getById(7)).index(0);
        assertThat(pinsDao.getById(8)).index(3);

        assertThat((String)pinsDao.getLastValueById(1)).isEqualTo("Hello");                        // texts(index=1)
        assertThat(pinsDao.getLastValueById(2)).isNull();                                          // id = 2 has been deleted
        assertThat((Integer)pinsDao.getLastValueById(3)).isEqualTo(8);                             // totalTextLength
        assertThat((Integer)pinsDao.getLastValueById(4)).isEqualTo(4);                             // numberOfTexts
        assertThat((BigDecimal)pinsDao.getLastValueById(5)).isEqualTo(new BigDecimal("2.00")); // textAverageSize (8 / 4 = 2.0)
        assertThat((String)pinsDao.getLastValueById(6)).isEqualTo("!");                            // texts(index=2)
        assertThat((String)pinsDao.getLastValueById(7)).isEqualTo("[");                            // texts(index=0)
        assertThat((String)pinsDao.getLastValueById(8)).isEqualTo("]");                            // texts(index=3)
    }

    @Test
    @DisplayName("Test updating a component having a new static connector")
    void testNewStaticConnector() {
        final var componentDao = componentsDao();
        final var connectorsDao = connectorsDao();
        final var pinsDao = pinsDao();

        final var logic = TestHelpers.createLogicComponent(TextSizeLogic.class);
        final var newComponentId = save(logic);

        // ---------------------------------
        // Verification before update (initial values)
        // ---------------------------------
        assertThat(componentDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(4);
        assertThat(pinsDao.size()).isEqualTo(5);

        assertThat(connectorsDao.getById(1).getConnectorName()).isEqualTo("texts");
        assertThat(connectorsDao.getById(2).getConnectorName()).isEqualTo("totalTextLength");
        assertThat(connectorsDao.getById(3).getConnectorName()).isEqualTo("numberOfTexts");
        assertThat(connectorsDao.getById(4).getConnectorName()).isEqualTo("textAverageSize");
        assertThat(connectorsDao.getById(5)).isNull(); // doesn't exists yet

        assertThat(pinsDao.getById(3).getUid()).isEqualTo(logic.getPin("totalTextLength").getUid());
        assertThat(pinsDao.getById(6)).isNull(); // doesn't exists yet

        // ---------------------------------
        // Remove the 'totalTextLength' static connector
        // ---------------------------------
        final var connectorIdToBeRemoved = connectorsDao.getByComponentId(newComponentId)
                .stream()
                .filter(c -> "totalTextLength".equalsIgnoreCase(c.getConnectorName()))
                .findFirst().orElseThrow().getId();
        connectorsDao.delete(connectorIdToBeRemoved);

        assertThat(componentDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(3); // 4-1 (one connector removed)
        assertThat(pinsDao.size()).isEqualTo(4);       // 5-1 (one pin removed)

        assertThat(connectorsDao.getById(1).getConnectorName()).isEqualTo("texts");
        assertThat(connectorsDao.getById(2)).isNull(); // has been removed
        assertThat(connectorsDao.getById(3).getConnectorName()).isEqualTo("numberOfTexts");
        assertThat(connectorsDao.getById(4).getConnectorName()).isEqualTo("textAverageSize");
        assertThat(connectorsDao.getById(5)).isNull(); // doesn't exists yet

        assertThat(pinsDao.getById(3)).isNull(); // pin 'totalTextLength' has been deleted
        assertThat(pinsDao.getById(6)).isNull(); // doesn't exists yet

        // ---------------------------------
        // Update the logic, expected is that the connector 'totalTextLength'
        // and static pin has been re-added to the database
        // ---------------------------------
        save(logic); // update mechanism will detect a new connector

        assertThat(componentDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(4);  // 3+1 (one connector added)
        assertThat(pinsDao.size()).isEqualTo(5);        // 4+1 (one pin added)

        assertThat(connectorsDao.getById(1).getConnectorName()).isEqualTo("texts");
        assertThat(connectorsDao.getById(2)).isNull(); // has been removed
        assertThat(connectorsDao.getById(3).getConnectorName()).isEqualTo("numberOfTexts");
        assertThat(connectorsDao.getById(4).getConnectorName()).isEqualTo("textAverageSize");
        assertThat(connectorsDao.getById(5).getConnectorName()).isEqualTo("totalTextLength"); // new one!

        assertThat(pinsDao.getById(3)).isNull(); // remain null, pin 'totalTextLength' will get a new primary key
        assertThat(pinsDao.getById(6).getUid()).isEqualTo(logic.getPin("totalTextLength").getUid()); // new static pin
    }

    @Test
    @DisplayName("Test updating a component having a new dynamic connector")
    void testNewDynamicConnector() {
        final var componentDao = componentsDao();
        final var connectorsDao = connectorsDao();
        final var pinsDao = pinsDao();

        final var logic = TestHelpers.createLogicComponent(TextSizeLogic.class);
        final var newComponentId = save(logic);

        // ---------------------------------
        // Verification before update (initial values)
        // ---------------------------------
        assertThat(componentDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(4);
        assertThat(pinsDao.size()).isEqualTo(5);

        assertThat(connectorsDao.getById(1).getConnectorName()).isEqualTo("texts");
        assertThat(connectorsDao.getById(2).getConnectorName()).isEqualTo("totalTextLength");
        assertThat(connectorsDao.getById(3).getConnectorName()).isEqualTo("numberOfTexts");
        assertThat(connectorsDao.getById(4).getConnectorName()).isEqualTo("textAverageSize");
        assertThat(connectorsDao.getById(6)).isNull(); // doesn't exists yet

        assertThat(pinsDao.getById(1).getUid()).isEqualTo(logic.getPin("texts[0]").getUid());
        assertThat(pinsDao.getById(2).getUid()).isEqualTo(logic.getPin("texts[1]").getUid());
        assertThat(pinsDao.getById(6)).isNull(); // doesn't exists yet
        assertThat(pinsDao.getById(7)).isNull(); // doesn't exists yet

        // ---------------------------------
        // Remove the 'texts' connector
        //   This will also remove the associated pins: texts[0] and texts[1]
        // ---------------------------------
        final var connectorIdToBeRemoved = connectorsDao.getByComponentId(newComponentId)
                .stream()
                .filter(c -> "texts".equalsIgnoreCase(c.getConnectorName()))
                .findFirst().orElseThrow().getId();
        connectorsDao.delete(connectorIdToBeRemoved);

        assertThat(componentDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(3); // 4-1 (one connector removed)
        assertThat(pinsDao.size()).isEqualTo(3);       // 5-2 (two pins removed)

        assertThat(connectorsDao.getById(1)).isNull(); // has been removed
        assertThat(connectorsDao.getById(2).getConnectorName()).isEqualTo("totalTextLength");
        assertThat(connectorsDao.getById(3).getConnectorName()).isEqualTo("numberOfTexts");
        assertThat(connectorsDao.getById(4).getConnectorName()).isEqualTo("textAverageSize");
        assertThat(connectorsDao.getById(5)).isNull(); // doesn't exists yet

        assertThat(pinsDao.getById(1)).isNull(); // 'texts[0]' has been deleted
        assertThat(pinsDao.getById(2)).isNull(); // 'texts[0]' has been deleted
        assertThat(pinsDao.getById(6)).isNull(); // doesn't exists yet
        assertThat(pinsDao.getById(7)).isNull(); // doesn't exists yet

        // ---------------------------------
        // Update the logic, expected is that the connector 'texts'
        // and pins 'texts[0]' and 'texts[1]' are re-added to the database
        // ---------------------------------
        save(logic); // update mechanism will detect a new connector

        assertThat(componentDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(4);  // 3+1 (one connector added)
        assertThat(pinsDao.size()).isEqualTo(5);        // 3+2 (two pins added)

        assertThat(connectorsDao.getById(1)).isNull();
        assertThat(connectorsDao.getById(2).getConnectorName()).isEqualTo("totalTextLength");
        assertThat(connectorsDao.getById(3).getConnectorName()).isEqualTo("numberOfTexts");
        assertThat(connectorsDao.getById(4).getConnectorName()).isEqualTo("textAverageSize");
        assertThat(connectorsDao.getById(5).getConnectorName()).isEqualTo("texts");

        assertThat(pinsDao.getById(1)).isNull(); // remain null, texts[0] will get a new primary key
        assertThat(pinsDao.getById(2)).isNull(); // remain null, texts[1] will get a new primary key
        assertThat(pinsDao.getById(6).getUid()).isEqualTo(logic.getPin("texts[0]").getUid()); // new pin #1
        assertThat(pinsDao.getById(7).getUid()).isEqualTo(logic.getPin("texts[1]").getUid()); // new pin #2
    }

}
