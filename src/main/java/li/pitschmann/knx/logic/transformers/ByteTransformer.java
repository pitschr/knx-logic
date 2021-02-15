package li.pitschmann.knx.logic.transformers;

/**
 * Transforms {@link String} to {@link Byte}
 *
 * @author PITSCHR
 */
public final class ByteTransformer extends AbstractTransformer<Byte> {

    @Override
    public Byte transformNullSafe(final String value) {
        return Byte.valueOf(value);
    }

    @Override
    public Byte getDefaultValue() {
        return (byte) 0;
    }

    @Override
    public Class<Byte> getTargetClass() {
        return Byte.class;
    }
}
