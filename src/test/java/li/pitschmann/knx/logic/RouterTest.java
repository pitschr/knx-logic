package li.pitschmann.knx.logic;

import li.pitschmann.knx.core.utils.Sleeper;
import li.pitschmann.knx.logic.components.Component;
import li.pitschmann.knx.logic.connector.Connector;
import li.pitschmann.knx.logic.connector.InputConnectorAware;
import li.pitschmann.knx.logic.descriptor.FieldDescriptor;
import li.pitschmann.knx.logic.event.Event;
import li.pitschmann.knx.logic.event.EventKey;
import li.pitschmann.knx.logic.exceptions.RouterException;
import li.pitschmann.knx.logic.pin.Pin;
import li.pitschmann.knx.logic.uid.UID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import test.TestHelpers;
import test.components.logic.AndLogic;
import test.components.logic.BooleanToIntegerLogic;
import test.components.logic.DelayLogic;
import test.components.logic.FunctionLogic;
import test.components.logic.IncrementLogic;
import test.components.logic.IntegerToBooleanLogic;
import test.components.logic.JoinerLogic;
import test.components.logic.NegationLogic;
import test.components.logic.ThroughputLogic;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static test.TestHelpers.createEvent;
import static test.TestHelpers.createInboxComponent;
import static test.TestHelpers.createLogicComponent;

/**
 * Test implementation of {@link Router}
 *
 * @author PITSCHR
 */
class RouterTest {

    /**
     * Simple Work-Flow
     *
     * <pre>
     *                 NOT Logic
     *                .---------.
     *     Inbox --> [x]       [x] --> Outbox
     *                `---------´
     * </pre>
     */
    @Test
    @DisplayName("Simple workflow with single NOT-Logic Component")
    void simpleWorkflow() throws ExecutionException, InterruptedException {
        final var router = spy(Router.createDefault());

        // --------------------------------------------------
        // create components
        // --------------------------------------------------
        final var inbox = createInboxComponent();
        final var notLogic = createLogicComponent(NegationLogic.class);
        final var outbox = TestHelpers.createOutboxComponent();

        // --------------------------------------------------
        // registering components
        // --------------------------------------------------
        router.register(inbox);
        router.register(notLogic);
        router.register(outbox);

        // --------------------------------------------------
        // linking components
        // --------------------------------------------------
        router.link(inbox.getOutputPin("data"), notLogic.getInputPin("input"));
        router.link(notLogic.getOutputPin("output"), outbox.getInputPin("data"));

        // ---------------------------------------
        // Test
        // ---------------------------------------
        final var falsyEvent = createEvent(inbox, false);
        final var truthEvent = createEvent(inbox, true);

        // Test #1  (input: false -> output: true)
        router.inbound(falsyEvent).get();
        assertThat(outbox.getData()).isEqualTo(true);

        // Test #2  (input: true -> output: false)
        router.inbound(truthEvent).get();
        assertThat(outbox.getData()).isEqualTo(false);

        // Test #3  (input: false -> output: true)
        router.inbound(falsyEvent).get();
        assertThat(outbox.getData()).isEqualTo(true);

        // Test #4 + 5 (no change)
        router.inbound(falsyEvent).get();
        assertThat(outbox.getData()).isEqualTo(true);
        router.inbound(falsyEvent).get();
        assertThat(outbox.getData()).isEqualTo(true);

        // Test #6  (input: true -> output: false)
        router.inbound(truthEvent).get();
        assertThat(outbox.getData()).isEqualTo(false);

        // Test #7 + 8 (no change)
        router.inbound(truthEvent).get();
        assertThat(outbox.getData()).isEqualTo(false);
        router.inbound(truthEvent).get();
        assertThat(outbox.getData()).isEqualTo(false);

        // --------------------------------------------------
        // Execution Count
        // --------------------------------------------------
        assertThat(notLogic.executedCount()).isEqualTo(8);  // executed
        assertThat(notLogic.logicCount()).isEqualTo(4);     // refreshed / value changes

        // --------------------------------------------------
        // History
        // --------------------------------------------------
        final var inputHistory = inbox.getHistory().copyAsList();
        assertThat(inputHistory.size()).isEqualTo(8);
        assertThat(inputHistory.get(0).getValue()).isEqualTo(false); // Test #1
        assertThat(inputHistory.get(1).getValue()).isEqualTo(true);  // Test #2
        assertThat(inputHistory.get(2).getValue()).isEqualTo(false); // Test #3
        assertThat(inputHistory.get(3).getValue()).isEqualTo(false); // Test #4 (no change)
        assertThat(inputHistory.get(4).getValue()).isEqualTo(false); // Test #5 (no change)
        assertThat(inputHistory.get(5).getValue()).isEqualTo(true);  // Test #6
        assertThat(inputHistory.get(6).getValue()).isEqualTo(true);  // Test #7 (no change)
        assertThat(inputHistory.get(7).getValue()).isEqualTo(true);  // Test #8 (no change)

        final var outputHistory = outbox.getHistory().copyAsList();
        assertThat(outputHistory.size()).isEqualTo(4);
        assertThat(outputHistory.get(0).getValue()).isEqualTo(true);  // Test #1
        assertThat(outputHistory.get(1).getValue()).isEqualTo(false); // Test #2
        assertThat(outputHistory.get(2).getValue()).isEqualTo(true);  // Test #3
        assertThat(outputHistory.get(3).getValue()).isEqualTo(false); // Test #6

        // check if the event channel received the values as well
        final var argumentCaptor = ArgumentCaptor.forClass(Event.class);
        verify(router, times(4)).outbound(argumentCaptor.capture());
        final var events = argumentCaptor.getAllValues();
        assertThat(events).hasSize(4);
        assertThat(events.get(0).getData()).isEqualTo(true);  // Test #1
        assertThat(events.get(1).getData()).isEqualTo(false); // Test #2
        assertThat(events.get(2).getData()).isEqualTo(true);  // Test #3
        assertThat(events.get(3).getData()).isEqualTo(false); // Test #6
    }

    /**
     * Long Pipeline Work-Flow
     *
     * <pre>
     *                Increment #1      Increment #2            Increment #1000
     *                .---------.       .---------.               .---------.
     *     Inbox --> [x]       [x] --> [x]       [x] --> ... --> [x]       [x] --> Outbox
     *                `---------´      `---------´                `---------´
     * </pre>
     */
    @Test
    @DisplayName("Long pipeline workflow with 1000 increment components")
    void longPipelineWorkflow() throws ExecutionException, InterruptedException {
        final var router = Router.createDefault();
        final var loops = 1000;

        // --------------------------------------------------
        // create components
        // --------------------------------------------------
        final var inbox = createInboxComponent();

        final var pipes = IntStream.range(0, loops)
                .mapToObj(i -> createLogicComponent(IncrementLogic.class))
                .collect(Collectors.toList());

        final var outbox = TestHelpers.createOutboxComponent();

        // --------------------------------------------------
        // registering components
        // --------------------------------------------------
        router.register(inbox);
        pipes.forEach(router::register);
        router.register(outbox);

        // --------------------------------------------------
        // linking pins
        // --------------------------------------------------
        // pipe(N-1) connected to pipe(N)
        var previousPin = inbox.getOutputPin("data");
        for (var pipe : pipes) {
            router.link(previousPin, pipe.getInputPin("input"));
            previousPin = pipe.getOutputPin("output");
        }

        // pipe(N) connected to output component
        router.link(previousPin, outbox.getInputPin("data"));

        // --------------------------------------------------
        // EXECUTION
        // (start with setting value to input component)
        // --------------------------------------------------
        final var workflows = router.inbound(createEvent(inbox, 0)).get();

        // --------------------------------------------------
        // VALIDATION
        // --------------------------------------------------
        assertThat(outbox.getData()).isEqualTo(loops);

        // 1 input component + 2 loop (input + output) + output component
        assertThat(workflows).hasSize(1);
        assertThat(workflows.get(0).getFlows()).hasSize(1 + loops * 2 + 1);
    }

