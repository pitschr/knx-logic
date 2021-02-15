package li.pitschmann.knx.logic.components;

import li.pitschmann.knx.logic.annotations.Input;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.components.RefreshAlwaysTriggerInputOnlyLogic;

import static org.assertj.core.api.Assertions.assertThat;
import static test.TestHelpers.createLogicComponent;

/**
 * Checks the {@link RefreshAlwaysTriggerInputOnlyLogic} component
 * with always trigger on {@link Input}
 */
// @formatter:off
public final class RefreshAlwaysTriggerInputOnlyLogicTest {

    @Test
    @DisplayName("Always Trigger: Refresh on different values")
    public void testDifferentValues() {
        final var logic = createLogicComponent(RefreshAlwaysTriggerInputOnlyLogic.class);

        // initially it should be empty string
        assertThat(logic.getInputPin("input").getValue()).isEqualTo("");
        assertThat(logic.getOutputPin("output").getValue()).isEqualTo("");
        assertThat(logic.getOutputPin("output2").getValue()).isEqualTo("");

        // add a 'ONE'
        logic.getInputPin("input").setValue("ONE");
        logic.execute();
        assertThat(logic.getOutputPin("output").getValue()).isEqualTo("ONE");
        assertThat(logic.getOutputPin("output2").getValue()).isEqualTo("ONE");

        // add a 'TWO'
        logic.getInputPin("input").setValue("TWO");
        logic.execute();
        assertThat(logic.getOutputPin("output").getValue()).isEqualTo("ONE,TWO");
        assertThat(logic.getOutputPin("output2").getValue()).isEqualTo("TWO");

        // add a 'THREE'
        logic.getInputPin("input").setValue("THREE");
        logic.execute();
        assertThat(logic.getOutputPin("output").getValue()).isEqualTo("ONE,TWO,THREE");
        assertThat(logic.getOutputPin("output2").getValue()).isEqualTo("THREE");

        //
        // VERIFICATION
        //
        // executed three times
        assertThat(logic.executedCount()).isEqualTo(3);
        assertThat(logic.logicCount()).isEqualTo(3);
        assertThat(logic.getInputPin("input").refreshCount()).isEqualTo(3);
        assertThat(logic.getOutputPin("output").refreshCount()).isEqualTo(3);
        assertThat(logic.getOutputPin("output2").refreshCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("Always Trigger: Refresh on same values")
    public void testSameValues() {
        final var logic = createLogicComponent(RefreshAlwaysTriggerInputOnlyLogic.class);

        // initially it should be empty string
        assertThat(logic.getInputPin("input").getValue()).isEqualTo("");
        assertThat(logic.getOutputPin("output").getValue()).isEqualTo("");
        assertThat(logic.getOutputPin("output2").getValue()).isEqualTo("");

        // add a 'ONE'
        logic.getInputPin("input").setValue("ONE");
        logic.execute();
        assertThat(logic.getOutputPin("output").getValue()).isEqualTo("ONE");
        assertThat(logic.getOutputPin("output2").getValue()).isEqualTo("ONE");

        // add a 'ONE' (AGAIN)
        logic.getInputPin("input").setValue("ONE");
        logic.execute();
        assertThat(logic.getOutputPin("output").getValue()).isEqualTo("ONE,ONE");
        assertThat(logic.getOutputPin("output2").getValue()).isEqualTo("ONE");

        // add a 'ONE' (AGAIN, 3rd attempt)
        logic.getInputPin("input").setValue("ONE");
        logic.execute();
        assertThat(logic.getOutputPin("output").getValue()).isEqualTo("ONE,ONE,ONE");
        assertThat(logic.getOutputPin("output2").getValue()).isEqualTo("ONE");

        //
        // VERIFICATION
        //
        // executed three times
        assertThat(logic.executedCount()).isEqualTo(3);
        assertThat(logic.logicCount()).isEqualTo(3);
        assertThat(logic.getInputPin("input").refreshCount()).isEqualTo(3);
        assertThat(logic.getOutputPin("output").refreshCount()).isEqualTo(3);
        assertThat(logic.getOutputPin("output2").refreshCount()).isEqualTo(1); // 1 change because value is changed only once time
    }

}
