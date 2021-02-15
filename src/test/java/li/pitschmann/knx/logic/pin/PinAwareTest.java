package li.pitschmann.knx.logic.pin;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;
import li.pitschmann.knx.logic.uid.UID;
import li.pitschmann.knx.logic.uid.UIDFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static test.TestHelpers.createLogicComponent;

/**
 * Test Suite for {@link PinAware}
 */
class PinAwareTest {

    @Test
    @DisplayName("Get pin by name")
    void testGetPinByName() {
        final var component = createLogicComponent(
                new Logic() {
                    @Input
                    private boolean boolInputValue;

                    @Input
                    private Integer intInputValue;

                    @Input
                    private Iterable<Object> objects;

                    @Output
                    private String string;

                    @Output(min = 10)
                    private List<String> stringList;

                    @Override
                    public void logic() {
                        // NO-OP
                    }
                }
        );

        assertThat(component.getPins()).hasSize(14);
        assertThat(component.getInputPins()).hasSize(3);
        assertThat(component.getOutputPins()).hasSize(11);

        // static input pin exists
        final var boolInputValuePin = component.getInputPins().get(1);
        assertThat(component.getPin("intInputValue")).isSameAs(boolInputValuePin);
        assertThat(component.getInputPin("intInputValue")).isSameAs(boolInputValuePin);
        assertThatThrownBy(() -> component.getOutputPin("intInputValue"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No Output Static Pin with field name found: intInputValue");

        // dynamic input pin exists
        final var objectsPin = component.getInputPins().get(2);
        assertThat(component.getPin("objects[0]")).isSameAs(objectsPin);
        assertThat(component.getInputPin("objects[0]")).isSameAs(objectsPin);
        assertThatThrownBy(() -> component.getOutputPin("objects[0]"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No Output Dynamic Pin with field name found: objects[0]");

        // static output pin exists
        final var stringPin = component.getOutputPins().get(0);
        assertThat(component.getPin("string")).isSameAs(stringPin);
        assertThatThrownBy(() -> component.getInputPin("string"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No Input Static Pin with field name found: string");
        assertThat(component.getOutputPin("string")).isSameAs(stringPin);

        // dynamic output pin exists
        final var stringListPin = component.getOutputPins().get(8); // static pin (string) + 7th pin from stringList
        assertThat(component.getPin("stringList[7]")).isSameAs(stringListPin);
        assertThatThrownBy(() -> component.getInputPin("stringList[7]"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No Input Dynamic Pin with field name found: stringList[7]");
        assertThat(component.getOutputPin("stringList[7]")).isSameAs(stringListPin);

        // no suitable static pin exists
        assertThatThrownBy(() -> component.getPin("foobar"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No Static Pin with field name found: foobar");
        assertThatThrownBy(() -> component.getInputPin("foobar"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No Input Static Pin with field name found: foobar");
        assertThatThrownBy(() -> component.getOutputPin("foobar"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No Output Static Pin with field name found: foobar");

        // no suitable dynamic pin exists
        assertThatThrownBy(() -> component.getPin("stringList[99]"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No Dynamic Pin with field name found: stringList[99]");
        assertThatThrownBy(() -> component.getInputPin("stringList[99]"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No Input Dynamic Pin with field name found: stringList[99]");
        assertThatThrownBy(() -> component.getOutputPin("stringList[99]"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No Output Dynamic Pin with field name found: stringList[99]");
    }

    @Test
    @DisplayName("Get pin by UID")
    void testGetPinByUID() {
        final var component = createLogicComponent(
                new Logic() {
                    @Input
                    private boolean boolInputValue;

                    @Input
                    private Integer intInputValue;

                    @Input
                    private Iterable<Object> objects;

                    @Output
                    private String string;

                    @Output(min = 10)
                    private List<String> stringList;

                    @Override
                    public void logic() {
                        // NO-OP
                    }
                }
        );

        // existing input pin check
        final var existingUidInput = component.getInputPin(1).getUid();
        assertThat(component.getPin(existingUidInput)).isNotNull();
        assertThat(component.getPin(existingUidInput).getDescriptor().getName()).isEqualTo("intInputValue");
        assertThat(component.getInputPin(existingUidInput)).isNotNull();
        assertThat(component.getInputPin(existingUidInput).getDescriptor().getName()).isEqualTo("intInputValue");
        assertThatThrownBy(() -> component.getOutputPin(existingUidInput)).isInstanceOf(NoSuchElementException.class);

        // existing output pin check
        final var existingUidOutput = component.getOutputPin(5).getUid();
        assertThat(component.getPin(existingUidOutput)).isNotNull();
        assertThat(component.getPin(existingUidOutput).getDescriptor().getName()).isEqualTo("stringList");
        assertThatThrownBy(() -> component.getInputPin(existingUidOutput)).isInstanceOf(NoSuchElementException.class);
        assertThat(component.getOutputPin(existingUidOutput)).isNotNull();
        assertThat(component.getOutputPin(existingUidOutput).getDescriptor().getName()).isEqualTo("stringList");

        // non-existing check
        final var nonExistingUid = UIDFactory.createUid("fooUid");
        assertThatThrownBy(() -> component.getPin(nonExistingUid))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No Pin by UID found: fooUid");
        assertThatThrownBy(() -> component.getInputPin(nonExistingUid))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No Input Pin by UID found: fooUid");
        assertThatThrownBy(() -> component.getOutputPin(nonExistingUid))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No Output Pin by UID found: fooUid");

        // null check
        assertThatThrownBy(() -> component.getPin((UID) null))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No Pin by UID found: null");
        assertThatThrownBy(() -> component.getInputPin((UID) null))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No Input Pin by UID found: null");
        assertThatThrownBy(() -> component.getOutputPin((UID) null))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No Output Pin by UID found: null");
    }
}