    /**
     * Normal Pipe Work-Flow
     *
     * <pre>
     *                   AND Logic         Throughput AND     BooleanToInt      IntToBoolean
     *                   .---------.        .---------.       .---------.       .---------.
     *                   |       [AND] --> [x]       [x] --> [x]       [x] --> [x]       [x] --> Outbox #1
     * Inbox #1 ------> [0]        |        `---------´       `---------´       `---------´
     *                   |         |       Throughput NAND
     * Inbox #2 ------> [1]        |        .---------.
     *                   |      [NAND] --> [x]       [x] --------------------------------------> Outbox #2
     *                   `--------´         `---------´
     * </pre>
     */
    @Test
    @DisplayName("Normal workflow with AND/NAND creating two new workflow")
    void pipeWorkflow() throws ExecutionException, InterruptedException {
        final var router = Router.createDefault();

        // --------------------------------------------------
        // create components
        // --------------------------------------------------
        final var inbox1 = createInboxComponent();
        final var inbox2 = createInboxComponent();

        final var outbox1 = TestHelpers.createOutboxComponent();
        final var outbox2 = TestHelpers.createOutboxComponent();

        final var andLogic = createLogicComponent(AndLogic.class);

        final var throughputLogicAnd = createLogicComponent(ThroughputLogic.class);
        final var throughputLogicNand = createLogicComponent(ThroughputLogic.class);

        final var pipeLogicAndBooleanToInt = createLogicComponent(BooleanToIntegerLogic.class);
        final var pipeLogicAndIntToBoolean = createLogicComponent(IntegerToBooleanLogic.class);


        // --------------------------------------------------
        // registering components
        // --------------------------------------------------
        router.register(inbox1);
        router.register(inbox2);
        router.register(andLogic);
        router.register(throughputLogicAnd);
        router.register(throughputLogicNand);
        router.register(pipeLogicAndBooleanToInt);
        router.register(pipeLogicAndIntToBoolean);
        router.register(outbox1);
        router.register(outbox2);

        // --------------------------------------------------
        // linking components
        // --------------------------------------------------
        router.link(inbox1.getOutputPin("data"), andLogic.getInputPin("inputs[0]")); // 1st input of AND
        router.link(inbox2.getOutputPin("data"), andLogic.getInputPin("inputs[1]")); // 2nd input of AND

        router.link(andLogic.getOutputPin("output"), throughputLogicAnd.getInputPin("input")); // AND result to AND-PIPE
        router.link(throughputLogicAnd.getOutputPin("output"), pipeLogicAndBooleanToInt.getInputPin("input"));
        router.link(pipeLogicAndBooleanToInt.getOutputPin("output"), pipeLogicAndIntToBoolean.getInputPin("input"));
        router.link(pipeLogicAndIntToBoolean.getOutputPin("output"), outbox1.getInputPin("data")); // AND-PIPE to output of 1st output component

        router.link(andLogic.getOutputPin("outputNegation"), throughputLogicNand.getInputPin("input"));
        router.link(throughputLogicNand.getOutputPin("output"), outbox2.getInputPin("data"));

        // --------------------------------------------------
        // Test
        // Invoke inbox components
        // --------------------------------------------------
        final var event1 = createEvent(inbox1, true);
        final var event2 = createEvent(inbox2, true);
        final var workflow1 = router.inbound(event1).get();
        final var workflow2 = router.inbound(event2).get();

        // --------------------------------------------------
        // Verification
        // --------------------------------------------------
        assertThat(outbox1.getData()).isEqualTo(true);  // 1 & 1 = 1
        assertThat(outbox2.getData()).isEqualTo(false); // 1 & 1 = 1 (negation) -> 0

        // --------------------------------------------------
        // History Result (Inbox / Outbox Component)
        // --------------------------------------------------
        // One entry, because input1 is invoked only once time
        final var input1HistoryList = inbox1.getHistory().copyAsList();
        assertThat(input1HistoryList).hasSize(1);
        assertThat(input1HistoryList.get(0).getValue()).isEqualTo(true);

        // One entry, because input2 is invoked only once time
        final var input2HistoryList = inbox2.getHistory().copyAsList();
        assertThat(input2HistoryList).hasSize(1);
        assertThat(input2HistoryList.get(0).getValue()).isEqualTo(true);

        // Two entries, because input1 and input2 are invoked.
        // input #1 invoked: 1 & 0 -> false
        // input #2 invoked: 1 & 1 -> true
        final var output1HistoryList = outbox1.getHistory().copyAsList();
        assertThat(output1HistoryList).hasSize(2);
        assertThat(output1HistoryList.get(0).getValue()).isEqualTo(false);
        assertThat(output1HistoryList.get(1).getValue()).isEqualTo(true);

        // input #1 invoked: 1 & 0 -> false (negation) -> true
        // input #2 invoked: 1 & 1 -> true (negation) -> false
        final var output2HistoryList = outbox2.getHistory().copyAsList();
        assertThat(output2HistoryList).hasSize(2);
        assertThat(output2HistoryList.get(0).getValue()).isEqualTo(true);
        assertThat(output2HistoryList.get(1).getValue()).isEqualTo(false);

        // --------------------------------------------------
        // History Result (And Component)
        // --------------------------------------------------
        // Check AND/NAND output history of component 'And'

        // 1 change
        // Invoked by input #1: true
        // Invoked by input #2: (no change)
        final var input1And = andLogic.getInputPin("inputs[0]");
        assertThat(input1And.refreshCount()).isEqualTo(1);

        // 2 changes
        // Invoked by input #1: false (init value)
        // Invoked by input #2: true
        final var input2And = andLogic.getInputPin("inputs[1]");
        assertThat(input2And.refreshCount()).isEqualTo(2);

        // 2 changes
        // Invoked by input #1: false (1 & 0 = false)
        // Invoked by input #2: true  (1 & 1 = true)
        final var outputAnd = andLogic.getOutputPin("output");
        assertThat(outputAnd.refreshCount()).isEqualTo(2);

        // 2 changes
        // Invoked by input #1: true  (!(1 & 0) = !0 = true)
        // Invoked by input #2: false (!(1 & 1) = !1 = false)
        final var outputNand = andLogic.getOutputPin("outputNegation");
        assertThat(outputNand.refreshCount()).isEqualTo(2);

        // --------------------------------------------------
        // History Result (BooleanToInteger Component)
        // --------------------------------------------------
        // Check input and output history of component 'Boolean To Integer'

        // 2 changes
        // Invoked by outputAnd #1: false
        // Invoked by outputAnd #2: true
        final var booleanToIntInput = pipeLogicAndBooleanToInt.getInputPin("input");
        assertThat(booleanToIntInput.refreshCount()).isEqualTo(2);

        // 2 changes
        // Invoked by outputAnd #1: false -> 0
        // Invoked by outputAnd #2: true  -> 1
        final var booleanToIntOutput = pipeLogicAndBooleanToInt.getOutputPin("output");
        assertThat(booleanToIntOutput.refreshCount()).isEqualTo(2);

        // --------------------------------------------------
        // Workflow Result
        // --------------------------------------------------
        // work flows of 1st submit
        assertThat(workflow1).hasSize(2);
        final var workflowFlows1_0 = workflow1.get(0).getFlows();
        final var workflowFlows1_1 = workflow1.get(1).getFlows();
        assertThat(workflowFlows1_0).hasSize(10);    // 1st workflow of 1st submit (AND)
        assertThat(workflowFlows1_1).hasSize(6);     // 2nd workflow of 1st submit (NAND)

        assertThat(workflowFlows1_0.get(workflowFlows1_0.size() - 1).getValue()).isEqualTo(Boolean.FALSE); // AND
        assertThat(workflowFlows1_1.get(workflowFlows1_1.size() - 1).getValue()).isEqualTo(Boolean.TRUE);  // NAND

        // work flows of 2nd submit
        assertThat(workflow2).hasSize(2);
        final var workflowFlows2_0 = workflow2.get(0).getFlows();
        final var workflowFlows2_1 = workflow2.get(1).getFlows();
        assertThat(workflowFlows2_0).hasSize(10);    // 1st workflow of 2nd submit (AND)
        assertThat(workflowFlows2_1).hasSize(6);     // 2nd workflow of 2nd submit (NAND)
        assertThat(workflowFlows2_0.get(workflowFlows2_0.size() - 1).getValue()).isEqualTo(Boolean.TRUE);   // AND
        assertThat(workflowFlows2_1.get(workflowFlows2_1.size() - 1).getValue()).isEqualTo(Boolean.FALSE);  // NAND
    }

