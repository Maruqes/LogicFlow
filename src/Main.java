import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import logicircuit.LCComponent;
import logicircuit.LCDFrameCmd;
import logicircuit.LCDFrameCmd.ResizeCallback;
import logicircuit.LCDPanel;
import logicircuit.LCInputPin;

import java.util.ArrayList;

public class Main {
    public static LCDPanel drawPannel;
    public static LCDFrameCmd frame;
    public static int LeftMenuWidth = 190;

    public static int SCREEN_WIDTH = 900;
    public static int SCREEN_HEIGHT = 700;

    public static void restartProgram(String... newArgs) throws IOException {
        String javaBin = System.getProperty("java.home") + "/bin/java";
        String classPath = System.getProperty("java.class.path");
        String className = Main.class.getName();

        ProcessBuilder builder = new ProcessBuilder(
                javaBin, "-cp", classPath, className);

        builder.command().addAll(Arrays.asList(newArgs));

        builder.start();

        System.exit(0);
    }

    private static void LeftMenuUsage(MainCircuit circuit) {
        int xy[] = drawPannel.getMouseXY();
        int x = xy[0];
        int y = xy[1];

        BasicComponent component = Menu.getLeftClickColision();
        if (component != null) {
            System.out.println("You pressed " + component.getType() + " at " + component.getXY()[0] + " "
                    + component.getXY()[1]);

            Menu.setAnythingSelected(component);

            // este timer é para desenhar o componente selecionado no mouse sem flickering
            Timer timer = new Timer(10, e -> {
                int xy2[] = drawPannel.getMouseXY();
                int x2 = xy2[0];
                int y2 = xy2[1];
                Menu.drawOnMouse(x2, y2, circuit);
            });
            timer.start();

            // Wait for the user to release the mouse button
            while (drawPannel.leftClick()) {
            }

            if (Menu.isAnythingSelected()) {
                timer.stop();
                xy = drawPannel.getMouseXY();
                x = xy[0];
                y = xy[1];
                Menu.moveSelected(x, y, circuit);
            }
        }
    }

    private static void moveObjects(MainCircuit circuit) {
        int xy[] = drawPannel.getMouseXY();
        int x = xy[0];
        int y = xy[1];
        BasicComponent cmp = circuit.colideWithCompontent(x, y);
        if (cmp != null) {
            Timer timer = new Timer(10, e -> {
                int xy2[] = drawPannel.getMouseXY();
                int x2 = xy2[0];
                int y2 = xy2[1];
                if (x2 < LeftMenuWidth) {
                    circuit.removeElement(cmp.getName());
                    drawPannel.clear();
                    circuit.drawCircuit();
                    return;
                }
                cmp.setPosition(x2, y2);
                circuit.drawCircuit();

            });
            timer.start();

            while (drawPannel.leftClick()) {
            }

            timer.stop();
        }
    }

    static BasicComponent selectedComponentWire1 = null;
    static BasicComponent selectedComponentWire2 = null;

    private static void turnOnSwitch(MainCircuit circuit) {
        if (selectedComponentWire1 instanceof Switch) {
            Switch sw = (Switch) selectedComponentWire1;
            sw.setState(!sw.getState());
            circuit.drawCircuit();
        }
    }

    private static void wireObjects(MainCircuit circuit) {
        int xy[] = drawPannel.getMouseXY();
        int x = xy[0];
        int y = xy[1];
        BasicComponent cmp = circuit.colideWithCompontent(x, y);
        if (cmp != null) {
            if (selectedComponentWire1 == null) {
                selectedComponentWire1 = cmp;
                while (drawPannel.rightClick()) {
                }

            } else {
                selectedComponentWire2 = cmp;
                if (selectedComponentWire1.getName().equals(selectedComponentWire2.getName())) {
                    // usar o timer para evitar o flickering nao enc0ntrei outra maneira ou seja sim
                    // vamos ter um loop que roda so uma vez :D
                    Timer timer = new Timer(10, e -> {
                        turnOnSwitch(circuit);
                        selectedComponentWire1 = null;
                        selectedComponentWire2 = null;

                        ((Timer) e.getSource()).stop();
                    });
                    timer.start();
                    while (drawPannel.rightClick()) {
                    }
                    return;
                }
                while (drawPannel.rightClick()) {
                }
                try {
                    circuit.wire(selectedComponentWire1.getName(), selectedComponentWire2.getName(), LCInputPin.PIN_A);
                } catch (Exception e) {
                    if (e.getMessage().equals("Invalid pin")) {
                        try {
                            circuit.wire(selectedComponentWire1.getName(), selectedComponentWire2.getName(),
                                    LCInputPin.PIN_B);
                        } catch (Exception e2) {

                            if (e.getMessage().equals("Invalid pin")) {
                                try {
                                    circuit.wire(selectedComponentWire1.getName(), selectedComponentWire2.getName(),
                                            LCInputPin.PIN_C);

                                } catch (Exception e3) {
                                    System.out.println("Error: " + e3.getMessage());
                                }
                            } else {
                                System.out.println("Error: " + e.getMessage());
                            }
                        }
                    } else {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                selectedComponentWire1 = null;
                selectedComponentWire2 = null;
                circuit.drawCircuit();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("You are running the modded version of LogicFlow");
        MainCircuit circuit = new MainCircuit();

        if (args.length > 0) {
            SCREEN_WIDTH = Integer.parseInt(args[0]);
            SCREEN_HEIGHT = Integer.parseInt(args[1]);
        }

        ProcessCommands parser = new ProcessCommands(circuit);
        frame = new LCDFrameCmd(parser, "LogicFlow", SCREEN_WIDTH, SCREEN_HEIGHT);
        frame.setMinimumSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        drawPannel = frame.drawPanel();
        drawPannel.clear();

        Menu.initComponents();

        Menu.callBackResize(SCREEN_WIDTH, SCREEN_HEIGHT, circuit);
        Menu.drawAllMenus();

        ResizeCallback resizeCallback = (int width, int height) -> Menu.callBackResize(width, height, circuit);

        frame.windowResizingCallback(frame, resizeCallback);

        boolean running = true;
        while (running) {
            try {
                if (drawPannel.leftClick()) {
                    LeftMenuUsage(circuit);
                    moveObjects(circuit);
                } else if (drawPannel.rightClick()) {
                    wireObjects(circuit);
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

        }

    }
}
