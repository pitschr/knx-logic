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

package li.pitschmann.knx.logic.helpers;

import experimental.api.AndLogic;
import li.pitschmann.knx.core.utils.Preconditions;
import li.pitschmann.knx.logic.components.Component;
import li.pitschmann.knx.logic.components.InboxComponentImpl;
import li.pitschmann.knx.logic.components.LogicComponentImpl;
import li.pitschmann.knx.logic.components.OutboxComponentImpl;
import li.pitschmann.knx.logic.components.inbox.DPT10Inbox;
import li.pitschmann.knx.logic.components.outbox.DPT10Outbox;
import li.pitschmann.knx.logic.components.outbox.DPT3Outbox;
import li.pitschmann.knx.logic.connector.Connector;
import li.pitschmann.knx.logic.connector.DynamicConnector;
import li.pitschmann.knx.logic.connector.InputConnectorAware;
import li.pitschmann.knx.logic.connector.OutputConnectorAware;
import li.pitschmann.knx.logic.event.EventKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

/**
 * ASCII Helper
 * <p>
 * This utility helps to visualize the Component, Connector and Pins
 * in an ASCII string representation
 */
public final class AsciiHelper {
    private static final ToIntFunction<Connector> MAX_CONNECTOR_NAME_LENGTH_FUNCTION = c -> {
        var connectorNameLength = c.getName().length();
        if (c instanceof DynamicConnector) {
            // dynamic connector has an index after name which needs to be added
            // Example: 'inputs[3]' (6 + 3 => 8)
            // --------------->123
            // Example: 'inputs[999]' (6 + 5 => 11)
            // --------------->12345
            var arrayNameLength = 2 + Integer.toString(((DynamicConnector) c).getPins().size() - 1).length();
            return connectorNameLength + arrayNameLength;
        } else {
            return connectorNameLength;
        }
    };

    /**
     * Returns a char matrix out of Component which contains input and output connectors / pins
     *
     * @param component the component
     * @return matrix representation of component
     */
    static char[][] toMatrix(final Component component) {
        var inputs = new char[0][0];
        var inputsHeight = 0;
        var inputsWidth = 0;
        if (component instanceof InputConnectorAware) {
            inputs = getInputConnectorsMatrix(((InputConnectorAware) component).getInputConnectors());
            inputsHeight = inputs.length;
            if (inputs.length > 0) {
                inputsWidth = inputs[0].length;
            }
        }

        var outputs = new char[0][0];
        var outputsHeight = 0;
        var outputsWidth = 0;
        if (component instanceof OutputConnectorAware) {
            outputs = getOutputConnectorsMatrix(((OutputConnectorAware) component).getOutputConnectors());
            outputsHeight = outputs.length;
            if (outputs.length > 0) {
                outputsWidth = outputs[0].length;
            }
        }

        // component start position
        final var leftPadding = inputs.length == 0 ? 0 : 3;
        final var rightPadding = outputs.length == 0 ? 0 : 3;
        // component width
        // 0123 (3 for input)        3210 (3 for output, from right to left)
        //     .--------------------.
        //     | AndLogic           |
        //  .--+--------.     .-----+--.
        // =| inputs[0] |     | output |=
        //  '--+--------'     '-----+--'
        //     '--------------------'
        var nameString = component.getName();
        var area = new char[Math.max(inputsHeight, outputsHeight) + 3]
                [Math.max(inputsWidth + 1 + outputsWidth, nameString.length() + leftPadding + rightPadding + 4)];

        // component header
        var header = new char[area[0].length];
        header[leftPadding] = '.';
        Arrays.fill(header, leftPadding + 1, area[0].length - rightPadding - 1, '-');
        header[area[0].length - rightPadding - 1] = '.';
        area[0] = header;

        // component name
        var name = new char[area[0].length];
        Arrays.fill(name, leftPadding + 1, area[0].length - rightPadding - 1, ' ');
        name[leftPadding] = '|';
        name[area[0].length - rightPadding - 1] = '|';
        nameString.getChars(0, nameString.length(), name, leftPadding + 2);
        area[1] = name;

        // component body (input and outputs)
        if (inputs.length > 0) {
            moveMatrix(area, 2, 0, inputs);
        }
        if (outputs.length > 0) {
            moveMatrix(area, 2, area[0].length - outputsWidth, outputs);
        }
        for (int i = inputs.length; i < outputs.length; i++) {
            area[i + 2][leftPadding] = '|';
        }
        for (int i = outputs.length; i < inputs.length; i++) {
            area[i + 2][area[0].length - rightPadding - 1] = '|';
        }

        // component footer
        var footer = new char[area[0].length];
        footer[leftPadding] = '\'';
        Arrays.fill(footer, leftPadding + 1, area[0].length - rightPadding - 1, '-');
        footer[area[0].length - rightPadding - 1] = '\'';
        area[area.length - 1] = footer;

        return area;
    }