    /**
     * Work-Flow with two pipes. The Output #2 should be non-blocked
     * while Output #1 may take several seconds due the {@link DelayLogic}.
     *
     * <pre>
     *                 Throughput #1      Delay Logic
     *                 .---------.        .---------.
     *            ,-- [x]       [x] ---> [x]       [x] --> Outbox #1
     *           /     `---------´        `---------´
     *   Inbox -+
     *          \      Throughput #2
     *           \     .---------.
     *            `-- [x]       [x] ---------------------> Outbox #2
     *                 `---------´
     * </pre>
     */
    @Test
    @DisplayName("Workflow with two pipes (one with delay, one without delay)")
    void workflowDelayPipe() throws ExecutionException, InterruptedException {
        final var router = Router.createDefault();

        // --------------------------------------------------
        // create components
        // --------------------------------------------------
        final var inbox = createInboxComponent();

        final var outbox1 = TestHelpers.createOutboxComponent();
        final var outbox2 = TestHelpers.createOutboxComponent();

        final var throughputLogic1 = createLogicComponent(ThroughputLogic.class);
        final var throughputLogic2 = createLogicComponent(ThroughputLogic.class);
        final var delayLogic = createLogicComponent(DelayLogic.class);

        // --------------------------------------------------
        // registering components
        // --------------------------------------------------
        router.register(inbox);
        router.register(throughputLogic1);
        router.register(throughputLogic2);
        router.register(delayLogic);
        router.register(outbox1);
        router.register(outbox2);

        // --------------------------------------------------
        // linking components
        // --------------------------------------------------

        // path 1
        router.link(inbox.getOutputPin("data"), throughputLogic1.getInputPin("input"));
        router.link(throughputLogic1.getOutputPin("output"), delayLogic.getInputPin("input"));
        router.link(delayLogic.getOutputPin("output"), outbox1.getInputPin("data"));
        // path 2
        router.link(inbox.getOutputPin("data"), throughputLogic2.getInputPin("input"));
        router.link(throughputLogic2.getOutputPin("output"), outbox2.getInputPin("data"));

        // ---------------------------------------
        // Test
        // ---------------------------------------
        final var event = createEvent(inbox, true);
        final var workflows = router.inbound(event).get();

        // ---------------------------------------
        // Verification
        // ---------------------------------------
        assertThat(workflows).hasSize(2);
        assertThat(workflows.get(0).calculateDuration()).isGreaterThan(2950L); // 3 sec delay, but sleep is not accurate
        assertThat(workflows.get(1).calculateDuration()).isLessThan(100L);     // 2nd workflow should be significant faster

        var workflowOutput1 = workflows.get(0).getFlows(); // 1st workflow
        assertThat(workflowOutput1).hasSize(6);
        assertThat(workflowOutput1.get(0).getUid()).isSameAs(inbox.getOutputPin("data").getUid());
        assertThat(workflowOutput1.get(1).getUid()).isSameAs(throughputLogic1.getInputPin("input").getUid());
        assertThat(workflowOutput1.get(2).getUid()).isSameAs(throughputLogic1.getOutputPin("output").getUid());
        assertThat(workflowOutput1.get(3).getUid()).isSameAs(delayLogic.getInputPin("input").getUid());
        assertThat(workflowOutput1.get(4).getUid()).isSameAs(delayLogic.getOutputPin("output").getUid());
        assertThat(workflowOutput1.get(5).getUid()).isSameAs(outbox1.getInputPin("data").getUid());

        var workflowOutput2 = workflows.get(1).getFlows(); // 2nd workflow
        assertThat(workflowOutput2).hasSize(4);
        assertThat(workflowOutput2.get(0).getUid()).isSameAs(inbox.getOutputPin("data").getUid());
        assertThat(workflowOutput2.get(1).getUid()).isSameAs(throughputLogic2.getInputPin("input").getUid());
        assertThat(workflowOutput2.get(2).getUid()).isSameAs(throughputLogic2.getOutputPin("output").getUid());
        assertThat(workflowOutput2.get(3).getUid()).isSameAs(outbox2.getInputPin("data").getUid());
    }

