import logicircuit.LCComponent;
import logicircuit.LCInputPin;

public class Switch extends BasicComponent {
    private boolean state;
    private boolean pin1 = false;

    public Switch(boolean state, String nome) {
        super(LCComponent.SWITCH, nome);
        this.state = state;
    }

    public Switch(boolean state, String nome, int setX, int setY) {
        super(LCComponent.SWITCH, nome, setX, setY);
        this.state = state;
    }

    public Switch(boolean state, String nome, int setX, int setY, String legend) {
        super(LCComponent.SWITCH, nome, setX, setY, legend);
        this.state = state;
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

    public boolean allowPin(LCInputPin pin) {
        if (pin != LCInputPin.PIN_A) {
            throw new IllegalArgumentException("Invalid pin");
        }
        return !pin1;
    }

    public void setPin(boolean pin1) {
        this.pin1 = pin1;
    }

    @Override
    public String Strigonize() {
        return "Switch//" + super.getType() + "//" + super.getName() + "//" + getState() + "//"
                + super.getXY()[0]
                + "//" + super.getXY()[1]
                + "//" + super.getLegend();
    }

    public void PrintAllInfo() {
        System.out.println("Switch: " + super.getName() + " " + getState() + " " + super.getXY()[0] + " "
                + super.getXY()[1] + " " + super.getLegend());
    }

}
