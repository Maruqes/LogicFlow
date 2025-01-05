import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.Arrays;
import java.util.concurrent.CompletionStage;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import logicircuit.LCComponent;
import logicircuit.LCDFrameCmd;
import logicircuit.LCDFrameCmd.ResizeCallback;
import logicircuit.LCDPanel;
import logicircuit.LCInputPin;

import java.util.ArrayList;

// probelma nos nomes
public class Main {
    public static LCDPanel drawPannel;
    public static LCDFrameCmd frame;
    public static int LeftMenuWidth = 190;

    public static int SCREEN_WIDTH = 900;
    public static int SCREEN_HEIGHT = 700;

    private static String[] argsPublic;

    public static void DRAW_ALL_STUFF(MainCircuit circuit) {
        Timer timer = new Timer(10, e -> {
            drawPannel.clear();
            Menu.drawAllMenus();
            circuit.drawCircuit();
            ((Timer) e.getSource()).stop();
        });
        timer.start();
    }

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

            // fazer com que o cmp nao va pra ponta do mouse hehe :D
            int xDiff = x - component.getXY()[0];
            int yDiff = y - component.getXY()[1];

            // este timer é para desenhar o componente selecionado no mouse sem flickering
            Timer timer = new Timer(10, e -> {
                int xy2[] = drawPannel.getMouseXY();
                int x2 = xy2[0];
                int y2 = xy2[1];
                Menu.drawOnMouse(x2 - xDiff, y2 - yDiff, circuit);
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
                Menu.moveSelected(x - xDiff, y - yDiff, circuit);
            }
        }
    }

    private static void moveObjects(MainCircuit circuit) {
        int xy[] = drawPannel.getMouseXY();
        int x = xy[0];
        int y = xy[1];
        BasicComponentInterface cmp = circuit.colideWithCompontent(x, y);
        if (cmp != null) {
            // fazer com que o cmp nao va pra ponta do mouse hehe :D
            int xDiff = x - cmp.getXY()[0];
            int yDiff = y - cmp.getXY()[1];
            Timer timer = new Timer(10, e -> {
                int xy2[] = drawPannel.getMouseXY();
                int x2 = xy2[0];
                int y2 = xy2[1];
                if (x2 < LeftMenuWidth) {
                    circuit.removeElement(cmp.getName());
                    DRAW_ALL_STUFF(circuit);
                    return;
                }

                cmp.setPosition(x2 - xDiff, y2 - yDiff);
                DRAW_ALL_STUFF(circuit);

            });
            timer.start();

            while (drawPannel.leftClick()) {
            }

            timer.stop();
        }
    }

    static BasicComponentInterface selectedComponentWire1 = null;
    static BasicComponentInterface selectedComponentWire2 = null;

    private static void turnOnSwitch(MainCircuit circuit) {
        if (selectedComponentWire1 instanceof Switch) {
            Switch sw = (Switch) selectedComponentWire1;
            sw.setState(!sw.getState());
            DRAW_ALL_STUFF(circuit);
        }
    }

    private static void wireObjects(MainCircuit circuit) {
        int xy[] = drawPannel.getMouseXY();
        int x = xy[0];
        int y = xy[1];
        BasicComponentInterface cmp = circuit.colideWithCompontent(x, y);
        if (cmp != null) {
            if (selectedComponentWire1 == null) {
                selectedComponentWire1 = cmp;
                Menu.SetCurrentHolderName("Selected: " + selectedComponentWire1.getName());
                DRAW_ALL_STUFF(circuit);

            } else {
                selectedComponentWire2 = cmp;
                if (selectedComponentWire1.getName().equals(selectedComponentWire2.getName())) {
                    // usar o timer para evitar o flickering nao enc0ntrei outra maneira ou seja sim
                    // vamos ter um loop que roda so uma vez :D
                    Timer timer = new Timer(10, e -> {
                        turnOnSwitch(circuit);
                        selectedComponentWire1 = null;
                        selectedComponentWire2 = null;
                        Menu.clearCurrentHolderName();
                        ((Timer) e.getSource()).stop();
                    });
                    timer.start();
                    while (drawPannel.rightClick()) {
                    }
                    return;
                }
                while (drawPannel.rightClick()) {
                }
                // Exemplificando com um array fixo, mas pode ser lido de qualquer outra fonte
                LCInputPin[] pinsToTry = LCInputPin.values();

                boolean wiringSuccessful = false;

                for (LCInputPin pin : pinsToTry) {
                    try {
                        circuit.wire(selectedComponentWire1.getName(), selectedComponentWire2.getName(), pin);
                        wiringSuccessful = true;
                        break;
                    } catch (Exception e) {
                        if (!"Invalid pin".equals(e.getMessage())) {
                            System.err.println("Erro inesperado: " + e.getMessage());
                            break;
                        }
                    }
                }

                if (!wiringSuccessful) {
                    System.out.println("Não foi possível ligar o componente em nenhum pino válido.");
                }

                selectedComponentWire1 = null;
                selectedComponentWire2 = null;
                Menu.clearCurrentHolderName();
                DRAW_ALL_STUFF(circuit);
            }
        } else {
            selectedComponentWire1 = null;
            selectedComponentWire2 = null;
            Menu.clearCurrentHolderName();
            DRAW_ALL_STUFF(circuit);
        }

        while (drawPannel.rightClick()) {
        }
    }

    public static void runFullApp() {
        MainCircuit circuit = new MainCircuit();

        if (argsPublic.length > 0) {
            SCREEN_WIDTH = Integer.parseInt(argsPublic[0]);
            SCREEN_HEIGHT = Integer.parseInt(argsPublic[1]);
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

    public static void ErrorBox(String message) {
        SwingUtilities.invokeLater(() -> {
            javax.swing.JOptionPane.showMessageDialog(null, message, "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        });
    }

    public static void main(String[] args) {
        argsPublic = args;
        System.out.println("You are running the modded version of LogicFlow");
        LoginRegisterPanel.drawTela();

        // um bocado porco mas funciona :D2
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        runFullApp();
    }
}
