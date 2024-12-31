import logicircuit.LCComponent;
import logicircuit.LCInputPin;

public class Led extends BasicComponent implements OutputInterface {
    private int value;

    private boolean pin1 = false;

    public Led(int value, String nome) {
        super(LCComponent.LED, nome);
        if (value < 0 || value > 1) {
            throw new IllegalArgumentException("Invalid value");
        }
        this.value = value;
    }

    public Led(int value, String nome, int setX, int setY) {
        super(LCComponent.LED, nome, setX, setY);
        if (value < 0 || value > 1) {
            throw new IllegalArgumentException("Invalid value");
        }
        this.value = value;
    }

    public Led(int value, String nome, int setX, int setY, String legend) {
        super(LCComponent.LED, nome, setX, setY, legend);
        if (value < 0 || value > 1) {
            throw new IllegalArgumentException("Invalid value");
        }
        this.value = value;
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

    public boolean allowPin(LCInputPin pin) {
        if (pin != LCInputPin.PIN_A) {
            throw new IllegalArgumentException("Invalid pin");
        }
        return !pin1;
    }

    public void setUsedPin(LCInputPin pin) {
        if (pin != LCInputPin.PIN_A) {
            throw new IllegalArgumentException("Invalid pin");
        }
        pin1 = true;
    }

    @Override
    public void PrintAllInfo() {
        System.out.println("Led: " + super.getName() + " " + getValue() + " " + super.getXY()[0] + " "
                + super.getXY()[1] + " " + super.getLegend());
    }

}
