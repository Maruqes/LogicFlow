import logicircuit.LCComponent;
import logicircuit.LCInputPin;

public class Not extends BasicComponent implements IOComponent {
    private boolean[] inputs;
    private boolean[] pins = { false };

    public Not(String nome) {
        super(LCComponent.NOT, nome);
    }

    public Not(String nome, int x, int y) {
        super(LCComponent.NOT, nome, x, y);
    }

    public Not(String nome, int x, int y, String legenda) {
        super(LCComponent.NOT, nome, x, y, legenda);
    }

    @Override
    public void setInput(boolean[] inputs) {
        if (inputs.length != 1) {
            throw new IllegalArgumentException("Not gate must have 1 input");
        }
        this.inputs = inputs;
    }

    @Override
    public boolean getOutput() {
        if (inputs == null) {
            throw new IllegalStateException("Not gate inputs are not set");
        }
        return !inputs[0];
    }

    @Override
    public void setUsedPin(LCInputPin pin) {
        if (pin == LCInputPin.PIN_A) {
            pins[0] = true;
        } else {
            throw new IllegalArgumentException("Not gate has only 1 input pin");
        }
    }

    @Override
    public boolean allowPin(LCInputPin pin) {
        if (!pins[0] && pin == LCInputPin.PIN_A) {
            return true;
        } else {
            return false;
        }
    }

}