    /**
     * Returns a char matrix out of list of input connectors
     */
    static char[][] getInputConnectorsMatrix(final List<Connector> inputConnectors) {
        return getConnectorsMatrix(
                inputConnectors,
                AsciiHelper::getInputConnectorMatrix,
                (matrix, maxCols) -> 0  // from most left side
        );
    }

    /**
     * Returns a char matrix out of list of output connectors
     */
    static char[][] getOutputConnectorsMatrix(final List<Connector> outputConnectors) {
        return getConnectorsMatrix(
                outputConnectors,
                AsciiHelper::getOutputConnectorMatrix,
                (matrix, maxCols) -> maxCols - matrix[0].length  // find right position of start of output connector
        );
    }

    /**
     * Get a char matrix out of input connector
     */
    static char[][] getInputConnectorMatrix(final Connector inputConnector) {
        var inputPins = inputConnector.getPinStream().collect(Collectors.toList());

        var connectorHeight = 1 + inputPins.size() + 1;  // 1 header + pins + 1 footer
        var connectorWidth = MAX_CONNECTOR_NAME_LENGTH_FUNCTION.applyAsInt(inputConnector) + 5;

        var newChar = new char[connectorHeight][connectorWidth];

        // header: " .------."
        var header = new char[connectorWidth];
        header[0] = ' ';
        header[1] = '.';
        Arrays.fill(header, 2, connectorWidth - 1, '-');
        header[3] = '+';
        header[connectorWidth - 1] = '.';
        newChar[0] = header;

        // static pin: "=| MyName |"
        // dynamic pin: "=| MyName[0] |"
        for (int i = 0; i < inputPins.size(); i++) {
            var pinNameArray = new char[connectorWidth];
            Arrays.fill(pinNameArray, ' ');
            pinNameArray[0] = '=';
            pinNameArray[1] = '|';
            pinNameArray[connectorWidth - 1] = '|';

            var pinNameString = inputPins.get(i).getName();
            pinNameString.getChars(0, pinNameString.length(), pinNameArray, 3);

            newChar[i + 1] = pinNameArray;
        }

        // footer: " '------'"
        var footer = new char[connectorWidth];
        footer[0] = ' ';
        footer[1] = '\'';
        Arrays.fill(footer, 2, connectorWidth - 1, '-');
        footer[3] = '+';
        footer[connectorWidth - 1] = '\'';
        newChar[connectorHeight - 1] = footer;

        return newChar;
    }

    private static char[][] getConnectorsMatrix(final List<Connector> connectors,
                                                final Function<Connector, char[][]> toMatrixFunction,
                                                final ToIntBiFunction<char[][], Integer> colPosFunction) {
        var matrixList = new ArrayList<char[][]>(connectors.size());

        // find the max row / columns
        var maxRows = 0;
        var maxCols = 0;
        for (var connector : connectors) {
            var matrix = toMatrixFunction.apply(connector);
            matrixList.add(matrix);
            maxRows += matrix.length;
            maxCols = Math.max(maxCols, matrix[0].length);
        }

        // create a new matrix and move the matrix
        var area = new char[maxRows][maxCols];

        var rowPos = 0;
        for (var matrix : matrixList) {
            moveMatrix(area, rowPos, colPosFunction.applyAsInt(matrix, maxCols), matrix);
            rowPos += matrix.length;
        }

        return area;
    }