    /**
     * Parallel Work-Flow. The Logic Diagram #2 should be completed before
     * Logic Diagram #1 although the Logic Diagram #1 is invoked first but
     * takes more time due {@link DelayLogic}.
     *
     * <pre>
     * LOGIC DIAGRAM #1
     * ----------------
     *                 Delay Logic
     *                 .---------.
     *   Inbox #1 --> [x]       [x] --> Outbox #1
     *                 `---------´
     *
     * LOGIC DIAGRAM #2
     * ----------------
     *               Throughput Logic
     *                 .---------.
     *   Inbox #2 --> [x]       [x] --> Outbox #2
     *                 `---------´
     * </pre>
     */
    @Test
    @DisplayName("Workflow with two logic diagrams (one with delay, one without delay)")
    void workflowParallel() throws ExecutionException, InterruptedException {
        final var router = Router.createDefault();

        // --------------------------------------------------
        // create components
        // --------------------------------------------------
        // Logic Diagram #1
        final var inbox1 = createInboxComponent();
        final var delayLogic = createLogicComponent(DelayLogic.class);
        final var outbox1 = TestHelpers.createOutboxComponent();

        // Logic Diagram #2
        final var inbox2 = createInboxComponent();
        final var throughputLogic = createLogicComponent(ThroughputLogic.class);
        final var outbox2 = TestHelpers.createOutboxComponent();

        // --------------------------------------------------
        // registering components
        // --------------------------------------------------
        // Logic Diagram #1
        router.register(inbox1);
        router.register(delayLogic);
        router.register(outbox1);

        // Logic Diagram #2
        router.register(inbox2);
        router.register(throughputLogic);
        router.register(outbox2);

        // --------------------------------------------------
        // linking components
        // --------------------------------------------------
        // Logic Diagram #1
        router.link(inbox1.getOutputPin("data"), delayLogic.getInputPin("input"));
        router.link(delayLogic.getOutputPin("output"), outbox1.getInputPin("data"));

        // Logic Diagram #2
        router.link(inbox2.getOutputPin("data"), throughputLogic.getInputPin("input"));
        router.link(throughputLogic.getOutputPin("output"), outbox2.getInputPin("data"));

        // ---------------------------------------
        // Test
        // ---------------------------------------
        final var event1 = createEvent(inbox1, true);
        final var event2 = createEvent(inbox2, true);
        final var workflowFuture1 = router.inbound(event1);
        Sleeper.seconds(1); // one second delay (workflow 1 is non-blocking)
        final var workflowFuture2 = router.inbound(event2);

        // ---------------------------------------
        // Verification
        // ---------------------------------------
        final var workflow1 = workflowFuture1.get();
        final var workflow2 = workflowFuture2.get();

        // InboxComponent of 1st logic diagram should be invoked BEFORE 2nd logic diagram
        final var instantInbox1 = workflow1.get(0).getFlows().get(0).getInstant();
        final var instantInbox2 = workflow2.get(0).getFlows().get(0).getInstant();
        assertThat(instantInbox1).isBefore(instantInbox2);

        // OutboxComponent of 1st logic diagram should be invoked AFTER 2nd logic diagram
        final var instantOutbox1 = workflow1.get(0).getFlows().get(3).getInstant();
        final var instantOutbox2 = workflow2.get(0).getFlows().get(3).getInstant();
        assertThat(instantOutbox1).isAfter(instantOutbox2);
    }

    /**
     * Work-Flow with two pipes. First pipe is with delay, second pipe
     * is fast (no delay). Both pipes will be joined afterwards.
     * <p>
     * Input is 'a' and expected final output is 'a A'
     *
     * <pre>
     *               Throughput Logic     Delay Logic
     *                 .---------.        .---------.
     *            ,-- [x]       [x] ---> [x]       [x] -.     Joiner Logic
     *           /     `---------´        `---------´   |     .---------.
     *   Inbox -+                                       `--> [0]        |
     *          \     Function Logic (Upper-Case)             |        [x] --> Outbox
     *           \            .---------.               .--> [1]        |
     *            `--------- [x]       [x] ------------´      `--------´
     *                        `---------´
     * </pre>
     */
    @Test
    @DisplayName("Workflow with two pipes (one with delay, one without delay) and will be joined")
    void workflowDelayAndJoinAfterwards() throws ExecutionException, InterruptedException {
        final var router = Router.createDefault();

        // --------------------------------------------------
        // create components
        // --------------------------------------------------
        final var inbox = createInboxComponent();
        final var pipeLogic = createLogicComponent(ThroughputLogic.class);
        final var delayLogic = createLogicComponent(DelayLogic.class);
        final var upperCaseLogic = createLogicComponent(new FunctionLogic<String, String>(String::toUpperCase));
        final var joinerLogic = createLogicComponent(JoinerLogic.class);
        final var outbox = TestHelpers.createOutboxComponent();

        // --------------------------------------------------
        // registering components
        // --------------------------------------------------
        router.register(inbox);
        router.register(pipeLogic);
        router.register(delayLogic);
        router.register(upperCaseLogic);
        router.register(joinerLogic);
        router.register(outbox);

        // --------------------------------------------------
        // linking components
        // --------------------------------------------------

        // path 1
        router.link(inbox.getOutputPin("data"), pipeLogic.getInputPin("input"));
        router.link(pipeLogic.getOutputPin("output"), delayLogic.getInputPin("input"));
        router.link(delayLogic.getOutputPin("output"), joinerLogic.getInputPin("inputs[0]"));
        // path 2
        router.link(inbox.getOutputPin("data"), upperCaseLogic.getInputPin("input"));
        router.link(upperCaseLogic.getOutputPin("output"), joinerLogic.getInputPin("inputs[1]"));

        // joined from path 1 + 2
        router.link(joinerLogic.getOutputPin("output"), outbox.getInputPin("data"));

        // ---------------------------------------
        // Test
        // ---------------------------------------
        final var event = createEvent(inbox, "a");
        final var workflows = router.inbound(event).get();

        // ---------------------------------------
        // Verification
        // ---------------------------------------
        assertThat(workflows).hasSize(2);
        assertThat(workflows.get(0).calculateDuration()).isGreaterThan(2950L); // 3 sec delay, but sleep is not accurate
        assertThat(workflows.get(1).calculateDuration()).isLessThan(100L);     // 2nd workflow should be significant faster

        final var historyEntries = outbox.getHistory().copyAsList();
        assertThat(historyEntries).hasSize(2);
        // Joiner Input[0]="a", Input[1]=null
        assertThat(historyEntries.get(0).getValue()).isEqualTo("a");
        // Joiner Input[0]="a", Input[1]="A"
        assertThat(historyEntries.get(1).getValue()).isEqualTo("a A");
    }

