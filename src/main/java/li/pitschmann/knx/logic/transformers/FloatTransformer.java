package li.pitschmann.knx.logic.transformers;

/**
 * Transforms {@link String} to {@link Float}
 *
 * @author PITSCHR
 */
public final class FloatTransformer extends AbstractTransformer<Float> {

    @Override
    public Float transformNullSafe(final String value) {
        return Float.valueOf(value);
    }

    @Override
    public Float getDefaultValue() {
        return 0f;
    }

    @Override
    public Class<Float> getTargetClass() {
        return Float.class;
    }
}
