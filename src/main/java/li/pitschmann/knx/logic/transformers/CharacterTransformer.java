package li.pitschmann.knx.logic.transformers;

/**
 * Transforms {@link String} to {@link Character}
 *
 * @author PITSCHR
 */
public final class CharacterTransformer extends AbstractTransformer<Character> {

    @Override
    public Character transformNullSafe(final String value) {
        if (value.length() == 1) {
            return Character.valueOf(value.charAt(0));
        } else if (value.startsWith("\\u") && value.length() == 6) {
            // e.g. A -> \u0041
            // parse digits (0041) with hex-decimal: (41)v16 = 65
            return Character.valueOf((char) Integer.parseInt(value.substring(2), 16));
        }
        throw new IllegalArgumentException(String.format("Given value '%s' cannot be transformed to Character!", value));
    }

    @Override
    public Character getDefaultValue() {
        return Character.MIN_VALUE;
    }

    @Override
    public Class<Character> getTargetClass() {
        return Character.class;
    }
}