    /**
     * Test link / unlink of path. At the beginning the workflow should look like
     * {@code 1) Inbox -> Replacer #1 -> Outbox} and after change the path the
     * workflow should look like {@code 2) Inbox -> Replacer #2 -> Outbox}
     * <ul>
     *     <li>The input is always 'A'.</li>
     *     <li>In first path the replacer changes from 'A' to 'B' and the output should be 'B'.</li>
     *     <li>In first path the replacer changes from 'A' to 'C' and the output should be 'C'.</li>
     * </ul>
     *
     * <pre>
     * Path #1
     * -------
     *               Function Logic (Replace: 'A' -> 'B')
     *                           .---------.
     *            ,------------ [x]       [x] --------------.
     *           /               `---------´                 \
     *          /                                             \
     *  Inbox -´     Function Logic (Replace: 'A' -> 'C')      `--> Outbox
     *                           .---------.
     *                          [x]       [x]
     *                           `---------´
     *
     * Path #2
     * -------
     *               Function Logic (Replace: 'A' -> 'B')
     *                           .---------.
     *                          [x]       [x]
     *                           `---------´
     *
     *  Inbox -.     Function Logic (Replace: 'A' -> 'C')      ,--> Outbox
     *          \                .---------.                  /
     *           `------------- [x]       [x] ---------------´
     *                           `---------´
     * </pre>
     */
    @Test
    @DisplayName("Change path of workflow")
    void workflowChangePath() throws ExecutionException, InterruptedException {
        final var router = Router.createDefault();

        // --------------------------------------------------
        // create components
        // --------------------------------------------------
        final var inbox = createInboxComponent();
        final var replaceLogic1 = createLogicComponent(new FunctionLogic<String, String>(s -> s.replaceAll("A", "B")));
        final var replaceLogic2 = createLogicComponent(new FunctionLogic<String, String>(s -> s.replaceAll("A", "C")));
        final var outbox = TestHelpers.createOutboxComponent();

        final var testEvent = createEvent(inbox, "A");

        // --------------------------------------------------
        // registering components
        // --------------------------------------------------
        router.register(inbox);
        router.register(replaceLogic1);
        router.register(replaceLogic2);
        router.register(outbox);

        // --------------------------------------------------
        // linking for Path #1
        // --------------------------------------------------
        router.link(inbox.getOutputPin("data"), replaceLogic1.getInputPin("input"));
        router.link(replaceLogic1.getOutputPin("output"), outbox.getInputPin("data"));

        // ---------------------------------------
        // Test & Verification (Path #1)
        // ---------------------------------------
        final var workflow_1 = router.inbound(testEvent).get();
        assertThat(outbox.getHistory().values()).containsExactly("B");

        assertThat(workflow_1).hasSize(1);
        final var workflowFlows_1 = workflow_1.get(0).getFlows();
        assertThat(workflowFlows_1).hasSize(4);
        assertThat(workflowFlows_1.get(0).getUid()).isEqualTo(inbox.getOutputPin("data").getUid());
        assertThat(workflowFlows_1.get(1).getUid()).isEqualTo(replaceLogic1.getInputPin("input").getUid());
        assertThat(workflowFlows_1.get(2).getUid()).isEqualTo(replaceLogic1.getOutputPin("output").getUid());
        assertThat(workflowFlows_1.get(3).getUid()).isEqualTo(outbox.getInputPin("data").getUid());

        // --------------------------------------------------
        // re-linking for Path #2
        // --------------------------------------------------
        router.unlink(inbox.getOutputPin("data"), replaceLogic1.getInputPin("input"));
        router.unlink(replaceLogic1.getOutputPin("output"), outbox.getInputPin("data"));

        router.link(inbox.getOutputPin("data"), replaceLogic2.getInputPin("input"));
        router.link(replaceLogic2.getOutputPin("output"), outbox.getInputPin("data"));

        // ---------------------------------------
        // Test & Verification (Path #2)
        // ---------------------------------------
        final var workflow_2 = router.inbound(testEvent).get();
        assertThat(outbox.getHistory().values()).containsExactly("B", "C");

        assertThat(workflow_2).hasSize(1);
        final var workflowFlows_2 = workflow_2.get(0).getFlows();
        assertThat(workflowFlows_2).hasSize(4);
        assertThat(workflowFlows_2.get(0).getUid()).isEqualTo(inbox.getOutputPin("data").getUid());
        assertThat(workflowFlows_2.get(1).getUid()).isEqualTo(replaceLogic2.getInputPin("input").getUid());
        assertThat(workflowFlows_2.get(2).getUid()).isEqualTo(replaceLogic2.getOutputPin("output").getUid());
        assertThat(workflowFlows_2.get(3).getUid()).isEqualTo(outbox.getInputPin("data").getUid());
    }

    /**
     * Test de-registration of inbox.
     * <p>
     * Inbox #1 and #2 have same key. Before de-registration it is expected that
     * Outbox will be triggered twice times (with 'A' and 'B').
     * After de-registration of Inbox #1 it it should be triggered once time
     *
     * <pre>
     *                  JOINER #1 ('A')
     *                    .---------.
     *   Inbox #1 -----> [x]       [x] -----------.
     *                    `---------´              |
     *                                             |
     *                  JOINER #2 ('B')            |
     *                    .---------.              |
     *   Inbox #2 -----> [x]       [x] ------------+---> Outbox
     *                    `---------´              |
     *                                             |
     *                  JOINER #3 ('C')            |
     *                    .---------.              |
     *   Inbox #3 -----> [x]       [x] -----------´
     *                    `---------´
     * </pre>
     */
    @Test
    @DisplayName("De-Registration of Components")
    void testDeRegistration() throws ExecutionException, InterruptedException {
        final var router = Router.createDefault();

        // --------------------------------------------------
        // create components
        // --------------------------------------------------
        final var key = "foo";

        final var inbox1 = createInboxComponent(key);
        final var inbox2 = createInboxComponent(key);
        final var inbox3 = createInboxComponent(key);

        final var appendLogic1 = createLogicComponent(new FunctionLogic<String, String>(s -> s + "A")); // Append 'A'
        final var appendLogic2 = createLogicComponent(new FunctionLogic<String, String>(s -> s + "B")); // Append 'B'
        final var appendLogic3 = createLogicComponent(new FunctionLogic<String, String>(s -> s + "C")); // Append 'C'

        final var outbox = TestHelpers.createOutboxComponent();

        // --------------------------------------------------
        // registering components
        // --------------------------------------------------
        router.register(inbox1);
        router.register(inbox2);
        router.register(inbox3);
        router.register(appendLogic1);
        router.register(appendLogic2);
        router.register(appendLogic3);
        router.register(outbox);

        // --------------------------------------------------
        // linking components
        // --------------------------------------------------
        router.link(inbox1.getOutputPin("data"), appendLogic1.getInputPin("input"));
        router.link(inbox2.getOutputPin("data"), appendLogic2.getInputPin("input"));
        router.link(inbox3.getOutputPin("data"), appendLogic3.getInputPin("input"));
        router.link(appendLogic1.getOutputPin("output"), outbox.getInputPin("data"));
        router.link(appendLogic2.getOutputPin("output"), outbox.getInputPin("data"));
        router.link(appendLogic3.getOutputPin("output"), outbox.getInputPin("data"));

        // ---------------------------------------
        // Test & Verification #1
        // ---------------------------------------
        final var event = createEvent(key, "a");
        final var workflow = router.inbound(event).get();
        assertThat(workflow).hasSize(3);
        assertThat(workflow.get(0).getFlows()).hasSize(4);
        assertThat(workflow.get(1).getFlows()).hasSize(4);
        assertThat(workflow.get(2).getFlows()).hasSize(4);
        assertThat(outbox.getHistory().values()).containsExactlyInAnyOrder("aA", "aB", "aC");

        // ---------------------------------------
        // Test & Verification #2
        // (De-Register Inbox #2)
        // ---------------------------------------
        router.deregister(inbox2);

        final var event2 = createEvent(key, "b");
        final var workflow2 = router.inbound(event2).get();
        assertThat(workflow2).hasSize(2);
        assertThat(workflow2.get(0).getFlows()).hasSize(4);
        assertThat(workflow2.get(1).getFlows()).hasSize(4);
        assertThat(outbox.getHistory().values()).containsExactlyInAnyOrder("aA", "aB", "aC", "bA", "bC");

        // ---------------------------------------
        // Test & Verification #3
        // (De-Register Joiner #1)
        // ---------------------------------------
        router.deregister(appendLogic1);

        final var event3 = createEvent(key, "c");
        final var workflow3 = router.inbound(event3).get();
        assertThat(workflow3).hasSize(2); // Inbox #1 (dead-end), Inbox #3 (completed)

        // find workflow for Inbox #1  --> this non-completed workflow (dead-end)
        final var input1Workflow = workflow3
                .stream()
                .filter(w -> inbox1.getOutputPin("data").getUid().equals(w.getFlows().get(0).getUid()))
                .findFirst()
                .orElseThrow();
        assertThat(input1Workflow.getFlows()).hasSize(1);

        // find workflow for Inbox #3  --> with completed workflow and value add to outbox
        final var input3Workflow = workflow3
                .stream()
                .filter(w -> inbox3.getOutputPin("data").getUid().equals(w.getFlows().get(0).getUid()))
                .findFirst()
                .orElseThrow();
        assertThat(input3Workflow.getFlows()).hasSize(4);

        assertThat(outbox.getHistory().values()).containsExactlyInAnyOrder("aA", "aB", "aC", "bA", "bC", "cC");
    }

