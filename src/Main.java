import logicircuit.LCComponent;
import logicircuit.LCDFrameCmd;
import logicircuit.LCDPanel;

public class Main {

    public static void main(String[] args) {
        Parser parser = new Parser();
        LCDFrameCmd frame = new LCDFrameCmd(parser, "LogicFlow", 900, 700);
        LCDPanel drawPannel = frame.drawPanel();
        drawPannel.clear();

        drawPannel.drawComponent(LCComponent.AND, 0, 0);
        drawPannel.drawComponent(LCComponent.OR, 0, 100);
        drawPannel.drawComponent(LCComponent.NOT, 0, 200);
        drawPannel.drawComponent(LCComponent.NAND, 0, 300);

        drawPannel.drawWire(LCComponent.AND, 0, 0, LCComponent.OR, 0, 100, null, true);

        drawPannel.drawComponent(LCComponent.BIT3_DISPLAY, 500, 500, 5);
        drawPannel.drawComponent(LCComponent.XOR, 0, 400);
        drawPannel.drawComponent(LCComponent.NOR, 0, 500);
        
    }
}
