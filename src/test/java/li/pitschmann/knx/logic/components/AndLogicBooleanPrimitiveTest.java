package li.pitschmann.knx.logic.components;

import test.components.AndLogicBooleanPrimitive;

/**
 * Test the {@link AndLogicBooleanPrimitive} component
 */
public class AndLogicBooleanPrimitiveTest extends AbstractLogicBooleanTest<AndLogicBooleanPrimitive> {

    @Override
    protected Class<AndLogicBooleanPrimitive> logicClass() {
        return AndLogicBooleanPrimitive.class;
    }

}
