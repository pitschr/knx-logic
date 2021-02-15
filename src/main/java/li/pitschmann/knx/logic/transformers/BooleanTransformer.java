package li.pitschmann.knx.logic.transformers;

/**
 * Transforms {@link String} to {@link Boolean}
 *
 * @author PITSCHR
 */
public final class BooleanTransformer extends AbstractTransformer<Boolean> {

    /**
     * Returns {@code Boolean.TRUE} when given {@code value} is a {@code 1} and or {@code true} (case-insensitive)
     *
     * @param value
     * @return boolean
     */
    @Override
    public Boolean transformNullSafe(final String value) {
        return "1".equals(value) || Boolean.parseBoolean(value);
    }

    @Override
    public Boolean getDefaultValue() {
        return Boolean.FALSE;
    }

    @Override
    public Class<Boolean> getTargetClass() {
        return Boolean.class;
    }
}
