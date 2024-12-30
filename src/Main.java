import logicircuit.LCComponent;
import logicircuit.LCDFrameCmd;
import logicircuit.LCDPanel;

public class Main {
    public static LCDPanel drawPannel;

    public static void main(String[] args) {
        MainCircuit circuit = new MainCircuit();

        Parser parser = new Parser();
        LCDFrameCmd frame = new LCDFrameCmd(parser, "LogicFlow", 900, 700);
        drawPannel = frame.drawPanel();
        drawPannel.clear();

        drawPannel.clear();
        circuit.add(LCComponent.SWITCH, true, "Switch1", 0, 10, "Switch1");
        circuit.add(LCComponent.SWITCH, false, "Switch2", 0, 110, "Switch2");
        circuit.add(LCComponent.SWITCH, true, "Switch3", 0, 210, "Switch3");
        circuit.add(LCComponent.AND, "And1", 100, 60, "And1");
        circuit.add(LCComponent.OR, "Or1", 100, 160, "Or1");
        circuit.add(LCComponent.OR, "Or2", 200, 110, "Or2");
        circuit.add(LCComponent.NOT, "Not1", 300, 110, "Not1");
        circuit.add(LCComponent.LED, 0, "Led1", 400, 110, "Led1");
        circuit.drawCircuit();
    }
}


