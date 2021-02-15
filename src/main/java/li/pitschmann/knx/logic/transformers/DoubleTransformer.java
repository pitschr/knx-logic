package li.pitschmann.knx.logic.transformers;

/**
 * Transforms {@link String} to {@link Double}
 *
 * @author PITSCHR
 */
public final class DoubleTransformer extends AbstractTransformer<Double> {

    @Override
    public Double transformNullSafe(final String value) {
        return Double.valueOf(value);
    }

    @Override
    public Double getDefaultValue() {
        return 0d;
    }

    @Override
    public Class<Double> getTargetClass() {
        return Double.class;
    }
}
