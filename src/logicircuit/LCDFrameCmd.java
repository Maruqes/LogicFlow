package logicircuit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * A window with a panel to draw the circuit and a text filed to read commands.
 */
public class LCDFrameCmd {
    private static final int WIDTH_DEFAULT = 800;
    private static final int HEIGHT_DEFAULT = 800;
    private static final int PADDING_DEFAULT = 5;
    private static final String exitCommand = "exit";

    // Fields
    public final JFrame frame;
    private final LCDPanel lcPanel;

    /**
     * Constructs a new LCDFrameCmd with default title and size
     * 
     * @param processor The object whose process method is invoked when the enter
     *                  key is pressed.
     */
    public LCDFrameCmd(CmdProcessor processor) {
        this(processor, "Simulador de circuito lógico", WIDTH_DEFAULT, HEIGHT_DEFAULT);
    }

    /**
     * Constructs a new LCDFrameCmd with default size and specified title
     * 
     * @param processor The object whose process method is invoked when the enter
     *                  key is pressed.
     * @param title     the title to be displayed in the frame's border.
     */
    public LCDFrameCmd(CmdProcessor processor, String title) {
        this(processor, title, WIDTH_DEFAULT, HEIGHT_DEFAULT);
    }

    /**
     * Constructs a new LCDFrameCmd with specified title and has width width and
     * height height
     * 
     * @param processor The object whose process method is invoked when the enter
     *                  key is pressed.
     * @param title     the title to be displayed in the frame's border.
     * @param width     the width of the frame in pixels
     * @param height    the height of the frame in pixels
     */
    public LCDFrameCmd(CmdProcessor processor, String title, int width, int height) {
        this.frame = new JFrame(title);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLayout(new BorderLayout());

        lcPanel = new LCDPanel(width, height);
        // Add the Logic Circuit panel to the frame
        this.frame.add(lcPanel.getDrawPanel(), BorderLayout.CENTER);

        // Create a panel to hold the region with the text filed to introduce commands
        JPanel cmdPanel = new JPanel();
        cmdPanel.setBorder(new EmptyBorder(PADDING_DEFAULT, PADDING_DEFAULT, PADDING_DEFAULT, PADDING_DEFAULT));
        cmdPanel.setLayout(new BorderLayout(PADDING_DEFAULT, PADDING_DEFAULT));
        // Create labels for title and prompt
        JLabel titleLabel = new JLabel("Introduzir comando (seguido de Enter) - Comando EXIT para terminar",
                SwingConstants.LEFT);
        titleLabel.setPreferredSize(new Dimension(width, 20));
        cmdPanel.add(titleLabel, BorderLayout.NORTH);
        JLabel promptLabel = new JLabel("> ", SwingConstants.LEFT);
        cmdPanel.add(promptLabel, BorderLayout.WEST);
        // Create a TextField to read a command
        JTextField cmdText = new JTextField();
        cmdText.setPreferredSize(new Dimension(width, 30));
        cmdPanel.add(cmdText, BorderLayout.CENTER);
        // Create a JLabel for error messages
        JLabel errorLabel = new JLabel("", SwingConstants.LEFT);
        errorLabel.setForeground(Color.RED); // Set the text color to red for errors
        errorLabel.setPreferredSize(new Dimension(width, 30));
        cmdPanel.add(errorLabel, BorderLayout.SOUTH);
        // Add the panel CmdPanel to the bottom of the frame
        this.frame.add(cmdPanel, BorderLayout.SOUTH);

        // Add an ActionListener to process the input of TextField cmdText
        // actionPerformed is called when the key Enter is pressed
        cmdText.addActionListener(new ActionListener() {
            // Event that
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = cmdText.getText();
                if (command.trim().equalsIgnoreCase(exitCommand)) {
                    frame.setVisible(true);
                    frame.dispose();
                    System.exit(0);
                } else {
                    String errorMsg = processor.process(command); // Call the placeholder method
                    cmdText.setText(""); // Clear the text field after submission
                    errorLabel.setText(errorMsg != null ? errorMsg : "");
                }
            }
        });

        this.frame.setPreferredSize(new Dimension(width, height + 80));
        this.frame.pack(); // Sizes the frame so that all its contents are at or above their preferred
                           // sizes
        this.frame.setLocationRelativeTo(null); // Centers the frame on the screen
        this.frame.setVisible(true);
    }

    public void setMinimumSize(Dimension dimension) {
        this.frame.setMinimumSize(dimension);
    }

    @FunctionalInterface
    public interface ResizeCallback {
        void onResize(int width, int height);
    }

    // Método que aceita uma função como callback
    public void windowResizingCallback(LCDFrameCmd frame, ResizeCallback callback) {
        frame.frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension newSize = frame.frame.getSize();
                callback.onResize(newSize.width, newSize.height);
            }
        });
    }

    /**
     * Return the drawing panel object (LCDPanel) to be used to draw components and
     * connection
     * 
     * @return the drawing object panel of the frame
     */
    public LCDPanel drawPanel() {
        return lcPanel;
    }

}
