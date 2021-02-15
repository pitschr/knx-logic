package li.pitschmann.knx.logic.transformers;

import java.math.BigDecimal;

/**
 * Transforms {@link String} to {@link BigDecimal}
 *
 * @author PITSCHR
 */
public final class BigDecimalTransformer extends AbstractTransformer<BigDecimal> {

    @Override
    public BigDecimal transformNullSafe(final String value) {
        return new BigDecimal(value);
    }

    @Override
    public BigDecimal getDefaultValue() {
        return BigDecimal.ZERO;
    }

    @Override
    public Class<BigDecimal> getTargetClass() {
        return BigDecimal.class;
    }
}
