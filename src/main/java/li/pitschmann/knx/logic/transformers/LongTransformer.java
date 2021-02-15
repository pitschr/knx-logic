package li.pitschmann.knx.logic.transformers;

/**
 * Transforms {@link String} to {@link Long}
 *
 * @author PITSCHR
 */
public final class LongTransformer extends AbstractTransformer<Long> {

    @Override
    public Long transformNullSafe(final String value) {
        return Long.valueOf(value);
    }

    @Override
    public Long getDefaultValue() {
        return 0L;
    }

    @Override
    public Class<Long> getTargetClass() {
        return Long.class;
    }
}
