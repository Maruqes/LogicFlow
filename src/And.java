import logicircuit.LCComponent;
import logicircuit.LCInputPin;

public class And extends BasicComponent implements IOComponent {
    private boolean[] inputs;

    private boolean[] pins = { false, false };

    public And(String nome) {
        super(LCComponent.AND, nome);
    }

    public And(String nome, int x, int y) {
        super(LCComponent.AND, nome, x, y);
    }

    public And(String nome, int x, int y, String legenda) {
        super(LCComponent.AND, nome, x, y, legenda);
    }

    @Override
    public void setInput(boolean[] inputs) {
        if (inputs.length != 2) {
            throw new IllegalArgumentException("AND gate must have 2 inputs");
        }
        this.inputs = inputs;
    }

    @Override
    public boolean getOutput() {
        if (inputs == null) {
            throw new IllegalStateException("AND gate inputs are not set");
        }
        return inputs[0] && inputs[1];
    }

    @Override
    public void setUsedPin(LCInputPin pin) {
        if (pin == LCInputPin.PIN_A) {
            pins[0] = true;
        } else if (pin == LCInputPin.PIN_B) {
            pins[1] = true;
        } else {
            throw new IllegalArgumentException("AND gate has only 2 input pins");
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
}

//
