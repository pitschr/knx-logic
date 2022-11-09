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

package test.assertions.model;

import li.pitschmann.knx.logic.components.ComponentType;
import li.pitschmann.knx.logic.db.models.ComponentModel;
import li.pitschmann.knx.logic.uid.UIDAware;

import static org.assertj.core.api.Assertions.assertThat;

public final class ComponentModelAssert extends ModelAssert<ComponentModelAssert, ComponentModel> {
    /**
     * Constructor for {@link ComponentModelAssert}
     *
     * @param actual
     */
    public ComponentModelAssert(final ComponentModel actual) {
        super(actual, ComponentModelAssert.class);
    }

    /**
     * Assert {@link ComponentModel#getUid()}
     *
     * @param expected
     * @return myself
     */
    public ComponentModelAssert uid(final UIDAware expected) {
        assertThat(this.actual.getUid()).isEqualTo(expected.getUid());
        return this.myself;
    }

    /**
     * Assert {@link ComponentModel#getClassName()}
     *
     * @param className the class name
     * @return myself
     */
    public ComponentModelAssert className(final String className) {
        assertThat(this.actual.getClassName()).isEqualTo(className);
        return this.myself;
    }

    /**
     * Assert {@link ComponentModel#getComponentType()}
     *
     * @param componentType
     * @return myself
     */
    public ComponentModelAssert componentType(final ComponentType componentType) {
        assertThat(this.actual.getComponentType()).isSameAs(componentType);
        return this.myself;
    }
}
