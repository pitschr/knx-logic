package experimental.api.v1.strategies;

import li.pitschmann.knx.logic.LogicRepository;
import li.pitschmann.knx.logic.exceptions.NoLogicClassFound;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.components.LogicC;

import java.util.Map;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

/**
 * Test for {@link LogicCreateStrategy}
 */
public class LogicCreateStrategyTest {

    @Test
    @DisplayName("Create Logic Component for a registered Logic")
    public void createLogicComponent() {
        final var logicRepositoryMock = mock(LogicRepository.class);
        doReturn(LogicC.class).when(logicRepositoryMock).findLogicClass(anyString());

        final var data = Map.of("class", "logic.mockClass");

        final var strategy = new LogicCreateStrategy(logicRepositoryMock);
        final var component = strategy.apply(data);

        assertThat(component).isNotNull();
        assertThat(component.getInputPins()).hasSize(1);
        assertThat(component.getOutputPins()).hasSize(1);

        // the wrapped component is a VARIABLE Inbox which contains one output connector
        final var wrappedComponent = component.getOutputConnector(0).getDescriptor().getOwner();
        assertThat(wrappedComponent).isInstanceOf(LogicC.class);
    }


    @Test
    @DisplayName("Create Logic Component for an unregistered Logic")
    public void createLogicComponentForUnregistered() {
        final var logicRepositoryMock = mock(LogicRepository.class);
        doThrow(new NoLogicClassFound("")).when(logicRepositoryMock).findLogicClass(anyString());

        final var data = Map.of("class", "logic.mockClass");

        final var strategy = new LogicCreateStrategy(logicRepositoryMock);
        assertThatThrownBy(() -> strategy.apply(data)).isInstanceOf(NoLogicClassFound.class);
    }
}
