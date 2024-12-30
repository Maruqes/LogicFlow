import logicircuit.LCComponent;
import logicircuit.LCInputPin;

public class Led extends BasicComponent implements OutputInterface {
    private int value;
    private int pinCallCount;
    private LCInputPin currentPin;

    public Led(int value, String nome) {
        super(LCComponent.LED, nome);
        if (value < 0 || value > 1) {
            throw new IllegalArgumentException("Invalid value");
        }
        this.value = value;
        pinCallCount = 0;
        currentPin = LCInputPin.PIN_A;
    }

    public Led(int value, String nome, int setX, int setY) {
        super(LCComponent.LED, nome, setX, setY);
        if (value < 0 || value > 1) {
            throw new IllegalArgumentException("Invalid value");
        }
        this.value = value;
        pinCallCount = 0;
        currentPin = LCInputPin.PIN_A;
    }

    public Led(int value, String nome, int setX, int setY, String legend) {
        super(LCComponent.LED, nome, setX, setY, legend);
        if (value < 0 || value > 1) {
            throw new IllegalArgumentException("Invalid value");
        }
        this.value = value;
        pinCallCount = 0;
        currentPin = LCInputPin.PIN_A;
    }

    public void setValue(int value) {
        if (value < 0 || value > 1) {
            throw new IllegalArgumentException("Invalid value");
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public void draw() {
        boolean state = (value != 0);
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
