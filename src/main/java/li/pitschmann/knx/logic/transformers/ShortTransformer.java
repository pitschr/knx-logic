package li.pitschmann.knx.logic.transformers;

/**
 * Transforms {@link String} to {@link Short}
 *
 * @author PITSCHR
 */
public final class ShortTransformer extends AbstractTransformer<Short> {

    @Override
    public Short transformNullSafe(final String value) {
        return Short.valueOf(value);
    }

    @Override
    public Short getDefaultValue() {
        return (short) 0;
    }

    @Override
    public Class<Short> getTargetClass() {
        return Short.class;
    }
}
