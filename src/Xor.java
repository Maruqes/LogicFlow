import logicircuit.LCComponent;
import logicircuit.LCInputPin;

public class Xor extends BasicComponent implements IOComponent {
    private boolean[] inputs;
    private boolean[] pins = { false, false };

    public Xor(String nome) {
        super(LCComponent.XOR, nome);
    }

    public Xor(String nome, int x, int y) {
        super(LCComponent.XOR, nome, x, y);
    }

    public Xor(String nome, int x, int y, String legenda) {
        super(LCComponent.XOR, nome, x, y, legenda);
    }

    @Override
    public void setInput(boolean[] inputs) {
        if (inputs.length != 2) {
            throw new IllegalArgumentException("XOR gate must have 2 inputs");
        }
        this.inputs = inputs;
    }

    @Override
    public boolean getOutput() {
        if (inputs == null) {
            throw new IllegalStateException("XOR gate inputs are not set");
        }
        return inputs[0] ^ inputs[1];
    }

    @Override
    public void setUsedPin(LCInputPin pin) {
        if (pin == LCInputPin.PIN_A) {
            pins[0] = true;
        } else if (pin == LCInputPin.PIN_B) {
            pins[1] = true;
        } else {
            throw new IllegalArgumentException("XOR gate has only 2 input pins");
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
}
