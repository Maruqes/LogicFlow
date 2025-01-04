package logicircuit;

import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

public class LCDPanel {
    // Default color definition
    protected static final Color BACKGROUND_COLOR_DEFAULT = Color.white;
    protected static final Color FILL_COLOR_DEFAULT = Color.lightGray;
    protected static final Color STROKE_COLOR_DEFAULT = Color.black;
    protected static final Color TEXT_COLOR_DEFAULT = Color.black;
    protected static final Color ON_COLOR_DEFAULT = new Color(105, 235, 119); // Color.green;
    protected static final Color OFF_COLOR_DEFAULT = new Color(168, 119, 112); // Color.red;

    // Private fields
    private final DrawPanel drawPanel;
    private BufferedImage canvas;

    private volatile boolean leftClicked = false;
    private volatile boolean rightClicked = false;

    // Constructor
    protected LCDPanel() {
        this(500, 500);
    }

    protected LCDPanel(int width, int height) {
        drawPanel = new DrawPanel();
        drawPanel.setPreferredSize(new Dimension(width, height));
        drawPanel.setBackground(BACKGROUND_COLOR_DEFAULT);
        drawPanel.setDoubleBuffered(true);

        drawPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        GroupLayout layout = new GroupLayout(drawPanel);
        drawPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize the canvas with the size of the panel
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        // Add mouse listener to track clicks
        drawPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    leftClicked = true; // Mark as clicked when pressed
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    rightClicked = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    leftClicked = false; // Reset when released
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    rightClicked = false;
                }
            }
        });
    }

    /**
     * Draw a component that has no state
     * 
     * @param elemType Component type from LCComponent (AND, OR, ...)
     * @param xPos     coordinate X
     * @param yPos     coordinate Y
     */
    public void drawComponent(LCComponent elemType, int xPos, int yPos) {
        if (elemType.hasState())
            throw new IllegalArgumentException(
                    "Illegal argument for ElemType \" + elemType + \" in addElement with state");
        this.drawElem(elemType, xPos, yPos, FILL_COLOR_DEFAULT);
    }

    /**
     * Draw a component that has no state with a legend
     * 
     * @param elemType Component type from LCComponent (AND, OR, ...)
     * @param xPos     coordinate X
     * @param yPos     coordinate Y
     * @param legend   Text to display below the component representation
     */
    public void drawComponent(LCComponent elemType, int xPos, int yPos, String legend) {
        drawComponent(elemType, xPos, yPos);
        if ((legend != null) && (!legend.isEmpty()))
            drawText(xPos, yPos, legend);
    }

    /**
     * Draw a component that has a different representation based on isOn value
     * 
     * @param elemType Component type from LCComponent with isOn (LED, SWITCH,
     *                 BIT3_DISPLAY)
     * @param xPos     coordinate X
     * @param yPos     coordinate Y
     * @param isOn     is the component in state ON or OFF
     */
    public void drawComponent(LCComponent elemType, int xPos, int yPos, boolean isOn) {
        if (!elemType.hasState())
            throw new IllegalArgumentException(
                    "Illegal argument for ElemType " + elemType + " in addElement with isOn");
        this.drawElem(elemType, xPos, yPos, isOn ? ON_COLOR_DEFAULT : OFF_COLOR_DEFAULT);
    }

    /**
     * Draw a component that has a different representation based on isOn value
     * 
     * @param elemType Component type from LCComponent with isOn (LED, SWITCH)
     * @param xPos     coordinate X
     * @param yPos     coordinate Y
     * @param isOn     is the component in state ON or OFF
     * @param legend   Text to display below the component representation
     */
    public void drawComponent(LCComponent elemType, int xPos, int yPos, boolean isOn, String legend) {
        drawComponent(elemType, xPos, yPos, isOn);
        if ((legend != null) && (!legend.isEmpty()))
            drawText(xPos, yPos, legend);
    }

    /**
     * Draw a component that accept an integer value - Used for a 3 bit Display
     * 
     * @param elemType Component type from LCComponent BIT3_DISPLAY
     * @param xPos     coordinate X
     * @param yPos     coordinate Y
     * @param value    Value to display on 3 bit display
     */
    public void drawComponent(LCComponent elemType, int xPos, int yPos, int value) {
        if (value < 0 || value > 7)
            throw new IllegalArgumentException(
                    "Illegal argument for ElemType " + elemType + ": Value must be between 0.0 and 1.0");
        this.drawElem(elemType, xPos, yPos, value > 0 ? ON_COLOR_DEFAULT : OFF_COLOR_DEFAULT, value);
    }

    /**
     * Draw a connection (wire) between 2 components
     * 
     * @param elemTypeFrom Component type from LCComponent that has an output pin
     * @param xFrom        coordinate X of the output component
     * @param yFrom        coordinate Y of the output component
     * @param elemTypeTo   Component type from LCComponent that has at least one
     *                     input pin
     * @param xTo          coordinate X of the input component
     * @param yTo          coordinate Y of the input component
     * @param pinNumberTo  Connection pin of input component (PIN_A, PIN_B, ...)
     * @param isOn         is the connection in state ON or OFF
     */
    public void drawWire(LCComponent elemTypeFrom, int xFrom, int yFrom,
            LCComponent elemTypeTo, int xTo, int yTo, LCInputPin pinNumberTo, boolean isOn) {
        // Validation of component type
        if (!elemTypeFrom.hasOutput)
            throw new IllegalArgumentException(
                    "Illegal argument for output component " + elemTypeFrom + " in drawWire");
        if (elemTypeTo.numberInputPins == null)
            throw new IllegalArgumentException("Illegal argument for input component " + elemTypeTo + " in drawWire");
        int[] coordIni = elemTypeFrom.getCoordOutputPin();
        int[] coordEnd = elemTypeTo.getCoordInputPin(pinNumberTo);
        this.drawSmoothLine(coordIni[0] + xFrom, coordIni[1] + yFrom, coordEnd[0] + xTo, coordEnd[1] + yTo,
                isOn ? ON_COLOR_DEFAULT : OFF_COLOR_DEFAULT);
    }

    /**
     * Clear the drawing area
     */
    public void clear() {
        Graphics2D g2d = canvas.createGraphics();
        g2d.setColor(BACKGROUND_COLOR_DEFAULT);
        g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g2d.dispose();
        drawPanel.repaint();
    }

    protected JPanel getDrawPanel() {
        return drawPanel;
    }

    // ============================================================
    private void drawElem(LCComponent component, int xPos, int yPos, Color fillColor, Object... extraArgs) {
        Graphics2D g2d = canvas.createGraphics();
        // Enable antialiasing for smoother drawing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        // Create a translation transform
        AffineTransform translateXY = AffineTransform.getTranslateInstance(xPos, yPos);

        // Draw the component
        component.draw(g2d, fillColor, STROKE_COLOR_DEFAULT, translateXY, extraArgs);

        g2d.dispose();
        drawPanel.repaint();
    }

    public void drawText(int x, int y, String text) {
        Graphics2D g2d = canvas.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Font baseFont = new Font("Arial", Font.PLAIN, 12);
        // Apply horizontal scaling to create a narrow font
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.scale(0.85, 1.0);
        Font narrowFont = baseFont.deriveFont(affineTransform);
        g2d.setFont(narrowFont);
        g2d.setColor(TEXT_COLOR_DEFAULT);
        // Get font metrics for positioning
        FontMetrics metrics = g2d.getFontMetrics(narrowFont);
        int textWidth = metrics.stringWidth(text);
        // int textHeight = metrics.getHeight();
        // Calculate x and y for centered text
        int xTxt = x + (LCComponent.DELTA_X_LEGEND - textWidth) / 2;
        int yTxT = y + LCComponent.DELTA_Y_LEGEND + metrics.getAscent();
        // Draw the string
        g2d.drawString(text, xTxt, yTxT);

        g2d.dispose();
        drawPanel.repaint();
    }

    private void drawSmoothLine(int x1, int y1, int x2, int y2, Color insideColor) {
        Graphics2D g2d = canvas.createGraphics();

        // Enable antialiasing for smoother drawing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        int stretching = 15; // Define how much to stretch the curve
        // Calculate midpoint of the rectangle formed by the two points
        int midX = (x1 + x2) / 2;
        // Define control points for a smooth curve
        int ctrl1X = midX + stretching;
        int ctrl2X = midX - stretching;

        // Create the path
        Path2D.Double path = new Path2D.Double();
        path.moveTo(x1, y1);
        path.curveTo(ctrl1X, y1, ctrl2X, y2, x2, y2);

        // Draw border
        g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(STROKE_COLOR_DEFAULT);
        g2d.draw(path);
        // Draw inside
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(insideColor);
        g2d.draw(path);
        // Draw connectors
        path.reset();
        LCComponent.drawPinConnector(path, x1, y1);
        LCComponent.drawPinConnector(path, x2, y2);
        g2d.setColor(STROKE_COLOR_DEFAULT);
        g2d.draw(path);
        g2d.fill(path);

        g2d.dispose();
        drawPanel.repaint();
    }

    public void drawRectangle(int x, int y, int width, int height, Color fillColor) {
        Graphics2D g2d = canvas.createGraphics();
        g2d.setColor(fillColor);
        g2d.fillRect(x, y, width, height);

        // Desenhar borda
        g2d.setColor(fillColor.darker());
        g2d.setStroke(new BasicStroke(3)); // Define a espessura da borda
        g2d.drawRect(x, y, width, height);

        g2d.dispose();
        drawPanel.repaint();
    }

    public void drawLine(int x1, int y1, int x2, int y2, int thickness, Color color) {
        Graphics2D g2d = canvas.createGraphics();
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(thickness));
        g2d.drawLine(x1, y1, x2, y2);
        g2d.dispose();
        drawPanel.repaint();
    }

    public int[] getMouseXY() {
        Point mousePosition = drawPanel.getMousePosition();
        if (mousePosition != null) {
            return new int[] { mousePosition.x, mousePosition.y };
        } else {
            return new int[] { -1, -1 }; // Return -1, -1 if the mouse is not within the panel
        }
    }

    // public boolean leftClick() {
    // AWTEvent event =
    // Toolkit.getDefaultToolkit().getSystemEventQueue().peekEvent();
    // if (event instanceof MouseEvent) {
    // return SwingUtilities.isLeftMouseButton((MouseEvent) event);
    // }
    // return false;
    // }

    public boolean leftClick() {
        // tiras o volatil para de funfar hahah la po crl do java moço
        return leftClicked;
    }

    public boolean rightClick() {
        return rightClicked;
    }

    public void setNewSize(int width, int height) {
        drawPanel.setPreferredSize(new Dimension(width, height));
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        drawPanel.revalidate();
        drawPanel.repaint();
    }

    /**
     * Inner class that extends JPanel to override de paint method
     */
    private class DrawPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw the buffered canvas image onto the panel
            g.drawImage(canvas, 0, 0, null);
        }

    }

}
