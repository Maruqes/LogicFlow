import java.awt.Color;
import java.util.ArrayList;

import logicircuit.LCComponent;

public class Menu {

    private static ArrayList<BasicComponent> components = new ArrayList<>();
    public static String currentHolderName = "Nothing selected";

    public static BasicComponent getLeftClickColision() {
        int xy[] = ProgCircuito.drawPannel.getMouseXY();
        int x = xy[0];
        int y = xy[1];
        for (BasicComponent component : components) {
            int[] pos = component.getXY();
            if (x >= pos[0] && x <= pos[0] + LCComponent.getWidth(component.getType()) && y >= pos[1]
                    && y <= pos[1] + LCComponent.getHeight(component.getType())) {
                return component;
            }
        }
        return null;
    }

    public static void initComponents() {
        components.add(new BasicComponent(LCComponent.AND, "and", 10, 50, "AND"));
        components.add(new BasicComponent(LCComponent.OR, "or", 10, 50 + ProgCircuito.SCREEN_HEIGHT / 15, "OR"));
        components.add(new BasicComponent(LCComponent.NOT, "not", 10, 50 + 2 * ProgCircuito.SCREEN_HEIGHT / 15, "NOT"));
        components.add(new BasicComponent(LCComponent.XOR, "xor", 110, 50, "XOR"));
        components.add(new BasicComponent(LCComponent.NAND, "nand", 110, 50 + ProgCircuito.SCREEN_HEIGHT / 15, "NAND"));
        components.add(new BasicComponent(LCComponent.NOR, "nor", 110, 50 + 2 * ProgCircuito.SCREEN_HEIGHT / 15, "NOR"));
        components.add(new Switch(false, "switch1", 10, 50 + 5 * ProgCircuito.SCREEN_HEIGHT / 15, "SWITCH"));
        components.add(new Led(0, "led", 10, 50 + 10 * ProgCircuito.SCREEN_HEIGHT / 15, "LED"));
        components.add(new Display3bit(0, "display", 110, 70 + 10 * ProgCircuito.SCREEN_HEIGHT / 15));
    }

    public static void drawExampleGates(int space) {
        components.get(0).setPosition(10, 50);
        components.get(1).setPosition(10, 50 + space);
        components.get(2).setPosition(10, 50 + 2 * space);
        components.get(3).setPosition(110, 50);
        components.get(4).setPosition(110, 50 + space);
        components.get(5).setPosition(110, 50 + 2 * space);
        components.get(6).setPosition(10, 50 + 5 * space);
        components.get(7).setPosition(10, 50 + 10 * space);
        components.get(8).setPosition(110, 70 + 10 * space);

        for (BasicComponent component : components) {
            component.draw();
        }
    }

    public static void drawLeftSideBar() {
        ProgCircuito.drawPannel.drawRectangle(0, 0, ProgCircuito.LeftMenuWidth, ProgCircuito.SCREEN_HEIGHT, Color.GRAY);
    }

    public static void drawAllMenus() {
        ProgCircuito.drawPannel.clear();
        drawLeftSideBar();
        drawExampleGates(ProgCircuito.SCREEN_HEIGHT / 15);
        ProgCircuito.drawPannel.drawText(50, -25, currentHolderName);
    }

    public static void callBackResize(int width, int height, MainCircuit circuit) {
        ProgCircuito.SCREEN_WIDTH = width;
        ProgCircuito.SCREEN_HEIGHT = height;
        ProgCircuito.drawPannel.setNewSize(width, height);
        drawAllMenus();
        circuit.drawCircuit();
        System.out.println("Width: " + ProgCircuito.SCREEN_WIDTH + " Height: " + ProgCircuito.SCREEN_HEIGHT);
    }

    private static boolean anythingSelected = false;
    private static BasicComponentInterface selectedComponent = null;

    public static boolean isAnythingSelected() {
        return anythingSelected;
    }

    public static void setAnythingSelected(BasicComponent selectedComponentt) {
        anythingSelected = true;
        selectedComponent = selectedComponentt.cloneCMP();
    }

    public static void moveSelected(int x, int y, MainCircuit circuit) {
        if (x < ProgCircuito.LeftMenuWidth) {
            ProgCircuito.DRAW_ALL_STUFF(circuit);
            return;
        }
        if (anythingSelected) {
            if (selectedComponent.getType() == LCComponent.SWITCH) {
                circuit.add(LCComponent.SWITCH, false, x - ProgCircuito.LeftMenuWidth, y, "");
            } else if (selectedComponent.getType() == LCComponent.LED
                    || selectedComponent.getType() == LCComponent.BIT3_DISPLAY) {
                circuit.add(selectedComponent.getType(), 0, x - ProgCircuito.LeftMenuWidth, y, "");
            } else {
                circuit.add(selectedComponent.getType(), x - ProgCircuito.LeftMenuWidth, y, "");
            }
            ProgCircuito.DRAW_ALL_STUFF(circuit);
            anythingSelected = false;
        }
    }

    public static void drawOnMouse(int x, int y, MainCircuit circuit) {
        if (anythingSelected) {
            ProgCircuito.drawPannel.clear();
            circuit.drawCircuit();
            if (selectedComponent.getType() == LCComponent.SWITCH) {
                ProgCircuito.drawPannel.drawComponent(selectedComponent.getType(), x, y, false, "");
            } else if (selectedComponent.getType() == LCComponent.LED) {
                ProgCircuito.drawPannel.drawComponent(selectedComponent.getType(), x, y, false, "");
            } else if (selectedComponent.getType() == LCComponent.BIT3_DISPLAY) {
                ProgCircuito.drawPannel.drawComponent(selectedComponent.getType(), x, y, 0);
            } else {
                ProgCircuito.drawPannel.drawComponent(selectedComponent.getType(), x, y, "");
            }

        }
    }

    public static void SetCurrentHolderName(String name) {
        currentHolderName = name;
    }

    public static void clearCurrentHolderName() {
        currentHolderName = "Nothing selected";
    }

}
