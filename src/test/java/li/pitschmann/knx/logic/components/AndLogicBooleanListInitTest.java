package li.pitschmann.knx.logic.components;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.components.AndLogicBooleanListInit;

import static org.assertj.core.api.Assertions.assertThat;
import static test.TestHelpers.createLogicComponent;

/**
 * Test the {@link AndLogicBooleanListInit} component
 */
public class AndLogicBooleanListInitTest extends AbstractLogicBooleanTest<AndLogicBooleanListInit> {

    @Override
    protected Class<AndLogicBooleanListInit> logicClass() {
        return AndLogicBooleanListInit.class;
    }

    @Test
    @DisplayName("Test Logic in initialization state")
    public void testInitialization() {
        final var logic = createLogicComponent(logicClass());
        assertThat(logic.getInputPin(0).getValue()).isEqualTo(false);
        assertThat(logic.getInputPin(1).getValue()).isEqualTo(false);    // at loading it is still 'false'
        assertThat(logic.getOutputPin(0).getValue()).isEqualTo(false);

        logic.execute();  // the initialization will be set in 'init' method which is invoked by execute() method

        assertThat(logic.getInputPin(0).getValue()).isEqualTo(true);     // set during initialization
        assertThat(logic.getInputPin(1).getValue()).isEqualTo(false);
        assertThat(logic.getOutputPin(0).getValue()).isEqualTo(false);
    }
}
