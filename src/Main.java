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
    }
}
