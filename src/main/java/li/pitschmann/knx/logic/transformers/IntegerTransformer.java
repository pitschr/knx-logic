package li.pitschmann.knx.logic.transformers;

/**
 * Transforms {@link String} to {@link Integer}
 *
 * @author PITSCHR
 */
public final class IntegerTransformer extends AbstractTransformer<Integer> {

    @Override
    public Integer transformNullSafe(final String value) {
        return Integer.valueOf(value);
    }

    @Override
    public Integer getDefaultValue() {
        return 0;
    }

    @Override
    public Class<Integer> getTargetClass() {
        return Integer.class;
    }
}
