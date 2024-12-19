import logicircuit.LCDFrameCmd;
import logicircuit.LCDPanel;

public class Main {

    public static void main(String[] args) {
        Parser parser = new Parser();
        LCDFrameCmd frame = new LCDFrameCmd(parser, "LogicFlow", 900, 700);
        LCDPanel drawPannel = frame.drawPanel();
        drawPannel.clear();
    }
}
