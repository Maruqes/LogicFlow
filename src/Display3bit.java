import logicircuit.LCComponent;
import logicircuit.LCInputPin;

public class Display3bit extends BasicComponent{
    private int value;
    private int pinCallCount;
    private LCInputPin currentPin;

    public Display3bit(int value, String nome) {
        super(LCComponent.BIT3_DISPLAY, nome);
        this.value = value;
        pinCallCount = 0;
        currentPin = LCInputPin.PIN_A;
    }

    public Display3bit(int value, String nome, int setX, int setY) {
        super(LCComponent.BIT3_DISPLAY, nome, setX, setY);        
        this.value = value;
        pinCallCount = 0;
        currentPin = LCInputPin.PIN_A;
    }

    public Display3bit(int value, String nome, int setX, int setY, String legend){
        super(LCComponent.BIT3_DISPLAY, nome, setX, setY, legend);
        this.value = value;
        pinCallCount = 0;
        currentPin = LCInputPin.PIN_A;
    }

    public void setvalue(int value) {
        this.value = value;
    }

    public int getvalue() {
        return value;
    }

    @Override
    public void draw() {
        Main.drawPannel.drawComponent(super.getType(), super.getXY()[0], super.getXY()[1], value);;
    }

    public LCInputPin getNextPin() {
        if (pinCallCount == 0) {
            currentPin = LCInputPin.PIN_A;
        } else if (pinCallCount == 1) {
            currentPin = LCInputPin.PIN_B;
        } else if (pinCallCount == 2) {
            currentPin = LCInputPin.PIN_C;
        }else {
            throw new IllegalStateException("Invalid pin state");
        }
        pinCallCount++;
        return currentPin;
    }
}
