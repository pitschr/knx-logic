/*
 * Copyright (C) 2022 Pitschmann Christoph
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

package test.components;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;

import java.util.Arrays;
import java.util.List;

/**
 * Test component with dynamic input that will be dynamically initialized directly
 */
public final class AndLogicBooleanListField implements Logic {
    @Input
    private List<Boolean> i = Arrays.asList(false, true); // initialized with 2 default values already!

    @Output
    private boolean o;

    @Override
    public void logic() {
        this.o = this.i.stream().allMatch(Boolean::booleanValue);
    }
}
