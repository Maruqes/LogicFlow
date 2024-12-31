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
            Main.drawPannel.drawWire(component1.getType(), component1.getXY()[0], component1.getXY()[1],
                    component2.getType(), component2.getXY()[0], component2.getXY()[1], pin,
                    state);
        }
    }

    private void draw_Switch() {
        if (component2 instanceof Switch) {
            Main.drawPannel.drawWire(component1.getType(), component1.getXY()[0], component1.getXY()[1],
                    component2.getType(), component2.getXY()[0], component2.getXY()[1], pin,
                    state);
        }
    }

    private void draw_Outs() {
        if (component2 instanceof OutputInterface) {
            Main.drawPannel.drawWire(component1.getType(), component1.getXY()[0], component1.getXY()[1],
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
        System.out.println("\n\nWire");
        System.out.println("State: " + state);
        System.out.println("Component1: " + component1.getName());
        System.out.println("Component2: " + component2.getName());
        System.out.println("Pin: " + pin + "\n\n");
    }

    public static LCInputPin getWithNome(String pin) {
        if (pin.equals("pin_a")) {
            return LCInputPin.PIN_A;
        } else if (pin.equals("pin_b")) {
            return LCInputPin.PIN_B;
        } else if (pin.equals("pin_c")) {
            return LCInputPin.PIN_C;
        } else {
            return null;
        }
    }

    public String Strigonize() {
        return "";
    }
}
