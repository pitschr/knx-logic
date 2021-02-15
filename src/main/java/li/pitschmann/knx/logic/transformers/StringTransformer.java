package li.pitschmann.knx.logic.transformers;

/**
 * Transforms {@link String} to {@link String}
 * <p>
 * There will be no real transformation done, just in case the String is
 * null, an empty String will be returned from {@link #getDefaultValue()}.
 *
 * @author PITSCHR
 */
public final class StringTransformer extends AbstractTransformer<String> {

    /**
     * No transformation, just pass-through as type is same for source and target.
     *
     * @param value the value to be transformed; may not be null
     * @return value, itself
     */
    @Override
    public String transformNullSafe(final String value) {
        return value;
    }

    @Override
    public String getDefaultValue() {
        return "";
    }

    @Override
    public Class<String> getTargetClass() {
        return String.class;
    }
}