    /**
     * Verify subscribers after removal several components
     *
     * <ol>
     *     <li>Removal of Throughput Logic #2</li>
     *     <li>Removal of Inbox #3</li>
     *     <li>Removal of Throughput Logic #3</li>
     *     <li>Removal of Outbox #1</li>
     * </ol>
     *
     * <pre>
     *                             Throughput #1
     *                              .---------.
     *   Inbox #1 ---------------> [x]       [x] -----------.
     *                              `---------´              \
     *                                                        > ---> Outbox #1
     *   Inbox #2 ---------.       Throughput  #2            /
     *                      \       .---------.             /
     *                       >---> [x]       [x] -----.----´
     *                      /       `---------´        \
     *   Inbox #3 ----.---´                             \
     *                 \           Throughput  #3        > --------> Outbox #2
     *                  \           .---------.         /
     *                   >-------> [x]       [x] ------´
     *                  /           `---------´
     *   Inbox #4 -----´
     * </pre>
     */
    @Test
    @DisplayName("Verify the behavior of subscribers when removing a component between inbox and outbox")
    void verifySubscribersRemovalMultipleComponents() {
        final var router = Router.createDefault();

        // --------------------------------------------------
        // create components
        // --------------------------------------------------
        final var inbox1 = createInboxComponent();
        final var inbox2 = createInboxComponent();
        final var inbox3 = createInboxComponent();
        final var inbox4 = createInboxComponent();

        final var throughputLogic1 = createLogicComponent(ThroughputLogic.class);
        final var throughputLogic2 = createLogicComponent(ThroughputLogic.class);
        final var throughputLogic3 = createLogicComponent(ThroughputLogic.class);

        final var outbox1 = TestHelpers.createOutboxComponent();
        final var outbox2 = TestHelpers.createOutboxComponent();

        // --------------------------------------------------
        // linking components
        // --------------------------------------------------
        final var inbox1Pin = inbox1.getOutputPin("data");
        final var inbox2Pin = inbox2.getOutputPin("data");
        final var inbox3Pin = inbox3.getOutputPin("data");
        final var inbox4Pin = inbox4.getOutputPin("data");
        final var pipe1InputPin = throughputLogic1.getInputPin("input");
        final var pipe2InputPin = throughputLogic2.getInputPin("input");
        final var pipe3InputPin = throughputLogic3.getInputPin("input");
        final var pipe1OutputPin = throughputLogic1.getOutputPin("output");
        final var pipe2OutputPin = throughputLogic2.getOutputPin("output");
        final var pipe3OutputPin = throughputLogic3.getOutputPin("output");
        final var outbox1Pin = outbox1.getInputPin("data");
        final var outbox2Pin = outbox2.getInputPin("data");

        router.link(inbox1Pin, pipe1InputPin);
        router.link(inbox2Pin, pipe2InputPin);
        router.link(inbox3Pin, pipe2InputPin);
        router.link(inbox3Pin, pipe3InputPin);
        router.link(inbox4Pin, pipe3InputPin);
        router.link(pipe1OutputPin, outbox1Pin);
        router.link(pipe2OutputPin, outbox1Pin);
        router.link(pipe2OutputPin, outbox2Pin);
        router.link(pipe3OutputPin, outbox2Pin);

        // verification (after linking)
        assertThat(router.getSubscribers(inbox1Pin)).containsExactly(pipe1InputPin);
        assertThat(router.getSubscribers(inbox2Pin)).containsExactly(pipe2InputPin);
        assertThat(router.getSubscribers(inbox3Pin)).containsExactlyInAnyOrder(pipe2InputPin, pipe3InputPin);
        assertThat(router.getSubscribers(inbox4Pin)).containsExactly(pipe3InputPin);
        assertThat(router.getSubscribers(pipe1InputPin)).containsExactly(inbox1Pin); // reverse
        assertThat(router.getSubscribers(pipe2InputPin)).containsExactlyInAnyOrder(inbox2Pin, inbox3Pin); // reverse
        assertThat(router.getSubscribers(pipe3InputPin)).containsExactlyInAnyOrder(inbox3Pin, inbox4Pin); // reverse

        assertThat(router.getSubscribers(pipe1OutputPin)).containsExactly(outbox1Pin);
        assertThat(router.getSubscribers(pipe2OutputPin)).containsExactlyInAnyOrder(outbox1Pin, outbox2Pin);
        assertThat(router.getSubscribers(pipe3OutputPin)).containsExactly(outbox2Pin);
        assertThat(router.getSubscribers(outbox1Pin)).containsExactlyInAnyOrder(pipe1OutputPin, pipe2OutputPin); // reverse
        assertThat(router.getSubscribers(outbox2Pin)).containsExactlyInAnyOrder(pipe2OutputPin, pipe3OutputPin); // reverse

        // --------------------------------------------------
        // Test #1 (Remove Pipe Logic #2)
        // --------------------------------------------------
        router.deregister(throughputLogic2);

        // verification
        assertThat(router.getSubscribers(inbox1Pin)).containsExactly(pipe1InputPin);
        assertThat(router.getSubscribers(inbox2Pin)).isEmpty();
        assertThat(router.getSubscribers(inbox3Pin)).containsExactly(pipe3InputPin);
        assertThat(router.getSubscribers(inbox4Pin)).containsExactly(pipe3InputPin);
        assertThat(router.getSubscribers(pipe1InputPin)).containsExactly(inbox1Pin);                      // reverse
        assertThat(router.getSubscribers(pipe2InputPin)).isEmpty();                                       // reverse
        assertThat(router.getSubscribers(pipe3InputPin)).containsExactlyInAnyOrder(inbox3Pin, inbox4Pin); // reverse

        assertThat(router.getSubscribers(pipe1OutputPin)).containsExactly(outbox1Pin);
        assertThat(router.getSubscribers(pipe2OutputPin)).isEmpty();
        assertThat(router.getSubscribers(pipe3OutputPin)).containsExactly(outbox2Pin);
        assertThat(router.getSubscribers(outbox1Pin)).containsExactly(pipe1OutputPin); // reverse
        assertThat(router.getSubscribers(outbox2Pin)).containsExactly(pipe3OutputPin); // reverse

        // --------------------------------------------------
        // Test #2 (Remove Inbox #3)
        // --------------------------------------------------
        router.deregister(inbox3);

        // verification
        assertThat(router.getSubscribers(inbox1Pin)).containsExactly(pipe1InputPin);
        assertThat(router.getSubscribers(inbox2Pin)).isEmpty();
        assertThat(router.getSubscribers(inbox3Pin)).isEmpty();
        assertThat(router.getSubscribers(inbox4Pin)).containsExactly(pipe3InputPin);
        assertThat(router.getSubscribers(pipe1InputPin)).containsExactly(inbox1Pin); // reverse
        assertThat(router.getSubscribers(pipe2InputPin)).isEmpty();                  // reverse
        assertThat(router.getSubscribers(pipe3InputPin)).containsExactly(inbox4Pin); // reverse

        assertThat(router.getSubscribers(pipe1OutputPin)).containsExactly(outbox1Pin);
        assertThat(router.getSubscribers(pipe2OutputPin)).isEmpty();
        assertThat(router.getSubscribers(pipe3OutputPin)).containsExactly(outbox2Pin);
        assertThat(router.getSubscribers(outbox1Pin)).containsExactly(pipe1OutputPin); // reverse
        assertThat(router.getSubscribers(outbox2Pin)).containsExactly(pipe3OutputPin); // reverse

        // --------------------------------------------------
        // Test #3 (Remove Pipe Logic #3)
        // --------------------------------------------------
        router.deregister(throughputLogic3);

        // verification
        assertThat(router.getSubscribers(inbox1Pin)).containsExactly(pipe1InputPin);
        assertThat(router.getSubscribers(inbox2Pin)).isEmpty();
        assertThat(router.getSubscribers(inbox3Pin)).isEmpty();
        assertThat(router.getSubscribers(inbox4Pin)).isEmpty();
        assertThat(router.getSubscribers(pipe1InputPin)).containsExactly(inbox1Pin); // reverse
        assertThat(router.getSubscribers(pipe2InputPin)).isEmpty();                  // reverse
        assertThat(router.getSubscribers(pipe3InputPin)).isEmpty();                  // reverse

        assertThat(router.getSubscribers(pipe1OutputPin)).containsExactly(outbox1Pin);
        assertThat(router.getSubscribers(pipe2OutputPin)).isEmpty();
        assertThat(router.getSubscribers(pipe3OutputPin)).isEmpty();
        assertThat(router.getSubscribers(outbox1Pin)).containsExactly(pipe1OutputPin); // reverse
        assertThat(router.getSubscribers(outbox2Pin)).isEmpty();                       // reverse

        // --------------------------------------------------
        // Test #4 (Remove Outbox)
        // --------------------------------------------------
        router.deregister(outbox1);

        // verification
        assertThat(router.getSubscribers(inbox1Pin)).containsExactly(pipe1InputPin);
        assertThat(router.getSubscribers(inbox2Pin)).isEmpty();
        assertThat(router.getSubscribers(inbox3Pin)).isEmpty();
        assertThat(router.getSubscribers(inbox4Pin)).isEmpty();
        assertThat(router.getSubscribers(pipe1InputPin)).containsExactly(inbox1Pin); // reverse
        assertThat(router.getSubscribers(pipe2InputPin)).isEmpty();                  // reverse
        assertThat(router.getSubscribers(pipe3InputPin)).isEmpty();                  // reverse

        assertThat(router.getSubscribers(pipe1OutputPin)).isEmpty();
        assertThat(router.getSubscribers(pipe2OutputPin)).isEmpty();
        assertThat(router.getSubscribers(pipe3OutputPin)).isEmpty();
        assertThat(router.getSubscribers(outbox1Pin)).isEmpty();      // reverse
        assertThat(router.getSubscribers(outbox2Pin)).isEmpty();      // reverse
    }

