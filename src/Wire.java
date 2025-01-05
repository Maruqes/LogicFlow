import logicircuit.LCInputPin;

public class Wire {
    private boolean state;
    private BasicComponentInterface component1;
    private BasicComponentInterface component2;
    private LCInputPin pin;

    public Wire(BasicComponentInterface component1, BasicComponentInterface component2, LCInputPin pin) {
        this.component1 = component1;
        this.component2 = component2;
        this.state = false;
        this.pin = pin;
        if (!canIusedPin()) {
            throw new IllegalArgumentException("Invalid pin");
        }
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean getState() {
        return state;
    }

    private boolean canIusedPin() {
        boolean result = false;
        if (component2 instanceof IOComponent) {
            result = ((IOComponent) component2).allowPin(pin);
            ((IOComponent) component2).setUsedPin(pin);
            return result;
        }
        if (component2 instanceof Switch) {
            result = ((Switch) component2).allowPin(pin);
            ((Switch) component2).setPin(true);
            return result;
        }
        if (component2 instanceof OutputInterface) {
            result = ((OutputInterface) component2).allowPin(pin);
            ((OutputInterface) component2).setUsedPin(pin);
            return result;
        }
        return false;
    }

    private void draw_IO() {
        if (component2 instanceof IOComponent) {
            ProgCircuito.drawPannel.drawWire(component1.getType(), component1.getXY()[0], component1.getXY()[1],
                    component2.getType(), component2.getXY()[0], component2.getXY()[1], pin,
                    state);
        }
    }

    private void draw_Switch() {
        if (component2 instanceof Switch) {
            ProgCircuito.drawPannel.drawWire(component1.getType(), component1.getXY()[0], component1.getXY()[1],
                    component2.getType(), component2.getXY()[0], component2.getXY()[1], pin,
                    state);
        }
    }

    private void draw_Outs() {
        if (component2 instanceof OutputInterface) {
            ProgCircuito.drawPannel.drawWire(component1.getType(), component1.getXY()[0], component1.getXY()[1],
                    component2.getType(), component2.getXY()[0], component2.getXY()[1], pin,
                    state);
        }
    }

    public BasicComponentInterface getComponent1() {
        return component1;
    }

    public BasicComponentInterface getComponent2() {
        return component2;
    }

    public void draw() {
        draw_IO();
        draw_Switch();
        draw_Outs();
    }

    public void PrintAllInfo() {
        System.out.println("Wire: " + component1.getName() + " " + component2.getName() + " " + pin);
    }

    public static LCInputPin getWithNome(String pin) {
        switch (pin.toLowerCase()) {
            case "pin_a":
                return LCInputPin.PIN_A;
            case "pin_b":
                return LCInputPin.PIN_B;
            case "pin_c":
                return LCInputPin.PIN_C;
            case "pin_d":
                return LCInputPin.PIN_D;
            case "pin_e":
                return LCInputPin.PIN_E;
            case "pin_f":
                return LCInputPin.PIN_F;
            case "pin_g":
                return LCInputPin.PIN_G;
            case "pin_h":
                return LCInputPin.PIN_H;
            default:
                return null;
        }
    }

    public String Strigonize() {
        return "Wire//" + component1.getName() + "//" + component2.getName() + "//" + pin;
    }

    public LCInputPin getPin() {
        return pin;
    }

    public Wire clone() {
        return new Wire(component1.cloneCMP(), component2.cloneCMP(), pin);
    }
}
