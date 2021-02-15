package li.pitschmann.knx.logic;

import li.pitschmann.knx.core.utils.Sleeper;
import li.pitschmann.knx.logic.uid.UID;
import li.pitschmann.knx.logic.uid.UIDAware;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Testing the {@link Workflow}
 *
 * @author PITSCHR
 */
class WorkflowTest {

    @Test
    @DisplayName("Test empty workflow")
    void testEmptyWorkflow() {
        final var workflow = Workflow.create();

        // methods
        assertThat(workflow.calculateDuration()).isZero();
        assertThat(workflow.getFlows()).isEmpty();

        // toString()
        assertThat(workflow).hasToString("Workflow{duration=0, flows=[]}");
    }

    @Test
    @DisplayName("Test non-empty workflow")
    void testNonEmptyWorkflow() {
        final var uid1Mock = mock(UID.class);
        when(uid1Mock.toString()).thenReturn("UID-1");

        final var uid2Mock = mock(UID.class);
        when(uid2Mock.toString()).thenReturn("UID-2");

        final var uid3Mock = mock(UID.class);
        when(uid3Mock.toString()).thenReturn("UID-3");
        final var uidAware3Mock = mock(UIDAware.class);
        when(uidAware3Mock.getUid()).thenReturn(uid3Mock);

        final var uid4Mock = mock(UID.class);
        when(uid4Mock.toString()).thenReturn("UID-4");
        final var uidAware4Mock = mock(UIDAware.class);
        when(uidAware4Mock.getUid()).thenReturn(uid4Mock);


        final var workflowEmpty = Workflow.create();
        final var workflow1 = workflowEmpty.add(uid1Mock, "ONE");
        final var workflow2 = workflow1.add(uid2Mock, "TWO");
        final var workflow3 = workflow2.add(uidAware3Mock, "THREE");
        final var workflow4 = workflow3.add(uidAware4Mock, "FOUR");

        // reference check
        assertThat(workflowEmpty).isNotSameAs(workflow1);
        assertThat(workflow1).isNotSameAs(workflow2);
        assertThat(workflow2).isNotSameAs(workflow1);
        assertThat(workflow3).isNotSameAs(workflow1);
        assertThat(workflow4).isNotSameAs(workflow1);

        // flow check
        assertThat(workflowEmpty.getFlows()).isEmpty();
        assertThat(workflow1.getFlows()).hasSize(1);
        assertThat(workflow2.getFlows()).hasSize(2);
        assertThat(workflow3.getFlows()).hasSize(3);
        assertThat(workflow4.getFlows()).hasSize(4);

        // flow entries check
        final var flows = workflow4.getFlows();
        assertThat(flows.get(0).getUid()).hasToString("UID-1");
        assertThat(flows.get(0).getValue()).isEqualTo("ONE");
        assertThat(flows.get(1).getUid()).hasToString("UID-2");
        assertThat(flows.get(1).getValue()).isEqualTo("TWO");
        assertThat(flows.get(2).getUid()).hasToString("UID-3");
        assertThat(flows.get(2).getValue()).isEqualTo("THREE");
        assertThat(flows.get(3).getUid()).hasToString("UID-4");
        assertThat(flows.get(3).getValue()).isEqualTo("FOUR");

        // toString()
        assertThat(workflow4).hasToString(
                String.format("Workflow{duration=%s, flows=%s}", workflow4.calculateDuration(), flows)
        );
    }

    @Test
    @DisplayName("Calculate the duration of workflow with Inbox only")
    void testCalculateDurationInbox() {
        final var workflow = Workflow.create();

        // Input only. Duration should be ZERO
        //    Inbox
        //    .-----.
        //   |     [x]
        //    `-----´
        //          ^
        //          1
        Sleeper.milliseconds(100);   // delay
        final var workflowInbox = workflow.add(mock(UID.class), 1); // inbox
        Sleeper.milliseconds(100);   // delay

        assertThat(workflowInbox.calculateDuration()).isZero();
    }

    @Test
    @DisplayName("Calculate the duration of workflow with Inbox and Outbox only")
    void testCalculateDurationInboxAndOutbox() {
        final var workflow0 = Workflow.create();

        // Input only. Duration should be ZERO
        //    Inbox          Outbox
        //    .-----.       .------.
        //   |     [x] --> [x]      |
        //    `-----´       `------´
        //          ^       ^
        //          1       2
        Sleeper.milliseconds(100);   // delay
        final var workflow1 = workflow0.add(mock(UID.class), 1); // inbox
        Sleeper.milliseconds(100);   // delay
        final var workflow2 = workflow1.add(mock(UID.class), 2); // outbox
        Sleeper.milliseconds(100);   // delay

        assertThat(workflow2.calculateDuration()).isZero();
    }

    @Test
    @DisplayName("Calculate the duration of workflow with Inbox, 1 Component and Outbox")
    void testCalculateDurationOneComponent() {
        final var workflow0 = Workflow.create();

        // Input + One Component + Output. Duration should be calculated of Component #1
        //    Inbox        Component #1        Outbox
        //    .-----.       .---------.       .------.
        //   |     [x] --> [x]       [x] --> [x]      |
        //    `-----´       `---------´       `------´
        //          ^       ^         ^       ^
        //          1       2         3       4
        Sleeper.milliseconds(100);   // delay
        final var workflow1 = workflow0.add(mock(UID.class), 1); // inbox
        Sleeper.milliseconds(100);   // delay
        final var workflow2 = workflow1.add(mock(UID.class), 2); // Component #1 (input pin)
        Sleeper.milliseconds(50);    // delay
        final var workflow3 = workflow2.add(mock(UID.class), 3); // Component #1 (output pin)
        Sleeper.milliseconds(100);   // delay
        final var workflow4 = workflow3.add(mock(UID.class), 2); // outbox
        Sleeper.milliseconds(100);   // delay

        assertThat(workflow4.calculateDuration()).isBetween(30L, 70L);
    }

    @Test
    @DisplayName("Calculate the duration of workflow with Inbox, 2 Components and Outbox")
    void testCalculateDurationTwoComponents() {
        final var workflow0 = Workflow.create();

        // Input + Two Components + Output. Duration should be calculated of Component #1 + #2

        //    Inbox        Component #1      Component #2       Outbox
        //    .-----.       .---------.       .---------.       .------.
        //   |     [x] --> [x]       [x] --> [x]       [x] --> [x]      |
        //    `-----´       `---------´       `---------´       `------´
        //          ^       ^         ^       ^         ^       ^
        //          1       2         3       4         5       6
        Sleeper.milliseconds(100);   // delay
        final var workflow1 = workflow0.add(mock(UID.class), 1); // inbox
        Sleeper.milliseconds(100);   // delay
        final var workflow2 = workflow1.add(mock(UID.class), 2); // Component #1 (input pin)
        Sleeper.milliseconds(25);    // delay
        final var workflow3 = workflow2.add(mock(UID.class), 3); // Component #1 (output pin)
        Sleeper.milliseconds(100);   // delay
        final var workflow4 = workflow3.add(mock(UID.class), 4); // Component #2 (input pin)
        Sleeper.milliseconds(25);   // delay
        final var workflow5 = workflow4.add(mock(UID.class), 5); // Component #2 (output pin)
        Sleeper.milliseconds(100);   // delay
        final var workflow6 = workflow5.add(mock(UID.class), 6); // outbox
        Sleeper.milliseconds(100);   // delay

        assertThat(workflow6.calculateDuration()).isBetween(30L, 70L);
    }
}