    /**
     * Get a char matrix out of output connector
     */
    static char[][] getOutputConnectorMatrix(final Connector outputConnector) {
        var outputPins = outputConnector.getPinStream().collect(Collectors.toList());

        var connectorHeight = 1 + outputPins.size() + 1;  // 1 header + pins + 1 footer
        var connectorWidth = MAX_CONNECTOR_NAME_LENGTH_FUNCTION.applyAsInt(outputConnector) + 5;

        var newChar = new char[connectorHeight][connectorWidth];

        // header: ".------. "
        var header = new char[connectorWidth];
        header[0] = '.';
        Arrays.fill(header, 1, connectorWidth - 2, '-');
        header[connectorWidth - 4] = '+';
        header[connectorWidth - 2] = '.';
        newChar[0] = header;

        // static pin: "| MyName |="
        // dynamic pin: "| MyName[0] |="
        for (var i = 0; i < outputPins.size(); i++) {
            var pinNameArray = new char[connectorWidth];
            Arrays.fill(pinNameArray, ' ');
            pinNameArray[0] = '|';
            pinNameArray[connectorWidth - 2] = '|';
            pinNameArray[connectorWidth - 1] = '=';

            var pinNameString = outputPins.get(i).getName();
            pinNameString.getChars(0, pinNameString.length(), pinNameArray, 2);

            newChar[i + 1] = pinNameArray;
        }

        // footer: "'------' "
        var footer = new char[connectorWidth];
        footer[0] = '\'';
        Arrays.fill(footer, 1, connectorWidth - 2, '-');
        footer[connectorWidth - 4] = '+';
        footer[connectorWidth - 2] = '\'';
        newChar[connectorHeight - 1] = footer;

        return newChar;
    }

    public static void main(String[] args) {
        System.out.println(toAsciiString(new LogicComponentImpl(new AndLogic())));
        System.out.println(toAsciiString(new InboxComponentImpl(new EventKey("x", "y"), new DPT10Inbox())));
        System.out.println(toAsciiString(new OutboxComponentImpl(new EventKey("x", "y"), new DPT3Outbox())));
    }

    /**
     * Helper function to move a {@code part} matrix  the component
     *
     * @param area   the area matrix where the {@code part} matrix should be moved; may not be null
     * @param rowPos the start row position where the {@code part} matrix should be moved; may not be negative
     * @param colPos the start col position where the {@code part} matrix should be moved; may not be negative
     * @param part   the smaller matrix that should be moved; may not be null
     */
    static void moveMatrix(final char[][] area, final int rowPos, final int colPos, final char[][] part) {
        Preconditions.checkArgument(rowPos >= 0, "Position of row may not be negative");
        Preconditions.checkArgument(colPos >= 0, "Position of column may not be negative");
        Preconditions.checkArgument(area.length >= (rowPos + part.length), "Area has not enough height");
        Preconditions.checkArgument(area[0].length >= (colPos + part[0].length), "Area has not enough width");
        for (int i = 0; i < part.length; i++) {
            System.arraycopy(part[i], 0, area[i + rowPos], colPos, part[i].length);
        }
    }

    /**
     * Converts a {@code Component} to ASCII string representation
     *
     * @param component to be converted; may not be null
     * @return ASCII String
     */
    public static String toAsciiString(final Component component) {
        final var matrix = toMatrix(component);
        final var sb = new StringBuilder(512);
        for (var row : matrix) {
            for (var col : row) {
                sb.append(col == '\0' ? ' ' : col);
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
