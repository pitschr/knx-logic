package li.pitschmann.knx.logic.db.jdbi.mappers;

/**
 * Type of component: LOGIC, INBOX or OUTBOX component
 *
 * @author PITSCHR
 */
public enum ComponentType {
    /**
     * The component is a {@link li.pitschmann.knx.logic.components.LogicComponent}
     */
    LOGIC(0),
    /**
     * The component is a {@link li.pitschmann.knx.logic.components.InboxComponent}
     */
    INBOX(1),
    /**
     * The component is a {@link li.pitschmann.knx.logic.components.OutboxComponent}
     */
    OUTBOX(2);

    private final int index;

    ComponentType(final int index) {
        this.index = index;
    }

    public static ComponentType valueOf(final int index) {
        if (index == LOGIC.getIndex()) {
            return LOGIC;
        } else if (index == INBOX.getIndex()) {
            return INBOX;
        } else if (index == OUTBOX.getIndex()) {
            return OUTBOX;
        }
        throw new IllegalArgumentException("Component Type Index not known: " + index);
    }

    public int getIndex() {
        return this.index;
    }
}
