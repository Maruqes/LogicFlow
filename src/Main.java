import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.util.Arrays;

import logicircuit.LCComponent;
import logicircuit.LCDFrameCmd;
import logicircuit.LCDFrameCmd.ResizeCallback;
import logicircuit.LCDPanel;

public class Main {
    public static LCDPanel drawPannel;
    public static LCDFrameCmd frame;

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

    private static void drawExampleGates(int space) {
        drawPannel.drawText(20, 0, "Logic Gates");
        drawPannel.drawComponent(LCComponent.AND, 10, 50, "AND");
        drawPannel.drawComponent(LCComponent.OR, 10, 50 + space, "OR");
        drawPannel.drawComponent(LCComponent.NOT, 10, 50 + 2 * space, "NOT");
        drawPannel.drawComponent(LCComponent.XOR, 110, 50, "XOR");
        drawPannel.drawComponent(LCComponent.NAND, 110, 50 + space, "NAND");
        drawPannel.drawComponent(LCComponent.NOR, 110, 50 + 2 * space, "NOR");

        drawPannel.drawText(20, 50 + 4 * space, "Switches");
        drawPannel.drawComponent(LCComponent.SWITCH, 10, 50 + 5 * space, false, "SWITCH");
        drawPannel.drawComponent(LCComponent.SWITCH, 110, 50 + 5 * space, true, "SWITCH");

        drawPannel.drawText(20, 50 + 9 * space, "Displays and LED");
        drawPannel.drawComponent(LCComponent.LED, 10, 50 + 10 * space, false, "LED");
        drawPannel.drawComponent(LCComponent.BIT3_DISPLAY, 110, 70 + 10 * space, 0);
    }

    private static void drawLeftSideBar() {
        drawPannel.drawRectagle(0, 0, 190, SCREEN_HEIGHT, Color.GRAY);
    }

    private static void drawAllMenus() {
        drawPannel.clear();
        drawLeftSideBar();
        drawExampleGates(SCREEN_HEIGHT / 15);
    }

    public static void callBackResize(int width, int height) {
        SCREEN_WIDTH = width;
        SCREEN_HEIGHT = height;
        drawPannel.setNewSize(width, height);
        drawAllMenus();
        System.out.println("Width: " + SCREEN_WIDTH + " Height: " + SCREEN_HEIGHT);
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

        callBackResize(SCREEN_WIDTH, SCREEN_HEIGHT);
        drawAllMenus();

        ResizeCallback resizeCallback = (int width, int height) -> callBackResize(width, height);

        frame.windowResizingCallback(frame, resizeCallback);

        boolean running = true;
        while (running) {
            if (drawPannel.leftClick()) {
                int xy[] = drawPannel.getMouseXY();
                int x = xy[0];
                int y = xy[1];
                System.out.println("X: " + x + " Y: " + y);
            }
        }

    }
}
