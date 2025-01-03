package logicircuit;

import java.awt.*;
import java.awt.geom.*;

public enum LCComponent {
    /**
     * AND gate
     */
    AND(false, NumberInputPins.TWO, true) {
        @Override
        protected void pathToFill(Path2D path) {
            path.moveTo(15, 0);
            path.lineTo(15, 30);
            path.lineTo(30, 30);
            path.curveTo(53, 30, 53, 0, 30, 0);
            path.closePath();
        }

        @Override
        protected void pathToOutline(Path2D path) {
            this.pathToFill(path);
            drawGateInputPins(path, this.numberInputPins, 13);
            drawGateOutputPin(path, false);
        }
    },
    /**
     * NAND gate
     */
    NAND(false, NumberInputPins.TWO, true) {
        @Override
        protected void pathToFill(Path2D path) {
            AND.pathToFill(path);
        }

        @Override
        protected void pathToOutline(Path2D path) {
            AND.pathToFill(path);
            drawGateInputPins(path, this.numberInputPins, 13);
            drawGateOutputPin(path, true);
        }
    },
    /**
     * OR gate
     */
    OR(false, NumberInputPins.TWO, true) {
        @Override
        protected void pathToFill(Path2D path) {
            drawOrGate(path);
        }

        @Override
        protected void pathToOutline(Path2D path) {
            this.pathToFill(path);
            drawGateInputPins(path, this.numberInputPins, 14);
            drawGateOutputPin(path, false);
        }
    },
    /**
     * NOR gate
     */
    NOR(false, NumberInputPins.TWO, true) {
        @Override
        protected void pathToFill(Path2D path) {
            OR.pathToFill(path);
        }

        @Override
        protected void pathToOutline(Path2D path) {
            OR.pathToFill(path);
            drawGateInputPins(path, this.numberInputPins, 14);
            drawGateOutputPin(path, true);
        }
    },
    /**
     * Xor gate
     */
    XOR(false, NumberInputPins.TWO, true) {
        @Override
        protected void pathToFill(Path2D path) {
            drawOrGate(path);
        }

        @Override
        protected void pathToOutline(Path2D path) {
            this.pathToFill(path);
            path.moveTo(6, 28);
            path.quadTo(17, 15, 6, 2);
            drawGateInputPins(path, this.numberInputPins, 8);
            drawGateOutputPin(path, false);
        }
    },
    /**
     * NOT gate
     */
    NOT(false, NumberInputPins.ONE, true) {
        @Override
        protected void pathToFill(Path2D path) {
            path.moveTo(15, 0);
            path.lineTo(15, 30);
            path.lineTo(47, 15);
            path.closePath();
        }

        @Override
        protected void pathToOutline(Path2D path) {
            this.pathToFill(path);
            drawGateInputPins(path, this.numberInputPins, 13);
            drawGateOutputPin(path, true);
        }
    },
    /**
     * LED
     */
    LED(true, NumberInputPins.ONE, false) {
        @Override
        protected void pathToFill(Path2D path) {
            for (int y = 0; y < 3; y++) {
                path.moveTo(15, 30 - y * 2);
                path.lineTo(55, 30 - y * 2);
            }
            path.moveTo(20, 28);
            path.curveTo(20, -8, 50, -8, 50, 28);
            path.closePath();
        }

        @Override
        protected void pathToOutline(Path2D path) {
            this.pathToFill(path);
            drawGateInputPins(path, this.numberInputPins, 13, -13);
        }

        @Override
        protected int[] getCoordInputPin(LCInputPin pin) {
            int[] coord = super.getCoordInputPin(pin);
            return new int[] { coord[0], coord[1] + 13 };
        }
    },
    /**
     * 3 bit display
     */
    BIT3_DISPLAY(true, NumberInputPins.THREE, false) {
        @Override
        protected void pathToFill(Path2D path) {
            path.append(new RoundRectangle2D.Double(15, -20, 40, 50, 10, 10), false);
        }

        @Override
        protected void pathToOutline(Path2D path) {
            this.pathToFill(path);
            drawGateInputPins(path, this.numberInputPins, 15);
        }

        @Override
        protected void drawExtra(Graphics2D g2d, Color fillColor, Color strokeColor, AffineTransform translateXY,
                Object... args) {
            if ((args.length != 1) && !(args[0] instanceof Integer))
                throw new IllegalArgumentException("Invalid argument in drawExtra of LCComponent." + this.name());
            Path2D path = new Path2D.Double();
            drawDigit(path, (Integer) args[0], true, 25, -16, 35, 56);
            if (translateXY != null)
                path.transform(translateXY);
            g2d.setColor(strokeColor);
            g2d.setStroke(new BasicStroke(STROKE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.draw(path);
            path.reset();
            drawDigit(path, (Integer) args[0], false, 25, -16, 35, 56);
            if (translateXY != null)
                path.transform(translateXY);
            g2d.setColor(darkenColor(fillColor, 0.4f));
            g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.draw(path);
        }
    },
    /**
     * SWITCH component
     */
    SWITCH(true, null, true) {
        @Override
        protected void pathToFill(Path2D path) {
            path.append(new RoundRectangle2D.Double(0, 0, 46, 30, 30, 30), false);
        }

        @Override
        protected void pathToOutline(Path2D path) {
            this.pathToFill(path);
            path.moveTo(23, 4);
            path.lineTo(23, 13);
            path.append(new Arc2D.Double(new Rectangle2D.Double(13, 5, 20, 20), 120, 300, Arc2D.OPEN), false);
            drawGateOutputPin(path, false);
        }
    };

    public void draw(Graphics2D g2d, Color fillColor, Color strokeColor, AffineTransform translateXY,
            Object... extraArgs) {
        Path2D path = new Path2D.Double();
        pathToFill(path);
        if (fillColor != null) {
            if (translateXY != null)
                path.transform(translateXY);
            g2d.setColor(fillColor);
            g2d.fill(path);
        }
        path.reset();
        pathToOutline(path);
        if (strokeColor != null) {
            if (translateXY != null)
                path.transform(translateXY);
            g2d.setColor(strokeColor);
            g2d.setStroke(new BasicStroke(STROKE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.draw(path);
        }
        if (extraArgs.length > 0) {
            drawExtra(g2d, fillColor, strokeColor, translateXY, extraArgs);// There is extra parameters
        }
    }

    public boolean hasState() {
        return hasState;
    }

    protected static final int DELTA_Y_LEGEND = 30;
    protected static final int DELTA_X_LEGEND = 65;
    protected static final int STROKE_WIDTH = 2;

    protected abstract void pathToFill(Path2D path);

    protected abstract void pathToOutline(Path2D path);

    protected void drawExtra(Graphics2D g2d, Color fillColor, Color strokeColor, AffineTransform translateXY,
            Object... args) {
        throw new UnsupportedOperationException("drawExtra not implemented in LCComponent." + this.name());
    }

    protected enum NumberInputPins {
        ONE(0),
        TWO(1),
        THREE(2);

        public final int index;

        NumberInputPins(int index) {
            this.index = index;
        }
    }

    protected final boolean hasState;
    protected final NumberInputPins numberInputPins;
    protected final boolean hasOutput;

    LCComponent(boolean hasState, NumberInputPins numberInputPins, boolean hasOutput) {
        this.hasState = hasState;
        this.numberInputPins = numberInputPins;
        this.hasOutput = hasOutput;
    }

    private static final int CONNECTOR_RADIUS = 2;

    protected static void drawPinConnector(Path2D path, int cx, int cy) {
        path.append(new Ellipse2D.Double(cx - CONNECTOR_RADIUS, cy - CONNECTOR_RADIUS, CONNECTOR_RADIUS * 2,
                CONNECTOR_RADIUS * 2), false);
    }

    private static final int[][][] COORD_INPUT_PIN = new int[/* NumberInputPins` */][][] { { { 0, 15 } },
            { { 0, 7 }, { 0, 23 } },
            { { 0, -12 }, { 0, 6 }, { 0, 22 } } };

    protected int[] getCoordInputPin(LCInputPin pin) {
        switch (this.numberInputPins) {
            case ONE:
                return COORD_INPUT_PIN[NumberInputPins.ONE.index][0];
            case TWO:
                int indexPin = pin == LCInputPin.PIN_B ? 1 : 0;
                return COORD_INPUT_PIN[NumberInputPins.TWO.index][indexPin];
            case THREE:
                int indexPin2 = pin == LCInputPin.PIN_C ? 2 : (pin == LCInputPin.PIN_B ? 1 : 0);
                return COORD_INPUT_PIN[NumberInputPins.THREE.index][indexPin2];
        }
        return null;
    }

    protected void drawGateInputPins(Path2D path, NumberInputPins numberInputPins, int xEnd) {
        drawGateInputPins(path, numberInputPins, xEnd, 0);
    }

    protected void drawGateInputPins(Path2D path, NumberInputPins numberInputPins, int xEnd, int yDelta) {
        int[][] coordArray = COORD_INPUT_PIN[numberInputPins.index];
        for (int[] coord : coordArray) {
            int y = coord[1] - yDelta;
            path.moveTo(coord[0], y);
            path.lineTo(xEnd, y);
            drawPinConnector(path, coord[0], y);
        }
    }

    private static final int[] COORD_OUTPUT_PIN = new int[] { 62, 15 };

    protected int[] getCoordOutputPin() {
        return COORD_OUTPUT_PIN;
    }

    private static void drawGateOutputPin(Path2D path, boolean isNotGate) {
        int left = 48;
        int r = 4;
        path.moveTo(isNotGate ? left + 2 * r : left, COORD_OUTPUT_PIN[1]);
        path.lineTo(COORD_OUTPUT_PIN[0], COORD_OUTPUT_PIN[1]);
        drawPinConnector(path, COORD_OUTPUT_PIN[0], COORD_OUTPUT_PIN[1]);
        if (isNotGate) {
            path.append(new Ellipse2D.Double(left, COORD_OUTPUT_PIN[1] - r, r * 2, r * 2), false);
        }
    }

    private static void drawOrGate(Path2D path) {
        path.moveTo(10, 0);
        path.quadTo(35, -3, 47, 15);
        path.quadTo(35, 32, 10, 30);
        path.quadTo(21, 15, 10, 0);
        path.closePath();
    }

    private static void drawDigit(Path2D path, int digit, boolean drawOn, int x, int y, int width, int height) {
        if (digit < 0 || digit > 9) {
            throw new IllegalArgumentException("Digit must be between 0 and 9 in drawDigit of LCComponent");
        }

        // Calculate segment dimensions
        double gap = 2.0; // Gap between segments
        double stroke = 2.0; // Stroke width
        double segWidth = (width - gap * 3) / 2 - 1; // Width of horizontal segments
        double segHeight = (height - gap * 4) / 3; // Height of vertical segments

        // Segment positions relative to (x, y)
        double[][] segments = {
                { x + gap + 1, y, segWidth, stroke }, // Top (A)
                { x + gap * 2 + segWidth, y + gap, stroke, segHeight }, // Top-right (B)
                { x + gap * 2 + segWidth, y + gap * 3 + segHeight, stroke, segHeight }, // Bottom-right (C)
                { x + gap + 1, y + gap * 4 + segHeight * 2, segWidth, stroke }, // Bottom (D)
                { x, y + gap * 3 + segHeight, stroke, segHeight }, // Bottom-left (E)
                { x, y + gap, stroke, segHeight }, // Top-left (F)
                { x + gap + 1, y + gap * 2 + segHeight, segWidth, stroke }, // Middle (G)
        };

        // Segments activation for digits 0-9
        boolean[][] digitSegments = {
                { true, true, true, true, true, true, false }, // 0
                { false, true, true, false, false, false, false }, // 1
                { true, true, false, true, true, false, true }, // 2
                { true, true, true, true, false, false, true }, // 3
                { false, true, true, false, false, true, true }, // 4
                { true, false, true, true, false, true, true }, // 5
                { true, false, true, true, true, true, true }, // 6
                { true, true, true, false, false, false, false }, // 7
                { true, true, true, true, true, true, true }, // 8
                { true, true, true, true, false, true, true }, // 9
        };

        // Draw the active segments for the specified digit
        for (int i = 0; i < 7; i++) {
            if (digitSegments[digit][i] == drawOn) {
                double[] segment = segments[i];
                path.moveTo(segment[0], segment[1]);
                path.lineTo(segment[0] + segment[2], segment[1]);
                path.lineTo(segment[0] + segment[2], segment[1] + segment[3]);
                path.lineTo(segment[0], segment[1] + segment[3]);
                path.closePath();
            }
        }
    }

    /**
     * Returns a lighter version of the given color using HSB adjustments.
     * 
     * @param color  the color to lighter
     * @param factor factor between 0 and 1
     * @return the lighter color
     */
    protected static Color lightenColor(Color color, float factor) {
        if (factor < 0.0f || factor > 1.0f) {
            throw new IllegalArgumentException("Factor must be between 0.0 and 1.0");
        }
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        float newBrightness = Math.min(hsb[2] + factor * (1.0f - hsb[2]), 1.0f); // Adjust brightness
        return Color.getHSBColor(hsb[0], hsb[1], newBrightness);
    }

    /**
     * Returns a darker version of the given color using HSB adjustments.
     * 
     * @param color  the color to darker
     * @param factor factor between 0 and 1
     * @return the darker color
     */
    protected static Color darkenColor(Color color, float factor) {
        if (factor < 0.0f || factor > 1.0f) {
            throw new IllegalArgumentException("Factor must be between 0.0 and 1.0");
        }
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        float newBrightness = Math.max(hsb[2] * (1.0f - factor), 0.0f); // Decrease brightness
        return Color.getHSBColor(hsb[0], hsb[1], newBrightness);
    }

    public static int getWidth(LCComponent component) {
        switch (component) {
            case AND:
                return 60;
            case NAND:
            case OR:
            case NOR:
            case XOR:
            case NOT:
                return 60;
            case LED:
            case BIT3_DISPLAY:
                return 70;
            case SWITCH:
                return 46;
            default:
                throw new IllegalArgumentException("Invalid LCComponent: " + component);
        }
    }

    public static int getHeight(LCComponent component) {
        switch (component) {
            case AND:
            case NAND:
            case OR:
            case NOR:
            case XOR:
            case NOT:
                return 30;
            case LED:
            case BIT3_DISPLAY:
                return 50;
            case SWITCH:
                return 30;
            default:
                throw new IllegalArgumentException("Invalid LCComponent: " + component);
        }
    }

}
