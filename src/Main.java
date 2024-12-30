import logicircuit.LCDFrameCmd;
import logicircuit.LCDPanel;

public class Main {
    public static LCDPanel drawPannel;

    public static void main(String[] args) {
        MainCircuit circuit = new MainCircuit();

        Parser parser = new Parser(circuit);
        LCDFrameCmd frame = new LCDFrameCmd(parser, "LogicFlow", 900, 700);
        drawPannel = frame.drawPanel();
        drawPannel.clear();

        // circuit.add(LCComponent.SWITCH, true, "Switch1", 0, 10, "Switch1");
        // circuit.add(LCComponent.SWITCH, false, "Switch2", 0, 110, "Switch2");
        // circuit.add(LCComponent.SWITCH, true, "Switch3", 0, 210, "Switch3");
        // circuit.add(LCComponent.AND, "And1", 100, 60, "And1");
        // circuit.add(LCComponent.OR, "Or1", 100, 160, "Or1");
        // circuit.add(LCComponent.OR, "Or2", 200, 110, "Or2");
        // circuit.add(LCComponent.NOT, "Not1", 300, 110, "Not1");
        // circuit.add(LCComponent.NOT, "Not2", 400, 110, "Not2");
        // circuit.add(LCComponent.BIT3_DISPLAY, 0, "bit1", 500, 110, "bit1");

        // circuit.wire("Switch1", "And1", LCInputPin.PIN_A);
        // circuit.wire("Switch2", "And1", LCInputPin.PIN_B);
        // circuit.wire("Switch2", "Or1", LCInputPin.PIN_A);
        // circuit.wire("Switch3", "Or1", LCInputPin.PIN_B);

        // circuit.wire("And1", "Or2", LCInputPin.PIN_A);
        // circuit.wire("Or1", "Or2", LCInputPin.PIN_B);

        // circuit.wire("Or2", "Not1", LCInputPin.PIN_A);

        // circuit.wire("Not1", "Not2", LCInputPin.PIN_A);
        // circuit.wire("Not2", "bit1", LCInputPin.PIN_A);
        // circuit.wire("Switch2", "bit1", LCInputPin.PIN_B);
        // circuit.wire("Switch3", "bit1", LCInputPin.PIN_C);

        // circuit.drawCircuit();
    }
}
