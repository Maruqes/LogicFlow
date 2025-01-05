import logicircuit.LCComponent;
import logicircuit.LCInputPin;

public class Display3bit extends BasicComponent implements OutputInterface {
    private int value;
    private boolean pins[] = { false, false, false };

    public Display3bit(int value, String nome) {
        super(LCComponent.BIT3_DISPLAY, nome);
        if (value < 0 || value > 7) {
            throw new IllegalArgumentException("Invalid value");
        }
        this.value = value;
    }

    public Display3bit(int value, String nome, int setX, int setY) {
        super(LCComponent.BIT3_DISPLAY, nome, setX, setY);
        if (value < 0 || value > 7) {
            throw new IllegalArgumentException("Invalid value");
        }
        this.value = value;
    }

    public Display3bit(int value, String nome, int setX, int setY, String legend) {
        super(LCComponent.BIT3_DISPLAY, nome, setX, setY, legend);
        if (value < 0 || value > 7) {
            throw new IllegalArgumentException("Invalid value");
        }
        this.value = value;
    }

    public void setValue(int value) {
        if (value < 0 || value > 7) {
            throw new IllegalArgumentException("Invalid value");
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public void draw() {
        ProgCircuito.drawPannel.drawComponent(super.getType(), super.getXY()[0], super.getXY()[1], value);
    }

    @Override
    public boolean allowPin(LCInputPin pin) {
        if (pin == LCInputPin.PIN_A) {
            return !pins[0];
        } else if (pin == LCInputPin.PIN_B) {
            return !pins[1];
        } else if (pin == LCInputPin.PIN_C) {
            return !pins[2];
        } else {
            throw new IllegalArgumentException("Invalid pin");
        }
    }

    @Override
    public void setUsedPin(LCInputPin pin) {
        if (pin == LCInputPin.PIN_A) {
            pins[0] = true;
        } else if (pin == LCInputPin.PIN_B) {
            pins[1] = true;
        } else if (pin == LCInputPin.PIN_C) {
            pins[2] = true;
        } else {
            throw new IllegalArgumentException("Invalid pin");
        }
    }

    public static int getNumberWithPins(boolean pin1, boolean pin2, boolean pin3) {
        int number = 0;

        if (pin1) {
            number = number | 1; // Define o bit 0
        }
        if (pin2) {
            number = number | 1 << 1; // Define o bit 1
        }
        if (pin3) {
            number = number | 1 << 2; // Define o bit 2
        }

        return number;
    }

    @Override
    public void PrintAllInfo() {
        System.out.println("Display3bit: " + super.getName() + " " + getValue() + " " + super.getXY()[0] + " "
                + super.getXY()[1] + " " + super.getLegend());
    }

    @Override
    public void setNotUsedPin(LCInputPin pin) {
        if (pin == LCInputPin.PIN_A) {
            pins[0] = false;
        } else if (pin == LCInputPin.PIN_B) {
            pins[1] = false;
        } else if (pin == LCInputPin.PIN_C) {
            pins[2] = false;
        } else {
            throw new IllegalArgumentException("Invalid pin");
        }
    }

    @Override
    public OutputInterface clone() {
        return new Display3bit(this.getValue(), this.getName(), this.getXY()[0], this.getXY()[1], this.getLegend());
    }

}