    /**
     * Verify the removal of Pin at inbox (source). Flow:
     * <ul>
     *     <li>Inbox #1 -> Outbox #1</li>
     *     <li>Inbox #2 -> Outbox #1 and #2</li>
     *     <li>Inbox #3 -> Outbox #2</li>
     * </ul>
     * <p>
     * Before:
     * <pre>
     *   Inbox #1 ----------.
     *                       >----> Outbox #1
     *   Inbox #2 ----------<
     *                       >----> Outbox #2
     *   Inbox #3 ----------´
     * </pre>
     * <p>
     * After:
     * <pre>
     *   Inbox #1 ----------.
     *                       `---> Outbox #1
     *   Inbox #2 (=no link)
     *                       .---> Outbox #2
     *   Inbox #3 ----------´
     * </pre>
     */
    @Test
    @DisplayName("Verify the unlink of an source pin")
    void verifyUnlinkSourcePin() {
        final var router = Router.createDefault();

        // --------------------------------------------------
        // create components
        // --------------------------------------------------
        final var inbox1 = createInboxComponent();
        final var inbox2 = createInboxComponent();
        final var inbox3 = createInboxComponent();

        final var outbox1 = TestHelpers.createOutboxComponent();
        final var outbox2 = TestHelpers.createOutboxComponent();

        // --------------------------------------------------
        // registering components
        // --------------------------------------------------
        router.register(inbox1);
        router.register(inbox2);
        router.register(inbox3);
        router.register(outbox1);
        router.register(outbox2);

        // --------------------------------------------------
        // linking components
        // --------------------------------------------------
        router.link(inbox1.getOutputPin("data"), outbox1.getInputPin("data"));
        router.link(inbox2.getOutputPin("data"), outbox1.getInputPin("data"));
        router.link(inbox2.getOutputPin("data"), outbox2.getInputPin("data"));
        router.link(inbox3.getOutputPin("data"), outbox2.getInputPin("data"));

        // ---------------------------------------
        // Test
        // ---------------------------------------
        assertThat(router.getSubscribers(inbox1.getOutputPin("data"))).containsExactly(outbox1.getInputPin("data"));
        assertThat(router.getSubscribers(inbox2.getOutputPin("data"))).containsExactlyInAnyOrder(outbox1.getInputPin("data"), outbox2.getInputPin("data"));
        assertThat(router.getSubscribers(inbox3.getOutputPin("data"))).containsExactly(outbox2.getInputPin("data"));

        router.unlink(inbox2.getOutputPin("data")); // remove pin of Inbox #2

        assertThat(router.getSubscribers(inbox1.getOutputPin("data"))).containsExactly(outbox1.getInputPin("data"));
        assertThat(router.getSubscribers(inbox2.getOutputPin("data"))).isEmpty();
        assertThat(router.getSubscribers(inbox3.getOutputPin("data"))).containsExactly(outbox2.getInputPin("data"));
    }

