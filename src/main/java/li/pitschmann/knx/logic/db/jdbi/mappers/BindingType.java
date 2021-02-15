package li.pitschmann.knx.logic.db.jdbi.mappers;

/**
 * Type of connector: STATIC or DYNAMIC
 * <p>
 * Static has a 1:1 relationship while Dynamic may have a
 * 0:N relationship.
 *
 * @author PITSCHR
 */
public enum BindingType {
    /**
     * The connector is static with always ONE pin as fixed
     */
    STATIC(0),
    /**
     * The connector is dynamic whereas the number of pin can
     * vary dynamically
     */
    DYNAMIC(1);

    private final int index;

    BindingType(final int index) {
        this.index = index;
    }

    public static BindingType valueOf(final int index) {
        if (index == STATIC.getIndex()) {
            return STATIC;
        } else if (index == DYNAMIC.getIndex()) {
            return DYNAMIC;
        }
        throw new IllegalArgumentException("Binding Type Index not known: " + index);
    }

    public int getIndex() {
        return this.index;
    }
}
