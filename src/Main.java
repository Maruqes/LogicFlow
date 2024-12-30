import logicircuit.LCComponent;
import logicircuit.LCDFrameCmd;
import logicircuit.LCDPanel;
import logicircuit.LCInputPin;

public class Main {
    public static LCDPanel drawPannel;

    public static void main(String[] args) {
        Parser parser = new Parser();
        LCDFrameCmd frame = new LCDFrameCmd(parser, "LogicFlow", 900, 700);
        drawPannel = frame.drawPanel();
        drawPannel.clear();

        // drawPannel.drawComponent(LCComponent.AND, 0, 0);
        // drawPannel.drawComponent(LCComponent.OR, 0, 100);
        // drawPannel.drawComponent(LCComponent.NOT, 0, 200);
        // drawPannel.drawComponent(LCComponent.NAND, 0, 300);

        // drawPannel.drawWire(LCComponent.AND, 0, 0, LCComponent.OR, 0, 100, null,
        // true);

        // drawPannel.drawComponent(LCComponent.BIT3_DISPLAY, 500, 500, 5);
        // drawPannel.drawComponent(LCComponent.XOR, 0, 400);
        // drawPannel.drawComponent(LCComponent.NOR, 0, 500);
        Or or1 = new Or("or1");
        Not not1 = new Not("not1");
        Nand nand1 = new Nand("nand1");
        Nor nor1 = new Nor("nor1");
        Xor xor1 = new Xor("xor1");
        Xor xor2 = new Xor("xor2");
        Xor xor3 = new Xor("xor2");
        Switch switch1 = new Switch(true, "dsdfsd", 123, 123, "scdw");
        Led led1 = new Led(false, "cddsxd");
        Display3bit display1 = new Display3bit(7, "dededdefdef");

        or1.setPosition(300, 100);
        not1.setPosition(400, 200);
        nand1.setPosition(500, 300);
        nor1.setPosition(600, 400);
        xor1.setPosition(700, 500);
        xor3.setPosition(800, 600);
        xor2.setPosition(000, 400);
        led1.setPosition(233, 233);
        display1.setPosition(800, 100);

        Wire wire4 = new Wire(or1, not1);
        Wire wire5 = new Wire(not1, nand1);
        Wire wire6 = new Wire(nand1, nor1);
        Wire wire7 = new Wire(nor1, xor1);
        Wire wire8 = new Wire(xor2, xor1);
        Wire wire10 = new Wire(switch1, display1);
        Wire wire11 = new Wire(xor3, display1);

        wire4.setState(true);
        wire5.setState(true);
        wire6.setState(true);
        wire7.setState(true);
        switch1.setState(true);
        led1.setState(true);

        drawPannel.clear();
        or1.draw();
        not1.draw();
        nand1.draw();
        nor1.draw();
        xor1.draw();
        wire4.draw();
        wire5.draw();
        wire6.draw();
        wire7.draw();
        xor2.draw();
        wire8.draw();
        xor3.draw();
        switch1.draw();
        led1.draw();
        wire10.draw();
        xor3.draw();
        wire11.draw();
        display1.draw();
    }
}
