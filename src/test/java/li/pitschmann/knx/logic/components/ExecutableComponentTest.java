package li.pitschmann.knx.logic.components;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link ExecutableComponent}
 */
public class ExecutableComponentTest {

    @Test
    @DisplayName("Test Execution Time on no execution")
    public void testExecutedTimeNoExecution() {
        final var component = mock(ExecutableComponent.class);
        when(component.executedTime()).thenReturn(0L);
        when(component.executedTime(any())).thenCallRealMethod();

        assertThat(component.executedTime(TimeUnit.NANOSECONDS)).isZero();
        assertThat(component.executedTime(TimeUnit.MILLISECONDS)).isZero();
        assertThat(component.executedTime(TimeUnit.SECONDS)).isZero();
    }

    @Test
    @DisplayName("Test Execution Time with executions")
    public void testExecutedTime() {
        final var component = mock(ExecutableComponent.class);
        when(component.executedTime()).thenReturn(24_000_000_000L);
        when(component.executedTime(any())).thenCallRealMethod();

        assertThat(component.executedTime(TimeUnit.NANOSECONDS)).isEqualTo(24_000_000_000L);
        assertThat(component.executedTime(TimeUnit.MILLISECONDS)).isEqualTo(24_000L);
        assertThat(component.executedTime(TimeUnit.SECONDS)).isEqualTo(24L);
    }

    @Test
    @DisplayName("Test Average Time on no execution")
    public void testAverageTimeNoExecution() {
        final var component = mock(ExecutableComponent.class);
        when(component.executedCount()).thenReturn(0L);
        when(component.executedTime()).thenReturn(0L);
        when(component.executedAverageTime()).thenCallRealMethod();
        when(component.executedAverageTime(any())).thenCallRealMethod();

        assertThat(component.executedAverageTime()).isZero();
        assertThat(component.executedAverageTime(TimeUnit.NANOSECONDS)).isZero();
        assertThat(component.executedAverageTime(TimeUnit.MICROSECONDS)).isZero();
        assertThat(component.executedAverageTime(TimeUnit.MILLISECONDS)).isZero();
        assertThat(component.executedAverageTime(TimeUnit.SECONDS)).isZero();
        assertThat(component.executedAverageTime(TimeUnit.MINUTES)).isZero();
        assertThat(component.executedAverageTime(TimeUnit.HOURS)).isZero();
        assertThat(component.executedAverageTime(TimeUnit.DAYS)).isZero();
    }

    @Test
    @DisplayName("Test Average Time with executions")
    public void testAverageTime() {
        final var component = mock(ExecutableComponent.class);
        when(component.executedCount()).thenReturn(1400L);
        when(component.executedTime()).thenReturn(Long.MAX_VALUE);
        when(component.executedAverageTime()).thenCallRealMethod();
        when(component.executedAverageTime(any())).thenCallRealMethod();

        assertThat(component.executedAverageTime()).isEqualTo(6.588122883467697E15);
        assertThat(component.executedAverageTime(TimeUnit.NANOSECONDS)).isEqualTo(6.588122883467697E15);
        assertThat(component.executedAverageTime(TimeUnit.MICROSECONDS)).isEqualTo(6.588122883467697E12);
        assertThat(component.executedAverageTime(TimeUnit.MILLISECONDS)).isEqualTo(6.588122883467E9);
        assertThat(component.executedAverageTime(TimeUnit.SECONDS)).isEqualTo(6588122.883);
        assertThat(component.executedAverageTime(TimeUnit.MINUTES)).isEqualTo(109802.048);
        assertThat(component.executedAverageTime(TimeUnit.HOURS)).isEqualTo(1830.034);
        assertThat(component.executedAverageTime(TimeUnit.DAYS)).isEqualTo(76.251);
    }
}
