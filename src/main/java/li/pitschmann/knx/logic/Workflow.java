package li.pitschmann.knx.logic;

import li.pitschmann.knx.core.annotations.Nullable;
import li.pitschmann.knx.core.utils.Strings;
import li.pitschmann.knx.logic.uid.UID;
import li.pitschmann.knx.logic.uid.UIDAware;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link Workflow} implementation containing {@link WorkflowEntry} and
 * representing the workflow of routing.
 *
 * @author PITSCHR
 */
public final class Workflow {
    private static final Workflow EMPTY = new Workflow();
    private final List<WorkflowEntry> flows;

    private Workflow() {
        flows = List.of();
    }

    private Workflow(final Workflow workflow, final WorkflowEntry newEntry) {
        // add new workflow entry to the list and make the list immutable
        final var tmpFlows = new ArrayList<>(workflow.flows);
        tmpFlows.add(newEntry);
        flows = List.copyOf(tmpFlows);
    }

    /**
     * Creates a new workflow and this is the enter point for
     * workflow recording.
     *
     * @return new workflow
     */
    public static Workflow create() {
        return Workflow.EMPTY;
    }

    /**
     * List of {@link WorkflowEntry}
     *
     * @return immutable list
     */
    public List<WorkflowEntry> getFlows() {
        return flows;
    }

    /**
     * Calculates the duration in milliseconds from beginning to end of workflow to
     * record how long the computation of each components took.
     * <p>
     * Computation of {@link li.pitschmann.knx.logic.components.InboxComponent} and
     * {@link li.pitschmann.knx.logic.components.OutboxComponent} are not considered.
     * <p>
     * This method may be helpful for diagnosis in case the
     * workflow takes unexpectedly long.
     *
     * <p>
     * A workflow looks like:
     * <pre>
     *               Inbox        Component #1      Component #2       Outbox
     *              .-----.       .---------.       .---------.       .------.
     *             |     [x] --> [x]       [x] --> [x]       [x] --> [x]     |
     *             `-----´       `---------´       `---------´       `------´
     *                    ^       ^         ^       ^         ^       ^
     * Workflow Entry  =  0       1         2       3         4       5
     *
     * </pre>
     * The formula to calculate the duration is {@code N - (N-1)} while N is even and starts with 2.
     * <p>
     * In given example above, the duration would be Index(2) - Index(1) + Index(4) - Index(3).
     * The Index(0) and Index(5) are subject to be ignored.
     *
     * @return the calculated duration in milliseconds
     */
    public long calculateDuration() {
        final var originalFlowSize = flows.size();
        if (originalFlowSize < 4) {
            return 0L;
        } else {
            long duration = 0;
            for (int i = 1; i < flows.size() - 1; i += 2) {
                duration += flows.get(i + 1).getInstant().toEpochMilli() - flows.get(i).getInstant().toEpochMilli();
            }
            return duration;
        }
    }

    /**
     * Creates a clone of current {@link Workflow} and record the new value.
     *
     * @param uidAware an instance that implements {@link UIDAware}; may not be null
     * @param value    the value to be recorded to new workflow entry; may be null
     * @return a new {@link Workflow} with the new value
     */
    public Workflow add(final UIDAware uidAware, final @Nullable Object value) {
        return add(uidAware.getUid(), value);
    }

    /**
     * Creates a clone of current {@link Workflow} and record the new value.
     *
     * @param uid   an instance of {@link UID}; may not be null
     * @param value the value to be recorded to new workflow entry; may be null
     * @return a new {@link Workflow} with the new value
     */
    public Workflow add(final UID uid, final @Nullable Object value) {
        return new Workflow(this, new WorkflowEntry(uid, value));
    }

    @Override
    public String toString() {
        return Strings.toStringHelper(this) //
                .add("duration", calculateDuration()) //
                .add("flows", flows) //
                .toString();
    }
}
