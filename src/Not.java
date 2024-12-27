import logicircuit.LCComponent;
import logicircuit.LCInputPin;

public class Not extends BasicComponent implements IOComponent {
    private boolean[] inputs;
    private LCInputPin currentPin;
    private int pinCallCount;

    public Not(String nome) {
        super(LCComponent.NOT, nome);
        currentPin = LCInputPin.PIN_A;
        pinCallCount = 0;
    }

    public Not(String nome, int x, int y) {
        super(LCComponent.NOT, nome, x, y);
        currentPin = LCInputPin.PIN_A;
        pinCallCount = 0;
    }

    public Not(String nome, int x, int y, String legenda) {
        super(LCComponent.NOT, nome, x, y, legenda);
        currentPin = LCInputPin.PIN_A;
        pinCallCount = 0;
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
    public LCInputPin getNextPin() {
        if (pinCallCount == 0) {
            currentPin = LCInputPin.PIN_A;
        } else {
            throw new IllegalStateException("Invalid pin state");
        }
        pinCallCount++;
        return currentPin;
    }
}
