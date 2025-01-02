import logicircuit.LCComponent;
import logicircuit.LCInputPin;

public class Nand extends BasicComponent implements IOComponent {
    private boolean[] inputs;
    private boolean[] pins = { false, false };

    public Nand(String nome) {
        super(LCComponent.NAND, nome);
    }

    public Nand(String nome, int x, int y) {
        super(LCComponent.NAND, nome, x, y);
    }

    public Nand(String nome, int x, int y, String legenda) {
        super(LCComponent.NAND, nome, x, y, legenda);
    }

    @Override
    public void setInput(boolean[] inputs) {
        if (inputs.length != 2) {
            throw new IllegalArgumentException("NAND gate must have 2 inputs");
        }
        this.inputs = inputs;
    }

    @Override
    public boolean getOutput() {
        if (inputs == null) {
            throw new IllegalStateException("NAND gate inputs are not set");
        }
        return !(inputs[0] && inputs[1]);
    }

    @Override
    public void setNotUsedPin(LCInputPin pin) {
        if (pin == LCInputPin.PIN_A) {
            pins[0] = false;
        } else if (pin == LCInputPin.PIN_B) {
            pins[1] = false;
        } else {
            throw new IllegalArgumentException("NAND gate has only 2 input pins");
        }
    }

    @Override
    public void setUsedPin(LCInputPin pin) {
        if (pin == LCInputPin.PIN_A) {
            pins[0] = true;
        } else if (pin == LCInputPin.PIN_B) {
            pins[1] = true;
        } else {
            throw new IllegalArgumentException("NAND gate has only 2 input pins");
        }
    }

    @Override
    public boolean allowPin(LCInputPin pin) {
        if (!pins[0] && pin == LCInputPin.PIN_A) {
            return true;
        } else if (!pins[1] && pin == LCInputPin.PIN_B) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int maxInput() {
        return 2;
    }

    @Override
    public int maxOutput() {
        return 1;
    }

    @Override
    public void PrintAllInfo() {
        System.out.println("Nand: " + super.getName() + " " + super.getXY()[0] + " " + super.getXY()[1] + " "
                + super.getLegend());
    }

    @Override   
    public IOComponent clone() {
        return new Nand(this.getName(), this.getXY()[0], this.getXY()[1], this.getLegend());
    }
}