    /**
     * Verify the removal of Pin at outbox (target). Flow:
     * <ul>
     *     <li>Inbox #1 -> Outbox #1</li>
     *     <li>Inbox #2 -> Outbox #1 and #2</li>
     *     <li>Inbox #3 -> Outbox #2</li>
     * </ul>
     * <p>
     * Before:
     * <pre>
     *   Inbox #1 ----------.
     *                       >----> Outbox #1
     *   Inbox #2 ----------<
     *                       >----> Outbox #2
     *   Inbox #3 ----------´
     * </pre>
     * <p>
     * After:
     * <pre>
     *   Inbox #1 ----------.
     *                       >----> Outbox #1
     *   Inbox #2 ----------´
     *
     *   Inbox #3 (=no link)
     * </pre>
     */
    @Test
    @DisplayName("Verify the unlink of an target pin")
    void verifyUnlinkTargetPin() {
        final var router = Router.createDefault();

        // --------------------------------------------------
        // create components
        // --------------------------------------------------
        final var inbox1 = createInboxComponent();
        final var inbox2 = createInboxComponent();
        final var inbox3 = createInboxComponent();

        final var outbox1 = TestHelpers.createOutboxComponent();
        final var outbox2 = TestHelpers.createOutboxComponent();

        // --------------------------------------------------
        // registering components
        // --------------------------------------------------
        router.register(inbox1);
        router.register(inbox2);
        router.register(inbox3);
        router.register(outbox1);
        router.register(outbox2);

        // --------------------------------------------------
        // linking components
        // --------------------------------------------------
        router.link(inbox1.getOutputPin("data"), outbox1.getInputPin("data"));
        router.link(inbox2.getOutputPin("data"), outbox1.getInputPin("data"));
        router.link(inbox2.getOutputPin("data"), outbox2.getInputPin("data"));
        router.link(inbox3.getOutputPin("data"), outbox2.getInputPin("data"));

        // ---------------------------------------
        // Test
        // ---------------------------------------
        assertThat(router.getSubscribers(inbox1.getOutputPin("data"))).containsExactly(outbox1.getInputPin("data"));
        assertThat(router.getSubscribers(inbox2.getOutputPin("data"))).containsExactlyInAnyOrder(outbox1.getInputPin("data"), outbox2.getInputPin("data"));
        assertThat(router.getSubscribers(inbox3.getOutputPin("data"))).containsExactly(outbox2.getInputPin("data"));

        router.unlink(outbox2.getInputPin("data")); // remove pin of Outbox #2

        assertThat(router.getSubscribers(inbox1.getOutputPin("data"))).containsExactly(outbox1.getInputPin("data"));
        assertThat(router.getSubscribers(inbox2.getOutputPin("data"))).containsExactly(outbox1.getInputPin("data"));
        assertThat(router.getSubscribers(inbox3.getOutputPin("data"))).isEmpty();
    }

    /**
     * Test an unknown/not-registered Event Channel.
     */
    @Test
    @DisplayName("Test an unknown/not-registered event channel")
    void testUnknownEventChannel() {
        final var router = Router.createDefault();

        // --------------------------------------------------
        // create components
        // --------------------------------------------------
        final var inbox = createInboxComponent();

        final var eventKeyMock = mock(EventKey.class);
        when(eventKeyMock.getChannel()).thenReturn("NONEXISTENT-CHANNEL");
        final var outboxSpy = spy(TestHelpers.createOutboxComponent());
        when(outboxSpy.getEventKey()).thenReturn(eventKeyMock);

        // --------------------------------------------------
        // registering components
        // --------------------------------------------------
        router.register(inbox);
        router.register(outboxSpy);

        // --------------------------------------------------
        // linking components
        // --------------------------------------------------
        router.link(inbox.getOutputPin("data"), outboxSpy.getInputPin("data"));

        // ---------------------------------------
        // Test
        // ---------------------------------------
        final var event = createEvent(inbox, new byte[0]);
        assertThatThrownBy(() -> router.inbound(event).get())
                .isInstanceOf(ExecutionException.class)
                .hasRootCauseInstanceOf(NullPointerException.class)
                .hasRootCauseMessage("No suitable event channel found for: NONEXISTENT-CHANNEL");
    }

    /**
     * Test requesting for an event with an key that is not registered
     * to any inbox component, therefore, no suitable inbox component
     * could be found. Expected is that workflow is empty.
     */
    @Test
    @DisplayName("No suitable inbox components registered")
    void testNoInboxComponent() throws InterruptedException, ExecutionException {
        final var router = Router.createDefault();

        final var workflow = router.inbound(createEvent("foo", new byte[0])).get();
        assertThat(workflow).isEmpty();
    }

    /**
     * If there is a missing linking, the workflow should be done
     * without any exception.
     */
    @Test
    @DisplayName("Dead End for Router because of no subscribers")
    void testNoSubscribers() throws InterruptedException, ExecutionException {
        final var router = Router.createDefault();

        // --------------------------------------------------
        // create components
        // --------------------------------------------------
        final var inbox = createInboxComponent();

        // --------------------------------------------------
        // registering components
        // --------------------------------------------------
        router.register(inbox);

        // --------------------------------------------------
        // linking components
        // --------------------------------------------------
        // No linking ...

        // ---------------------------------------
        // Test
        // ---------------------------------------
        final var event = createEvent(inbox, new byte[0]);
        final var workflow = router.inbound(event).get();
        assertThat(workflow).hasSize(1);
        assertThat(workflow.get(0).getFlows()).hasSize(1);
        assertThat(workflow.get(0).getFlows().get(0).getUid()).isEqualTo(inbox.getOutputPin("data").getUid());
    }

    /**
     * Test throwing an {@link RouterException} when router calls
     * an unsupported component. Adding / validation of component
     * should be considered outside of the router logic.
     */
    @Test
    @DisplayName("ERROR: Dead End for Router because of unsupported component")
    void testUnsupportedComponent() {
        final var router = Router.createDefault();

        // --------------------------------------------------
        // create components
        // --------------------------------------------------
        final var inbox = createInboxComponent();

        final var connectorMock = mock(Connector.class);

        final var componentMock = mock(UnsupportedComponent.class);
        when(componentMock.getConnectors()).thenReturn(List.of(connectorMock));

        final var descriptorMock = mock(FieldDescriptor.class);
        doReturn(Object.class).when(descriptorMock).getFieldValueClass();

        final var pinMock = mock(Pin.class);
        when(pinMock.getUid()).thenReturn(mock(UID.class));
        when(pinMock.getConnector()).thenReturn(connectorMock);
        when(pinMock.getDescriptor()).thenReturn(descriptorMock);

        // --------------------------------------------------
        // registering components
        // --------------------------------------------------
        router.register(inbox);
        router.register(componentMock);

        // --------------------------------------------------
        // linking components
        // --------------------------------------------------
        router.link(inbox.getOutputPin("data"), pinMock);

        // ---------------------------------------
        // Test
        // ---------------------------------------
        final var event = createEvent(inbox, new byte[0]);
        assertThatThrownBy(() -> router.inbound(event).get())
                .isInstanceOf(ExecutionException.class)
                .hasRootCauseInstanceOf(RouterException.class)
                .hasRootCauseMessage("Dead End because of unsupported component: " + componentMock);
    }

    /*
     * Router supports only OutboxComponent or LogicComponent
     */
    private interface UnsupportedComponent extends Component, InputConnectorAware {
    }
}
