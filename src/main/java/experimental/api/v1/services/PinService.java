/*
 * Copyright (C) 2021 Pitschmann Christoph
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package experimental.api.v1.services;

import li.pitschmann.knx.core.annotations.Nullable;
import li.pitschmann.knx.core.utils.Preconditions;
import li.pitschmann.knx.logic.pin.Pin;
import li.pitschmann.knx.logic.transformers.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for {@link Pin}
 */
public final class PinService {
    private static final Logger LOG = LoggerFactory.getLogger(PinService.class);

    /**
     * Sets the given {@code valueAsString} to {@link Pin}
     *
     * @param pin           the pin to be updated; may not be null
     * @param valueAsString the value as string; will try to be transformed
     */
    public void setValue(final Pin pin, final @Nullable String valueAsString) {
        Preconditions.checkNonNull(pin, "Pin is required.");

        final var obj = Transformers.transform(valueAsString, pin.getDescriptor().getFieldType());
        LOG.debug("Set Value for Pin ({}): Original={}, Transformed={}", pin.getUid(), valueAsString, obj);
        pin.setValue(obj);
    }
}