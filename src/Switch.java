import logicircuit.LCComponent;
import logicircuit.LCInputPin;

public class Switch extends BasicComponent{
    private boolean state;
    private int pinCallCount;
    private LCInputPin currentPin;


    public Switch(boolean state, String nome) {
        super(LCComponent.SWITCH, nome);
        this.state = state;
        pinCallCount = 0;
        currentPin = LCInputPin.PIN_A;
    }

    public Switch(boolean state, String nome, int setX, int setY) {
        super(LCComponent.SWITCH, nome, setX, setY);        
        this.state = state;
        pinCallCount = 0;
        currentPin = LCInputPin.PIN_A;
    }

    public Switch(boolean state, String nome, int setX, int setY, String legend){
        super(LCComponent.SWITCH, nome, setX, setY, legend);
        this.state = state;
        pinCallCount = 0;
        currentPin = LCInputPin.PIN_A;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean getState() {
        return state;
    }

    @Override
    public void draw() {
        Main.drawPannel.drawComponent(super.getType(), super.getXY()[0], super.getXY()[1], state, super.getLegend());
    }

    public LCInputPin getNextPin() {
        if (pinCallCount == 0) {
            currentPin = LCInputPin.PIN_A;
        } else {
            throw new IllegalStateException("Invalid pin state");
        }
        pinCallCount++;
        return currentPin;
    }
}
