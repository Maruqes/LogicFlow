import logicircuit.LCComponent;
import logicircuit.LCInputPin;

public class Or extends BasicComponent implements IOComponent {
    private boolean[] inputs;
    private boolean[] pins = { false, false };

    public Or(String nome) {
        super(LCComponent.OR, nome);
    }

    public Or(String nome, int x, int y) {
        super(LCComponent.OR, nome, x, y);
    }

    public Or(String nome, int x, int y, String legenda) {
        super(LCComponent.OR, nome, x, y, legenda);
    }

    @Override
    public void setInput(boolean[] inputs) {
        if (inputs.length != 2) {
            throw new IllegalArgumentException("OR gate must have 2 inputs");
        }
        this.inputs = inputs;
    }

    @Override
    public boolean getOutput() {
        if (inputs == null) {
            throw new IllegalStateException("OR gate inputs are not set");
        }
        return inputs[0] || inputs[1];
    }

    @Override
    public void setNotUsedPin(LCInputPin pin) {
        if (pin == LCInputPin.PIN_A) {
            pins[0] = false;
        } else if (pin == LCInputPin.PIN_B) {
            pins[1] = false;
        } else {
            throw new IllegalArgumentException("OR gate has only 2 input pins");
        }
    }

    @Override
    public void setUsedPin(LCInputPin pin) {
        if (pin == LCInputPin.PIN_A) {
            pins[0] = true;
        } else if (pin == LCInputPin.PIN_B) {
            pins[1] = true;
        } else {
            throw new IllegalArgumentException("OR gate has only 2 input pins");
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
        System.out.println(
                "Or: " + super.getName() + " " + super.getXY()[0] + " " + super.getXY()[1] + " " + super.getLegend());
    }
    @Override   
    public IOComponent clone()  {
        return new Or(this.getName(), this.getXY()[0], this.getXY()[1], this.getLegend());
    }
}
