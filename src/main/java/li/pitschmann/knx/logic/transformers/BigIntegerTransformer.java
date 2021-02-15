package li.pitschmann.knx.logic.transformers;

import java.math.BigInteger;

/**
 * Transforms {@link String} to {@link BigInteger}
 *
 * @author PITSCHR
 */
public final class BigIntegerTransformer extends AbstractTransformer<BigInteger> {

    @Override
    public BigInteger transformNullSafe(final String value) {
        return new BigInteger(value);
    }

    @Override
    public BigInteger getDefaultValue() {
        return BigInteger.ZERO;
    }

    @Override
    public Class<BigInteger> getTargetClass() {
        return BigInteger.class;
    }
}
