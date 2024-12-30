import logicircuit.LCComponent;
import logicircuit.LCInputPin;

public class Nor extends BasicComponent implements IOComponent {
    private boolean[] inputs;
    private boolean[] pins = { false, false };

    public Nor(String nome) {
        super(LCComponent.NOR, nome);
    }

    public Nor(String nome, int x, int y) {
        super(LCComponent.NOR, nome, x, y);
    }

    public Nor(String nome, int x, int y, String legenda) {
        super(LCComponent.NOR, nome, x, y, legenda);
    }

    @Override
    public void setInput(boolean[] inputs) {
        if (inputs.length != 2) {
            throw new IllegalArgumentException("NOR gate must have 2 inputs");
        }
        this.inputs = inputs;
    }

    @Override
    public boolean getOutput() {
        if (inputs == null) {
            throw new IllegalStateException("NOR gate inputs are not set");
        }
        return !(inputs[0] || inputs[1]);
    }

    @Override
    public void setUsedPin(LCInputPin pin) {
        if (pin == LCInputPin.PIN_A) {
            pins[0] = true;
        } else if (pin == LCInputPin.PIN_B) {
            pins[1] = true;
        } else {
            throw new IllegalArgumentException("NOR gate has only 2 input pins");
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
